package com.oodmi.notes.controller;


import com.oodmi.notes.dto.NoteDto;
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
    public List<NoteDto> getAll(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int sizePerPage) {
        return service.getAll(page, sizePerPage);
    }

    @GetMapping("/{id}")
    public NoteDto getById(@PathVariable String id) {
        return service.getById(id);
    }

    @PostMapping
    public NoteDto create(@RequestBody NoteDto note) {
        validator.validate(note);
        note.setId(null);
        return service.create(note);
    }

    @PutMapping("/{id}")
    public NoteDto update(@PathVariable String id,
                       @RequestBody NoteDto note) {
        validator.validate(note);
        return service.update(id, note);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @GetMapping("/{id}/stats")
    public Map<String, Long> getStats(@PathVariable String id) {
        return service.getStats(id);
    }
}
