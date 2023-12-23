package com.oodmi.notes.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.oodmi.notes.model.Tag;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Accessors(chain = true)
public class NoteDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String id;
    private String title;
    private LocalDate createdDate = LocalDate.now();
    private String text;
    private List<Tag> tags = new ArrayList<>();
}
