package com.oodmi.notes.repository;

import com.oodmi.notes.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends MongoRepository<Note, Long> {

//    @Query("{name:'?0'}")
//    Note findItemByName(String name);

//    @Query(value="{category:'?0'}", fields="{'name' : 1, 'quantity' : 1}")
//    List<Note> findAll(String category);
}