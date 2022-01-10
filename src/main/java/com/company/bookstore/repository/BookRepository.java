package com.company.bookstore.repository;

import com.company.bookstore.domain.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Integer> {

    Book findByLangAndLangId(Book.Lang lang, Integer langId);

    boolean existsByLangAndLangId(Book.Lang lang, Integer langId);

}
