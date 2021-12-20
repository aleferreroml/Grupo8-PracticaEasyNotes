package com.example.easynotes.integration;

import com.example.easynotes.dto.NoteResponseWithCantLikesDTO;
import com.example.easynotes.dto.NoteResponseWithTypeDTO;
import com.example.easynotes.dto.TypeNote;
import com.example.easynotes.model.Note;
import com.example.easynotes.service.NoteService;
import com.example.easynotes.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class NotesIntegrationTest {

    private static ObjectWriter writer;

    @Autowired
    MockMvc mockMvc;

    // antes de cada ejecucicon de un test vacia el cache de los datos en memoria
    // cada test tiene un estado inicial(como si fuera el priemro en ejecutarse) gracias a la
    // anotacion
/*    @MockBean
    NoteService noteService;*/

    @BeforeAll
    static void initData() {
        writer = new ObjectMapper()
                .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
                // fechas
                .registerModule(new JSR310Module())
                .writer()
                .withDefaultPrettyPrinter();
    }

    @Test
    public void testCorrectMostThanksCountByYear2() throws Exception {
        //Arrange
        List<NoteResponseWithCantLikesDTO> noteResponseExpected = List.of(
                new NoteResponseWithCantLikesDTO(37L, 6),
                new NoteResponseWithCantLikesDTO(1L, 1),
                new NoteResponseWithCantLikesDTO(14L, 1)
        );
        Integer year = 2021;
        String listNoteResponseJson = writer.writeValueAsString(noteResponseExpected);

        ResultMatcher expectedStatus = status().isOk();
        ResultMatcher expectedContentType = content().contentType(MediaType.APPLICATION_JSON);
        ResultMatcher expectedJson = content().json(listNoteResponseJson);

        //Act and Assert
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/note/threeMostThanked/{year}", year))
                .andDo(print())
                .andExpect(expectedStatus)
                .andExpect(expectedContentType)
                .andExpect(expectedJson);
    }

//    @Test
//    public void testCorrectMostThanksCountByYear() throws Exception {
//        // Arrange
//        int year = 2020;
//
//        var noteFirst   = new NoteResponseWithCantLikesDTO(1L, 5);
//        var noteSecond  = new NoteResponseWithCantLikesDTO(29L, 4);
//        var noteThird   = new NoteResponseWithCantLikesDTO(14L, 3);
//        List<NoteResponseWithCantLikesDTO> notes = List.of(noteFirst, noteSecond, noteThird);
//
//        String expected = writer.writeValueAsString(notes);
//
//        // Act & Assert
//        mockMvc.perform( get("/api/note/threeMostThanked/" + year) )
//                .andDo(print())
//                .andExpectAll(
//                        status().isOk(),
//                        content().json(expected),
//                        content().contentType(MediaType.APPLICATION_JSON)
//                );
//    }

    @Test
    public void testCorrectGetTypeNoteById() throws Exception {
        Long id = 0L;

        NoteResponseWithTypeDTO expectedNoteResponse = new NoteResponseWithTypeDTO();
        expectedNoteResponse.setType(TypeNote.DeInteres);
        expectedNoteResponse.setId(0L);
        expectedNoteResponse.setTitle("Que hacemos1?");
        expectedNoteResponse.setContent("Si el tiempo no se me pasa más cuando se corta la luz1");
        expectedNoteResponse.setCreatedAt(LocalDate.of(2019, 12, 6));
        expectedNoteResponse.setUpdatedAt(LocalDate.of(2021, 12, 6));

        String expectedNoteResponseJson = writer.writeValueAsString(expectedNoteResponse);

        ResultMatcher expectedStatus = status().isOk();
        ResultMatcher expectedContentType = content().contentType(MediaType.APPLICATION_JSON);
        ResultMatcher expectedJson = content().json(expectedNoteResponseJson);


        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/note/type/{id}", id))
                .andDo(print())
                .andExpectAll(expectedStatus, expectedContentType, expectedJson);
    }
}
