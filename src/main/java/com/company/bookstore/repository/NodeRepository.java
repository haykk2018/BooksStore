package com.company.bookstore.repository;

import com.company.bookstore.domain.Node;
import com.company.bookstore.domain.Parent;
import org.springframework.data.repository.CrudRepository;


public interface NodeRepository extends CrudRepository<Node, Integer> {

    Iterable<Node> findAllByParentId(String parentId);

    Iterable<Node> findAllByParent(Parent p);

//    Iterable<Node> findAllByStartDailyTimeBeforeAndEndDailyTimeAfter(LocalTime time,LocalTime time2);
//
//    Iterable<Node> findAllByStartDailyTimeBeforeAndEndDailyTimeAfterAndOpenedFalse(LocalTime time,LocalTime time2);
//
//    Iterable<Node> findAllByStartDailyTimeAfterOrEndDailyTimeBeforeAndOpenedIsTrue(LocalTime time,LocalTime time2);

    void deleteByParent(String parentId);

    void deleteByParent(Parent parentId);

    void deleteAllByParent(String parentId);

    void deleteAllByParent(Parent parentId);

    Iterable<Parent> deleteAllByParentId(String p);

}