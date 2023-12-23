package com.oodmi.notes.service;

import com.oodmi.notes.converter.NoteConverter;
import com.oodmi.notes.dto.NoteDto;
import com.oodmi.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository repository;
    private final NoteConverter converter;

    public List<NoteDto> getAll(int page, int sizePerPage) {
        var pageable = PageRequest.of(page, sizePerPage, Sort.Direction.DESC, "createdDate");

        return repository.findAll(pageable)
                .stream()
                .map(converter::convert)
                .toList();
    }

    public NoteDto getById(String id) {
        return repository.findById(id)
                .map(converter::convertWithText)
                .orElseThrow(() -> new IllegalArgumentException());
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
        var node = getById(id);

        String[] split = node.getText().split(" ");

        Map<String, Long> map = new HashMap<>();

        for (String s : split) {
            map.put(s, map.getOrDefault(s, 0L) + 1);
        }

        return sortByValue(map);
    }

    public Map<String, Long> sortByValue(Map<String, Long> map) {
        List<Map.Entry<String, Long>> list = new ArrayList<>(map.entrySet());
        list.sort((c1, c2) -> c2.getValue().compareTo(c1.getValue()));

        Map<String, Long> result = new LinkedHashMap<>();
        for (Map.Entry<String, Long> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }
}
