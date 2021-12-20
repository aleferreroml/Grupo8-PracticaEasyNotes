package com.example.easynotes.service;

import com.example.easynotes.dto.*;
import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.model.Note;
import com.example.easynotes.model.Thank;
import com.example.easynotes.model.User;
import com.example.easynotes.repository.NoteRepository;
import com.example.easynotes.repository.ThankRepository;
import com.example.easynotes.repository.UserRepository;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserService implements IUserService {

    UserRepository userRepository;

    NoteRepository noteRepository;

    ThankRepository thankRepository;

    ModelMapper modelMapper;

    @PersistenceContext
    EntityManager entityManager;

    UserService(UserRepository userRepository,
                NoteRepository noteRepository,
                ThankRepository thankRepository,
                ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
        this.thankRepository = thankRepository;


        Converter<Long, User> authorIdToUserConverter = new AbstractConverter<Long, User>() {
            @Override
            protected User convert(Long authorId) throws ResourceNotFoundException {
                return userRepository.findById(authorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Author", "id", authorId));
            }
        };
        //Load converter to modelMapper used when we want convert from User to UserResponseWithCantNotesDTO
        modelMapper.typeMap(NoteRequestDTO.class, Note.class).addMappings( (mapper) ->
                mapper.using(authorIdToUserConverter)
                        .map(NoteRequestDTO::getAuthorId, Note::setAuthor)
        );

        this.modelMapper = modelMapper;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> listUsers = userRepository.findAll();
        return listUsers.stream()
                .map( user -> modelMapper.map( user, UserResponseDTO.class ) )
                .collect( Collectors.toList() );
    }

    @Override
    public List<UserResponseWithNotesDTO> getAllUsersWithNotes() {
        List<User> listUsers = userRepository.findAll();
        return listUsers.stream()
                .map( user -> modelMapper.map( user, UserResponseWithNotesDTO.class ) )
                .collect( Collectors.toList() );
    }

    @Override
    public List<UserResponseWithCantNotesDTO> getAllUsersWithCantNotes() {
        List<User> listUsers = userRepository.findAll();
        return listUsers.stream()
                .map( user -> modelMapper.map( user, UserResponseWithCantNotesDTO.class ) )
                .collect( Collectors.toList() );
    }

    @Override
    public UserResponseDTO createUSer(UserRequestDTO userRequestDTO) {
        User user = modelMapper.map(userRequestDTO, User.class);
        return modelMapper.map(userRepository.save(user), UserResponseDTO.class);
    }

    @Override
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return modelMapper.map(user, UserResponseDTO.class);
    }

    @Override
    public UserResponseWithNotesDTO getUserWithNotesById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return modelMapper.map(user, UserResponseWithNotesDTO.class);
    }
    @Override
    public UserResponseWithCantNotesDTO getUserWithCantNotesById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        return modelMapper.map(user, UserResponseWithCantNotesDTO.class);
    }


    @Override
    public UserResponseDTO updateUser(Long userId,
                                      UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());

        return modelMapper.map(userRepository.save(user), UserResponseDTO.class);
    }

    @Override
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        userRepository.delete(user);

    }

    @Override
    public List<UserResponseDTO> getUsersLastNameLike(String lastName) {
        List<?> users =  userRepository.findUserByLastNameLike(lastName);

        return null;
    }

    @Override
    public List<UserResponseWithNotesDTO> getUsersByNoteTitleLike(String title) {
        List<User> users = userRepository.findUserByNoteTitleLike(title);

        return users.stream()
                .map( user -> modelMapper.map( user, UserResponseWithNotesDTO.class ) )
                .collect( Collectors.toList() );
    }

    @Override
    public List<UserResponseWithNotesDTO> getUsersByNoteCreatedAfterDate(Date date) {
        List<User> users = userRepository.findUserByNoteCreatedAtLessOrEqualDate(date);

        return users.stream()
                .map( user -> modelMapper.map( user, UserResponseWithNotesDTO.class ) )
                .collect( Collectors.toList() );
    }

    @Override
    public void createThank(Long userId, Long noteId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        Thank thank = new Thank(user, note);

        thankRepository.save(thank);
    }

    @Override
    public UserResponseWithCategoryDTO getCategory(Long userId){
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<Note> notas = this.userRepository.findUserByIdAndNotes(userId);

        UserResponseWithCategoryDTO userDTO = this.modelMapper
                .map(user, UserResponseWithCategoryDTO.class);

        Boolean ayer = false, anteayer = false, anteanteayer = false;
        for (Note nota : notas) {
            if(nota.getCreatedAt().isEqual(LocalDate.now().minusDays(1))){
                ayer = true;
            } else if(nota.getCreatedAt().isEqual(LocalDate.now().minusDays(2))){
                anteayer = true;
            } else if(nota.getCreatedAt().isEqual(LocalDate.now().minusDays(3))){
                anteanteayer = true;
            }
        }

        if(ayer && anteayer && anteanteayer){
            userDTO.setCategory(CategoriaUsuario.PublicadorDiario);
            return userDTO;
        }

        //verifica los ultimo 7 dias a partir de hoy, no considera semanas de Domingo a Sabado
        Boolean ultimaSem = false, anteUltimaSem = false, anteanteUltimaSem = false;
        for (Note nota : notas) {
            if(nota.getCreatedAt().isAfter(LocalDate.now().minusWeeks(1))){
                ultimaSem = true;
            } else if(nota.getCreatedAt().isAfter(LocalDate.now().minusWeeks(2))){
                anteUltimaSem = true;
            } else if(nota.getCreatedAt().isAfter(LocalDate.now().minusWeeks(3))){
                anteanteUltimaSem = true;
            }
        }

        if(ultimaSem && anteUltimaSem && anteanteUltimaSem){
            userDTO.setCategory(CategoriaUsuario.PublicadorSemanal);
            return userDTO;
        } else {
            userDTO.setCategory(CategoriaUsuario.Publicador);
            return userDTO;
        }
    }

}
