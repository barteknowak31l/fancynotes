package com.fazdevguy.fancynotes.controller;

import com.fazdevguy.fancynotes.entity.Category;
import com.fazdevguy.fancynotes.entity.CustomTextFields;
import com.fazdevguy.fancynotes.entity.Note;
import com.fazdevguy.fancynotes.entity.User;
import com.fazdevguy.fancynotes.misc.NotesControllerErrorCodesImpl;
import com.fazdevguy.fancynotes.service.CategoryService;
import com.fazdevguy.fancynotes.service.NoteService;
import com.fazdevguy.fancynotes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private UserService userService;
    @Autowired
    private NotesControllerErrorCodesImpl errorCodes;

    @GetMapping("/categories/showAll")
    public String showAllCategories(Model model){

        List<Category> categoryList = categoryService.findAll();
        model.addAttribute("categoryList",categoryList);

        return "admin/admin-category-list";
    }

    @GetMapping("/notes/showAll")
    public String showAllNotes(@RequestParam(value = "categoryId") Integer categoryId,
                               @RequestParam(value = "archived", required = false) Boolean archived,
                               Model model){

        // check if archived is specified if no set default to false
        if(archived == null)
            archived=false;

        Category category = categoryService.findCategoryWithNotesWithArchivedSpecified(categoryId,archived);
        List<Note> notesList = category.getNotesList();

        // add model attribues
        model.addAttribute("category",category);
        model.addAttribute("notesList",notesList);
        model.addAttribute("archived",archived);

        return "admin/admin-notes-list";

    }


    @PostMapping("/categories/addCategory")
    public String addCategory(@ModelAttribute("category") Category category, Model model,
                              @RequestParam(value = "updateParam", required = false) Boolean isUpdate,
                              @RequestParam(value = "oldCategoryId", required = false) Integer oldCategoryId,
                              @RequestParam(value = "userId") String username){


        //todo somehow get user!!

        if(category.getName().isBlank())
        {
            model.addAttribute("category", category);
            model.addAttribute("emptyNameError",true);
            return "admin/add-category-form";
        }

        // get user db handle
        User user = userService.findUserByUsernameWithCategories(username);

        // add category to user and save
        try{

            if(isUpdate != null && isUpdate){
                Category oldCategory = categoryService.findCategoryById(oldCategoryId);
                oldCategory.setName(category.getName());
                oldCategory.setNote(category.getNote());
                categoryService.save(oldCategory);
            }

            else{
                user.addCategory(category);
                userService.save(user);
            }

        }
        catch (Exception e){

            // e.printStackTrace();
            model.addAttribute("category", new Category());
            model.addAttribute("saveError",true);
            return "/admin/add-category-form";
        }

        // redirect to category list
        return "redirect:/admin/categories/showAll";

    }

    @GetMapping("/categories/update")
    public String updateCategory(@RequestParam("categoryId") int categoryId,
                                 @RequestParam("userId") String username,
                                 Model model){

        Category category = categoryService.findCategoryById(categoryId);

        model.addAttribute("category",category);
        model.addAttribute("oldCategoryId",category.getId());

        model.addAttribute("username",username);

        model.addAttribute("update",true);

        return "admin/admin-add-category-form";

    }


    // delete mapping
    @GetMapping("/categories/delete")
    public String deleteCategory(@RequestParam(value = "categoryId") Integer categoryId){

        categoryService.deleteCategoryById(categoryId);

        return "redirect:/admin/categories/showAll";
    }

    @GetMapping("/notes/switchArchived")
    public String switchArchived(@RequestParam(value = "noteId") int noteId,
                                 @RequestParam(value = "archived", required = false) Boolean archived,
                                 Model model){
        if(archived == null)
            archived = false;

        Note note = noteService.findNoteById(noteId);
        note.setArchived(!note.isArchived());
        noteService.save(note);

        Integer categoryId = note.getCategoryId();

        return "redirect:/admin/notes/showAll?archived="+archived+"&categoryId="+categoryId;
    }

    @GetMapping("/notes/update")
    public String updateNote(@RequestParam(value = "noteId") Integer noteId,
                             Model model){

        Note note = noteService.findNoteById(noteId);
        Category category = note.getCategory();
        model.addAttribute("note",note);
        model.addAttribute("category",category);
        model.addAttribute("customTextFields",note.getCustomTextFields());
        model.addAttribute("update",true);


        return "admin/admin-add-note-form";

    }

    @PostMapping(value = "/notes/addNote", params = "save")
    public String addNoteSave(@ModelAttribute(value = "note") Note note,
                              @ModelAttribute(value = "customTextFields") ArrayList<CustomTextFields> ctf,
                              Model model,
                              @RequestParam(value="updateParam", required = false) Boolean isUpdate) {

        Category category = categoryService.findCategoryWithNotes(note.getCategoryId());


        String noteValidationResult = validateNoteBeforeAddOrUpdate(note);

        // check if note has a name
        if (noteValidationResult.equals(errorCodes.getCode(NotesControllerErrorCodesImpl.ErrorCodes.EMPTY_NAME_ERROR))) {

            model.addAttribute("note", note);
            model.addAttribute("emptyNameError", true);
            model.addAttribute("category", category);
            model.addAttribute("customTextFields", note.getCustomTextFields());

            return "admin/admin-add-note-form";
        }


        try {

            if (isUpdate != null && isUpdate) { // THIS IS FOR UPDATE - ALSO NEW WITH CTF INCLUDED (its treated as update)

                note = updateAndSaveNote(note);
            } else { // THIS IS FOR NEW NOTE
                note = saveNote(note, category, ctf);
            }

        } catch (Exception e) // check if there was database error
        {
            if (isUpdate != null && isUpdate) // just for update
            {
                model.addAttribute("note", note);
                model.addAttribute("update", true);
            } else { // just for new note
                model.addAttribute("note", new Note(note.getCategoryId()));
            }


            // here for both
            model.addAttribute("saveError", true);
            model.addAttribute("category", category);
            model.addAttribute("customTextFields", note.getCustomTextFields());

            return "admin/admin-add-note-form";

        }

        return "redirect:/admin/notes/showAll?categoryId=" + note.getCategoryId();
    }

    @PostMapping(value = "/notes/addNote", params = "addCtf")
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

            return "admin/admin-add-note-form";
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

            return "admin/admin-add-note-form";

        }

        model.addAttribute("note", note);
        model.addAttribute("update",true);
        model.addAttribute("category",category);
        model.addAttribute("customTextFields",note.getCustomTextFields());

        return "admin/admin-add-note-form";

    }

    @PostMapping(value = "/notes/addNote", params = "removeCtf")
    public String deleteCtf(@RequestParam("removeCtf") String removeCtfId,Model model)
    {

        Integer ctfId = Integer.parseInt(removeCtfId);

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

        return "admin/admin-add-note-form";
    }

    @GetMapping("/notes/delete")
    public String deleteNote(@RequestParam(value = "noteId") Integer noteId){

        Integer categoryId = noteService.findNoteById(noteId).getCategory().getId();
        noteService.deleteNoteById(noteId);

        return "redirect:/admin/notes/showAll?categoryId="+categoryId;
    }

    @GetMapping("/notes/showDetails")
    public String showNoteDetails(@RequestParam(value = "noteId") int noteId,
                                  Model model) {

        // get note from db
        Note note = noteService.findNoteById(noteId);

        // add note to the model
        model.addAttribute("note",note);


        return "admin/admin-show-note-details";
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
