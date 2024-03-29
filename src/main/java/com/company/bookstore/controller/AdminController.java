package com.company.bookstore.controller;

import com.company.bookstore.domain.ContentPage;
import com.company.bookstore.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Map;


@Controller // This means that this class is a Controller
@RequestMapping(path = "/adminpanel") // This means URL's start with /demo (after Application path)
public class AdminController {

    @Autowired
    private PageRepository pageRepository;

    //      admin panel first page
    @GetMapping(path = "/main")
    public String mainAdmin(Map<String, Object> model) {
        Iterable<ContentPage> pages = pageRepository.findAll();

        model.put("pages", pages);

        return "admin/main";
    }
    //      add new page
    @GetMapping(path = "/new-page")
    public String pageAddEdit(@RequestParam(name = "id", required = false) Integer id, Map<String, Object> model) {

        ContentPage contentPage;

        if (id != null) {
            // if id nul its doing edit
            contentPage = pageRepository.findById(id).get();

        } else {
            // if isn't  nul its doing new
            contentPage = new ContentPage();
        }
        model.put("contentPage", contentPage);
        // pages need for menu sequence
        Iterable<ContentPage> pages = pageRepository.findByLang(contentPage.getLang(), Sort.by("menuSequence").ascending());
        model.put("pages", pages);

        return "admin/editAddPage";
    }

    @PostMapping(path = "/add-page")
    public String pageAdd(@Valid ContentPage contentPage, BindingResult bindingResult) {

        //validating unique filds
        //if we check it when pages create, isn't necessity to check it by edit
        if (contentPage.getId() == null && pageRepository.existsByLangAndLangId(contentPage.getLang(), contentPage.getLangId())) {
            bindingResult.rejectValue("langId", "messageCode", "The field must be unique. Page with your value already exist ");
        }
        if (contentPage.getBeginDate() == null) {
            contentPage.setBeginDate(new Date());
        } else {
            contentPage.setEditDate(new Date());
        }
        if (bindingResult.hasErrors()) {
            return "admin/editAddPage";
        }
        // time automatic set if isn't sent, they aren't important operations

        //if menu sequence isn't changed we don't push the sequence։ two case
        //1 create new page, when isn't exist page with current menu sequence in database
        //2 edit page, when page menu sequence remains the same
        ContentPage pageWhichPlaceWillToSave = pageRepository.findByLangAndMenuSequence(contentPage.getLang(), contentPage.getMenuSequence());


        if (contentPage.getId() == null && pageWhichPlaceWillToSave != null) {
            //in this case we add new page, and isn't exist old page, and us didn't need old page
            menuSequenceDisposer(contentPage, null);
        }
        else if (contentPage.getId() != null && (pageWhichPlaceWillToSave == null || (pageWhichPlaceWillToSave != null && pageWhichPlaceWillToSave.getId() != contentPage.getId()))) {
            ContentPage oldPage = pageRepository.findById(contentPage.getId()).get();
            menuSequenceDisposer(contentPage, oldPage.getMenuSequence());
        }

        pageRepository.save(contentPage);
        return "redirect:/adminpanel/main";
    }

    void menuSequenceDisposer(ContentPage page, Integer oldPageMenuSequence) {
        //when we add new page we need only to push +1
        if (oldPageMenuSequence == null) {
            pageRepository.pushSequenceOneStep(page.getMenuSequence(), page.getLang());
        }
        //   oldPageMenuSequence can't be equal menuSequence, because that case we already excluded
        else if (oldPageMenuSequence > page.getMenuSequence()) {
            pageRepository.pushSequenceOneStep(page.getMenuSequence(), oldPageMenuSequence, page.getLang());
        } else if (oldPageMenuSequence < page.getMenuSequence()) {
            pageRepository.pushSequenceOneStepToBack(page.getMenuSequence(), oldPageMenuSequence-1, page.getLang());
            page.setMenuSequence(page.getMenuSequence()-1);
        }
    }

    //      delete page
    @GetMapping(path = "/delete-page")
    public String pageDelete(@RequestParam Integer id) {

        pageRepository.deleteById(id);
        return "redirect:/adminpanel/main";
    }

    // ajax request from admin main page
    @GetMapping(path = "/get-pages")
    public String getPagesByLang(@RequestParam String lang, Map<String, Object> model) {

        Iterable<ContentPage> pages;

        if (lang.equals("all")) {
            pages = pageRepository.findAll();
        } else {
            pages = pageRepository.findByLang(ContentPage.Lang.valueOf(lang), Sort.by("menuSequence").ascending());
        }

        model.put("pages", pages);
        return "admin/ajaxPagesByLang :: table-by-pages";
    }

    // ajax request from admin edit or add page
    @GetMapping(path = "/get-sequence")
    public String getSequencesByLang(@RequestParam String lang, @RequestParam(name = "msequence", required = false) Integer msequence, Map<String, Object> model) {

        Iterable<ContentPage> pages = pageRepository.findByLangAndHiddenIsFalse(ContentPage.Lang.valueOf(lang), Sort.by("menuSequence").ascending());

        model.put("currentMenuSequence", msequence);
        model.put("pages", pages);
        return "admin/ajaxSequensesByLang :: menu-sequense";
    }

}