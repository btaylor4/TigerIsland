@MeepleTest

  Feature: Placing meeple
    Scenario: Placing meeple invalidly
      Given a meeple is being placed
      When the meeple’s attempted placement is invalid(on volcano or a hexagon that’s already taken)
      Then disallow the invalid meeple placement

    Scenario: Placing meeple validly
      Given a meeple is being placed
      When the meeple is not on a volcano or occupied territory
      Then place the meeple on the selected tile