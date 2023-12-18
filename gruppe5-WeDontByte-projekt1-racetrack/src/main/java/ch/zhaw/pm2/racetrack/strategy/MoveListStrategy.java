package ch.zhaw.pm2.racetrack.strategy;

import ch.zhaw.pm2.racetrack.PositionVector.Direction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class handles the MoveStrategy MoveList.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public class MoveListStrategy implements MoveStrategy {
    private List<Direction> moveList = new ArrayList<>();
    private int currentLine;

    /**
     * This constructor creates the MoveListStrategy and creates a scanner.
     *
     * @param moveFile is the file for this strategy.
     * @throws FileNotFoundException if the given move file could not be found
     */
    public MoveListStrategy(File moveFile) throws FileNotFoundException {
        currentLine = -1;
        readFile(moveFile);
    }

    /**
     * This method reads the file line by line.
     *
     * @return the direction of the current line.
     */
    @Override
    public Direction nextMove() {
        currentLine++;
        return moveList.get(currentLine);
    }

    /**
     * This method reads a specific file
     *
     * @param moveFile the file with the moves
     * @throws FileNotFoundException if no file is found
     */
    private void readFile(File moveFile) throws FileNotFoundException {
        String nextLineString;
        Scanner scanner = new Scanner(moveFile);
        while (scanner.hasNextLine()) {
            nextLineString = scanner.nextLine();
            if (!nextLineString.isBlank()) {
                moveList.add(Direction.valueOf(nextLineString));
            }
        }
        scanner.close();
    }
}
