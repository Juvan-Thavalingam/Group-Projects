package ch.zhaw.catan;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;


/***
 * TODO Write your own tests in this class.
 * <p>
 * Note:  Have a look at {@link ch.zhaw.catan.games.ThreePlayerStandard}. It can be used
 * to get several different game states.
 * </p>
 */
class SiedlerGameTest {
    private final static int DEFAULT_WINPOINTS = 5;
    private final static int DEFAULT_NUMBER_OF_PLAYERS = 3;


    private SiedlerGame model = new SiedlerGame(DEFAULT_WINPOINTS, 3);

    private static final ArrayList<Point> FIELD_COORDINATES_GR_LU_OR_BR_WO =
            new ArrayList<>(Arrays.asList(
                    new Point(5, 5),
                    new Point(7, 5),
                    new Point(9, 5),
                    new Point(4, 8),
                    new Point(6, 8),
                    new Point(8, 8),
                    new Point(10, 8),
                    new Point(3, 11),
                    new Point(5, 11),
                    new Point(9, 11),
                    new Point(11, 11),
                    new Point(4, 14),
                    new Point(6, 14),
                    new Point(8, 14),
                    new Point(10, 14),
                    new Point(5, 17),
                    new Point(7, 17),
                    new Point(9, 17)
            ));

    private static final ArrayList<Point> NON_FIELD_COORDINATES =
            new ArrayList<>(Arrays.asList(
                    new Point(0,3),
                    new Point(1,4),
                    new Point(2,21),
                    new Point(12,21),
                    new Point(12,1),
                    new Point(13,2),
                    new Point(13,22)
                    ));
    private static final Map<Config.Faction, Tuple<Point, Point>> INITIAL_SETTLEMENT_POSITIONS_AROUND_FIELD_6_8 =
            Map.of( Config.Faction.values()[0], new Tuple<>(
                            new Point(5, 7),
                            new Point(10, 16)),
                    Config.Faction.values()[1], new Tuple<>(
                            new Point(6, 10),
                            new Point(8, 4)),
                    Config.Faction.values()[2], new Tuple<>(
                            new Point(7, 7),
                            new Point(11, 13)));

    private static final Map<Config.Faction, Tuple<Point, Point>> INITIAL_ROAD_ENDPOINTS_AROUND_FIELD_6_8 =
            Map.of( Config.Faction.values()[0], new Tuple<>(
                            new Point(4, 6),
                            new Point(11, 15)),
                    Config.Faction.values()[1], new Tuple<>(
                            new Point(6, 12),
                            new Point(9, 3)),
                    Config.Faction.values()[2], new Tuple<>(
                            new Point(8, 6),
                            new Point(11, 15)));

    private static final Map<Config.Faction, Tuple<Point, Point>> INITIAL_2P_SETTLEMENT_POSITIONS_AROUND_FIELD_6_8 =
            Map.of( Config.Faction.values()[0], new Tuple<>(
                            new Point(5, 7),
                            new Point(10, 16)),
                    Config.Faction.values()[1], new Tuple<>(
                            new Point(6, 10),
                            new Point(8, 4)));

    private static final Map<Config.Faction, Tuple<Point, Point>> INITIAL_2P_ROAD_ENDPOINTS_AROUND_FIELD_6_8 =
            Map.of( Config.Faction.values()[0], new Tuple<>(
                            new Point(4, 6),
                            new Point(11, 15)),
                    Config.Faction.values()[1], new Tuple<>(
                            new Point(6, 12),
                            new Point(9, 3)));

