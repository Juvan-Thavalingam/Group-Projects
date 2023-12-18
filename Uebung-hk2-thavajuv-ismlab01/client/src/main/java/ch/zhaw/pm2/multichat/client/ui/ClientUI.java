package ch.zhaw.pm2.multichat.client.ui;

import ch.zhaw.pm2.multichat.protocol.MultichatLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        chatWindow(primaryStage);
    }

    private void chatWindow(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ChatWindow.fxml"));
            Pane rootPane = loader.load();
            Scene scene = new Scene(rootPane);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setMinWidth(420);
            primaryStage.setMinHeight(250);
            primaryStage.setTitle("Multichat Client");
            primaryStage.show();

            primaryStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
                ChatWindowController controller = loader.getController();
                controller.applicationClose();
            });

        } catch (Exception e) {
            e.printStackTrace();
            MultichatLogger.error("Error starting up UI" + e.getMessage());
        }
    }
}
