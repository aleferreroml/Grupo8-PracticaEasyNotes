package com.example.easynotes.controller;

import com.example.easynotes.dto.notes.NoteRequestDTO;
import com.example.easynotes.dto.notes.NoteResponseTypeNoteDTO;
import com.example.easynotes.dto.notes.NoteResponseWithAuthorDTO;
import com.example.easynotes.dto.notes.NoteResponseWithCantLikesDTO;
import com.example.easynotes.dto.thanks.ThankDTO;
import com.example.easynotes.model.Note;
import com.example.easynotes.service.INoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/notes")
public class NoteController {

    INoteService noteService;

    NoteController(INoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/all")
    public List<NoteResponseWithAuthorDTO> getAllNotes() {
        return noteService.getAllNotes();
    }

    @PostMapping()
    public NoteResponseWithAuthorDTO createNote(@Valid @RequestBody NoteRequestDTO noteRequestDTO) {
        return noteService.createNote(noteRequestDTO);
    }

    @PostMapping("{id}/addReviser/{reviserId}")
    public void createNote(@PathVariable Long id, @PathVariable Long reviserId) {
        noteService.addReviser(id, reviserId);
    }

    @GetMapping("/{id}")
    public NoteResponseWithAuthorDTO getNoteById(@PathVariable(value = "id") Long noteId) {
        return noteService.getNoteById(noteId);
    }

    @PutMapping("/{id}")
    public NoteResponseWithAuthorDTO updateNote(@PathVariable(value = "id") Long noteId, @Valid @RequestBody Note noteDetailsDTO) {
        return noteService.updateNote(noteId, noteDetailsDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable(value = "id") Long noteId) {
        noteService.deleteNote(noteId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}/thank")
    public Set<ThankDTO> getThanks(@PathVariable(value = "id") Long id) {
        return noteService.getThanks(id);
    }

    @GetMapping("threeMostThanked/{year}")
    public List<NoteResponseWithCantLikesDTO> getNotesWithLikesByYear(@PathVariable(value = "year") int year){
        return noteService.getThreeMoreThankedNotes(year);
    }

    @GetMapping("{id}/typeNote")
    public NoteResponseTypeNoteDTO getTypeNote(@PathVariable(value = "id") Long id) {
        return noteService.getNoteWithTypeNote(id);
    }

}