package com.fazdevguy.fancynotes.service;

import com.fazdevguy.fancynotes.dao.NoteDAO;
import com.fazdevguy.fancynotes.entity.Note;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService{

    private NoteDAO noteDAO;

    public NoteServiceImpl(){}

    @Autowired
    public NoteServiceImpl(NoteDAO dao){
        this.noteDAO = dao;
    }


    @Override
    public void save(Note note) {
        noteDAO.save(note);
    }

    @Override
    public Note findNoteById(int id) {
        return noteDAO.findNoteById(id);
    }

    @Override
    public List<Note> findAll() {
        return noteDAO.findAll();
    }

    @Override
    public void deleteNoteById(int id) {
        noteDAO.deleteNoteById(id);
    }
}
