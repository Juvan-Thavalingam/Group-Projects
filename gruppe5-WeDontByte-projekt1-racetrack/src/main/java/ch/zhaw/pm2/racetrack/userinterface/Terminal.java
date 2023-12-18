package ch.zhaw.pm2.racetrack.userinterface;

import ch.zhaw.pm2.racetrack.PositionVector.*;
import ch.zhaw.pm2.racetrack.Track;

import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.SpaceType.*;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification.*;
import org.beryx.textio.TerminalProperties;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;


import java.util.List;
import java.awt.Color;



/**
 * This class is for all the input of the race game. It uses TextIO to read the input
 * from the console.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public class Terminal {
    private final org.beryx.textio.TextIO textIO;
    private final TextTerminal<?> textTerminal;
    private TerminalProperties<?> terminalProperties;
    private final Message message;
    public static final String EMPTY_STRING = "";

    private static final int PANEL_WIDTH = 900;
    private static final int PANEL_HEIGHT = 700;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color OUTPUT_COLOR = Color.ORANGE;
    private static final Color OUTPUT_BACKGROUND_COLOR = Color.BLACK;
    private static final Color INPUT_COLOR = Color.RED;

    /**
     * Terminal initialize the input of the game.
     */
    public Terminal() {
        textIO = TextIoFactory.getTextIO();
        message = new Message();
        textTerminal = textIO.getTextTerminal();
        initializeTerminal();
    }

    /**
     * Set the terminalsize and terminalcolor.
     */
    private void initializeTerminal() {
        terminalProperties = textTerminal.getProperties();
        textTerminal.setBookmark("Clear");

        terminalProperties.setPromptBold(true);
        terminalProperties.setInputBold(true);

        terminalProperties.setPaneDimension(PANEL_WIDTH, PANEL_HEIGHT);
        terminalProperties.setPaneBackgroundColor(BACKGROUND_COLOR);
        terminalProperties.setInputColor(INPUT_COLOR);
        terminalProperties.setPromptColor(OUTPUT_COLOR);
    }

    /**
     * Prints the track from a String in the terminal. it adds for each char color
     *
     * @param track the track to print as String
     */
    public void printTrack(String track) {
        int alternate = 0;
        for (int i = 0; i < track.length(); i++) {
            char character = track.charAt(i);

            if (character == WALL.getValue()) {
                printColorChar(character, Color.GREEN);
            } else if (character == Track.CRASH_INDICATOR) {
                printColorChar(character, Color.RED);
            } else if (character == FINISH_UP.getValue() || character == FINISH_DOWN.getValue()
                || character == FINISH_LEFT.getValue() || character == FINISH_RIGHT.getValue()) {
                alternateColorFinish(character, alternate);
                alternate++;
            } else if (character == TRACK.getValue()) {
                printColorChar(character, Color.LIGHT_GRAY, Color.LIGHT_GRAY);
            } else if (character == '\n') {
                textTerminal.println(EMPTY_STRING);
            } else {
                printColorChar(character, Color.RED, Color.LIGHT_GRAY);
            }
        }
    }

    /**
     * This method prints the InstructionsMessage to understand, what the player should do.
     *
     * @param key is the message, which will be printed.
     */
    public void printInstructionMessage(String key) {
        textTerminal.println(message.getInformation(key));
    }

    /**
     * This method prints the carId whose turn it is.
     *
     * @param carId whose turn it is.
     */
    public void printWhosTurn(char carId) {
        textTerminal.println(message.whosTurn(carId));
    }

    /**
     * This method give a normal message for Information!
     *
     * @param key ist the message key, which will be printed.
     */
    public void printInformationMessage(String key) {
        textTerminal.println(message.getInformation(key));
    }

    /**
     * This method give a normal message for Errors!
     *
     * @param key ist the message key, which will be printed.
     */
    public void printErrorMessage(String key) {
        textTerminal.println(message.getError(key));
    }

    /**
     * This method prints the carId, who has won.
     *
     * @param carId who has won.
     */
    public void printWhoHasWon(char carId) {
        textTerminal.println(message.whoIsTheWinner(carId));
    }

    /**
     * Clears the Terminal from content.
     */
    public void clearTerminal() {
        textTerminal.resetToBookmark("Clear");
    }

    /**
     * This method set the characters and background with color.
     *
     * @param character       the char
     * @param charColor       the char color
     * @param backgroundColor the background color
     */
    private void printColorChar(char character, Color charColor, Color backgroundColor) {
        terminalProperties.setPromptColor(charColor);
        terminalProperties.setPromptBackgroundColor(backgroundColor);
        textTerminal.printf("%c", character);
        terminalProperties.setPromptColor(OUTPUT_COLOR);
        terminalProperties.setPromptBackgroundColor(OUTPUT_BACKGROUND_COLOR);
    }

    /**
     * his method set the characters with color
     *
     * @param character the char
     * @param charColor the char color
     */
    private void printColorChar(char character, Color charColor) {
        printColorChar(character, charColor, OUTPUT_BACKGROUND_COLOR);
    }


    /**
     * this method sets the finish line alternate black and white
     *
     * @param character the char
     * @param alternate the current alternate int
     */
    private void alternateColorFinish(char character, int alternate) {
        if (alternate % 2 == 0) {
            printColorChar(character, Color.BLACK, Color.LIGHT_GRAY);
        } else {
            printColorChar(character, Color.WHITE, Color.LIGHT_GRAY);
        }
    }

    /**
     * Ask which Track should be selected.
     *
     * @param trackNames List with all Track Names
     * @return The selected Track
     */
    public String getTrackName(List<String> trackNames) {
        return textIO.newStringInputReader().withNumberedPossibleValues(trackNames).read(message.getInstruction("chooseTrack"));
    }

    /**
     * Ask which Strategy should be selected for the specific car.
     *
     * @param carId specific car ID
     * @return the Strategy Enum
     */
    public StrategyType getStrategy(char carId) {
        textTerminal.printf(message.getInstruction("chooseStrategy") + " '%c'", carId);
        return textIO.newEnumInputReader(StrategyType.class).read();
    }

    public Direction getDirection() {
        return textIO.newEnumInputReader(Direction.class).read(message.getInstruction("chooseDirection"));
    }

    public void closeGame() {
        textTerminal.printf("\nThank you for playing our RaceTrack game. To end press enter.");
        textTerminal.read(false);
        textTerminal.dispose();
        textIO.dispose();
    }

    public boolean askNewGame() {
        boolean newGame = false;
        if (textIO.newBooleanInputReader().read("\nWould you like to start a new game?")) {
            newGame = true;
        } else {
            closeGame();
        }
        return newGame;
    }
}
