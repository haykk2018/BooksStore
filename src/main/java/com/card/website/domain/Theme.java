package com.card.website.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity // This tells Hibernate to make a table out of this class
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    private String themeId;

    @Size(min = 2, max = 90)
    private String name;

    private String description;

    @OneToMany(mappedBy = "theme", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Book> books;

}