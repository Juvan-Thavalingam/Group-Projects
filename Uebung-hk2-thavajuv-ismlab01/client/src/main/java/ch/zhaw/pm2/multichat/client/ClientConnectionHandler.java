package ch.zhaw.pm2.multichat.client;

import static ch.zhaw.pm2.multichat.protocol.ConnectionHandler.ConnectionState.CONFIRM_CONNECT;
import static ch.zhaw.pm2.multichat.protocol.ConnectionHandler.ConnectionState.CONFIRM_DISCONNECT;
import static ch.zhaw.pm2.multichat.protocol.ConnectionHandler.ConnectionState.CONNECTED;
import static ch.zhaw.pm2.multichat.protocol.ConnectionHandler.ConnectionState.DISCONNECTED;
import static ch.zhaw.pm2.multichat.protocol.ConnectionHandler.ConnectionState.NEW;
import static ch.zhaw.pm2.multichat.protocol.Message.DataType.CONNECT;
import static ch.zhaw.pm2.multichat.protocol.Message.DataType.DISCONNECT;
import static ch.zhaw.pm2.multichat.protocol.Message.DataType.ERROR;
import static ch.zhaw.pm2.multichat.protocol.Message.DataType.INFO;

import java.io.IOException;

import ch.zhaw.pm2.multichat.protocol.ChatProtocolException;
import ch.zhaw.pm2.multichat.protocol.ConnectionHandler;
import ch.zhaw.pm2.multichat.protocol.Message;
import ch.zhaw.pm2.multichat.protocol.Message.DataType;
import ch.zhaw.pm2.multichat.protocol.MultichatLogger;
import ch.zhaw.pm2.multichat.protocol.NetworkHandler;

/**
 * Responsible for the connection of a client. It can start and stop receiving.
 * For each received message an action is executed based on the type of the
 * message (e.g. {@link ClientConnectionHandler#handleDisconnect} is executed
 * when a message with the type {@link DataType#DISCONNECT} is received).
 */
public class ClientConnectionHandler extends ConnectionHandler {

    private final ClientState clientState;

    /**
     * Creates a new {@link ClientConnectionHandler} from an existing client state.
     * This will set the username and create a connection to the host that is set in
     * the client state.
     *
     * @param clientState which to use to create the connection
     * @throws IOException if an error occurred opening the connection, e.g. server
     *                     is not responding.
     */
    public ClientConnectionHandler(ClientState clientState) throws IOException {
        this.clientState = clientState;
        onStartup();
    }

    /**
     * Sets the new connection state.
     *
     * If the new state is of type {@link ConnectionState#CONNECTED} or
     * {@link ConnectionState@CONFIRM_DISCONNECT} the {@code IsConnectedProperty}
     * in the {@link ClientState} will be set to {@code true}.
     *
     * @param newState to set
     */
    @Override
    protected void setConnectionState(ConnectionState newState) {
        super.setConnectionState(newState);
        clientState.isConnectedProperty().set(newState == CONNECTED || newState == CONFIRM_DISCONNECT);
    }

    /**
     * Sends a message to the server with the username and the
     * {@link DataType#CONNECT} type. Once the message is sent the state of the
     * connection handler will be changed to {@link ConnectionState#CONFIRM_CONNECT}
     *
     * @throws ChatProtocolException if the connection state isn't
     *                               {@link ConnectionState#NEW}
     */
    public void connect() throws ChatProtocolException {
        if (getConnectionState() != NEW) {
            throw new ChatProtocolException("Illegal state for connect: " + getConnectionState());
        }
        sendData(new Message(userName, USER_NONE, CONNECT));
        setConnectionState(CONFIRM_CONNECT);
    }

    /**
     * Sends a message to the server to disconnect the current user. After that the
     * connection handler state will change to
     * {@link ConnectionState#CONFIRM_DISCONNECT}
     *
     * @throws ChatProtocolException if the connection state isn't
     *                               {@link ConnectionState#NEW} and not
     *                               {@link ConnectionState#CONNECTED}
     */
    public void disconnect() throws ChatProtocolException {
        if (getConnectionState() != NEW && getConnectionState() != CONNECTED) {
            throw new ChatProtocolException("Illegal state for disconnect: " + getConnectionState());
        }
        sendData(new Message(userName, USER_NONE, DISCONNECT));
        setConnectionState(CONFIRM_DISCONNECT);
    }

    /**
     * @param message to be sent
     * @throws ChatProtocolException if the connection state isn't
     *                               {@link ConnectionState#CONNECTED}
     */
    public void sendMessage(Message message) throws ChatProtocolException {
        if (getConnectionState() != CONNECTED) {
            throw new ChatProtocolException("Illegal state for message: " + getConnectionState());
        }
        message.setSender(userName);
        sendData(message);
    }

