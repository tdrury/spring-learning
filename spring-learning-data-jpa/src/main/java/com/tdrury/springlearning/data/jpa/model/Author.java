package com.tdrury.springlearning.data.jpa.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor(access=AccessLevel.PROTECTED, force=true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @ManyToMany(mappedBy = "authors", fetch = FetchType.EAGER)
    private List<Book> books = new ArrayList<>();

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Need to define our own toString. If we use Lombok we get an infinite loop and stack overflow.
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Author(id=").append(id)
          .append(", firstName=").append(firstName)
          .append(", lastName=").append(lastName)
          .append(", books=[");
//        if (books != null) {
            for (int i = 0; i < books.size(); i++) {
                sb.append("Book(isbn=").append(books.get(i).getIsbn()).append(", title=").append(books.get(i).getTitle()).append(")");
                if (i < books.size()-1) {
                    sb.append(", ");
                }
            }
//        }
        sb.append("])");
        return sb.toString();
    }
}
