@test
  Feature: Calculator Smoke Test

    Scenario: Test Calculator addition
      Given Open Calculator
      And Calculator powered up
      When Typed 10 plus 5
      Then Calculator displays 15
