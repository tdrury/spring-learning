package com.tdrury.springlearning.security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Entity
@Getter
@ToString
public class UserInfo {
    @Id
    @Setter
    String email;
    @Setter
    String firstName;
    @Setter
    String lastName;
    @Setter
    String oauthSource;
    Instant createdAt = Instant.now();

    public UserInfo() { }
    public UserInfo(String email) {
        this.email = email;
    }
    public UserInfo(String email, String oauthSource) {
        this.email = email;
        this.oauthSource = oauthSource;
    }

}
