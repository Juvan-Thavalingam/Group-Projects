package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification.*;
import ch.zhaw.pm2.racetrack.strategy.*;
import ch.zhaw.pm2.racetrack.userinterface.TextInterface;

import static ch.zhaw.pm2.racetrack.Game.NO_WINNER;
import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.FileType.*;
import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.PrintType.*;
import static ch.zhaw.pm2.racetrack.given.ConfigSpecification.StrategyType.DO_NOT_MOVE;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the game process.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public class GameController {
    private TextInterface textInterface;
    private Track track;
    private Game game;
    private boolean correctSetup;

    /**
     * This constructor creates a GameController object, which creates a new TextInterface.
     */
    public GameController() {
        textInterface = new TextInterface();
    }

    /**
     * This method calls up the methods of the individual phases of the game.
     */
    public void run() {
        setup();
        mainGame();
    }


    /**
     * This method starts the main phase.
     */
    private void mainGame() {
        if (correctSetup) {
            gamePlay();
        } else {
            askPlayerForNewGame();
        }
    }

    /**
     * This method handles the setup phase.
     */
    private void setup() {
        textInterface = new TextInterface();
        textInterface.printMessage(INFORMATION, "welcome");
        initTrack();
        game = new Game(track);
        initStrategy();
    }

    /**
     * This method handles the main phase.
     */
    private void gamePlay() {
        do {
            if (onlyDoNotMove()) {
                break;
            }
            Car activeCar = track.getCarList().get(game.getCurrentCarIndex());
            textInterface.printTrack(track.toString());
            textInterface.printWhosTurn(activeCar.getCarID());
            game.doCarTurn(activeCar.getMoveStrategy().nextMove());
            game.switchToNextActiveCar();
        }
        while (game.getWinner() == NO_WINNER);
        textInterface.printTrack(track.toString());
        if (!onlyDoNotMove()) {
            textInterface.printWhoHasWon(track.getCarList().get(game.getWinner()).getCarID());
        } else {
            textInterface.printMessage(INFORMATION, "allDoNotMove");
        }
        askPlayerForNewGame();
    }

    /**
     * This method checks if all remaining cars have the strategy do not move.
     *
     * @return true if all non do not move cars are crashed.
     */
    private boolean onlyDoNotMove() {
        boolean onlyDoNot;
        List<Car> doNotCarList = new ArrayList<>();
        List<Car> livingCarList = new ArrayList<>();

        for (Car car : track.getCarList()) {
            if (car.getMoveStrategy().toString().equals("DO_NOT_MOVE")) {
                doNotCarList.add(car);
            }

            if (!car.isCrashed() && !(car.getMoveStrategy().toString().equals("DO_NOT_MOVE"))) {
                livingCarList.add(car);
            }
        }

        onlyDoNot = doNotCarList.size() > 1 && livingCarList.isEmpty();

        return onlyDoNot;
    }

    /**
     * This method asks if the player want to start a new game.
     */
    private void askPlayerForNewGame() {
        if (textInterface.askNewGame()) {
            textInterface.clearTerminal();
            run();
        }
    }

    /**
     * This method assigns the move strategy to the player.
     */
    private void initStrategy() {
        boolean isPathFollow = false;
        boolean correctMoveFile = true;
        int countDoNotMoveCars = 0;

        for (int i = 0; i < track.getCarList().size(); i++) {
            StrategyType strategyType = textInterface.selectStrategyType(game.getCarId(i));

            switch (strategyType) {
                case DO_NOT_MOVE -> track.getCarList().get(i).setMoveStrategy(new DoNotMoveStrategy());
                case USER -> track.getCarList().get(i).setMoveStrategy(new UserMoveStrategy(textInterface));
                case MOVE_LIST -> {
                    try {
                        track.getCarList().get(i).setMoveStrategy(new MoveListStrategy(textInterface.selectFile(MOVE_FILE)));
                    } catch (FileNotFoundException exception) {
                        textInterface.printMessage(ERROR, "noFolderMove");
                        correctMoveFile = false;
                    } catch (NullPointerException exception) {
                        textInterface.printMessage(ERROR, "noMoveFile");
                        correctMoveFile = false;
                    } catch (IllegalArgumentException exception) {
                        textInterface.printMessage(ERROR, "invalidMove");
                        correctMoveFile = false;
                    }
                }
                case PATH_FOLLOWER -> {
                    textInterface.printMessage(INFORMATION, "pathFollow");
                    isPathFollow = true;
                }
            }
            if (isPathFollow) {
                textInterface.closeGame();
                break;
            }
            if (strategyType == DO_NOT_MOVE) {
                countDoNotMoveCars++;
            }
        }
        correctSetup = checkDoNotMoveCars(countDoNotMoveCars) && correctMoveFile;
    }


    /**
     * returns false if all cars have the DO_NOT_MOVE strategy selected
     *
     * @param countDoNotMoveCars how many cars have O_NOT_MOVE strategy selected
     * @return false or true
     */
    private boolean checkDoNotMoveCars(int countDoNotMoveCars) {
        if (countDoNotMoveCars == track.getCarList().size()) {
            textInterface.clearTerminal();
            textInterface.printMessage(INFORMATION, "allDoNotMove");
            return false;
        }
        return true;
    }

    /**
     * This method selects the track for the game.
     */
    private void initTrack() {
        try {
            File trackFile = textInterface.selectFile(TRACK_FILE);
            track = new Track(trackFile);
        } catch (FileNotFoundException exception) {
            textInterface.printMessage(ERROR, "noTrack");
            textInterface.closeGame();
        } catch (InvalidTrackFormatException exception) {
            textInterface.printMessage(ERROR, "invalidTrack");
            textInterface.closeGame();
        } catch (NullPointerException exception) {
            textInterface.printMessage(ERROR, "noTrackFile");
            textInterface.closeGame();
        }
    }
}

