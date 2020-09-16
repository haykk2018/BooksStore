package com.card.website.repository;

import com.card.website.domain.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {

    Book findByLangAndLangId(Book.Lang lang, Integer langId);

}
