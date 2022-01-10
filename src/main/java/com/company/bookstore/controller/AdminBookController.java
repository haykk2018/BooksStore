package com.company.bookstore.controller;

import com.company.bookstore.domain.Author;
import com.company.bookstore.domain.Book;
import com.company.bookstore.repository.AuthorRepository;
import com.company.bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;


@Controller // This means that this class is a Controller
@RequestMapping(path = "/book-panel") // This means URL's start with /demo (after Application path)
public class AdminBookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping(path = "/main")
    public String mainBookAdmin(Map<String, Object> model) {

        Iterable<Book> books = bookRepository.findAll();

        model.put("books", books);

        return "bookAdmin/main";
    }

    @GetMapping(path = "/editAddBook")
    public String bookAddEdit(@RequestParam(name = "id", required = false) Integer id, Map<String, Object> model) {

        Book book;
        if (id == null) {
            book = new Book();
        } else {
            book = bookRepository.findById(id).get();
        }
        Iterable<Author> authors = authorRepository.findAll();

        model.put("authors", authors);
        model.put("book", book);

        return "bookAdmin/editAddBook";
    }

    //      add new page
    @PostMapping(path = "/save-book")
    public String bookSave(@Valid Book book, BindingResult bindingResult) {
        //validating unique filds
        if (book.getId() == null && bookRepository.existsByLangAndLangId(book.getLang(), book.getLangId())) {
            bindingResult.rejectValue("langId", "messageCode", "The field must be unique. Page with your value already exist ");
        }

        if (bindingResult.hasErrors()) {
            return "bookAdmin/editAddBook";
        }
        bookRepository.save(book);
        return "redirect:/book-panel/main";
    }

    //      delete book
    @GetMapping(path = "/delete-book")
    public String bookDelete(@RequestParam Integer id) {

        bookRepository.deleteById(id);
        return "redirect:/book-panel/main";
    }

    @GetMapping(path = "/authors")
    public String bookAuthorsAdmin(Map<String, Object> model) {

        Iterable<Author> authors = authorRepository.findAll();

        model.put("authors", authors);

        return "bookAdmin/authors";
    }

    @GetMapping(path = "/editAddAuthor")
    public String authorAddEdit(@RequestParam(name = "id", required = false) Integer id, Map<String, Object> model) {

        Author author;
        if (id == null) {
            author = new Author();
        } else {
            author = authorRepository.findById(id).get();
        }

        model.put("author", author);

        return "bookAdmin/editAddAuthor";
    }

    //      add new page
    @PostMapping(path = "/save-author")
    public String authorSave(@Valid Author author, BindingResult bindingResult) {

        //validating unique filds
        if (bindingResult.hasErrors()) {
            return "bookAuthor/editAddAuthor";
        }
        authorRepository.save(author);
        return "redirect:/book-panel/authors";
    }

    //      delete book
    @GetMapping(path = "/delete-author")
    public String authorDelete(@RequestParam Integer id) {

        authorRepository.deleteById(id);
        return "redirect:/book-panel/authors";
    }

}