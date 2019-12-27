package com.tdrury.example.spring.data.jpa.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

//public interface AuthorRepository extends PagingAndSortingRepository<Author, Long> {
public interface AuthorRepository extends JpaRepository<Author, Long> {

    List<Author> findByLastName(@Param("lastName") String lastName);
}
