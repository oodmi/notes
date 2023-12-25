package com.oodmi.notes.service;

import com.oodmi.notes.converter.NoteConverter;
import com.oodmi.notes.dto.NoteDto;
import com.oodmi.notes.model.Tag;
import com.oodmi.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository repository;
    private final NoteConverter converter;

    public List<NoteDto> getAll(int page, int sizePerPage, List<Tag> tags) {
        var pageable = PageRequest.of(page, sizePerPage, Sort.Direction.DESC, "createdDate");

        if (tags.isEmpty()) {
            return repository.findAll(pageable)
                    .stream()
                    .map(converter::convert)
                    .toList();
        } else {
            return repository.findAllFilterTags(tags, pageable)
                    .stream()
                    .map(converter::convert)
                    .toList();
        }
    }

    public NoteDto getById(String id) {
        return repository.findById(id)
                .map(converter::convertWithText)
                .orElseThrow(() -> new IllegalArgumentException("note does not exist"));
    }

    public NoteDto create(NoteDto noteDto) {
        var note = converter.convert(noteDto);
        repository.insert(note);

        return noteDto.setId(note.getId());
    }

    public NoteDto update(String id, NoteDto noteDto) {
        var saved = getById(id);
        noteDto.setId(saved.getId());
        var note = converter.convert(noteDto);
        repository.save(note);

        return noteDto;
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public Map<String, Long> getStats(String id) {
        var note = getById(id);

        return Arrays.stream(note.getText().split("\\s+"))
                .collect(Collectors.groupingBy(String::toLowerCase, Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
