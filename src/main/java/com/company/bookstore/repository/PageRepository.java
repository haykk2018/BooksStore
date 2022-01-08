package com.company.bookstore.repository;

import com.company.bookstore.domain.ContentPage;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

@Transactional // it(@Transactional) put to works normal, without it give "Unable to access lob stream" error
public interface PageRepository extends CrudRepository<ContentPage, Integer> {

    List<ContentPage> findByLang(ContentPage.Lang lang, Sort sort);

    List<ContentPage> findByLangAndHiddenIsFalse(ContentPage.Lang lang, Sort sort);

    ContentPage findByLangAndLangId(ContentPage.Lang lang, Integer langId);

    ContentPage findByLangAndMenuSequence(ContentPage.Lang lang, Integer menuSequence);

    boolean existsByLangAndLangId(ContentPage.Lang lang, Integer langId);

    // when old Sequence great then current => pushing + 1
    @Modifying
    @Query("update ContentPage p SET p.menuSequence = p.menuSequence+1 WHERE p.menuSequence >=?1 AND p.lang = ?2")
    int pushSequenceOneStep(int mSequence, ContentPage.Lang lang);

    @Modifying
    @Query("update ContentPage p SET p.menuSequence = p.menuSequence+1 WHERE p.menuSequence >=?1 AND p.menuSequence < ?2  AND p.lang = ?3")
    int pushSequenceOneStep(int mSequence, int oldSequence, ContentPage.Lang lang);

    // when old Sequence less then current => pushing - 1
    @Modifying
    @Query("update ContentPage p SET p.menuSequence = p.menuSequence-1 WHERE p.menuSequence <=?1 AND p.lang = ?2")
    int pushSequenceOneStepToBack(int mSequence, ContentPage.Lang lang);

    @Modifying
    @Query("update ContentPage p SET p.menuSequence = p.menuSequence-1 WHERE p.menuSequence < ?1 AND p.menuSequence > ?2  AND p.lang = ?3")
    int pushSequenceOneStepToBack(int mSequence, int oldSequence, ContentPage.Lang lang);
}