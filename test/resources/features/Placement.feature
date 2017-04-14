@PlacementTest
  Feature: Placing Tiles

    Scenario: Placing a tile
      Given a player’s turn
      When the player must play a tile
      Then the player is assigned a tile to play
      And the overall amount of tiles is decreased
      And the next tile to be played is prepared
      And the player will enter the build phase

    Scenario: Invalidly placing a tile
      Given a player’s turn
      And the selected tile placement is invalid
      When the player is assigned to play a tile
      Then the player may not place the tile
      And the player must place the tile elsewhere