    /**
     * The client doesn't expect a connect message.
     */
    @Override
    protected void handleConnect(Message data) {
        MultichatLogger.error("Illegal connect request from server");
    }

    /**
     * Called if a message with type {@link Message.DataType#CONFIRM} is received.
     *
     * Updates the {@code userName} in the connection handler and the client state
     * based on the incoming message receiver.
     *
     * If the current state of the connection handler is {@link ConnectionState#CONFIRM_CONNECT}
     * the state will change to {@link ConnectionState#CONNECTED}.
     * If the current state of the connection handler is {@link ConnectionState#CONFIRM_DISCONNECT}
     * the state will change to {@link ConnectionState#DISCONNECTED}.
     *
     * @param data received
     */
    @Override
    protected void handleConfirm(Message data) {
        userName = data.getReceiver();
        clientState.userNameProperty().set(userName);
        clientState.addMessage(data, INFO);
        MultichatLogger.info("CONFIRM: " + data.getPayload());

        if (state == ConnectionState.CONFIRM_CONNECT) {
            setConnectionState(ConnectionState.CONNECTED);
        } else if (state == ConnectionState.CONFIRM_DISCONNECT) {
            setConnectionState(ConnectionState.DISCONNECTED);
        }
    }

    /**
     * Called if a message with type {@link Message.DataType#DISCONNECT} is received.
     * And set the connection state to {@link Message.DataType#DISCONNECT}.
     *
     * @param data received
     */
    @Override
    protected void handleDisconnect(Message data) {
        if (getConnectionState() == DISCONNECTED) {
            MultichatLogger.error("DISCONNECT: Already in disconnected: " + data.getPayload());
            return;
        }
        clientState.addMessage(data, INFO);
        MultichatLogger.info("DISCONNECT: " + data.getPayload());
        setConnectionState(DISCONNECTED);
    }

    /**
     * Called if a message with type {@link Message.DataType#MESSAGE} is received.
     * And adds the message to the {@link ClientState}, if the connection is connected.
     *
     * @param data received
     */
    @Override
    protected void handleMessage(Message data) {
        if (getConnectionState() != CONNECTED) {
            MultichatLogger.error("MESSAGE: Illegal state " + getConnectionState() + " for message: " + data.getPayload());
            return;
        }
        clientState.addMessage(data);
        MultichatLogger.info("MESSAGE: From " + data.getSender() + " to " + data.getReceiver() + ": " + data.getPayload());
    }

    /**
     * Called if a message with type {@link Message.DataType#ERROR} is received.
     * Error message will be added to the {@link ClientState}.
     *
     * @param data received
     */
    @Override
    protected void handleError(Message data) {
        clientState.addMessage(data, ERROR);
        MultichatLogger.error("ERROR: " + data.getPayload());
    }

    private void onStartup() {
        new Thread(() -> establishConnection(clientState.getRemoteHost(), clientState.getRemotePort())).start();
    }

    /**
     * Called when the state changes to {@link ConnectionState#NEW}.
     * Stars a thread ro receive messages and sends a connection request.
     */
    @Override
    protected void onNew() {
        new Thread(() -> startReceiving()).start();

        try {
            connect();
        } catch (ChatProtocolException e) {
            clientState.addMessage(new Message(DataType.ERROR, e.getMessage()));
        } finally {
            clientState.isLoadingProperty().set(false);
        }
    }

    /**
     * Called if the receiving is interrupted.
     * And sets the connection state to {@link ConnectionState#DISCONNECTED}.
     */
    @Override
    protected void onInterrupted() {
        MultichatLogger.info("Disconnected because of an interruption.");
        setConnectionState(DISCONNECTED);
    }

    /**
     * Called when the state changes to {@link ConnectionState#DISCONNECTED}.
     * Stops the message receiving
     */
    @Override
    protected void onDisconnected() {
        stopReceiving();
    }

    /**
     * Opens a connection with the set hostname and port.
     * Set connection state to {@link ConnectionState#NEW}.
     *
     * @param hostname to open the connection
     * @param port to open the connection
     */
    private void establishConnection(String hostname, int port) {
        try {
            clientState.isLoadingProperty().set(true);
            userName = clientState.getUserName();
            connection = NetworkHandler.openConnection(hostname, port);
            setConnectionState(ConnectionState.NEW);
        } catch (IOException e) {
            clientState.addMessage(new Message(DataType.ERROR, e.getMessage()));
            clientState.isLoadingProperty().set(false);
        }
    }
}