    private static final Map<Config.Faction, Map<Config.Resource, Integer>> INITIAL_THREE_PLAYER_CARD_STOCK_AROUND_FIELD_6_8 =
            Map.of( Config.Faction.values()[0], Map.of(
                            Config.Resource.GRAIN, 0,
                            Config.Resource.WOOL, 1,
                            Config.Resource.BRICK, 1,
                            Config.Resource.ORE, 0,
                            Config.Resource.LUMBER, 0),
                    Config.Faction.values()[1], Map.of(
                            Config.Resource.GRAIN, 0,
                            Config.Resource.WOOL, 2,
                            Config.Resource.BRICK, 0,
                            Config.Resource.ORE, 0,
                            Config.Resource.LUMBER, 0),
                    Config.Faction.values()[2], Map.of(
                            Config.Resource.GRAIN, 1,
                            Config.Resource.WOOL, 1,
                            Config.Resource.BRICK, 0,
                            Config.Resource.ORE, 0,
                            Config.Resource.LUMBER, 0));

    private static final Map<Config.Faction, Map<Config.Resource, Integer>> INITIAL_TWO_PLAYER_CARD_STOCK_AROUND_FIELD_6_8 =
            Map.of( Config.Faction.values()[0], Map.of(
                            Config.Resource.GRAIN, 0,
                            Config.Resource.WOOL, 1,
                            Config.Resource.BRICK, 1,
                            Config.Resource.ORE, 0,
                            Config.Resource.LUMBER, 0),
                    Config.Faction.values()[1], Map.of(
                            Config.Resource.GRAIN, 0,
                            Config.Resource.WOOL, 2,
                            Config.Resource.BRICK, 0,
                            Config.Resource.ORE, 0,
                            Config.Resource.LUMBER, 0));

    private void assertPlayerResourceCardStockEquals(SiedlerGame model, Map<Config.Faction, Map<Config.Resource, Integer>> expected) {
        for (int i = 0; i < expected.keySet().size(); i++) {
            Config.Faction f = model.getCurrentPlayerFaction();
            for (Config.Resource r : Config.Resource.values()) {
                assertEquals(expected.get(f).get(r), model.getCurrentPlayerResourceStock(r),
                        "Resource card stock of player " + i + " [faction " + f + "] for resource type " + r + " does not match.");
            }
            model.switchToNextPlayer();
        }
    }
    private static SiedlerGame getAfterSetupPhaseForThief(int winpoints, int numberOfPlayers) {
        SiedlerGame model = new SiedlerGame(winpoints, numberOfPlayers);
        if (numberOfPlayers == 3) {
            for (int i = 0; i < model.getPlayerFactions().size(); i++) {
                Config.Faction f = model.getCurrentPlayerFaction();
                assertTrue(model.placeInitialSettlement(INITIAL_SETTLEMENT_POSITIONS_AROUND_FIELD_6_8.get(f).first, false));
                assertTrue(model.placeInitialRoad(INITIAL_SETTLEMENT_POSITIONS_AROUND_FIELD_6_8.get(f).first, INITIAL_ROAD_ENDPOINTS_AROUND_FIELD_6_8.get(f).first));
                model.switchToNextPlayer();
            }
            for (int i = 0; i < model.getPlayerFactions().size(); i++) {
                model.switchToPreviousPlayer();
                Config.Faction f = model.getCurrentPlayerFaction();
                assertTrue(model.placeInitialSettlement(INITIAL_SETTLEMENT_POSITIONS_AROUND_FIELD_6_8.get(f).second, true));
                assertTrue(model.placeInitialRoad(INITIAL_SETTLEMENT_POSITIONS_AROUND_FIELD_6_8.get(f).second, INITIAL_ROAD_ENDPOINTS_AROUND_FIELD_6_8.get(f).second));
            }
        } else if (numberOfPlayers == 2) {
            for (int i = 0; i < model.getPlayerFactions().size(); i++) {
                Config.Faction f = model.getCurrentPlayerFaction();
                assertTrue(model.placeInitialSettlement(INITIAL_2P_SETTLEMENT_POSITIONS_AROUND_FIELD_6_8.get(f).first, false));
                assertTrue(model.placeInitialRoad(INITIAL_2P_SETTLEMENT_POSITIONS_AROUND_FIELD_6_8.get(f).first, INITIAL_2P_ROAD_ENDPOINTS_AROUND_FIELD_6_8.get(f).first));
                model.switchToNextPlayer();
            }
            for (int i = 0; i < model.getPlayerFactions().size(); i++) {
                model.switchToPreviousPlayer();
                Config.Faction f = model.getCurrentPlayerFaction();
                assertTrue(model.placeInitialSettlement(INITIAL_2P_SETTLEMENT_POSITIONS_AROUND_FIELD_6_8.get(f).second, true));
                assertTrue(model.placeInitialRoad(INITIAL_2P_SETTLEMENT_POSITIONS_AROUND_FIELD_6_8.get(f).second, INITIAL_2P_ROAD_ENDPOINTS_AROUND_FIELD_6_8.get(f).second));
            }
        }

        return model;
    }

