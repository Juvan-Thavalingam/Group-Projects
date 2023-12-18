package ch.zhaw.catan;


import java.awt.*;

/**
 * This class is for SiedlerGameTest to test all the methods.
 */
public class TwoPlayers {
    private SiedlerGame game;

    /**
     * This constructor creates a game with three winpoints and two players.
     */
    public TwoPlayers(){
        game = new SiedlerGame(3, 2);
        setup();
    }

    /**
     * This method implements the second phase of the game.
     */
    public void setup(){
        game.placeInitialSettlement(new Point(7,7), false);
        game.placeInitialRoad(new Point(7,7), new Point(7,9));

        game.switchToNextPlayer();

        game.placeInitialSettlement(new Point(5,15), false);
        game.placeInitialRoad(new Point(5,15), new Point(6,16));

        game.placeInitialSettlement(new Point(7,15), true);
        game.placeInitialRoad(new Point(7,15), new Point(8,16));

        game.switchToPreviousPlayer();

        game.placeInitialSettlement(new Point(9,15), true);
        game.placeInitialRoad(new Point(9,15), new Point(8,16));
    }

    /**
     * This method build a road and calls three times throwDice, to build a settlement.
     */
    public void setupTestSettlement(){
        game.buildRoad(new Point(8,16), new Point(8,18));
        game.throwDice(11); //Brick
        game.throwDice(5); //Grain
        game.throwDice(3); //Lumber
    }

    public SiedlerGame getGame(){
        return game;
    }
}
