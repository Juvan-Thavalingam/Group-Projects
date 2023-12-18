package ch.zhaw.pm2.racetrack.userinterface;

import ch.zhaw.pm2.racetrack.PositionVector.*;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * This class handles the TextInterface.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public class TextInterface implements UserInterface {
    private final Terminal terminal;
    private static final int EMPTY = 0;

    /**
     * The constructor creates a new terminal.
     */
    public TextInterface() {
        terminal = new Terminal();
    }

    /**
     * This methods asks if the player(s) want to play a new game.
     *
     * @return true if a new game should start and false not.
     */
    @Override
    public boolean askNewGame() {
        return terminal.askNewGame();
    }

    /**
     * This method closes the terminal.
     */
    @Override
    public void closeGame() {
        terminal.closeGame();
    }

    /**
     * This method prints a specific message.
     *
     * @param type defines the kind of message.
     * @param key  specifies the message.
     */
    @Override
    public void printMessage(PrintType type, String key) {
        switch (type) {
            case INSTRUCTION -> terminal.printInstructionMessage(key);
            case INFORMATION -> terminal.printInformationMessage(key);
            case ERROR -> terminal.printErrorMessage(key);
        }
    }

    /**
     * This method prints who's turn it is.
     *
     * @param carId is the char of the car.
     */
    public void printWhosTurn(char carId) {
        terminal.printWhosTurn(carId);
    }

    /**
     * This method prints who won.
     *
     * @param carId is the char of the car.
     */
    public void printWhoHasWon(char carId) {
        terminal.printWhoHasWon(carId);
    }

    /**
     * This method prints the track.
     *
     * @param track is the track as a string.
     */
    @Override
    public void printTrack(String track) {
        terminal.clearTerminal();
        terminal.printTrack(track);
    }

    /**
     * This method clears the terminal.
     */
    @Override
    public void clearTerminal() {
        terminal.clearTerminal();
    }

    /**
     * This method selects the strategy of the car.
     *
     * @param carId is the char of the car.
     * @return the selected strategy.
     */
    @Override
    public StrategyType selectStrategyType(char carId) {
        return terminal.getStrategy(carId);
    }

    /**
     * This method selects the direction of the acceleration.
     *
     * @return the direction of the acceleration.
     */
    @Override
    public Direction selectDirection() {
        return terminal.getDirection();
    }

    /**
     * This method handles the selection of a file.
     *
     * @param fileType defines which kind of file can be selected.
     * @return the selcted file.
     */
    @Override
    public File selectFile(FileType fileType) throws FileNotFoundException {

        ArrayList<String> fileNameList = new ArrayList<>();

        File[] fileList;
        fileList = getFileList(fileType);

        if (fileList == null) {
            throw new FileNotFoundException();
        } else if (fileList.length == 0) {
            throw new NullPointerException();
        }

        for (File track : fileList) {
            fileNameList.add(getFileNameWithoutExtension(track));
        }

        String trackName = terminal.getTrackName(fileNameList);

        for (int i = 0; i < fileNameList.size(); i++) {
            if (trackName.equals(getFileNameWithoutExtension(fileList[i]))) {
                return fileList[i];
            }
        }
        return fileList[EMPTY];
    }

    /**
     * This method removes the extension of the file (bsp. "example.txt").
     *
     * @param file is the source path of the desired file.
     * @return the filename without extension.
     */
    private String getFileNameWithoutExtension(File file) {
        String fileName = "";

        if (file != null && file.exists()) {
            String name = file.getName();
            fileName = name.replaceFirst("[.][^.]+$", "");
        }
        return fileName;
    }

    /**
     * This method returns a list of the desired type files.
     *
     * @param fileType selects the type of files.
     * @return the file list.
     */
    private File[] getFileList(FileType fileType) {
        return switch (fileType) {
            case TRACK_FILE -> new File("./tracks").listFiles(pathname -> pathname.getName().toLowerCase().endsWith(".txt"));
            case MOVE_FILE -> new File("./moves").listFiles(pathname -> pathname.getName().toLowerCase().endsWith(".txt"));
        };
    }
}
