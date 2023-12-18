package ch.zhaw.pm2.multichat.protocol;


import ch.zhaw.pm2.multichat.protocol.NetworkHandler.NetworkConnection;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

import static ch.zhaw.pm2.multichat.protocol.ConnectionHandler.ConnectionState.CONNECTED;

public abstract class ConnectionHandler {

    protected NetworkHandler.NetworkConnection<Message> connection;
    protected static final String USER_NONE = "";
    protected String userName = USER_NONE;
    protected ConnectionState state = ConnectionState.NEW;

    public ConnectionHandler() {

    }

    /**
     * Enums are showing possible connection states and gives information
     * about the status of a connection
     */
    public enum ConnectionState {
        NEW, CONFIRM_CONNECT, CONNECTED, CONFIRM_DISCONNECT, DISCONNECTED
    }

    protected ConnectionHandler(NetworkConnection<Message> connection) {
        this.connection = connection;
    }

    /**
     * Calls the event handler depending on the new state.
     */
    protected void onConnectionStateChange() {
        switch (state) {
            case NEW:
                onNew();
                break;
            case DISCONNECTED:
                onDisconnected();
                break;
            default:
                break;
        }
    }

    /**
     * Is called when the connection handler state changes.
     */
    protected abstract void onNew();

    /**
     * Is called when the connection handler state changes to
     * {@link ConnectionState}
     */
    protected abstract void onDisconnected();{
    }

    public ConnectionState getConnectionState() {
        return state;
    }

    public void sendData(Message message) {
        if (connection.isAvailable()) {
            try {
                connection.send(message);
            } catch (SocketException e) {
                MultichatLogger.error("Connection closed: " + e.getMessage());
            } catch (EOFException e) {
                MultichatLogger.error("Connection terminated by remote");
            } catch(IOException e) {
                MultichatLogger.error("Communication error: " + e.getMessage());
            }
        }
    }

    /**
     * @param message to be sent
     * @throws ChatProtocolException if the connection state isn't
     * {@link ConnectionState}
     */
    public void sendMessage(Message message) throws ChatProtocolException {
        if (getConnectionState() != CONNECTED) {
            throw new ChatProtocolException("Illegal state for message: " + getConnectionState());
        }
        message.setSender(userName);
        sendData(message);
    }

    /**
     * sets a new connection state and calls the {@link ConnectionHandler}
     * @param newState to set
     */
    protected void setConnectionState(ConnectionState newState) {
        state = newState;
        onConnectionStateChange();
    }

    public String getUserName() {
        return userName;
    }

    /**
     * Starts receiving messages. If a message is received it will execute a message type.
     * Should an exception interrupt during the receiving or the connection being
     * stopped then the processing stops.
     */
    public void startReceiving() {
        MultichatLogger.info("Starting Connection Handler for " + userName);
        try {
            MultichatLogger.info("Start receiving data...");
            while (connection.isAvailable()) {
                Message data = connection.receive();
                processData(data);
            }
            MultichatLogger.info("Stopped recieving data");
        } catch (SocketException e) {
            MultichatLogger.error("Connection terminated locally");
            onInterrupted();
            MultichatLogger.error("Unregistered because client connection terminated: " + userName + " " + e.getMessage());
        } catch (EOFException e) {
            MultichatLogger.error("Connection terminated by remote");
            onInterrupted();
            MultichatLogger.error("Unregistered because client connection terminated: " + userName + " " + e.getMessage());
        } catch (IOException e) {
            MultichatLogger.error("Communication error: " + e);
        } catch (ClassNotFoundException e) {
            MultichatLogger.error("Received object of unknown type: " + e.getMessage());
        }
        MultichatLogger.info("Stopping Connection Handler for " + userName);
    }

    /**
     * Is called when {@link ConnectionHandler#startReceiving()}
     * is interrupted from an exception.
     */
    protected abstract void onInterrupted();

    /**
     * Stops receiving by closing the connection.
     */
    public void stopReceiving() {
        MultichatLogger.info("Closing Connection Handler for " + userName);
        try {
            MultichatLogger.info("Stop receiving data...");
            connection.close();
            MultichatLogger.info("Stopped receiving data.");
        } catch (IOException e) {
            MultichatLogger.error("Failed to close connection." + e);
        }
        MultichatLogger.info("Closed Connection Handler for " + userName);
    }

    /**
     * In this method the data is read from the given Message and then it is handled
     * according to the type.
     *
     * @param data The given Message to process.
     */
    protected void processData(Message data) {
         try{
             switch (data.getType()){
                 case CONNECT:
                     handleConnect(data);
                     break;
                 case CONFIRM:
                     handleConfirm(data);
                     break;
                 case DISCONNECT:
                     handleDisconnect(data);
                     break;
                 case MESSAGE:
                     handleMessage(data);
                     break;
                 case ERROR:
                     handleError(data);
                     break;
                 default:
                     MultichatLogger.error("Unknown data type received: " + data.getType());
                     break;
             }
         } catch (Exception error){
             MultichatLogger.error("Error processing data: " + error.getMessage());
             sendData(new Message(USER_NONE, getUserName(), Message.DataType.ERROR, error.getMessage()));
         }
    }

    /**
     * If, when processing a message, the data uses the
     * {@link Message.DataType#CONNECT} type, it is further handled individually
     * with this abstract method.
     *
     * @param data the data of the message.
     * @throws ChatProtocolException If an exception interrupt in the ChatProtocol, it will be thrown.
     */
    protected abstract void handleConnect(Message data) throws ChatProtocolException;

    /**
     * If, when processing a message, the data uses the
     * {@link Message.DataType} type, it is further handled individually
     * with this abstract method.
     *
     * @param data The data of the message.
     * @throws ChatProtocolException If an exception interrupt in the ChatProtocol, it will be thrown.
     */
    protected abstract void handleConfirm(Message data) throws ChatProtocolException;

    /**
     *  If, when processing a message, the data uses the
     * {@link Message.DataType} type, it is further handled individually
     * with this abstract method.
     *
     * @param data The data of the message
     * @throws ChatProtocolException If an exception interrupt in the ChatProtocol, it will be thrown.
     */
    protected abstract void handleDisconnect(Message data) throws ChatProtocolException;

    /**
     * If, when processing a message, the data uses the
     * {@link Message.DataType#MESSAGE} type, it is further handled individually
     * with this abstract method.
     *
     * @param data The data of the message
     * @throws ChatProtocolException If an exception interrupt in the ChatProtocol, it will be thrown.
     */
    protected abstract void handleMessage(Message data) throws ChatProtocolException;

    /**
     * If, when processing a message, the data uses the
     * {@link Message.DataType#ERROR} type, it is further handled individually with
     * this abstract method.
     *
     * @param data The data of the message
     * @throws ChatProtocolException If an exception interrupt in the ChatProtocol, it will be thrown.
     */
    protected abstract void handleError(Message data) throws ChatProtocolException;
}


