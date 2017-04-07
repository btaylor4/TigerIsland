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

    Scenario: When starting the game
      Given a game is being started
      When a player is added
      Then create the player

    Scenario: Player loses when they have no valid options
      Given a player has the option to build
      When player cannot play meeple or Totoro & the player has at least 1 meeple or Totoro
      Then the player immediately loses

    Scenario: Player builds a settlement
      Given a player chooses to create a settlement & the player has at least one meeple
      When the player chooses to make a settlement
      Then the player will establish a new settlement &  place the meeple on a valid terrain

    Scenario: Game does not end at end of player’s turn
      Given a player’s turn has ended
      When the game does not end
      Then it is the next player turn

    Scenario: Game does not end at end of player’s turn
      Given a player has used all their meeples & a player has used all their totoros
      When the player has use all their game pieces
      Then end the game

    Scenario: Placing meeples
      Given a player chooses to place a meeple or meeples validly
      When the player's has chosen to build
      Then multiply the number of meeples by the level on which they are placed and add to score

    Scenario: When placing meeple validly
      Given a meeple is being placed
      When the meeple is not on a volcano or occupied territory
      Then place the meeple on the selected tile

    Scenario: Placing totoro validly
      Given a settlement size of 5 or greater & the player has at least one totoro
      When the player chooses to place a totoro for that settlement of size 5 or greater
      Then ensure the totoro is placed on a valid terrain adjacent to that settlement

    Scenario: Placing totoro invalidly
      Given a player chooses to place a totoro
      When the player selects a settlement of less than size 5 to place adjacent to
      Then do not allow the totoro to be placed
