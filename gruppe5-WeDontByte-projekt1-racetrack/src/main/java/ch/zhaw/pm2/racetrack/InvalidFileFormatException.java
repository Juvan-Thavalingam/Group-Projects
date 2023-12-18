package ch.zhaw.pm2.racetrack;

/**
 * This class defines the InvalidFileFormatException.
 *
 * @author Labinot Ismaili, Philipp Schiess, Juvan Thavalingam, Philemon Wildberger
 * @version 1.0
 */
public class InvalidFileFormatException extends Exception {

    private final String key; // property for diagnostic/recovery

    /**
     * This constructor creates an InvalidFileFormatException object.
     * @param key is the error message for the toString method.
     */
    public InvalidFileFormatException(String key) {
        this.key = key;
    }

    /**
     * This method returns the error message of the exception.
     * @return the error message.
     */
    public String getKey() {
        return key;
    }

    /**
     * This method defines the Exception toString.
     * @return the prefix for the kind of exception + the error message
     */
    @Override
    public String toString() {
        return "Invalid file format: '" + key +"'.";
    }
}
