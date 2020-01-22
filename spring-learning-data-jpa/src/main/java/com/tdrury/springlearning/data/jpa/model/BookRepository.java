package com.tdrury.springlearning.data.jpa.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, String> {

    List<Book> findByTitle(@Param("title") String title);

    List<Book> findByAuthorsContaining(Author a);
}
