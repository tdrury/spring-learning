package com.tdrury.springlearning.data.jpa.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor(access=AccessLevel.PROTECTED, force=true)
@Data
@Entity
public class Author {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
//    private @Id @GeneratedValue Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
