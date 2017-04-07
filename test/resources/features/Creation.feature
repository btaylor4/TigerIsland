@CreationTest
  Feature: Creating a new game
    Scenario: Starting a game
      Given There is no game
      When A game is made
      Then Create the game

    Scenario: Creating a tile
      Given A tile is being made
      When a tile is made
      Then create the tile

    Scenario: Generating tiles with terrains
      Given the game is created
      When the tiles are generated
      Then the tiles should be permuted in sequence


