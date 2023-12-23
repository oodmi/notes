package com.oodmi.notes.controller;


import com.oodmi.notes.config.MongoDBTestContainerConfig;
import com.oodmi.notes.model.Note;
import com.oodmi.notes.model.Tag;
import com.oodmi.notes.repository.NoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = MongoDBTestContainerConfig.class)
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private NoteRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void getAllTest(@Value("classpath:stub/getAllResponse.json")
                    Resource response) throws Exception {
        createNoteAndGetId();
        createAnotherNoteAndGetId();

        String actual = mockMvc.perform(get("/notes")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals(asString(response), actual,
                new CustomComparator(STRICT, new Customization("[*].id", (it1, it2) -> true)));
    }

    @Test
    void getSecondPageTest(@Value("classpath:stub/getSecondPageResponse.json")
                           Resource response) throws Exception {
        createNoteAndGetId();
        createAnotherNoteAndGetId();

        String actual = mockMvc.perform(get("/notes")
                        .queryParam("page", "1")
                        .queryParam("sizePerPage", "1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals(asString(response), actual,
                new CustomComparator(STRICT, new Customization("[*].id", (it1, it2) -> true)));
    }

    @Test
    void getByIdTest(@Value("classpath:stub/getByIdResponse.json")
                     Resource response) throws Exception {
        var id = createNoteAndGetId();

        String actual = mockMvc.perform(get("/notes/" + id)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals(asString(response), actual,
                new CustomComparator(STRICT, new Customization("id", (it1, it2) -> true)));
    }

    @Test
    void createTest(@Value("classpath:stub/createRequest.json")
                    Resource request) throws Exception {

        mockMvc.perform(post("/notes")
                        .contentType("application/json")
                        .content(asString(request)))
                .andExpect(status().isOk());

        Assertions.assertEquals(1, repository.findAll().size());
    }

    @Test
    void updateTest(@Value("classpath:stub/updateRequest.json")
                    Resource request) throws Exception {
        var id = createNoteAndGetId();

        mockMvc.perform(put("/notes/" + id)
                        .contentType("application/json")
                        .content(asString(request)))
                .andExpect(status().isOk());

        var note = repository.findById(id);
        Assertions.assertTrue(note.isPresent());
        Assertions.assertEquals(note.get().getTitle(), "Title 2");
    }

    @Test
    void deleteTest() throws Exception {
        var id = createNoteAndGetId();

        mockMvc.perform(delete("/notes/" + id)
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Assertions.assertTrue(repository.findById(id).isEmpty());
    }

    @Test
    void getStatsTest(@Value("classpath:stub/getStatsResponse.json")
                      Resource response) throws Exception {
        var id = createNoteAndGetId();

        String actual = mockMvc.perform(get("/notes/" + id + "/stats")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals(asString(response), actual, true);
    }

    private String createNoteAndGetId() {
        return repository.save(new Note()
                        .setTitle("Title 1")
                        .setText("note is just a note")
                        .setCreatedDate(LocalDate.of(2023, 12, 23))
                        .setTags(List.of(Tag.PERSONAL, Tag.IMPORTANT)))
                .getId();
    }

    private String createAnotherNoteAndGetId() {
        return repository.save(new Note()
                        .setTitle("Title 2")
                        .setText("another note is just a note")
                        .setCreatedDate(LocalDate.of(2023, 12, 24))
                        .setTags(List.of(Tag.PERSONAL, Tag.IMPORTANT)))
                .getId();
    }

    private String asString(Resource resource) {
        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            var string = FileCopyUtils.copyToString(reader);
            reader.close();
            return string;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
