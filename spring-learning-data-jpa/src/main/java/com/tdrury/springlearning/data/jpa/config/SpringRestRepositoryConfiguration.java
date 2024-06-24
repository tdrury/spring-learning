package com.tdrury.springlearning.data.jpa.config;

import com.tdrury.springlearning.data.jpa.model.Author;
import com.tdrury.springlearning.data.jpa.model.Book;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Component
public class SpringRestRepositoryConfiguration implements RepositoryRestConfigurer {

    // Normally we wouldn't return entity IDs in a HATEOAS app as the client has no use of it, but
    // for testing/learning, we'll return it.
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(Author.class, Book.class);
    }
}