    @Disabled
    @Test
    void dummyTestMethod() {
        fail("Write you own tests in this class.");
    }

    /**
     * Valid equivalence class 1: Get the true winner of this game.
     * Expected: "rr"
     * Actual: "rr"
     */
    @Test
    void testGetWinner(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.setupTestSettlement();
        twoPlayers.getGame().buildSettlement(new Point(8,18));
        assertEquals(Config.Faction.RED, twoPlayers.getGame().getWinner());
    }

    /**
     * Valid equivalence class 2: Tests if the switching works correctly.
     * expected 1; actual 1
     * expected 2; actual 2
     * expected 0; actual 0
     */
    @Test
    void TestIfSwitchToNextPlayerSwitchesToNextPlayer(){
        model.switchToNextPlayer();
        assertEquals(1, model.getActualPlayerNumber());
        model.switchToNextPlayer();
        assertEquals(2, model.getActualPlayerNumber());
        model.switchToNextPlayer();
        assertEquals(0, model.getActualPlayerNumber());
    }

    /**
     * Valid equivalence class 3: Tests if the switching works correctly.
     * expected 2; actual 2
     * expected 1; actual 1
     */
    @Test
    void TestIfSwitchToPreviousPlayerSwitchesToPreviousPlayer(){
        model.switchToPreviousPlayer();
        assertEquals(2, model.getActualPlayerNumber());
        model.switchToPreviousPlayer();
        assertEquals(1, model.getActualPlayerNumber());
    }

    /**
     * Valid equivalence class 4: test if the amount of resources is correct.
     * expected: WL=1, LU=0, OR=1, GR=1, BR=0
     * actual: WL=1, LU=0, OR=1, GR=1, BR=0
     */
    @Test
    void TestCurrentPlayerResourceStock(){
        model.placeInitialSettlement(new Point(7,7), true);
        assertEquals(1,model.getCurrentPlayerResourceStock(Config.Resource.WOOL));
        assertEquals(1, model.getCurrentPlayerResourceStock(Config.Resource.ORE));
        assertEquals(1, model.getCurrentPlayerResourceStock(Config.Resource.GRAIN));
    }

    /**
     * Valid and invalid equivalence class 5: test if the given coordinate are in water.
     * False: Is in Water
     * True: Isn't in Water
     */
    @Test
    void TestCheckWater(){
        assertFalse(model.checkWater(new Point(0,4)));
        assertFalse(model.checkWater(new Point(0,6)));
        assertFalse(model.checkWater(new Point(0,8)));
        assertFalse(model.checkWater(new Point(0,10)));
        assertFalse(model.checkWater(new Point(1,3)));
        assertFalse(model.checkWater(new Point(1,5)));
        assertFalse(model.checkWater(new Point(1,7)));
        assertFalse(model.checkWater(new Point(1,9)));
        assertFalse(model.checkWater(new Point(1,11)));
        assertFalse(model.checkWater(new Point(3,3)));
        assertFalse(model.checkWater(new Point(3,5)));
        assertTrue(model.checkWater(new Point(3,7)));
        assertTrue(model.checkWater(new Point(3,9)));
        assertFalse(model.checkWater(new Point(3,11)));
        assertTrue(model.checkWater(new Point(4,4)));
    }

