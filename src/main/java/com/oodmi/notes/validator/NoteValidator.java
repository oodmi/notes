package com.oodmi.notes.validator;

import com.oodmi.notes.dto.NoteDto;
import org.springframework.stereotype.Component;

@Component
public class NoteValidator {

    public void validate(NoteDto note) {
        if (note.getText() == null) {
            throw new IllegalArgumentException("text");
        }
        if (note.getTitle() == null) {
            throw new IllegalArgumentException("title");
        }
    }
}
