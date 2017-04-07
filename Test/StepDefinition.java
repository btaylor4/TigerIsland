import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import main.* ;
import main.enums.TerrainType;
import main.states.*;
/**
 * Created by jdavi on 3/17/17.
 */
public class StepDefinition {
    Player testP1;
    Player testP2;
    GameBoard testGame ;

    @Given("^There is no game")
    public void creationGiven() throws Throwable{
        assert (testGame == null);
    }

    @When("^A game is made")
    public void creationWhen() throws Throwable{
        testGame = new GameBoard();
    }

    @Then("^Create the game")
    public void creationThen() throws Throwable{
        assert (testGame != null);
    }

    @Given("^a game is being started")
    public void playerCreationGiven() throws Throwable{
        testGame = new GameBoard();
        assert (testP1 == null);
        assert (testGame != null);
    }

    @When("^a player is added")
    public void playerCreationWhen() throws Throwable{
        testP1 = new Player(testGame, 1);
        assert (testP1 != null);
        testP1.playFirstTile();
        //testP1 = new Player(mockGame);
    }

    @Then("^create the player")
    public void playerCreationThen() throws Throwable{
        assert (testP1 != null);
    }

    @Given("^the game is created")
    public void gameCreationGiven() throws Throwable{
        CreateGame();
        assert (testGame != null);
    }

    @When("^the tiles are generated")
    public void gammeCreationWhen() throws Throwable{
        assert (testGame.tileStack != null);

    }

    @Then("^the tiles should be permuted in sequence")
    public void gameCreationThen() throws Throwable{
        assert (testGame.TilesShuffled());
    }

    Tile testTile ;

    @Given("^A tile is being made")
    public void tileCreationGiven() throws Throwable{
        assert (testTile == null);
    }

    @When("^a tile is made")
    public void tileCreationWhen() throws Throwable{
        testTile = new Tile();
    }

    @Then("^create the tile")
    public void tileCreationThen() throws Throwable{
        assert (testTile != null);
        //System.out.println("  New tile created");
    }


    @Given("^a player has the option to build")
    public void playerBuildPhaseGiven() throws Throwable{
        testGame = new GameBoard();

        testP1 = new Player(testGame, 1);
        testP2 = new Player(testGame, 2);
        assert (testP1 != null);
        assert (testP2 != null);

        testP1.playFirstTile();
        //testP2.playTilePhase();
        //testSettlement = new Settlement(testGame);
        //testP2.build(3, testSettlement );
        testP1.drawTile();

        assert (testP1.HasTile());
    }

    @When("^player cannot play meeple or Totoro & the player has at least 1 meeple or Totoro")
    public void playerBuildPhaseWhen() throws Throwable {
        boolean didBuild = false;//testP1.build();
        assert(testP1.HasMeeplesOrTotoros());
        assert (!didBuild && !testP1.isOutOfPieces());
    }
    @Then("^the player immediately loses")
    public void playerBuildPhaseThen() throws Throwable{
        assert (testP1 != null);

        testP1.Lose();
        assert (testP1.HasLost());

    }

    @Given("^a player chooses to create a settlement & the player has at least one meeple")
    public void playerBuildSettlementGiven() throws Throwable{

        StartGame();
        testP1.drawTile();
        assert (testP1.HasTile());
        assert (testP1.HasMeeples());
    }

    @When("^the player chooses to make a settlement")
    public void playerBuildSettlementWhen() throws Throwable {
        boolean didBuild = false;//testP1.build();
        assert(testP1.HasMeeplesOrTotoros());
        assert (!didBuild && !testP1.isOutOfPieces());

    }
    @Then("^the player will establish a new settlement &  place the meeple on a valid terrain")
    public void playerBuildSettlementThen() throws Throwable{

        assert (testP1 != null);
        CreatePlacement();
        testP1.expandSettlementMeeple(testSettlement, TerrainType.FOREST);
        assert (testP1.SettlementSuccess());

    }

    @Given("^a player’s turn has ended")
    public void playerTurnEndedGiven() throws Throwable{

        testGame = new GameBoard();
        testP1 = new Player(testGame, 1);
        testP1.playFirstTile();
        testP2 = new Player(testGame, 2);
        testP1.SetTurnEnded();
        assert (testP1.TurnEnded());
    }

    @When("^the game does not end")
    public void playerTurnEndedWhen() throws Throwable {

        assert(!testGame.IsGameOver());
    }

    @Then("^it is the next player turn")
    public void playerTurnEndedThen() throws Throwable{
        assert (testP1.TurnEnded());
        assert (!testP2.TurnEnded());
    }

