package com.company.bookstore.controller;

import com.company.bookstore.domain.Book;
import com.company.bookstore.domain.ContentPage;
import com.company.bookstore.repository.BookRepository;
import com.company.bookstore.repository.PageRepository;
import com.company.bookstore.repository.PagingBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Locale;
import java.util.Map;

@Controller // This means that this class is a Controller
public class MainController {

    @Autowired
    private PageRepository pageRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private PagingBookRepository pagingBookRepository;

    //  controller for open page with content template
    @GetMapping
    public String page(@RequestParam(name = "id", defaultValue = "1") Integer id, @RequestParam(name = "lang", defaultValue = "eng") String lang, Map<String, Object> model, Locale locale) {

        Iterable<ContentPage> contentPages = pageRepository.findByLangAndHiddenIsFalse(ContentPage.Lang.valueOf(locale.getLanguage()), Sort.by("menuSequence").ascending());
        ContentPage curPage = pageRepository.findByLangAndLangId(ContentPage.Lang.valueOf(locale.getLanguage()), id);

        model.put("curPage", curPage);
        model.put("pages", contentPages);

        return "page";
    }

    //  controller for open page with book template
    @GetMapping(path = "/book")
    public String bookPage(@RequestParam(name = "id", defaultValue = "1") Integer id, @RequestParam(name = "lang", defaultValue = "eng") String lang, Map<String, Object> model, Locale locale) {

//        Iterable<Page> pages = bookRepository.findByLangAndHiddenIsFalse(Page.Lang.valueOf(locale.getLanguage()), Sort.by("menuSequence").ascending());
        Book curBook = bookRepository.findByLangAndLangId(Book.Lang.valueOf(locale.getLanguage()), id);

        model.put("curBook", curBook);
        return "bookPage";
    }
//  by  https://www.kindsonthegenius.com/how-to-implement-pagination-in-spring-boot-with-thymeleaf/ article
    @GetMapping(path = "/books")
    public String books(@RequestParam(name = "page", defaultValue = "1") Integer id, Map<String, Object> model) {


        Pageable pageable = PageRequest.of(id - 1,5);
        Page<Book> books = pagingBookRepository.findAll(pageable);

        model.put("books", books);

        return "booksPage";
    }
}