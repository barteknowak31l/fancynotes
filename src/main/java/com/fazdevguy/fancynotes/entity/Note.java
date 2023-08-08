package com.fazdevguy.fancynotes.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "notes")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "category_id", insertable=false, updatable=false)
    private int categoryId;

    @Column()
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @Column(name = "remind")
    private boolean remind;

    @Column(name = "archived")
    private boolean archived;

    @ManyToOne(fetch = FetchType.EAGER,
                cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;


    @OneToMany(mappedBy = "note"
            ,cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CustomTextFields> customTextFields;

    public Note(){
        this.customTextFields = new ArrayList<>();
    }

    public Note(Integer categoryId){
        this.categoryId = categoryId;
        this.customTextFields = new ArrayList<>();
    }

    public Note(String name, String description) {
        this.name = name;
        this.description = description;
        this.customTextFields = new ArrayList<>();

    }

    public Note(String name, String description, Date startDate, Date endDate, boolean remind) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remind = remind;
        this.archived = false;
        this.customTextFields = new ArrayList<>();
    }

    public Note(String name, String description, Date startDate, Date endDate, boolean remind, List<CustomTextFields> customTextFields) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remind = remind;
        this.archived = false;
        this.customTextFields = customTextFields;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isRemind() {
        return remind;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public void setRemind(boolean remind) {
        this.remind = remind;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<CustomTextFields> getCustomTextFields() {
        return customTextFields;
    }

    public void setCustomTextFields(List<CustomTextFields> customTextFields) {
        this.customTextFields = customTextFields;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", remind=" + remind +
                ", archived=" + archived +
                '}';
    }

    // utilities

    public void deepCopy(Note note)
    {
        this.id = note.getId();
        this.categoryId = note.getCategoryId();
        this.name = note.getName();
        this.description = note.getDescription();
        this.startDate = note.getStartDate();
        this.endDate = note.getEndDate();
        this.remind = note.isRemind();
        this.archived=note.isArchived();
        this.customTextFields=note.getCustomTextFields();

        for( CustomTextFields ct : customTextFields)
            ct.setNote(this);

    }

    public void addCustomTextFieldToList(CustomTextFields ctf){
        if(customTextFields == null){
            customTextFields = new ArrayList<>();
        }

        customTextFields.add(ctf);
        ctf.setNote(this);

    }

    public void removeCustomTextFieldFromList(CustomTextFields ctf){
        if(customTextFields == null) return;

        customTextFields.remove(ctf);
        ctf.setNote(null);

    }

    public void removeCustomTextFieldFromListByIndex(Integer index) {
        if(customTextFields == null) return;
        customTextFields.get(index).setNote(null);
        customTextFields.remove(index);
    }
}
