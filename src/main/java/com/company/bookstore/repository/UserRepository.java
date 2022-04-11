package com.company.bookstore.repository;


import com.company.bookstore.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {

    User findByUsername(String userName);

}
