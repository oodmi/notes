package com.oodmi.notes.controller;


import com.oodmi.notes.model.Note;
import com.oodmi.notes.service.NoteService;
import com.oodmi.notes.validator.NoteValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService service;
    private final NoteValidator validator;

    @GetMapping
    public List<Note> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public Note getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public Note create(@RequestBody Note note) {
        validator.validate(note);
        return service.create(note);
    }

    @PutMapping("/{id}")
    public Note update(@PathVariable Long id,
                       @RequestBody Note note) {
        validator.validate(note);
        return service.update(id, note);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{id}/stats")
    public Map<String, Long> getStats(@PathVariable Long id){
        return service.getStats(id);
    }
}
