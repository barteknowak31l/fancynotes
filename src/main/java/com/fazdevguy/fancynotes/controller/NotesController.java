package com.fazdevguy.fancynotes.controller;

import com.fazdevguy.fancynotes.entity.Category;
import com.fazdevguy.fancynotes.entity.Note;
import com.fazdevguy.fancynotes.service.CategoryService;
import com.fazdevguy.fancynotes.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private NoteService noteService;

    @GetMapping("/showAll")
    public String showAllNotes(@RequestParam(value = "categoryId") Integer categoryId,
                               @RequestParam(value = "archived", required = false) Boolean archived,
                               Model model){

        // check if archived is specified if no set default to false
        if(archived == null)
            archived=false;

        // todo: @RequestParam for archived, also button in view
        Category category = categoryService.findCategoryWithNotesWithArchivedSpecified(categoryId,archived);
        List<Note> notesList = category.getNotesList();

        // add model attribues
        model.addAttribute("category",category);
        model.addAttribute("notesList",notesList);
        model.addAttribute("archived",archived);

        return "notes-list";

    }

    @GetMapping("/addForm")
    public String showAddForm(@RequestParam(value = "categoryId") Integer categoryId,Model model){

        Category category = categoryService.findCategoryWithNotes(categoryId);
        model.addAttribute("note", new Note());
        model.addAttribute("category",category);


        return "add-note-form";
    }

    @PostMapping("/addNote")
    public String addNote(@ModelAttribute(value = "note") Note note,
                          Model model,
                          @RequestParam(value = "categoryId") Integer categoryId,
                          @RequestParam(value="updateParam", required = false) Boolean isUpdate){

        Category category = categoryService.findCategoryWithNotes(categoryId);

        // check if note has a name
        if(note.getName().isBlank()){
            model.addAttribute("note", new Note());
            model.addAttribute("emptyNameError",true);
            model.addAttribute("category",category);

            return "add-note-form";
        }

        // check if note already exist
        if(category.getNotesList().contains(note)){
            model.addAttribute("note", new Note());
            model.addAttribute("saveError",true);
            model.addAttribute("category",category);

            return "add-note-form";
        }

        // add note to category - this may require adding @RequestParam with categoryId and @RequestParam determining if it is an update instead
        try{

            if(isUpdate != null && isUpdate) {
                Note oldNote = noteService.findNoteById(note.getId());
                oldNote.deepCopy(note);
                noteService.save(oldNote);
            }
            else{
                category.addNote(note);
                categoryService.save(category);
            }

        }
        catch (Exception e)
        {
           // e.printStackTrace();

            if(isUpdate != null && isUpdate)
            {
                model.addAttribute("note", note);
                model.addAttribute("saveError",true);
                model.addAttribute("update",true);
            }
            else {
                model.addAttribute("note", new Note());
                model.addAttribute("saveError",true);
            }

            model.addAttribute("category",category);

            return "add-note-form";
        }

        return "redirect:/notes/showAll?categoryId="+categoryId;
    }


    @GetMapping("/update")
    public String updateNote(@RequestParam(value = "noteId") Integer noteId,

                             Model model){

        Note note = noteService.findNoteById(noteId);
        Category category = note.getCategory();
        model.addAttribute("note",note);
        model.addAttribute("category",category);

        model.addAttribute("update",true);

        return "add-note-form";

    }

    // get mapping for delete note
    @GetMapping("/delete")
    public String deleteNote(@RequestParam(value = "noteId") Integer noteId){

        Integer categoryId = noteService.findNoteById(noteId).getCategory().getId();
        noteService.deleteNoteById(noteId);

        return "redirect:/notes/showAll?categoryId="+categoryId;
    }

    @GetMapping("/switchArchived")
    public String switchArchived(@RequestParam(value = "noteId") int noteId,
                                 @RequestParam(value = "archived", required = false) Boolean archived,
                                 Model model){
        if(archived == null)
            archived = false;

        Note note = noteService.findNoteById(noteId);
        note.setArchived(!note.isArchived());
        noteService.save(note);

        Integer categoryId = note.getCategoryId();

        return "redirect:/notes/showAll?archived="+archived+"&categoryId="+categoryId;
    }


    //Get Mapping for showDetails @RequestParam noteId (int)
    @GetMapping("/showDetails")
    public String showNoteDetails(@RequestParam(value = "noteId") int noteId,
                                  Model model) {

        // get note from db
        Note note = noteService.findNoteById(noteId);

        // add note to the model
        model.addAttribute("note",note);


        return "show-note-details";
    }

}
