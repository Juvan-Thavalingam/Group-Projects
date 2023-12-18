package ch.zhaw.pm2.multichat.protocol;

import java.io.IOException;
import java.util.logging.*;

/**
 * This class is used as logger
 * It's responsible to log infos and errors into file and the console
 */
public class MultichatLogger {
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private MultichatLogger(){}

    /**
     * In this method the logs will be created and are ready
     * to put messages in the log file and in the console.
     * @param logName name of the log file
     */
    public static void createLog(String logName) {
        LogManager.getLogManager().reset();
        LOGGER.setLevel(Level.ALL);

        ConsoleHandler consoleHandler = new ConsoleHandler();
        LOGGER.addHandler(consoleHandler);

        try {
            FileHandler fileHandler = new FileHandler(logName);
            LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            error(e.getMessage());
        }
    }

    /**
     * Adds an info message.
     * @param message to add
     */
    public static void  info (String message){
        LOGGER.info(message);
    }

    /**
     * Adds an error message.
     * @param message to add
     */
    public static void error(String message){
        LOGGER.severe(message);
    }
}
