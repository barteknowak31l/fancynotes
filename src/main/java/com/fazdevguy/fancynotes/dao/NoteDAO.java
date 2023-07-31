package com.fazdevguy.fancynotes.dao;

import com.fazdevguy.fancynotes.entity.Note;

import java.util.List;

public interface NoteDAO {


    void save(Note note);

    Note findNoteById(int id);

    List<Note> findAll();

    void deleteNoteById(int id);


}
