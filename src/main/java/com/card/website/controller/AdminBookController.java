package com.card.website.controller;

import com.card.website.domain.Author;
import com.card.website.domain.Book;
import com.card.website.repository.AuthorRepository;
import com.card.website.repository.BookRepository;
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
        if (bindingResult.hasErrors()) {
            return "bookAdmin/editAddBook";
        }
        bookRepository.save(book);
        return "redirect:/book-panel/main";
    }

    //      delete book
    @GetMapping(path = "/delete-book")
    public String pageDelete(@RequestParam Integer id) {

        bookRepository.deleteById(id);
        return "redirect:/book-panel/main";
    }

}