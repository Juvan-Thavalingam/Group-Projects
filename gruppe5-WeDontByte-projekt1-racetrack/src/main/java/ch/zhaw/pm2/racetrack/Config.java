package ch.zhaw.pm2.racetrack;

import ch.zhaw.pm2.racetrack.given.ConfigSpecification;

import java.io.File;
import java.util.Objects;

public class Config implements ConfigSpecification {

    // Directory containing the track files
    private File trackDirectory = new File("tracks");

    // Directory containing the track files
    private File moveDirectory = new File("moves");

    // Directory containing the follower files
    private File followerDirectory = new File("follower");

    public File getMoveDirectory() {
        return moveDirectory;
    }

    public void setMoveDirectory(File moveDirectory) {
        Objects.requireNonNull(moveDirectory);
        this.moveDirectory = moveDirectory;
    }

    public File getFollowerDirectory() {
        return followerDirectory;
    }

    public void setFollowerDirectory(File followerDirectory) {
        Objects.requireNonNull(followerDirectory);
        this.followerDirectory = followerDirectory;
    }

    public File getTrackDirectory() {
        return trackDirectory;
    }

    public void setTrackDirectory(File trackDirectory) {
        Objects.requireNonNull(trackDirectory);
        this.trackDirectory = trackDirectory;
    }


}
