package com.oodmi.notes.repository;

import com.oodmi.notes.model.Note;
import com.oodmi.notes.model.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    @Query(value = "{tags: { $all: ?0 }}")
    List<Note> findAllFilterTags(List<Tag> tags, PageRequest pageable);
}