    /**
     * Valid equivalence class 6: test if the amount of Resources is correct after placeInitialSettlement.
     * expected: 0, actual: 0
     * expected : 0, actual: 0
     * expected: 0, actual: 0
     * expected: 0, actual: 0
     */
    @Test
    void TestPlaceInitialRoadCost(){
        model.placeInitialSettlement(new Point(7,7), true);
        assertEquals(0, model.getCurrentPlayerResourceStock(Config.Resource.LUMBER));
        assertEquals(0, model.getCurrentPlayerResourceStock(Config.Resource.BRICK));
        model.placeInitialRoad(new Point(7,7), new Point(7,8));
        assertEquals(0, model.getCurrentPlayerResourceStock(Config.Resource.LUMBER));
        assertEquals(0, model.getCurrentPlayerResourceStock(Config.Resource.BRICK));
    }

    /**
     * Invalid equivalence class 7: test if the settlement can be placed on same spot.
     * expected: true, actual: true
     * expected: false, actual: false
     */
    @Test
    void invalidTestInitialSettlementOnSamePlace(){
        assertTrue(model.placeInitialSettlement(new Point(7,7), true));
        assertFalse(model.placeInitialSettlement(new Point(7,7), true));
    }

    /**
     * Invalid equivalence class 8: test if the settlement can be placed on port or into water.
     * expected: true, actual: true
     * expected: false, actual: false
     */
    @Test
    void testInitialSettlementOnWater(){
        //port
        assertTrue(model.placeInitialSettlement(new Point(10,16), false));
        //water
        assertFalse(model.placeInitialSettlement(new Point(5,1), true));
    }

    /**
     * Invalid equivalence class 9: test if the neighbour of the corner is occupied.
     * If yes, placeInitialSettlement mus be false.
     * expected: false because Corner(8,6) is a neighbour of Corner(9,7)
     * actual: false
     */
    @Test
    void testAdjacentInitialSettlement(){
        model.placeInitialSettlement(new Point(8,6), true);
        assertFalse(model.placeInitialSettlement(new Point(9,7), true));
    }

    /**
     * Valid equivalence class 10: test if road can be set on the position, where the Settlement is.
     * expected: true
     * actual: true
     */
    @Test
    void testInitialRoadSamePlace(){
        model.placeInitialSettlement(new Point(4,4), false);
        assertTrue(model.placeInitialRoad(new Point(4, 4), new Point(4,6)));
    }

    /**
     * Valid equivalence class 11: test if road can be set, if roadStart and roadEnd have change their position.
     * expected: true
     * actual: true
     */
    @Test
    void testInitialRoadReverseCoordinate(){
        model.placeInitialSettlement(new Point(4,4), false);
        assertTrue(model.placeInitialRoad(new Point(4, 6), new Point(4,4)));
    }

    /**
     * Invalid equivalence class 12: test if the road doesn't exists.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestFalseCoordinate(){
        model.placeInitialSettlement(new Point(6,12), false);
        assertFalse(model.placeInitialRoad(new Point(6, 12), new Point(4,4)));
    }

    /**
     * Invalid equivalence class 13: test if a coordinate is not valid.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestNotExistingCoordinate(){
        model.placeInitialSettlement(new Point(4,4), false);
        assertFalse(model.placeInitialRoad(new Point(20, 6), new Point(4,50)));
    }

    /**
     * Invalid equivalence class 14: test if a settlement is not present by a road.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestNoSettlementForInitialRoad(){
        model.placeInitialSettlement(new Point(4,4), false);
        assertFalse(model.placeInitialRoad(new Point(6, 6), new Point(6,4)));
    }

    /**
     * Invalid equivalence class 15: test if a road can be placed on another road.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestPlaceOnSamePlace(){
        SiedlerGame model = new SiedlerGame(DEFAULT_WINPOINTS, DEFAULT_NUMBER_OF_PLAYERS);
        model.placeInitialSettlement(new Point(7,7), true);
        model.placeInitialRoad(new Point(7,7), new Point(7,8));
        assertFalse(model.placeInitialRoad(new Point(7, 7), new Point(7, 8)));
    }

    /**
     * Invalid equivalence class 16: test if an initial road can be placed in water.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestInitialRoadOnWater(){
        SiedlerGame model = new SiedlerGame(DEFAULT_WINPOINTS, DEFAULT_NUMBER_OF_PLAYERS);
        model.placeInitialSettlement(new Point(4,4), true);
        assertFalse(model.placeInitialRoad(new Point(4,4), new Point(3,3)));
        assertFalse(model.placeInitialRoad(new Point(3,3), new Point(4,4)));
    }

    /**
     * Valid equivalence class 17: test if an initial road can be placed in water.
     * expected: true
     * actual: true
     */
    @Test
    void testRoadWithSettlement(){
        TwoPlayers twoPlayers = new TwoPlayers();
        assertTrue(twoPlayers.getGame().buildRoad(new Point(9,13), new Point(9,15)));
    }

