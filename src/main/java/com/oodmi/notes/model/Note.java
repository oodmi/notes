package com.oodmi.notes.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.List;

@Data
@Accessors(chain = true)
@Document("note")
public class Note {

    @MongoId

    private Long id;
    private String title;
    private LocalDate createdDate = LocalDate.now();
    private String text;
    private List<Tag> tags;
}
