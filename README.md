# spring-learning

Test Driven Learning - using TDD for learning tools and languages. 

This one is targeted at Spring and Spring Boot with emphasis on unit testing and integration testing technologies.
TDD is also useful for documenting library features and letting you return to the tests later to see how a 
particular feature works.

## spring-learning-data-jpa

Simple JPA project with REST (HATEOAS) interface.

This module will also test numerous ways of testing Spring REST services so there will be a lot of duplication of functional tests.
* Unit tests
* MockMvc tests
* RestTemplate tests against the running service
* Cucumber tests

## spring-learning-properties

Learn Spring properties and profiles and order-of-precedence when multiple profiles are active.

## spring-learning-rest

Spring REST (not HATEOAS) controllers and testing.

## spring-learning-security

Learn the basics of Spring's API security.

## spring-learning-security-oauth2

Learn how to authorize a user via 3rd-party OAUTH2 provider. In this case, google and github.

## hexagonal-mock-adapters

Demonstrate how using hexagonal architecture allows mocking all external interactions very easily. This method
is useful for kick-starting app development when external services are not yet ready. Actual implementations
can then be switched on when the external service is ready. Then the mock adapters can be switched on when doing
local testing and development.

## testcontainers

Use testcontainers tool to test docker images.