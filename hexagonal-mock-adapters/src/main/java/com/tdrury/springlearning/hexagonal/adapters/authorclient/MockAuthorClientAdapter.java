package com.tdrury.springlearning.hexagonal.adapters.authorclient;

import com.tdrury.springlearning.hexagonal.port.outbound.AuthorClientPort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockAuthorClientAdapter implements AuthorClientPort {

    @Override
    public AuthorDTO getAuthor(String id) {
        return null;
    }
}
