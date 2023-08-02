package com.fazdevguy.fancynotes.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "custom_text_fields")
public class CustomTextFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="field1name")
    private String field1name;
    @Column(name="field1value")
    private String field1value;
    @Column(name="field2name")
    private String field2name;
    @Column(name="field2value")
    private String field2value;

    @Column(name="field3name")
    private String field3name;
    @Column(name="field3value")
    private String field3value;

    @OneToOne(mappedBy = "customTextFields",
            cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    private Note note;

    public CustomTextFields(){}

    public CustomTextFields(String field1name, String field1value, String field2name, String field2value, String field3name, String field3value) {
        this.field1name = field1name;
        this.field1value = field1value;
        this.field2name = field2name;
        this.field2value = field2value;
        this.field3name = field3name;
        this.field3value = field3value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getField1name() {
        return field1name;
    }

    public void setField1name(String field1name) {
        this.field1name = field1name;
    }

    public String getField1value() {
        return field1value;
    }

    public void setField1value(String field1value) {
        this.field1value = field1value;
    }

    public String getField2name() {
        return field2name;
    }

    public void setField2name(String field2name) {
        this.field2name = field2name;
    }

    public String getField2value() {
        return field2value;
    }

    public void setField2value(String field2value) {
        this.field2value = field2value;
    }

    public String getField3name() {
        return field3name;
    }

    public void setField3name(String field3name) {
        this.field3name = field3name;
    }

    public String getField3value() {
        return field3value;
    }

    public void setField3value(String field3value) {
        this.field3value = field3value;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "CustomTextFields{" +
                "id=" + id +
                ", field1name='" + field1name + '\'' +
                ", field1value='" + field1value + '\'' +
                ", field2name='" + field2name + '\'' +
                ", field2value='" + field2value + '\'' +
                ", field3name='" + field3name + '\'' +
                ", field3value='" + field3value + '\'' +
                '}';
    }
}
