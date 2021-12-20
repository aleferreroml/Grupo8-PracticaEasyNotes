package com.example.easynotes.service;

import com.example.easynotes.dto.*;
import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.model.Note;
import com.example.easynotes.model.Thank;
import com.example.easynotes.model.User;
import com.example.easynotes.repository.NoteRepository;
import com.example.easynotes.repository.UserRepository;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoteService implements INoteService {

    NoteRepository noteRepository;
    UserRepository userRepository;
    ModelMapper modelMapper;

    @Autowired
    NoteService(NoteRepository noteRepository,
                UserRepository userRepository,
                ModelMapper modelMapper) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;

        //Converter used to retrieve cant of user's notes
        Converter<Set<Note>, Integer> notesToCantNotesConverter = new AbstractConverter<Set<Note>, Integer>() {
            @Override
            protected Integer convert(Set<Note> notes) {
                return notes.size();
            }
        };

        //Load converter to modelMapper used when we want convert from User to UserResponseWithCantNotesDTO
        modelMapper.typeMap(User.class, UserResponseWithCantNotesDTO.class).addMappings( (mapper) ->
                mapper.using(notesToCantNotesConverter)
                        .map(User::getAuthorNotes, UserResponseWithCantNotesDTO::setCantNotes)
        );


        //Converter used to retrieve cant of user's notes
        Converter<Note, Long> userToIdConverter = new AbstractConverter<Note, Long>() {
            @Override
            protected Long convert(Note note) {
                return note.getId();
            }
        };

        modelMapper.typeMap(Thank.class, ThankDTO.class).addMappings( (mapper) ->
                mapper.using(userToIdConverter)
                        .map(Thank::getNote, ThankDTO::setNoteId)
        );



        /*modelMapper.typeMap(NoteRequestDTO.class, Note.class).addMappings( (mapper) ->
                mapper.with( req -> new Date() ).map(Note::setCreatedAt));*/

        this.modelMapper = modelMapper;
    }

    @Override
    public List<NoteResponseWithAuthorDTO> getAllNotes() {
        List<Note> notes = noteRepository.findAll();
        return notes
                .stream()
                .map( note -> modelMapper.map( note, NoteResponseWithAuthorDTO.class ))
                .collect(Collectors.toList());
    }

    @Override
    public NoteResponseWithAuthorDTO createNote(NoteRequestDTO noteRequestDTO) {
        // Create new note
        Note note = modelMapper.map(noteRequestDTO, Note.class);

        //!FIXME
        note.setCreatedAt(LocalDate.now());
        note.setUpdatedAt(LocalDate.now());


        Note noteReq = noteRepository.save(note);

        //FIXME
        //Long idNote = note.getId();

        NoteResponseWithAuthorDTO resp =  modelMapper.map(noteReq, NoteResponseWithAuthorDTO.class);
        return resp;
    }

    @Override
    public NoteResponseWithAuthorDTO getNoteById(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
        return modelMapper.map(note, NoteResponseWithAuthorDTO.class);
    }

    @Override
    public NoteResponseWithAuthorDTO updateNote(Long noteId,
                                                Note noteDetailsDTO) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        note.setTitle(noteDetailsDTO.getTitle());
        note.setContent(noteDetailsDTO.getContent());
        note.setAuthor(noteDetailsDTO.getAuthor());

        Note updatedNote = noteRepository.save(note);
        return modelMapper.map(updatedNote, NoteResponseWithAuthorDTO.class);
    }

    @Override
    public void deleteNote(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));

        noteRepository.delete(note);
    }

    @Override
    public void addReviser(Long id, Long authorId) {

        User user = userRepository.findById(authorId).
                orElseThrow(  () -> new ResourceNotFoundException("User", "id", id) );

        Note note = noteRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Note", "id", id) );

        user.addRevisedNote(note);

        userRepository.save(user);
    }

    @Override
    public Set<ThankDTO> getThanks(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Note", "id", id) );

        return note.getThanks().stream()
                        .map( thank -> modelMapper.map( thank, ThankDTO.class ))
                        .collect(Collectors.toSet());
    }

    public List<NoteResponseWithCantLikesDTO> getThreeMoreThankedNotes (int year){

        List<HashMap<String, Object>> notesMoreThanked = noteRepository.findTopThreeNotesMostThankedByDate(year);
        return notesMoreThanked.stream()
                .limit(3L)
                .map( m -> {
                    Long id = (Long) m.get("id");
                    Long cant = (Long) m.get("cant_thanks");
                    return new NoteResponseWithCantLikesDTO(id, Math.toIntExact(cant) );
                }
                )
                .collect(Collectors.toList());
    }

    @Override
    public NoteResponseWithTypeDTO getTypeNote(Long noteId){
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new ResourceNotFoundException("Note", "id", noteId));
        List<HashMap<String, Object>> thanksNote = noteRepository.findCantThanksForNote(noteId);

        Long cantThanks = (Long) thanksNote.get(0).get("cant_thanks");
        NoteResponseWithTypeDTO noteWithType = modelMapper.map(note, NoteResponseWithTypeDTO.class);
        //noteWithType.setId();
        if(cantThanks > 10) {
            noteWithType.setType(TypeNote.Destacada);
        } else if(cantThanks < 5) {
            noteWithType.setType(TypeNote.Normal);
        } else {
            noteWithType.setType(TypeNote.DeInteres);
        }

        return noteWithType;
    }
}


