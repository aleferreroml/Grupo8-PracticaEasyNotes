package com.example.easynotes.unit.controller;

import com.example.easynotes.controller.NoteController;
import com.example.easynotes.dto.NoteRequestDTO;
import com.example.easynotes.model.Note;
import com.example.easynotes.service.INoteService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class NoteControllerTest {
    INoteService noteService = Mockito.mock(INoteService.class);

    NoteController noteController = new NoteController(noteService);

    @Test
    void getAllNotes() {
        noteController.getAllNotes();
    }

    @Test
    void createNote() {
        noteController.createNote(new NoteRequestDTO());
    }

    @Test
    void testCreateNote() {
        noteController.createNote(new NoteRequestDTO());
    }

    @Test
    void getNoteById() {
        noteController.getNoteById(1L);
    }

    @Test
    void updateNote() {
        noteController.updateNote(1L, new Note());
    }

    @Test
    void deleteNote() {
        noteController.deleteNote(1L);
    }

    @Test
    void getThanks() {
        noteController.getThanks(1L);
    }

    @Test
    void getNotesWithLikesByYear() {
        noteController.getNotesWithLikesByYear(2020);
    }
}
