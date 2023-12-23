package com.oodmi.notes.service;

import com.oodmi.notes.model.Note;
import com.oodmi.notes.repository.NoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository repository;

    public List<Note> getAll() {
        return repository.findAll();
    }

    public Note getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException());
    }

    public Note create(Note note) {
        return repository.save(note);
    }

    public Note update(Long id, Note note) {
        return repository.save(note.setId(id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Map<String, Long> getStats(Long id) {
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
