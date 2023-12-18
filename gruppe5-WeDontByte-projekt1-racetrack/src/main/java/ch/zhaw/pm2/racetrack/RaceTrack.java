package ch.zhaw.pm2.racetrack;

/**
 * This class contains the main method.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public class RaceTrack {

    /**
     * This method is the start point for the whole project.
     * @param args
     */
    public static void main(String args[]) {
        GameController gameController = new GameController();
        gameController.run();
    }
}
