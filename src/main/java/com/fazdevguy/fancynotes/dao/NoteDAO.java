package com.fazdevguy.fancynotes.dao;

import com.fazdevguy.fancynotes.entity.CustomTextFields;
import com.fazdevguy.fancynotes.entity.Note;

import java.util.List;

public interface NoteDAO {


    Note save(Note note);

    void refresh(Note note);

    Note persist(Note note);

    Note findNoteById(int id);


    List<Note> findAll();
    List<Note> findAllByCategoryIdWithArchivedSpecified(int categoryId, boolean archived);

    void deleteNoteById(int id);

    CustomTextFields findCustomTextFieldsById(int id);

    void deleteCustomTextFieldById(int id);


}
