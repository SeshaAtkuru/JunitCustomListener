@test
Feature: Calculator Smoke Test

  Scenario: Test Calculator addition
    Given Open Calculator
    And Calculator powered up
    When Typed 10 plus 5
    Then Calculator displays 15

  @calc
  Scenario Outline: Test Caluclator basics
    Given Open Caluclator
    When enter <num1> and <num2>
    And press <key>
    Then calculator displays <result>

    Examples: 
      | num1 | num2 | key      | result |
      |    5 |    5 | plus     |     10 |
      |   10 |    5 | minus    |      5 |
      |    5 |    5 | multiply |     25 |
      |   30 |    5 | divide   |      6 |
