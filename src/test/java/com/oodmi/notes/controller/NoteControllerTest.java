package com.oodmi.notes.controller;


import com.oodmi.notes.config.MongoDBTestContainerConfig;
import com.oodmi.notes.model.Note;
import com.oodmi.notes.model.Tag;
import com.oodmi.notes.repository.NoteRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
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

    @Test
    public void getAllTest() throws Exception {
        mockMvc.perform(get("/notes")
                        .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void getByIdTest(@Value("classpath:stub/getByIdResponse.json")
                            Resource response) throws Exception {
        repository.save(new Note()
                .setId(1L)
                .setTitle("Title 1")
                .setText("note is just a note")
                .setCreatedDate(LocalDate.of(2023, 12, 23))
                .setTags(List.of(Tag.PERSONAL, Tag.IMPORTANT)));

        String actual = mockMvc.perform(get("/notes/1")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals(asString(response), actual, true);
    }

    @Test
    public void createTest(@Value("classpath:stub/createRequest.json")
                           Resource request) throws Exception {

        mockMvc.perform(post("/notes")
                        .contentType("application/json")
                        .content(asString(request)))
                .andExpect(status().isOk());

        Assertions.assertTrue(repository.findById(1L).isPresent());
    }

    @Test
    public void updateTest(@Value("classpath:stub/updateRequest.json")
                           Resource request) throws Exception {
        repository.save(new Note()
                .setId(1L)
                .setTitle("Title 1")
                .setText("note is just a note")
                .setCreatedDate(LocalDate.of(2023, 12, 23))
                .setTags(List.of(Tag.PERSONAL, Tag.IMPORTANT)));

        mockMvc.perform(put("/notes/1")
                        .contentType("application/json")
                        .content(asString(request)))
                .andExpect(status().isOk());

        var note = repository.findById(1L);
        Assertions.assertTrue(note.isPresent());
        Assertions.assertEquals(note.get().getTitle(), "Title 2");
    }

    @Test
    public void deleteTest() throws Exception {
        repository.save(new Note()
                .setId(1L)
                .setTitle("Title 1")
                .setText("note is just a note")
                .setCreatedDate(LocalDate.of(2023, 12, 23))
                .setTags(List.of(Tag.PERSONAL, Tag.IMPORTANT)));

        mockMvc.perform(delete("/notes/1")
                        .contentType("application/json"))
                .andExpect(status().isOk());

        Assertions.assertTrue(repository.findById(1L).isEmpty());
    }

    @Test
    public void getStatsTest(@Value("classpath:stub/getStatsResponse.json")
                             Resource response) throws Exception {
        repository.save(new Note()
                .setId(1L)
                .setTitle("Title 1")
                .setText("note is just a note")
                .setCreatedDate(LocalDate.of(2023, 12, 23))
                .setTags(List.of(Tag.PERSONAL, Tag.IMPORTANT)));

        String actual = mockMvc.perform(get("/notes/1/stats")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JSONAssert.assertEquals(asString(response), actual, true);
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