    /**
     * Valid equivalence class 18: test if the road can be extended.
     * expected: true
     * actual: true
     */
    @Test
    void testRoadToExtend(){
        TwoPlayers twoPlayers = new TwoPlayers();
        assertTrue(twoPlayers.getGame().buildRoad(new Point(8,18), new Point(8,16)));
    }

    /**
     * Invalid equivalence class 19: test if the roads already exists.
     * expected: false, actual: false
     * expected: false, actual: false
     */
    @Test
    void invalidTestBuildRoadOnExistingSpot(){
        TwoPlayers twoPlayers = new TwoPlayers();
        assertFalse(twoPlayers.getGame().buildRoad(new Point(9,15), new Point(8,16)));
        twoPlayers.getGame().switchToNextPlayer();
        assertFalse(twoPlayers.getGame().buildRoad(new Point(7,15), new Point(8,16)));
    }

    /**
     * Invalid equivalence class 20: test if the actualPlayer can build a road on the enemy road.
     * expected: false, actual: false
     */
    @Test
    void invalidTestBuildRoadOnEnemyRoad(){
        TwoPlayers twoPlayers = new TwoPlayers();
        assertFalse(twoPlayers.getGame().buildRoad(new Point(7,15), new Point(8,16)));
        twoPlayers.getGame().switchToNextPlayer();
        assertFalse(twoPlayers.getGame().buildRoad(new Point(9,15), new Point(8,16)));
    }

    /**
     * Invalid equivalence class 21: test if the enemy can extend my road.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestBuildRoadAndExtendEnemy(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.getGame().buildRoad(new Point(8,18), new Point(8,16));
        twoPlayers.getGame().switchToNextPlayer();
        assertFalse(twoPlayers.getGame().buildRoad(new Point(8,18), new Point(9,19)));
    }

    /**
     * Invalid equivalence class 22: test if road can be build into water.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestBuildRoadIntoWater(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.getGame().throwDice(3);
        twoPlayers.getGame().throwDice(3);
        twoPlayers.getGame().throwDice(11);
        twoPlayers.getGame().throwDice(11);
        twoPlayers.getGame().buildRoad(new Point(8,16), new Point(8,18));
        twoPlayers.getGame().buildRoad(new Point(8,18), new Point(7,19));
        assertFalse(twoPlayers.getGame().buildRoad(new Point(7,19), new Point(7,21)));
    }

    /**
     * Invalid equivalence class 23: test if road can be build, without resources.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestBuildRoadNoResource(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.getGame().buildRoad(new Point(8,18), new Point(8,16));
        assertFalse(twoPlayers.getGame().buildRoad(new Point(8,18), new Point(9,19)));
    }

    /**
     * Valid equivalence class 24: tests if buildSettlement works.
     * expected: true
     * actual: true
     */
    @Test
    void buildSettlement(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.setupTestSettlement();
        assertTrue(twoPlayers.getGame().buildSettlement(new Point(8,18)));
    }