    @Given("^a player has used all their meeples & a player has used all their totoros")
    public void playerOutOfPiecesGiven() throws Throwable{

        testGame = new GameBoard();
        testP1 = new Player(testGame, 1);
        testP1.playFirstTile();

        testP1.DiscardPieces();
        assert (testP1.TurnEnded());
    }

    @When("^the player has use all their game pieces")
    public void playerOutOfPiecesWhen() throws Throwable {

        assert(!testP1.HasMeeplesOrTotoros());
    }

    @Then("^end the game")
    public void playerOutOfPiecesThen() throws Throwable{
        testGame.EndGame();
        assert (testGame.IsGameOver());
    }

    @Given("^a player chooses to place a meeple or meeples validly")
    public void playerPlaceMeepleGiven() throws Throwable{

        testGame = new GameBoard();
        testP1 = new Player(testGame, 1);
        testP1.playFirstTile();
        testP1.placeMeeple(new Point(100, 100), new Settlement(testGame));

    }

    @When("^the player's has chosen to build")
    public void playerPlaceMeepleWhen() throws Throwable {

        assert(!testP1.HasMeeplesOrTotoros());
    }

    @Then("^multiply the number of meeples by the level on which they are placed and add to score")
    public void playerPlaceMeepleThen() throws Throwable{
        int currentScore = testGame.GetScore();
        int currentMeepels = testGame.MeeplesPlacedThisTurn();
        testGame.SetScore(currentScore * testGame.LastPlacedLevel() );

    }

    Settlement testSettlement;
    Point testPoint;
    @Given("^a meeple is being placed")
    public void playerPlaceMeeple2Given() throws Throwable{
        PlaceMeeple();
    }

    @When("^the meeple is not on a volcano or occupied territory")
    public void playerPlaceMeeple2When() throws Throwable {
        assert(!testSettlement.isMeepleValidHere(TerrainType.VOLCANO, testPoint));
    }

    @Then("^place the meeple on the selected tile")
    public void playerPlaceMeeple2Then() throws Throwable{
        assert ( testP1.placeMeepleWithCheck(testPoint ,testSettlement ));
    }

    @Given("^a meeple is being placed invalidly")
    public void playerPlaceMeepleInvalidGiven() throws Throwable{

        PlaceMeeple();

    }

    @When("^the meeple’s attempted placement is invalid")
    public void playerPlaceMeepleInvalidWhen() throws Throwable {
        assert(!testSettlement.isMeepleValidHere(TerrainType.VOLCANO, testPoint));
    }

    @Then("^disallow the invalid meeple placement")
    public void playerPlaceMeepleInvalidThen() throws Throwable{
        assert (!testP1.placeMeepleWithCheck(testPoint ,testSettlement ));
    }

    @Given("^a settlement size of 5 or greater & the player has at least one totoro")
    public void playerPlaceTotoroGiven() throws Throwable{

        CreateGame();
        assert (testP1.HasTotoros());
        testSettlement = testP1.foundNewSettlement(testPoint, 5);
    }

    @When("^the player chooses to place a totoro for that settlement of size 5 or greater")
    public void playerPlaceTotoroWhen() throws Throwable {
        assert(testP1.build(1, testSettlement));
    }

    @Then("^ensure the totoro is placed on a valid terrain adjacent to that settlement")
    public void playerPlaceTotoroThen() throws Throwable{
        assert (testSettlement.isSettlementValid());
    }

    @Given("^a player chooses to place a totoro")
    public void playerPlaceTotoro2Given() throws Throwable{

        CreateGame();
        assert (testP1.HasTotoros());
    }

    @When("^the player selects a settlement of less than size 5 to place adjacent to")
    public void playerPlaceTotoro2When() throws Throwable {
        testSettlement = testP1.foundNewSettlement(testPoint, 4);

        assert(!testP1.build(4, testSettlement));
    }

    @Then("^do not allow the totoro to be placed")
    public void playerPlaceTotoro2Then() throws Throwable{
        assert (!testSettlement.isSettlementValid());
    }


    // axuliary functions
    private void PlaceMeeple()
    {
        testGame = new GameBoard();
        testSettlement = new Settlement(testGame);
        testP1 = new Player(testGame, 1);
        testP1.playFirstTile();
        testPoint = new Point(101, 101);
    }
    private void StartGame()
    {
        CreateGame();

        testP1.playFirstTile();
        //testP2.playTilePhase();
        //testP2.build();

    }
    private void CreatePlacement()
    {
        this.testPoint = new Point(100, 100);
        if(testGame != null) {
            this.testSettlement = new Settlement(testGame);
            this.testSettlement.size = 5;
        }
    }
    private void CreateGame()
    {
        testGame = new GameBoard();

        testP1 = new Player(testGame, 1);
        testP2 = new Player(testGame, 2);
        assert (testP1 != null);
        assert (testP2 != null);
    }
}
