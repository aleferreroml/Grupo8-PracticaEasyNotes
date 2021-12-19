package com.example.easynotes.service;

import com.example.easynotes.dto.notes.NoteRequestDTO;
import com.example.easynotes.dto.notes.NoteResponseTypeNoteDTO;
import com.example.easynotes.dto.notes.NoteResponseWithAuthorDTO;
import com.example.easynotes.dto.notes.NoteResponseWithCantLikesDTO;
import com.example.easynotes.dto.thanks.ThankDTO;
import com.example.easynotes.model.Note;

import java.util.List;
import java.util.Set;

public interface INoteService {

    List<NoteResponseWithAuthorDTO> getAllNotes();

    NoteResponseWithAuthorDTO createNote(NoteRequestDTO noteRequestDTO);

    NoteResponseWithAuthorDTO getNoteById(Long noteId);

    NoteResponseWithAuthorDTO updateNote(Long noteId, Note noteDetailsDTO);

    void deleteNote(Long noteId);

    void addReviser(Long id, Long authorId);

    Set<ThankDTO> getThanks(Long id);

    List<NoteResponseWithCantLikesDTO> getThreeMoreThankedNotes (int year);

    NoteResponseTypeNoteDTO getNoteWithTypeNote(Long noteId);
}
