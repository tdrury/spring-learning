
Feature: Testing REST API

  Scenario: Create and Retrieve an Author
    When Author with name Fred Claus is created and saved
    Then Searching for Authors with last name Claus returns 1 result
