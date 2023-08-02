package com.fazdevguy.fancynotes.dao;

import com.fazdevguy.fancynotes.entity.CustomTextFields;
import com.fazdevguy.fancynotes.entity.Note;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class NoteDAOImpl implements NoteDAO{

    private EntityManager em;

    public NoteDAOImpl(){}

    @Autowired
    public NoteDAOImpl(EntityManager entityManager){
        this.em = entityManager;
    }

    @Override
    @Transactional
    public void save(Note note) {
        em.merge(note);
    }

    @Override
    public Note findNoteById(int id) {
        return em.find(Note.class,id);
    }

    @Override
    public List<Note> findAll() {

        TypedQuery<Note> query = em.createQuery("from Note n", Note.class);
        List<Note> notesList = query.getResultList();

        return notesList;
    }

    @Override
    public List<Note> findAllByCategoryIdWithArchivedSpecified(int categoryId, boolean archived) {
        TypedQuery<Note> query = em.createQuery("select n from Note n where n.categoryId=:data and n.archived=:arch", Note.class);
        query.setParameter("data",categoryId);
        query.setParameter("arch",archived);
        List<Note> notesList = query.getResultList();

        return notesList;
    }

    @Override
    @Transactional
    public void deleteNoteById(int id) {

        Note note = em.find(Note.class,id);
        em.remove(note);
    }

    @Override
    public CustomTextFields findCustomTextFieldsById(int id) {

        TypedQuery<CustomTextFields> query = em.createQuery("from CustomTextFields where id=:data",CustomTextFields.class);
        query.setParameter("data",id);
        CustomTextFields ctf = query.getSingleResult();

        return ctf;


    }
}
