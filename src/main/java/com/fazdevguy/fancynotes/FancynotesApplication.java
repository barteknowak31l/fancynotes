package com.fazdevguy.fancynotes;

import com.fazdevguy.fancynotes.dao.UserDAO;
import com.fazdevguy.fancynotes.entity.Category;
import com.fazdevguy.fancynotes.entity.Note;
import com.fazdevguy.fancynotes.entity.User;
import com.fazdevguy.fancynotes.service.CategoryService;
import com.fazdevguy.fancynotes.service.NoteService;
import com.fazdevguy.fancynotes.service.UserService;
import org.hibernate.Hibernate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class FancynotesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FancynotesApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserService userService, CategoryService categoryService, NoteService noteService){
		return runner -> {

			//addUsers(userService);

			//findUserById(userService);

			//findUserByUsername(userService);

			//findAllUsers(userService);

			//findUserWithCategories(userService);

			//deleteUser(userService);

			//addCategoriesToUser(userService,categoryService);

			//findAllCategories(userService,categoryService);

			//deleteCategory(userService,categoryService);

			//deleteCategoryById(categoryService);

			//addNotes(categoryService,noteService);

			//findAllNotes(noteService);

			//findCategoryWithNotes(categoryService);

			//deleteNote(noteService);
			//deleteNoteViaItsCategory(categoryService);


		};
	}

	private void deleteCategoryById(CategoryService categoryService) {

		int id = 2;
		System.out.println("Deleting category with id: " + id);
		categoryService.deleteCategoryById(id);
		System.out.println("Done!");

	}

	private void findUserWithCategories(UserService userService) {


		int id = 3;
		System.out.println("Finding user with id: " + id);
		User user = userService.findUserWithCategoriesById(id);
		System.out.println("User found: " + user);

		System.out.println("Categories: " + user.getCategoryList());
		System.out.println("Done!");

	}

	private void deleteNoteViaItsCategory(CategoryService categoryService) {

		int id = 4;
		System.out.println("finding category with id: " +id);
		Category category = categoryService.findCategoryWithNotesById(id);
		System.out.println("Category found: " + category);
		System.out.println("Notes: " + category.getNotesList());

		Note note = category.getNotesList().get(0);
		System.out.println("Removing note: " + note);
		category.deleteNote(note);
		System.out.println("Note deleted, save category and load it again");
		categoryService.save(category);

		Category loadedCategory = categoryService.findCategoryWithNotesById(id);
		System.out.println("Loaded category notes: " + loadedCategory.getNotesList());
		System.out.println("Done!");


	}

	private void deleteNote(NoteService noteService) {

		int id = 6;
		System.out.println("Finding note with id: " + id);
		Note note = noteService.findNoteById(id);
		System.out.println("Note found: " + note);

		System.out.println("Deleting note");
		noteService.deleteNoteById(note.getId());

		System.out.println("Done!");

	}

	private void findCategoryWithNotes(CategoryService categoryService) {

		int id = 4;
		System.out.println("Finding category with id: " + id);
		Category category = categoryService.findCategoryWithNotesById(id);
		System.out.println("Category found: " + category);

		System.out.println("Notes: " + category.getNotesList());
		System.out.println("Done!");
	}

	private void findAllNotes(NoteService noteService) {

		System.out.println("Finding all notes");
		List<Note> noteList = noteService.findAll();
		System.out.println("Notes found: " + noteList);
		System.out.println("Done!");
	}

	private void addNotes(CategoryService categoryService, NoteService noteService) {

		int id = 2;
		System.out.println("Finding category with id: " + id);
		Category category = categoryService.findCategoryById(id);
		System.out.println("Found category: " + category);

		System.out.println("Creating notes and adding them to the category");

		Note note1 = null;
		try {
			note1 = new Note("Nothing else matters","Author: Metallica", new SimpleDateFormat("yyyy-MM-dd").parse("2023-07-01"),
					new SimpleDateFormat("yyyy-MM-dd").parse("2023-10-01"),true);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		Note note2 = new Note("Whole lotta love","Author: Led Zeppelin");

		category.addNote(note1);
		category.addNote(note2);

		System.out.println("Save category and its new notes");
		categoryService.save(category);

		System.out.println("Load that category again from database and check for its notes");
		Category loadedCategory = categoryService.findCategoryById(id);
		System.out.println("Loaded category notes: " + loadedCategory.getNotesList());

		System.out.println("Done!");

	}

	private void deleteCategory(UserService userService, CategoryService categoryService) {

		// find category id through userService, delete it through categoryService
		int id = 1;
		System.out.println("Finding user by id: " + id);
		User user = userService.findUserWithCategoriesById(id);
		System.out.println("User found: "+ user);

		System.out.println("Finding users categories");
		List<Category> categoryList = user.getCategoryList();

		System.out.println("Categories: " + categoryList);
		Category categoryToDelete = categoryList.get(0);
		System.out.println("Delete 1st category on the list: " + categoryToDelete);
		System.out.println(user.deleteCategory(categoryToDelete));
		System.out.println("User categories after deletion: " + user.getCategoryList());

		System.out.println("Make sure category is deleted ... save it and load from db");
		userService.save(user);
		User userAfterDelete = userService.findUserWithCategoriesById(id);
		System.out.println("Categories after deletion: " + userAfterDelete.getCategoryList());

		System.out.println("Done!");

	}

	private void findAllCategories(UserService userService, CategoryService categoryService) {

		System.out.println("Finding all categories directly through category service: ");
		List<Category> categoryList1 = categoryService.findAll();
		System.out.println("Categories found: " + categoryList1);

		int id = 3;
		System.out.println("Finding all categories of user id: " + id);
		List<Category> categoryList2 = userService.findUserById(id).getCategoryList();
		System.out.println("Categories found: " + categoryList2);
		System.out.println("Done!");

	}

	private void addCategoriesToUser(UserService userService,CategoryService categoryService) {

		int id = 3;
		System.out.println("Finding user with id: "+ id);

		User user = userService.findUserById(id);

		System.out.println("User: " + user);

		System.out.println("Adding categories to user ...");

		Category cat1 = new Category("Books","Books I want to read this summer");
		Category cat2 = new Category("Songs","Songs to master on my guitar");

		user.addCategory(cat1);
		user.addCategory(cat2);

		userService.save(user);

		System.out.println("Done!");




	}

	private void deleteUser(UserService userService) {

		int id =  3;
		System.out.println("Deleting user with id: "+ id);
		userService.deleteUserById(id);
		System.out.println("Done!");

	}

	private void findAllUsers(UserService userService) {
		System.out.println("Finding all users");

		List<User> users = userService.findAllUsers();

		System.out.println("Users: " + users);

	}

	private void findUserByUsername(UserService userService) {

		String username = "Ptoszek";
		System.out.println("Finding user with username: " + username);

		User user = userService.findUserByUsername(username);

		System.out.println("User: " + user);

	}

	private void findUserById(UserService userService) {

		int theId = 1;
		System.out.println("Finding user with id: " + theId);

		User user = userService.findUserById(theId);

		System.out.println("User: " + user);

	}

	private void addUsers(UserService userService) {

		System.out.println("Creating users and saving to database ...");
		User user1 = new User("Maciek123","123", "Maciej", "Nowak");
		User user2 = new User("Ptoszek","123");
		User user3 = new User("SnowNeedle","123", "Arya", "Stark");

		userService.save(user1);
		userService.save(user2);
		userService.save(user3);

		System.out.println("Done!");

	}

}