    /**
     * Invalid equivalence class 25: tests if buildSettlement return false, if there is one neigbour.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestAdjacentSettlement(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.setupTestSettlement();
        assertFalse(twoPlayers.getGame().buildSettlement(new Point(8,16)));
    }

    /**
     * Invalid equivalence class 26: tests if buildSettlements return false, if the coordinate doesn't exist for Corner.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestBuildSettlementNotExistingCoordinate(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.setupTestSettlement();
        assertFalse(twoPlayers.getGame().buildSettlement(new Point(9,30)));
    }

    /**
     * Invalid equivalence class 27: tests if buildSettlements return false, if the corner is occupied of the own player.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestBuildSettlementOnActualPlayerSpot(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.setupTestSettlement();
        assertFalse(twoPlayers.getGame().buildSettlement(new Point(7,7)));
    }

    /**
     * Invalid equivalence class 28: tests if buildSettlements return false, if the corner is occupied from the enemy.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestBuildSettlementOnEnemySpot(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.setupTestSettlement();
        assertFalse(twoPlayers.getGame().buildSettlement(new Point(7,15)));
    }

    /**
     * Valid equivalence class 29: test if a settlement can be built on the shore.
     * expected: true
     * actual: true
     */
    @Test
    void TestBuildSettlementOnPort(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.setupTestSettlement();
        assertTrue(twoPlayers.getGame().buildSettlement(new Point(8,18)));
    }

    /**
     * invalid equivalence class 30: test if a settlement can be built on water.
     * expected: false
     * actual: false
     */
    @Test
    void invalidTestBuildSettlementOnWater(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.setupTestSettlement();
        assertFalse(twoPlayers.getGame().buildSettlement(new Point(9,21)));
    }

    /**
     * Valid equivalence class 31: simulates a game, where red should win:
     * A test mid-game checks, that no one has won.
     * A test at the end checks, that red has won.
     * expected: false, actual: false
     * expected: true, actual: true
     */
    @Test
    void getSevenToWin(){
        SiedlerGame game = new SiedlerGame(7,2);
        game.placeInitialSettlement(new Point(9,13), false);
        game.placeInitialRoad(new Point(9,13), new Point(9,15));

        game.placeInitialSettlement(new Point(4,12), true);
        game.placeInitialRoad(new Point(4,12), new Point(5,13));

        game.throwDice(9); //Brick
        game.throwDice(9); //Brick
        game.throwDice(9); //Brick
        game.throwDice(9); //Brick
        game.throwDice(5); //Lumber
        game.throwDice(5); //Lumber
        game.throwDice(5); //Lumber
        game.throwDice(5); //Lumber

        assertTrue(game.buildRoad(new Point(5,13), new Point(5,15)));
        assertTrue(game.buildRoad(new Point(5,15), new Point(6,16)));
        assertTrue(game.buildRoad(new Point(6,16), new Point(7,15)));

        assertTrue(game.buildRoad(new Point(9,15), new Point(8,16)));
        assertTrue(game.buildRoad(new Point(8,16), new Point(8,18)));

        game.throwDice(9);
        game.throwDice(9);
        game.throwDice(9);
        game.throwDice(9);
        game.throwDice(5);
        game.throwDice(5);
        game.throwDice(5);
        game.throwDice(5);
        game.throwDice(10);
        game.throwDice(10);
        game.throwDice(10);
        game.throwDice(12);
        game.throwDice(12);
        game.throwDice(12);
        game.throwDice(12);

        assertTrue(game.buildSettlement(new Point(5,15)));
        assertTrue(game.buildSettlement(new Point(7,15)));
        assertTrue(game.buildSettlement(new Point(8,18)));

        assertNull(game.getWinner());

        game.throwDice(9); //Brick
        game.throwDice(9); //Brick
        game.throwDice(5); //Lumber
        game.throwDice(5); //Lumber

        assertTrue(game.buildRoad(new Point(8,18), new Point(9,19)));
        assertTrue(game.buildRoad(new Point(9,19), new Point(10,18)));
        assertFalse(game.buildSettlement(new Point(10,18)));

        game.throwDice(10); //Grain
        game.throwDice(10); //Grain
        game.throwDice(6); //Ore
        game.throwDice(6); //Ore
        game.throwDice(6); //Ore
        game.throwDice(6); //Ore
        game.throwDice(6); //Ore
        game.throwDice(6); //Ore

        assertTrue(game.buildCity(new Point(8,18)));
        assertTrue(game.buildCity(new Point(7,15)));

        assertEquals(Config.Faction.RED, game.getWinner());
    }

