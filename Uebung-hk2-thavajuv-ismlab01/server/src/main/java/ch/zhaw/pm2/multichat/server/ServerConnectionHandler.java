package ch.zhaw.pm2.multichat.server;

import static ch.zhaw.pm2.multichat.protocol.ConnectionHandler.ConnectionState.CONNECTED;
import static ch.zhaw.pm2.multichat.protocol.ConnectionHandler.ConnectionState.DISCONNECTED;
import static ch.zhaw.pm2.multichat.protocol.ConnectionHandler.ConnectionState.NEW;

import java.util.concurrent.atomic.AtomicInteger;

import ch.zhaw.pm2.multichat.protocol.ChatProtocolException;
import ch.zhaw.pm2.multichat.protocol.ConnectionHandler;
import ch.zhaw.pm2.multichat.protocol.Message;
import ch.zhaw.pm2.multichat.protocol.MultichatLogger;
import ch.zhaw.pm2.multichat.protocol.NetworkHandler.NetworkConnection;

/**
 * Connection handler for the service. It's responsible to process incoming
 * messages.
 */
public class ServerConnectionHandler extends ConnectionHandler implements DisposableConnectionHandler<Message> {
    private static final AtomicInteger connectionCounter = new AtomicInteger(0);
    private final ConnectionRegistry connectionRegistry;

    /**
     * Creates a new instance of a server connection handler sets the state to
     * {@link ConnectionState#NEW} and the username to anonymous.
     *
     * @param connection         new connection to handle
     * @param connectionRegistry containing all connections the server holds
     */
    public ServerConnectionHandler(NetworkConnection<Message> connection, ConnectionRegistry connectionRegistry) {
        super(connection);
        this.connectionRegistry = connectionRegistry;
        userName = "Anonymous-" + connectionCounter.incrementAndGet();

        setConnectionState(NEW);
    }

    /**
     * Sends a disconnect message and changes the state to
     * {@link ConnectionState#DISCONNECTED}
     */
    public void disconnect() {
        sendData(new Message(USER_NONE, userName, Message.DataType.DISCONNECT, "Disconnecting " + userName + " from server"));
        setConnectionState(DISCONNECTED);
    }

    /**
     * Called if a message with type {@link Message.DataType#CONNECT} is received.
     * If no connection with the username from the incoming message exists the
     * connection will be added to the registry and the confirmation message is sent
     * back to the client.
     *
     * @param data received
     * @throws ChatProtocolException If the connection state is invalid or the
     *                               username already exists in the connection
     *                               registry.
     */
    @Override
    protected void handleConnect(Message data) throws ChatProtocolException {
        if (getConnectionState() != NEW) {
            throw new ChatProtocolException("Illegal state for connect request: " + getConnectionState());
        }
        if (data.getSender() == null || data.getSender().isBlank()) {
            data.setSender(userName);
        }

        if (connectionRegistry.connectionNameExists(data.getSender())) {
            throw new ChatProtocolException("User name already taken: " + data.getSender());
        }

        userName = data.getSender();
        connectionRegistry.addConnection(userName, this);
        sendData(new Message(USER_NONE, userName, Message.DataType.CONFIRM, "Registration successful for " + userName));
        setConnectionState(CONNECTED);
    }

    /**
     * The server doesn't expect a confirmation message.
     *
     * @param data received
     */
    @Override
    protected void handleConfirm(Message data) {
        MultichatLogger.info("Not expecting to receive a CONFIRM request from client");
    }

    /**
     * Called if a message with type {@link Message.DataType#DISCONNECT} is
     * received. A confirmation message is sent back to the client and the
     * connection is removed from the registry.
     *
     * @param data received
     */
    @Override
    protected void handleDisconnect(Message data) throws ChatProtocolException {
        if (getConnectionState() == DISCONNECTED) {
            throw new ChatProtocolException("Illegal state for disconnect request: " + getConnectionState());
        }

        sendData(new Message(USER_NONE, userName, Message.DataType.CONFIRM, "Confirm disconnect of " + userName));
        connectionRegistry.removeConnection(userName);
    }

    /**
     * Called if a message with type {@link Message.DataType#MESSAGE} is received.
     * Depending on the receiver it's either send to a specific user or to all users.
     *
     * If the receiver doesn't exist in the connection registry a message will be sent to the sender that the receiver doesn't exist.
     *
     * A message to a specific receiver is also sent to the sender.
     *
     * @param data received
     */
    @Override
    protected void handleMessage(Message data) throws ChatProtocolException {
        if (getConnectionState() != CONNECTED) {
            throw new ChatProtocolException("Illegal state for message request: " + getConnectionState());
        }
        if (Message.USER_ALL.equals(data.getReceiver())) {
            for (DisposableConnectionHandler<Message> handler : connectionRegistry.getAllConnections()) {
                handler.sendData(data);
            }
        } else {
            DisposableConnectionHandler<Message> connection = connectionRegistry.getConnection(data.getReceiver());
            if (connection != null) {
                connection.sendData(data);
                sendData(data);
            } else {
                sendData(new Message(USER_NONE, userName, Message.DataType.ERROR,
                    "Unknown User: " + data.getReceiver()));
            }
        }
    }

    /**
     * Called if a message with type {@link Message.DataType#ERROR} is received.
     * All error messages will be logged.
     *
     * @param data received
     */
    @Override
    protected void handleError(Message data) {
        MultichatLogger.error("Received error from client (" + data.getSender() + "): " + data.getType());
    }

    /**
     * Called when the state changes to {@link ConnectionState#NEW}.
     * Stars a thread ro receive messages.
     */
    @Override
    protected void onNew() {
        Thread receivingThread;
        receivingThread = new Thread(this::startReceiving);
        receivingThread.start();
    }

    /**
     * Called if the receiving is interrupted.
     * Removes the current connection from the registry.
     */
    @Override
    protected void onInterrupted() {
        connectionRegistry.removeConnection(userName);
    }

    /**
     * Called when the state changes to {@link ConnectionState#DISCONNECTED}.
     * Stops the message receiving
     */
    @Override
    protected void onDisconnected() {
        stopReceiving();
    }
}
