package com.company.bookstore.repository;

import com.company.bookstore.domain.Parent;
import org.springframework.data.repository.CrudRepository;

public interface ParentRepository extends CrudRepository<Parent, Integer> {

    Parent findParentByParentId(String parentId);

}
