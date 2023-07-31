package com.fazdevguy.fancynotes.service;

import com.fazdevguy.fancynotes.entity.Note;

import java.util.List;

public interface NoteService {

    void save(Note note);

    Note findNoteById(int id);

    List<Note> findAll();

    void deleteNoteById(int id);
}
