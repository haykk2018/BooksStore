package com.card.website.controller;

import com.card.website.domain.Book;
import com.card.website.domain.Page;
import com.card.website.repository.BookRepository;
import com.card.website.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    //  controller for open page with content template
    @GetMapping
    public String page(@RequestParam(name = "id", defaultValue = "1") Integer id, @RequestParam(name = "lang", defaultValue = "eng") String lang, Map<String, Object> model, Locale locale) {

        Iterable<Page> pages = pageRepository.findByLangAndHiddenIsFalse(Page.Lang.valueOf(locale.getLanguage()), Sort.by("menuSequence").ascending());
        Page curPage = pageRepository.findByLangAndLangId(Page.Lang.valueOf(locale.getLanguage()),id);

        model.put("curPage", curPage);
        model.put("pages", pages);


        return "page";
    }

    //  controller for open page with book template
    @GetMapping(path = "/book")
    public String bookPage(@RequestParam(name = "id", defaultValue = "1") Integer id, @RequestParam(name = "lang", defaultValue = "eng") String lang, Map<String, Object> model, Locale locale) {

//        Iterable<Page> pages = bookRepository.findByLangAndHiddenIsFalse(Page.Lang.valueOf(locale.getLanguage()), Sort.by("menuSequence").ascending());
        Book curBook = bookRepository.findByLangAndLangId(Book.Lang.valueOf(locale.getLanguage()),id);

        model.put("curBook", curBook);
//        model.put("pages", pages);


        return "bookPage";
    }

}