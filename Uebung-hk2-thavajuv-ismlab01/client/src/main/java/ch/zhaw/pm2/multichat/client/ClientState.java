package ch.zhaw.pm2.multichat.client;

import ch.zhaw.pm2.multichat.protocol.Message;
import ch.zhaw.pm2.multichat.protocol.NetworkHandler;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class ClientState {
    private final ListProperty<Message> messages;

    private final StringProperty filter = new SimpleStringProperty();
    private final StringProperty userName = new SimpleStringProperty();
    private final StringProperty remoteHost = new SimpleStringProperty();
    private final StringProperty remotePort = new SimpleStringProperty();
    private final BooleanProperty isConnected = new SimpleBooleanProperty();
    private final BooleanProperty isLoading = new SimpleBooleanProperty();

    public ClientState() {
        ObservableList<Message> observableList = FXCollections.observableArrayList();
        messages = new SimpleListProperty<>(observableList);

        remoteHost.set(NetworkHandler.DEFAULT_ADDRESS.getCanonicalHostName());
        remotePort.set(String.valueOf(NetworkHandler.DEFAULT_PORT));
    }

    public String getUserName() {
        return userName.get();
    }

    public StringProperty userNameProperty() {
        return userName;
    }

    public String getRemoteHost() {
        return remoteHost.get();
    }

    public StringProperty remoteHostProperty() {
        return remoteHost;
    }

    public StringProperty remotePortProperty() {
        return remotePort;
    }

    public BooleanProperty isConnectedProperty() {
        return isConnected;
    }

    public Boolean isConnected() {
        return isConnected.get();
    }

    public BooleanProperty isLoadingProperty() {
        return isLoading;
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addMessage(Message message, Message.DataType dataType) {
        message.setType(dataType);
        messages.add(message);
    }


    /**
     * @return a list of messages based on the current filter.
     */
    public List<Message> getFilteredMessageList() {
        List<Message> filteredMessages = new ArrayList<>();

        for (Message message : messages) {
            if (messageContainsFilter(message)) {
                filteredMessages.add(message);
            }
        }

        return filteredMessages;
    }

    public ListProperty<Message> getClientMessageListProperty() {
        return messages;
    }

    public StringProperty getFilterProperty() {
        return filter;
    }


    /**
     * @return the server port from the input field. If the field is empty or can't
     *         be parsed into a number the default port will be used.
     */
    public int getRemotePort() {
        int serverPort = NetworkHandler.DEFAULT_PORT;
        String serverPortValue = remotePort.get();

        if (serverPortValue.isBlank()) {
            addMessage(new Message(Message.DataType.INFO, "Use default port number: " + serverPort));
            return serverPort;
        }

        try {
            serverPort = Integer.parseInt(serverPortValue);
        } catch (NumberFormatException e) {
            addMessage(new Message(Message.DataType.ERROR, "Port should be a number"));
        }

        return serverPort;
    }

    /**
     * Checks if the filter applies to a given message. It checks the
     * Sender, Receiver and the payload.
     * @param message to check
     * @return if the message should be shown or not.
     */
    private boolean messageContainsFilter(Message message) {
        String filterText = filter.get();
        boolean showAll = filterText == null || filterText.isBlank();

        return showAll || message.getSender().contains(filterText) || message.getReceiver().contains(filterText)
            || message.getPayload().contains(filterText);
    }

}
