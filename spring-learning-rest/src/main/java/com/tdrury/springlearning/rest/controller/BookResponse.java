package com.tdrury.springlearning.rest.controller;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookResponse extends BookRequest {
    int copyCount;

}
