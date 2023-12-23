package com.oodmi.notes.converter;

import com.oodmi.notes.dto.NoteDto;
import com.oodmi.notes.model.Note;
import org.springframework.stereotype.Component;

@Component
public class NoteConverter {

    public Note convert(NoteDto dto) {
        return new Note()
                .setId(dto.getId())
                .setCreatedDate(dto.getCreatedDate())
                .setTitle(dto.getTitle())
                .setText(dto.getText())
                .setTags(dto.getTags());
    }

    public NoteDto convert(Note dto) {
        return new NoteDto()
                .setId(dto.getId())
                .setCreatedDate(dto.getCreatedDate())
                .setTitle(dto.getTitle())
                .setTags(dto.getTags());
    }

    public NoteDto convertWithText(Note dto) {
        return new NoteDto()
                .setId(dto.getId())
                .setCreatedDate(dto.getCreatedDate())
                .setTitle(dto.getTitle())
                .setText(dto.getText())
                .setTags(dto.getTags());
    }
}
