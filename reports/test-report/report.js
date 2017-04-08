$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("Creation.feature");
formatter.feature({
  "line": 2,
  "name": "Creating a new game",
  "description": "",
  "id": "creating-a-new-game",
  "keyword": "Feature",
  "tags": [
    {
      "line": 1,
      "name": "@CreationTest"
    }
  ]
});
formatter.scenario({
  "line": 3,
  "name": "Starting a game",
  "description": "",
  "id": "creating-a-new-game;starting-a-game",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 4,
  "name": "There is no game",
  "keyword": "Given "
});
formatter.step({
  "line": 5,
  "name": "A game is made",
  "keyword": "When "
});
formatter.step({
  "line": 6,
  "name": "Create the game",
  "keyword": "Then "
});
formatter.match({
  "location": "StepDefinition.creationGiven()"
});
formatter.result({
  "duration": 153760065,
  "status": "passed"
});
formatter.match({
  "location": "StepDefinition.creationWhen()"
});
formatter.result({
  "duration": 1312910,
  "status": "passed"
});
formatter.match({
  "location": "StepDefinition.creationThen()"
});
formatter.result({
  "duration": 199178,
  "status": "passed"
});
formatter.scenario({
  "line": 8,
  "name": "Creating a tile",
  "description": "",
  "id": "creating-a-new-game;creating-a-tile",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 9,
  "name": "A tile is being made",
  "keyword": "Given "
});
formatter.step({
  "line": 10,
  "name": "a tile is made",
  "keyword": "When "
});
formatter.step({
  "line": 11,
  "name": "create the tile",
  "keyword": "Then "
});
formatter.match({
  "location": "StepDefinition.tileCreationGiven()"
});
formatter.result({
  "duration": 198169,
  "status": "passed"
});
formatter.match({
  "location": "StepDefinition.tileCreationWhen()"
});
formatter.result({
  "duration": 110662,
  "status": "passed"
});
formatter.match({
  "location": "StepDefinition.tileCreationThen()"
});
formatter.result({
  "duration": 127961,
  "status": "passed"
});
formatter.scenario({
  "line": 13,
  "name": "Generating tiles with terrains",
  "description": "",
  "id": "creating-a-new-game;generating-tiles-with-terrains",
  "type": "scenario",
  "keyword": "Scenario"
});
formatter.step({
  "line": 14,
  "name": "the game is created",
  "keyword": "Given "
});
formatter.step({
  "line": 15,
  "name": "the tiles are generated",
  "keyword": "When "
});
formatter.step({
  "line": 16,
  "name": "the tiles should be permuted in sequence",
  "keyword": "Then "
});
formatter.match({
  "location": "StepDefinition.tileGenerationGiven()"
});
formatter.result({
  "duration": 758830,
  "status": "passed"
});
formatter.match({
  "location": "StepDefinition.tileGenerationWhen()"
});
formatter.result({
  "duration": 184331,
  "status": "passed"
});
formatter.match({
  "location": "StepDefinition.tileGenerationThen()"
});
formatter.result({
  "duration": 149633,
  "status": "passed"
});
});