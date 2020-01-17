package com.tdrury.springlearning.data.jpa.model;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = { "src/it/resources/author" },
        plugin = { "pretty", "json:target/it/author_json_cucumber.json", "junit:target/it/author_junit_cucumber.xml" }
)
public class AuthorIT {

}
