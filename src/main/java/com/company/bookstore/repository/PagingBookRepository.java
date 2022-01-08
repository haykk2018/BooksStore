package com.company.bookstore.repository;

import com.company.bookstore.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface PagingBookRepository extends CrudRepository<Book, Integer> {
    Page<Book> findAll(Pageable pageable);

//    Page<Book> findByTag(String tag, Pageable pageable);

}
