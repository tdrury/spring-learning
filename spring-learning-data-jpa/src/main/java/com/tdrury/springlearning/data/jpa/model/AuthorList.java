package com.tdrury.springlearning.data.jpa.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthorList {
    List<Author> authors;

    public AuthorList() {
        authors = new ArrayList<>();
    }
}
