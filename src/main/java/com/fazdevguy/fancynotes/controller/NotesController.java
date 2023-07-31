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
    public String showAllNotes(@RequestParam(value = "categoryId") Integer categoryId, Model model){

        Category category = categoryService.findCategoryById(categoryId);
        List<Note> notesList = category.getNotesList();

        model.addAttribute("category",category);
        model.addAttribute("notesList",notesList);

        return "notes-list";

    }

    @GetMapping("/addForm")
    public String showAddForm(@RequestParam(value = "categoryId") Integer categoryId,Model model){

        Category category = categoryService.findCategoryWithNotesById(categoryId);
        model.addAttribute("note", new Note());
        model.addAttribute("category",category);


        return "add-note-form";
    }

    @PostMapping("/addNote")
    public String addNote(@ModelAttribute(value = "note") Note note,
                          Model model,
                          @RequestParam(value = "categoryId") Integer categoryId,
                          @RequestParam(value="updateParam", required = false) Boolean isUpdate){

        // check if note has a name
        if(note.getName().isBlank()){
            model.addAttribute("note", new Note());
            model.addAttribute("emptyNameError",true);
            return "add-note-form";
        }

        // check if note already exist
        Category category = categoryService.findCategoryWithNotesById(categoryId);
        if(category.getNotesList().contains(note)){
            model.addAttribute("note", new Note());
            model.addAttribute("saveError",true);
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




}
