package ch.zhaw.catan;

import ch.zhaw.hexboard.Label;

import java.awt.*;

import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.Map;

/**
 * This class starts and handle the Siedlergame.
 */
public class Driver {

    private TextIO textIO;
    private TextTerminal<?> textTerminal;
    private SiedlerGame game;
    private SiedlerBoardTextView view;

    private boolean gameRunning = true;
    private boolean winnerFinish = false;

    private static final int DEFAULT_WINPOINTS = 5;

    private String indexStringInsertion;

    /**
     * This constructor creates a new game and starts the TextIO.
     */
    public Driver(){
        textIO = TextIoFactory.getTextIO();
        textTerminal = textIO.getTextTerminal();
        textTerminal.getProperties().setPaneDimension(1200, 900);
        textTerminal.println(createHeaderForTerminal());
        game = new SiedlerGame(DEFAULT_WINPOINTS, setNumberOfPlayers());
        view = new SiedlerBoardTextView(game.getBoard());
        connectLandWithNumber();
    }

    /**
     * These Enums is in the main menu of this program.
     */
    public enum MainMenu {
        Print, Roll_Dice, ShowMyResource, Points, Quit
    }

    /**
     * After throwDice, these Enums will be shown.
     * With this, you can build roads, settlement, trade...
     */
    public enum Actions {
        Print, ShowResource, Settlement, City, Street, Trade, FinishMyTurn
    }

    /**
     * These enums will only be activated, if someone has won the game.
     */
    public enum Win {
        Print, Quit
    }

    /**
     * get the Enum Values.
     * @param textIO TextIO
     * @param commands
     * @return all the enums
     * @param <T>
     */
    public static <T extends Enum<T>> T getEnumValue(TextIO textIO, Class<T> commands) {
        return textIO.newEnumInputReader(commands).read("What would you like to do?");
    }

    private void run(){
        placeInitialSettlementAndRoad();
        while (gameRunning){
            textTerminal.println("It's " + game.getActualPlayer().getFaction().toString().toUpperCase() + "'s turn!");
            switch (getEnumValue(textIO, MainMenu.class)){
                case Print -> textTerminal.println(view.toString());
                case Roll_Dice -> spreadResourcesToPlayers(rollDice());
                case ShowMyResource -> showResource();
                case Points -> textTerminal.println("Points: " + game.getActualPlayer().getWinPoints());
                case Quit -> gameRunning = false;
                default -> throw new IllegalArgumentException("Error: Command not found!");
            }
        }

        while(winnerFinish){
            textTerminal.println("The winner is " + game.getWinner());
            switch (getEnumValue(textIO, Win.class)){
                case Print -> textTerminal.println(view.toString());
                case Quit -> winnerFinish = false;
                default -> throw new IllegalArgumentException("Error: Command not found!");
            }
        }
        textIO.dispose();
    }

    private int setNumberOfPlayers(){
        return textIO.newIntInputReader().withMinVal(2).withMaxVal(4).read("How many players?");
    }

    private void connectLandWithNumber(){
        for (Map.Entry<Point, Integer> diceNum : Config.getStandardDiceNumberPlacement().entrySet()) {
            String num = String.valueOf(diceNum.getValue());
            char[] numArray = num.toCharArray();
            if(numArray.length == 1){
                view.setLowerFieldLabel(diceNum.getKey(), new Label('0', numArray[0]));
            } else {
                view.setLowerFieldLabel(diceNum.getKey(), new Label(numArray[0], numArray[1]));
            }
        }
    }

    private void placeInitialSettlementAndRoad(){
        for(int i = 0; i < game.getPlayerFactions().size(); i++){
            textTerminal.println(view.toString());
            textTerminal.println("Settlement: ");
            while(!game.placeInitialSettlement(setCoordinate(), false)){
                textTerminal.println("Error: Coordinates are invalid for Settlement!");
            }

            textTerminal.println(view.toString());
            textTerminal.println("Road: ");
            while(!game.placeInitialRoad(setCoordinateForRoad(1), setCoordinateForRoad(2))){
                textTerminal.println("Error: Coordinates for road invalid. Please enter again!");
            }
            game.switchToNextPlayer();
        }

        for(int index = 0; index < game.getPlayerFactions().size(); index++){
            game.switchToPreviousPlayer();
            textTerminal.println(view.toString());
            textTerminal.println("Settlement: ");
            while (!game.placeInitialSettlement(setCoordinate(), true)){
                textTerminal.println("Error: Coordinates are invalid for Settlement!");
            }
            textTerminal.println(view.toString());
            textTerminal.println("Road: ");
            while(!game.placeInitialRoad(setCoordinateForRoad(1), setCoordinateForRoad(2))){
                textTerminal.println("Error: Coordinates for road invalid. Please enter again!");
            }
        }
    }

    private Point setCoordinate(){
        int x = textIO.newIntInputReader().read("Please enter x-coordinate!");
        int y = textIO.newIntInputReader().read("Please enter y-coordinate!");
        return new Point(x,y);
    }

    private Point setCoordinateForRoad(int indexOfCorner) {
        switch (indexOfCorner) {
            case 1 -> indexStringInsertion = "first";
            case 2 -> indexStringInsertion = "second";
        }
        int x = textIO.newIntInputReader().read("Please enter the " + indexStringInsertion + " x-coordinate!");
        int y = textIO.newIntInputReader().read("Please enter the " + indexStringInsertion + " y-coordinate!");
        return new Point(x, y);
    }

