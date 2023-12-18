package ch.zhaw.pm2.multichat.client.ui;

import ch.zhaw.pm2.multichat.protocol.MultichatLogger;
import javafx.application.Application;

public class Client {
    private static final String LOGNAME = "client.log";

    public static void main(String[] args) {
        MultichatLogger.createLog(LOGNAME);
        MultichatLogger.info("Starting Client Application");
        Application.launch(ClientUI.class, args);
        MultichatLogger.info("Client Application ended");
    }
}

