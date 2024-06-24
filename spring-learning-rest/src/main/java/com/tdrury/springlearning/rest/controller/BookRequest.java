package com.tdrury.springlearning.rest.controller;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookRequest {
    String title;
    String author;
}