    private void showResource(){
        for (Map.Entry<Config.Resource, Integer> entry : game.getActualPlayer().getPlayerResource().entrySet()){
            textTerminal.println(entry.getKey() + " " + entry.getValue());
        }
    }

    private int rollDice(){
        return (int)(Math.random() * 6 + 1) + (int)(Math.random() * 6 + 1);
    }

    private void spreadResourcesToPlayers(int dice){
        textTerminal.println("Dice: " + dice);
        game.throwDice(dice);
        if (dice == 7) {
            textTerminal.println(view.toString());
            textTerminal.println("To which field should the thief move?");
            while (!game.placeThiefAndStealCard(setCoordinate())) {
                textTerminal.println("Error: Invalid field coordinates!");
            }
        }
        boolean cont = false;
        while(!cont && !winnerFinish){
            textTerminal.println("It's still " + game.getActualPlayer().getFaction().toString().toUpperCase() + "'s turn!");
            switch (getEnumValue(textIO, Actions.class)){
                case Print -> textTerminal.println(view.toString());
                case ShowResource -> showResource();
                case Settlement -> buildHouse();
                case City -> buildCity();
                case Street -> buildStreet();
                case Trade -> game.tradeWithBankFourToOne(getResourceToTrade(), getResourceToTrade());
                case FinishMyTurn -> cont = true;
                default -> throw new IllegalArgumentException("Error: Command doesn't exist!");
            }
        } game.switchToNextPlayer();
    }

    private void buildHouse(){
        if(!game.buildSettlement(setCoordinate())){
            textTerminal.println("Error: Settlement could not be built!");
        }
        if(game.getWinner() != null){
            winnerFinish = true;
            gameRunning = false;
        }
    }

    private void buildCity(){
        if(!game.buildCity(setCoordinate())){
            textTerminal.println("Error: Settlement could not be built!");
        }

        if(game.getWinner() != null){
            winnerFinish = true;
            gameRunning = false;
        }
    }

    private void buildStreet(){
        if(!game.buildRoad(setCoordinate(), setCoordinate())){
            textTerminal.println("Error: Road could not be built!");
        }
    }

    private Config.Resource getResourceToTrade(){
        Config.Resource resource;
        switch (getEnumValue(textIO, Config.Resource.class)){
            case WOOL -> resource = Config.Resource.WOOL;
            case LUMBER -> resource = Config.Resource.LUMBER;
            case BRICK -> resource = Config.Resource.BRICK;
            case GRAIN -> resource = Config.Resource.GRAIN;
            case ORE -> resource = Config.Resource.ORE;
            default -> throw new IllegalArgumentException("Error: Command doesn't exist! Try again!");
        }
        return resource;
    }

    private String createHeaderForTerminal() {
        return System.lineSeparator()
                + System.lineSeparator() + "-----------------------------------------------------------"
                + System.lineSeparator() + "|   _    _      _                            _            |"
                + System.lineSeparator() + "|  | |  | |    | |                          | |           |"
                + System.lineSeparator() + "|  | |  | | ___| | ___ ___  _ __ ___   ___  | |_ ___      |"
                + System.lineSeparator() + "|  | |/\\| |/ _ \\ |/ __/ _ \\| '_ ` _ \\ / _ \\ | __/ _ \\     |"
                + System.lineSeparator() + "|  \\  /\\  /  __/ | (_| (_) | | | | | |  __/ | || (_) |    |"
                + System.lineSeparator() + "|   \\/  \\/ \\___|_|\\___\\___/|_| |_| |_|\\___|  \\__\\___/     |"
                + System.lineSeparator() + "|   _   _            _____      _   _   _                 |"
                + System.lineSeparator() + "|  | | | |          /  ___|    | | | | | |                |"
                + System.lineSeparator() + "|  | |_| |__   ___  \\ `--.  ___| |_| |_| | ___ _ __ ___   |"
                + System.lineSeparator() + "|  | __| '_ \\ / _ \\  `--. \\/ _ \\ __| __| |/ _ \\ '__/ __|  |"
                + System.lineSeparator() + "|  | |_| | | |  __/ /\\__/ /  __/ |_| |_| |  __/ |  \\__ \\  |"
                + System.lineSeparator() + "|   \\__|_| |_|\\___| \\____/ \\___|\\__|\\__|_|\\___|_|  |___/  |"
                + System.lineSeparator() + "|          __   _____       _              _              |"
                + System.lineSeparator() + "|         / _| /  __ \\     | |            | |             |"
                + System.lineSeparator() + "|    ___ | |_  | /  \\/ __ _| |_ __ _ _ __ | |             |"
                + System.lineSeparator() + "|   / _ \\|  _| | |    / _` | __/ _` | '_ \\| |             |"
                + System.lineSeparator() + "|  | (_) | |   | \\__/\\ (_| | || (_| | | | |_|             |"
                + System.lineSeparator() + "|   \\___/|_|    \\____/\\__,_|\\__\\__,_|_| |_(_)             |"
                + System.lineSeparator() + "|                                                         |"
                + System.lineSeparator() + "-----------------------------------------------------------"
                + System.lineSeparator();
    }

    /**
     * This is the main method. This method starts the game.
     */
    public static void main(String[] args) {
        new Driver().run();
    }
}
