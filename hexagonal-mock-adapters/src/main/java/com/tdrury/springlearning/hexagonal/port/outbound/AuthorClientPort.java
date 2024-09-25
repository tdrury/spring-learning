package com.tdrury.springlearning.hexagonal.port.outbound;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public interface AuthorClientPort {

    AuthorDTO getAuthor(String id);

    @Data
    @NoArgsConstructor
    class AuthorDTO {
        String id;
        String lastName;
        String firstName;
    }
}
