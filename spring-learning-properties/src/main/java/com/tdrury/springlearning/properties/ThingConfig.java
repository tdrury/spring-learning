package com.tdrury.springlearning.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix="tdrury")
@Configuration
@Data
public class ThingConfig {

    List<Thing> things;

    @Data
    public static class Thing {
        String name;
        String value;
    }
}
