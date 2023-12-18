package ch.zhaw.pm2.racetrack.userinterface;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles all kind of messages.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public class Message {

    Map<String, String> instructionList = new HashMap<>();
    Map<String, String> informationList = new HashMap<>();
    Map<String, String> errorList = new HashMap<>();

    /**
     * Creates a Message object with three initialised lists.
     */
    public Message() {
        initInstruction();
        initInformation();
        initErrors();
    }

    /**
     * This method initialise the instruction message list.
     */
    private void initInstruction() {
        instructionList.put("chooseTrack", "\nPlease choose a track");
        instructionList.put("chooseStrategy", "\nPlease choose a strategy for the car");
        instructionList.put("chooseDirection", "\nPlease enter a number for the direction you choose:\n");
    }

    /**
     * This method initialise the information message list.
     */
    private void initInformation() {
        informationList.put("welcome", "\nWelcome to RaceTrack.");
        informationList.put("allDoNotMove", "\nAll cars have set to 'do not move' strategy.");
        informationList.put("pathFollow", "\nPath follow is not implemented.");
    }


    /**
     * Create the String whose turn it ist
     * @param carId the char from the car
     * @return the String whose turn it is with the char from the car
     */
    public String whosTurn(char carId) {
        return String.format("\nCar " + "%c" + "'s turn", carId);
    }

    /**
     * Create the String who has won the game
     * @param carId the char from the car
     * @return the String who has won with the char from the winner
     */
    public String whoIsTheWinner(char carId) {
        return String.format("\nCongratulation Player with the car " + "%c" + " has won!", carId);
    }


    /**
     * This method initialise the error message list.
     */
    private void initErrors() {
        errorList.put("noTrack", "\nNo track file found");
        errorList.put("noTrackFile", "\nNo track files were found in track folder.");
        errorList.put("invalidTrack", "\nInvalid Track file, please choose a different one");
        errorList.put("noFolderMove", "\nNo move folder found");
        errorList.put("noMoveFile", "\nNo move files were found in moves folder.");
        errorList.put("invalidMove", "\nInvalid Move File, please choose a different one.");
    }

    /**
     * This method returns the corresponding instruction.
     *
     * @param key for the message
     * @return related message
     */
    public String getInstruction(String key) {
        return instructionList.get(key);
    }

    /**
     * This method returns the corresponding information.
     *
     * @param key for the message
     * @return related message
     */
    public String getInformation(String key) {
        return informationList.get(key);
    }

    /**
     * This method returns the corresponding error.
     *
     * @param key for the message
     * @return related message
     */
    public String getError(String key) {
        return errorList.get(key);
    }
}

