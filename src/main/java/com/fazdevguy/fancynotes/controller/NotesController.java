package com.fazdevguy.fancynotes.controller;

import com.fazdevguy.fancynotes.entity.Category;
import com.fazdevguy.fancynotes.entity.CustomTextFields;
import com.fazdevguy.fancynotes.entity.Note;
import com.fazdevguy.fancynotes.misc.CustomFieldFormData;
import com.fazdevguy.fancynotes.misc.NotesControllerErrorCodesImpl;
import com.fazdevguy.fancynotes.service.CategoryService;
import com.fazdevguy.fancynotes.service.NoteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    private NotesControllerErrorCodesImpl errorCodes;

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

        Note note = new Note(categoryId);
        model.addAttribute("note",note);
        model.addAttribute("category",category);
        model.addAttribute("customTextFields",note.getCustomTextFields());

        return "add-note-form";
    }



    @PostMapping(value = "/addNote", params = "save")
    public String addNoteSave(@ModelAttribute(value = "note") Note note,
                              @ModelAttribute(value = "customTextFields")ArrayList<CustomTextFields> ctf,
                              Model model,
                              @RequestParam(value="updateParam", required = false) Boolean isUpdate){


        System.out.println("=====>>> IN SAVE VERSION");
        Category category = categoryService.findCategoryWithNotes(note.getCategoryId());


        String noteValidationResult = validateNoteBeforeAddOrUpdate(note);

        // check if note has a name
        if(noteValidationResult.equals(errorCodes.getCode(NotesControllerErrorCodesImpl.ErrorCodes.EMPTY_NAME_ERROR))){

            model.addAttribute("note", new Note(note.getCategoryId()));
            model.addAttribute("emptyNameError",true);
            model.addAttribute("category",category);
            model.addAttribute("customTextFields",note.getCustomTextFields());

            return "add-note-form";
        }


        // add note to category - this may require adding @RequestParam with categoryId and @RequestParam determining if it is an update instead
        try{

            if(isUpdate != null && isUpdate) { // THIS IS FOR UPDATE - ALSO NEW WITH CTF INCLUDED (its treated as update)

                note = updateAndSaveNote(note);
            }
            else{ // THIS IS FOR NEW NOTE
                note = saveNote(note,category,ctf);
            }

        }
        catch (Exception e) // check if there was database error
        {
            if(isUpdate != null && isUpdate) // just for update
            {
                model.addAttribute("note", note);
                model.addAttribute("update",true);
            }
            else { // just for new note
                model.addAttribute("note", new Note(note.getCategoryId()));
            }


            // here for both
            model.addAttribute("saveError",true);
            model.addAttribute("category",category);
            model.addAttribute("customTextFields",note.getCustomTextFields());

            return "add-note-form";
        }


        return "redirect:/notes/showAll?categoryId="+note.getCategoryId();
    }

    @PostMapping(value = "/addNote", params = "addCtf")
    public String addNoteAddCtf(@ModelAttribute(value = "note") Note note,
                                @ModelAttribute(value = "customTextFields")ArrayList<CustomTextFields> ctf,
                          Model model,
                          @RequestParam(value="updateParam", required = false) Boolean isUpdate){


        Category category = categoryService.findCategoryWithNotes(note.getCategoryId());

        // validate note
        String noteValidationResult = validateNoteBeforeAddOrUpdate(note);


        // check if note has a name
        if(noteValidationResult.equals(errorCodes.getCode(NotesControllerErrorCodesImpl.ErrorCodes.EMPTY_NAME_ERROR))){

            model.addAttribute("note", note);
            model.addAttribute("emptyNameError",true);
            model.addAttribute("category",category);
            model.addAttribute("customTextFields",note.getCustomTextFields());

            return "add-note-form";
        }


        // add note to category - this may require adding @RequestParam with categoryId and @RequestParam determining if it is an update instead
        try{

            if(isUpdate != null && isUpdate) { // THIS IS UPDATE
                note = updateAndSaveNoteWithCTF(note);
            }
            else{  // THIS IS NEW NOTE
                note = saveNoteWithCTF(note,category);
            }

        }
        catch (Exception e) // check if there was database error
        {
            // e.printStackTrace();

            if(isUpdate != null && isUpdate) // for update
            {
                model.addAttribute("note", note);
                model.addAttribute("saveError",true);
                model.addAttribute("update",true);
            }
            else { // for new

                model.addAttribute("note", note);
                model.addAttribute("saveError",true);
            }
            // for both
            model.addAttribute("category",category);
            model.addAttribute("customTextFields",note.getCustomTextFields());

            return "add-note-form";
        }



        model.addAttribute("note", note);
        model.addAttribute("update",true);
        model.addAttribute("category",category);
        model.addAttribute("customTextFields",note.getCustomTextFields());

        return "add-note-form";
    }


    @GetMapping("/update")
    public String updateNote(@RequestParam(value = "noteId") Integer noteId,
                             Model model){

        Note note = noteService.findNoteById(noteId);
        Category category = note.getCategory();
        model.addAttribute("note",note);
        model.addAttribute("category",category);
        model.addAttribute("customTextFields",note.getCustomTextFields());
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


    @PostMapping(value = "/addNote", params = "removeCtf")
    public String deleteCtf(@RequestParam("removeCtf") String removeCtfId,Model model)
    {

        Integer ctfId = Integer.parseInt(removeCtfId);
        System.out.println("=====>>> ctfID " + ctfId);

        CustomTextFields ctf = noteService.findCustomTextFieldsById(ctfId);

        // fill note, category from ctfObject
        Note note = ctf.getNote();
        Category category = note.getCategory();

        //delete reference from note and delete ctf object
        note.removeCustomTextFieldFromList(ctf);
        noteService.deleteCustomTextFieldById(ctfId);

        //save note object
        noteService.save(note);

        model.addAttribute("note",note);
        model.addAttribute("category",category);
        model.addAttribute("customTextFields",note.getCustomTextFields());
        model.addAttribute("update",true);

        return "add-note-form";
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

// utilities

    private String validateNoteBeforeAddOrUpdate(Note note){

        if(note.getName().isBlank())
            return errorCodes.getCode(NotesControllerErrorCodesImpl.ErrorCodes.EMPTY_NAME_ERROR);


        return errorCodes.getCode(NotesControllerErrorCodesImpl.ErrorCodes.OK);
    }


    private Note updateAndSaveNote(Note note) {
        Note oldNote = noteService.findNoteById(note.getId());
        oldNote.deepCopy(note);
        return noteService.save(oldNote);
    }

    private Note saveNote(Note note, Category category, ArrayList<CustomTextFields> ctf) {
        note.setCustomTextFields(ctf);
        category.addNote(note);
        return noteService.save(note);
    }

    private Note updateAndSaveNoteWithCTF(Note note) {
        Note oldNote = noteService.findNoteById(note.getId());
        oldNote.deepCopy(note);
        oldNote.addCustomTextFieldToList( new CustomTextFields());

        return noteService.save(oldNote);
    }

    private Note saveNoteWithCTF(Note note, Category category) {
        note.addCustomTextFieldToList(new CustomTextFields());
        category.addNote(note);
        return noteService.save(note);
    }



}