    /**
     * Invalid equivalence class 32: test if a settlement can be built without resources.
     * expected for the first 3 buildMethods: true, actual: true
     * expected for last buildMethod: false, actual: false
     */
    @Test
    void invalidTestBuildSettlementNoResource(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.setupTestSettlement();
        assertTrue(twoPlayers.getGame().buildSettlement(new Point(8,18)));
        twoPlayers.getGame().throwDice(11);
        twoPlayers.getGame().throwDice(11);
        twoPlayers.getGame().throwDice(3);
        twoPlayers.getGame().throwDice(3);
        assertTrue(twoPlayers.getGame().buildRoad(new Point(8,18), new Point(7,19)));
        assertTrue(twoPlayers.getGame().buildRoad(new Point(7,19), new Point(6,18)));
        assertFalse(twoPlayers.getGame().buildSettlement(new Point(6,18)));
    }

    /**
     * Valid equivalence class 33: test if a city can be built when the needed resources have been thrown.
     * expected: true, actual: true
     * expected: 2, actual: 2
     */
    @Test
    void testThrowDiceWithCity(){
        TwoPlayers twoPlayers = new TwoPlayers();
        twoPlayers.setupTestSettlement();

        twoPlayers.getGame().throwDice(4); //Ore
        twoPlayers.getGame().throwDice(4); //Ore
        twoPlayers.getGame().throwDice(4); //Ore
        twoPlayers.getGame().throwDice(5); //Grain
        assertTrue(twoPlayers.getGame().buildCity(new Point(7,7)));

        twoPlayers.getGame().throwDice(5); //Grain
        assertEquals(2, twoPlayers.getGame().getActualPlayer().getPlayerResource().get(Config.Resource.GRAIN));
    }

    /**
     * Valid equivalence class 34: test if the thief steals a card from a three player game.
     * expected: 3, actual: 3
     * expected: 5, actual: 5
     */
    @Test
    void TestThiefStealingThreePlayers() {
        SiedlerGame model = getAfterSetupPhaseForThief(DEFAULT_WINPOINTS, 3);
        assertPlayerResourceCardStockEquals(model, INITIAL_THREE_PLAYER_CARD_STOCK_AROUND_FIELD_6_8);
        model.throwDice(4);
        model.throwDice(7);
        model.placeThiefAndStealCard(new Point(6, 8));
        assertEquals(3, calculateTotalResourcesForPlayer(model.getActualPlayer()));
        model.switchToNextPlayer();
        int number = calculateTotalResourcesForPlayer(model.getActualPlayer());
        model.switchToNextPlayer();
        number += calculateTotalResourcesForPlayer(model.getActualPlayer());
        assertEquals(5, number);
    }

    /**
     * Valid equivalence class 35: test if thief doesn't steal anything, when no one has resources.
     * expected: true
     * actual: true
     */
    @Test
    void TestThiefWhenNoOneHasResources() {
        SiedlerGame model = getAfterSetupPhaseForThief(DEFAULT_WINPOINTS, 2);
        assertPlayerResourceCardStockEquals(model, INITIAL_TWO_PLAYER_CARD_STOCK_AROUND_FIELD_6_8);
        model.getActualPlayer().removeResource(Config.Resource.WOOL);
        model.getActualPlayer().removeResource(Config.Resource.BRICK);
        model.switchToNextPlayer();
        model.getActualPlayer().removeResource(Config.Resource.WOOL);
        model.getActualPlayer().removeResource(Config.Resource.WOOL);
        model.throwDice(7);
        assertTrue(model.placeThiefAndStealCard(new Point(6, 8)));
    }

