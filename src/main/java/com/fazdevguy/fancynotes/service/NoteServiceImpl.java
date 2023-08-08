package com.fazdevguy.fancynotes.service;

import com.fazdevguy.fancynotes.dao.NoteDAO;
import com.fazdevguy.fancynotes.entity.CustomTextFields;
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
    public Note save(Note note) {
       return noteDAO.save(note);
    }

    @Override
    public void refresh(Note note) {
        noteDAO.refresh(note);
    }

    @Override
    public Note persist(Note note) {
        return noteDAO.persist(note);
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
    public List<Note> findAllByCategoryIdWithArchivedSpecified(int categoryId, boolean archived){
        return noteDAO.findAllByCategoryIdWithArchivedSpecified(categoryId,archived);
    }

    @Override
    public void deleteNoteById(int id) {
        noteDAO.deleteNoteById(id);
    }

    @Override
    public CustomTextFields findCustomTextFieldsById(int id) {
        return noteDAO.findCustomTextFieldsById(id);
    }

    @Override
    public void deleteCustomTextFieldById(int id) {
        noteDAO.deleteCustomTextFieldById(id);
    }
}
