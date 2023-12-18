package ch.zhaw.pm2.multichat.client.ui;
import ch.zhaw.pm2.multichat.client.ClientConnectionHandler;
import ch.zhaw.pm2.multichat.client.ClientState;
import ch.zhaw.pm2.multichat.protocol.ChatProtocolException;
import ch.zhaw.pm2.multichat.protocol.Message;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.io.IOException;
import java.util.List;


public class ChatWindowController {
    private ClientConnectionHandler connectionHandler;
    private ClientState clientState;

    @FXML
    private TextField remoteHostField;
    @FXML
    private TextField remotePortField;
    @FXML
    private TextField userNameField;
    @FXML
    private TextField messageField;
    @FXML
    private TextFlow messageArea;
    @FXML
    private Button connectButton;
    @FXML
    private Button sendButton;
    @FXML
    private TextField filterValue;
    @FXML
    private ScrollPane messageScroll;

    @FXML
    public void initialize() {
        clientState = new ClientState();

        sendButton.setDisable(true);
        messageField.setDisable(true);

        remoteHostField.textProperty().bindBidirectional(clientState.remoteHostProperty());
        remoteHostField.disableProperty().bind(clientState.isConnectedProperty());

        remotePortField.textProperty().bindBidirectional(clientState.remotePortProperty());
        remotePortField.disableProperty().bind(clientState.isConnectedProperty());

        userNameField.textProperty().bindBidirectional(clientState.userNameProperty());
        userNameField.disableProperty().bind(clientState.isConnectedProperty());

        filterValue.textProperty().bindBidirectional(clientState.getFilterProperty());

        connectButton.disableProperty().bindBidirectional(clientState.isLoadingProperty());

        clientState.isConnectedProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> connectButton.setText(newValue ? "Disconnect" : "Connect"));
            messageField.setDisable(!newValue);
        });

        clientState.getClientMessageListProperty().addListener((observable, oldValue, newValue) -> redrawMessageList());

        messageField.textProperty().addListener((observable, oldValue, newValue) -> sendButton.setDisable(newValue == null || newValue.isBlank()));

        filterValue.textProperty().addListener((observable, oldValue, newValue) -> redrawMessageList());
    }

    public void applicationClose() {
        if (connectionHandler != null) {
            disconnect();
        }
    }

    @FXML
    private void toggleConnection() {
        if (!clientState.isConnected()) {
            connect();
        } else {
            disconnect();
        }
    }

    private void connect() {
        try {
            if(clientState.getUserName() != null && !clientState.getUserName().isBlank()) {
                clientState.userNameProperty().set(clientState.getUserName().replace(" ", ""));
            }

            connectionHandler = new ClientConnectionHandler(clientState);
        } catch (IOException e) {
            clientState.addMessage(new Message(Message.DataType.ERROR, e.getMessage()));
        }
    }

    private void disconnect() {
        if (!clientState.isConnected()) {
            clientState.addMessage(new Message(Message.DataType.ERROR, "Client is not connected"));
            return;
        }
        try {
            connectionHandler.disconnect();
        } catch (ChatProtocolException e) {
            clientState.addMessage(new Message(Message.DataType.ERROR, e.getMessage()));
        }
    }

    /**
     * Sends a message based on the message text input.
     *
     * A message can contain a receiver by using the following syntax:
     * `@<username>`. If no receiver is specified the message is sent to all
     * connected clients.
     *
     * An error will be shown if:
     * <ul>
     * <li>Something went wrong during sending it</li>
     * </ul>
     *
     * Once a message is sent or an error is shown the message field is cleared.
     */
    @FXML
    private void sendMessage() {
        if (!clientState.isConnected()) {
            clientState.addMessage(new Message(Message.DataType.ERROR, "Client is not connected"));
            return;
        }

        String messageString = messageField.getText().strip();
        if(messageString.isBlank()) {
            return;
        }

        Message messageToSend = new Message(messageString);
        try {
            connectionHandler.sendMessage(messageToSend);
        } catch (ChatProtocolException e) {
            clientState.addMessage(new Message(Message.DataType.ERROR, e.getMessage()));
        }

        messageField.clear();
    }


    /**
     * clears and redraw the message List.
     */
    private void redrawMessageList() {
        Platform.runLater(() -> {
            messageArea.getChildren().clear();
            List<Message> filteredMessages = clientState.getFilteredMessageList();

            for (Message message : filteredMessages) {
                HighlightedMessage highlightedMessage = new HighlightedMessage(message);
                Text messageText = highlightedMessage.getMessageText();
                messageArea.getChildren().add(messageText);
            }

            messageScroll.setVvalue(messageScroll.getVmax());
        });
    }
}