    /**
     * Valid equivalence class 36: test if thief steals,
     * when only the current dice thrower has resources, but no one else has.
     *
     * expected: true, actual: true
     * expected(player1stats): true, actual: true
     * expected(player2stats): true, actual: true
     */
    @Test
    void TestOnlyThiefThrowerHasResources() {
        SiedlerGame model = getAfterSetupPhaseForThief(DEFAULT_WINPOINTS, 2);
        assertPlayerResourceCardStockEquals(model, INITIAL_TWO_PLAYER_CARD_STOCK_AROUND_FIELD_6_8);
        model.switchToNextPlayer();
        model.getActualPlayer().removeResource(Config.Resource.WOOL);
        model.getActualPlayer().removeResource(Config.Resource.WOOL);
        int player2Stats = calculateTotalResourcesForPlayer(model.getActualPlayer());
        model.switchToNextPlayer();
        int player1Stats = calculateTotalResourcesForPlayer(model.getActualPlayer());
        model.throwDice(7);
        assertTrue(model.placeThiefAndStealCard(new Point(6, 8)));
        assertEquals(player1Stats, calculateTotalResourcesForPlayer(model.getActualPlayer()));
        model.switchToNextPlayer();
        assertEquals(player2Stats, calculateTotalResourcesForPlayer(model.getActualPlayer()));

    }

    /**
     * Valid equivalence class 37: test if the thief can be placed on a free field (!= WATER, DESERT, NON FIELD).
     *
     * expected: true
     * actual: true
     */
    @Test
    void TestThiefOnFreeField() {
        SiedlerGame model = getAfterSetupPhaseForThief(DEFAULT_WINPOINTS, 2);
        for (Point point : FIELD_COORDINATES_GR_LU_OR_BR_WO) {
            assertTrue(model.placeThiefAndStealCard(point));
        }
    }

    /**
     * Invalid equivalence class 38: test if the thief can be placed on an occupied field.
     *
     * expected: false
     * actual: false
     */
    @Test
    void InvalidTestThiefOnOccupiedField() {
        SiedlerGame model = getAfterSetupPhaseForThief(DEFAULT_WINPOINTS, 2);
        for (Point point : FIELD_COORDINATES_GR_LU_OR_BR_WO) {
            model.placeThiefAndStealCard(point);
            assertFalse(model.placeThiefAndStealCard(point));
        }
    }

    /**
     * Invalid equivalence class 39: test if the thief can be placed on water.
     *
     * expected: false
     * actual: false
     */
    @Test
    void InvalidTestThiefOnWater() {
        SiedlerGame model = getAfterSetupPhaseForThief(DEFAULT_WINPOINTS, 2);
        assertFalse(model.placeThiefAndStealCard(new Point(0,4)));
        assertFalse(model.placeThiefAndStealCard(new Point(0,6)));
        assertFalse(model.placeThiefAndStealCard(new Point(0,8)));
        assertFalse(model.placeThiefAndStealCard(new Point(0,10)));
        assertFalse(model.placeThiefAndStealCard(new Point(1,3)));
        assertFalse(model.placeThiefAndStealCard(new Point(3,3)));
        assertFalse(model.placeThiefAndStealCard(new Point(1,7)));
        assertFalse(model.placeThiefAndStealCard(new Point(1,11)));
    }

    /**
     * Invalid equivalence class 40: test if the thief can be placed on a non field coordinate.
     *
     * expected: false
     * actual: false
     */
    @Test
    void InvalidTestThiefOnNonFieldCoordinate() {
        SiedlerGame model = getAfterSetupPhaseForThief(DEFAULT_WINPOINTS, 2);
        for (Point point : NON_FIELD_COORDINATES) {
            assertFalse(model.placeThiefAndStealCard(point));
        }
    }

    private int calculateTotalResourcesForPlayer(Player player) {
        int totalResources = 0;
        for (Config.Resource resource : player.getPlayerResource().keySet()) {
            totalResources += player.getPlayerResource().get(resource);
        }

        return totalResources;
    }
}