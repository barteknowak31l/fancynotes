package com.fazdevguy.fancynotes.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "note")
    private String note;


    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "category",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH},
            orphanRemoval = true
    )
    private List<Note> notesList;


    // create constructors
    public Category(){};

    public Category(String name) {
        this.name = name;
    }

    public Category(String name, String note) {
        this.name = name;
        this.note = note;
    }

    // getters setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Note> getNotesList() {
        return notesList;
    }

    public void setNotesList(List<Note> notesList) {
        this.notesList = notesList;
    }


    // toString


    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", note='" + note +
                '}';
    }


    // utility functions

    public void addNote(Note note){
        if(notesList == null){
            notesList = new ArrayList<>();
        }

        notesList.add(note);
        note.setCategory(this);

    }

    public boolean deleteNote(Note note){
        if(notesList != null){
            notesList.remove(note);
            note.setCategory(null);
            return true;
        }
        return false;
    }

}
