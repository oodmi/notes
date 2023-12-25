package com.oodmi.notes.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
@Document("notes")
public class Note {

    @MongoId
    @Indexed(unique = true)
    private String id;
    private String title;
    private LocalDate createdDate = LocalDate.now();
    private String text;
    private List<Tag> tags = new ArrayList<>();
}
