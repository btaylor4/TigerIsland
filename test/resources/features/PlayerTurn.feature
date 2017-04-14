@PlayerTurnTest

  Feature: Player turn
    Scenario: Player loses when they have no valid options
      Given a player is in the build phase
      And the player has at least 1 meeple or Totoro
      When player cannot play meeple or Totoro
      Then the player immediately loses

    Scenario: Player’s turn in the build phase
      Given A player is in the build phase
      And the player has at least 1 meeple
      And the player has at least 1 Totoro
      When player can place a meeple or a Totoro
      Then the player must place a meeple or a Totoro




