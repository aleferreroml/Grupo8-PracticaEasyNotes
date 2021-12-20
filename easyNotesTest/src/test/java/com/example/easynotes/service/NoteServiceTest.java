package com.example.easynotes.service;

import com.example.easynotes.dto.NoteRequestDTO;
import com.example.easynotes.dto.NoteResponseWithTypeDTO;
import com.example.easynotes.dto.TypeNote;
import com.example.easynotes.exception.ResourceNotFoundException;
import com.example.easynotes.model.Note;
import com.example.easynotes.model.Thank;
import com.example.easynotes.model.User;
import com.example.easynotes.repository.NoteRepository;
import com.example.easynotes.repository.UserRepository;
import org.aspectj.weaver.ast.Not;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.naming.spi.ResolveResult;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {
    @Mock
    UserRepository userRepository = Mockito.mock(UserRepository.class);

    @Mock
    NoteRepository noteRepository = Mockito.mock(NoteRepository.class);

    ModelMapper modelMapper = new ModelMapper();

    @InjectMocks
    NoteService noteService = new NoteService(noteRepository, userRepository, modelMapper);

    @Test
    void getAllNotes() {
        noteService.getAllNotes();
    }

    @Test
    void createNote() {
        when(noteRepository.save(any(Note.class))).thenReturn(new Note());
        Assertions.assertDoesNotThrow(
                () -> noteService.createNote(new NoteRequestDTO()) );
    }

    @Test
    void getNoteById() {
        Assertions.assertThrows(
                ResourceNotFoundException.class,
                () -> noteService.getNoteById(1L) );
    }

    @Test
    void updateNote() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> noteService.updateNote(1L, new Note()));
    }

    @Test
    void updateNoteOk() {
        when(noteRepository.findById(1L)).thenReturn(Optional.of(new Note()));
        when(noteRepository.save(any(Note.class))).thenReturn(new Note());
        Assertions.assertDoesNotThrow(
                () -> noteService.updateNote(1L, new Note()));
    }

    @Test
    void deleteNote() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> noteService.deleteNote(1L) );
    }

    @Test
    void addReviser() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> noteService.addReviser(2L, 1L) );
    }


    @Test
    void addReviserOk() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(noteRepository.findById(2L)).thenReturn(Optional.of(new Note()));
        Assertions.assertDoesNotThrow(
                () -> noteService.addReviser(2L, 1L) );
    }

    @Test
    void getThanks() {
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> noteService.getThanks(3L) );
    }

    @Test
    void getThanksOk() {
        var pedro = new User();
        var note = new Note();
        note.setThanks(Set.of(new Thank(pedro, note)));
        when(noteRepository.findById(3L)).thenReturn(Optional.of(note));
        Assertions.assertDoesNotThrow(
                () -> noteService.getThanks(3L) );
    }


    @Test
    void getThreeMoreThankedNotes() {
        noteService.getThreeMoreThankedNotes(2020);
    }

    @Test
    void getTypeNoteWithNotFoundIdThenResourceNotFoundException(){
        //Arrange
        Long nonExistentId = -1L;

        //Act and Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> this.noteService.getTypeNote(nonExistentId));
    }

    @Test
    void getTypeNoteThenReturnsDestacada(){
        Long noteId = 0L;
        NoteResponseWithTypeDTO expected = new NoteResponseWithTypeDTO();
        expected.setType(TypeNote.Destacada);
        expected.setId(0L);
        expected.setTitle("Que hacemos1?");
        expected.setContent("Si el tiempo no se me pasa más cuando se corta la luz1");
        expected.setCreatedAt(LocalDate.of(2019, 12, 6));
        expected.setUpdatedAt(LocalDate.of(2021, 12,6));
        HashMap<String, Object> cantThanks = new HashMap();
        cantThanks.put("cant_thanks", 11L);

        when(noteRepository.findById(noteId)).thenReturn(Optional.of(new Note()));
        when(noteRepository.findCantThanksForNote(noteId))
                .thenReturn(List.of(cantThanks));
        NoteResponseWithTypeDTO current = this.noteService.getTypeNote(noteId);

        Assertions.assertAll(
                () -> Assertions.assertEquals(current.getType(), expected.getType())
        );
        verify(noteRepository, atLeastOnce()).findById(anyLong());
        verify(noteRepository, atLeastOnce()).findCantThanksForNote(anyLong());
    }

   /* @Test
    void getTypeNoteThenReturnsNormal(){

    }

    @Test
    void getTypeNoteThenReturnsDeInteres(){

    }*/
}