package com.tdrury.springlearning.data.jpa.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access= AccessLevel.PROTECTED, force=true)
@Data
@Entity
public class Book {

    @Id
    private String isbn;

    @Column(nullable = false)
    private String title;

    @ManyToMany
    @JoinTable(name="author_book",
    joinColumns = @JoinColumn(name="author_id"),
    inverseJoinColumns = @JoinColumn(name="book_isbn"))
    private List<Author> authors;

    public Book(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }

}
