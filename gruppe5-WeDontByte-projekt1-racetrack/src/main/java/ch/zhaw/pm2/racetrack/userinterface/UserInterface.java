package ch.zhaw.pm2.racetrack.userinterface;

import ch.zhaw.pm2.racetrack.PositionVector.*;
import ch.zhaw.pm2.racetrack.given.ConfigSpecification.*;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * This interface defines the interface for the UserInterface.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public interface UserInterface {

    boolean askNewGame();

    void closeGame();

    void printMessage(PrintType type, String key);

    void printTrack(String track);

    void clearTerminal();

    StrategyType selectStrategyType(char carId);

    Direction selectDirection();

    File selectFile(FileType fileType) throws FileNotFoundException;
}
