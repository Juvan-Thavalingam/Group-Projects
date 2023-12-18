package ch.zhaw.pm2.multichat.server;

/**
 * A disposable connection has the following actions:
 *
 * <ul>
 * <li>Send data of type {@code T}</li>
 * <li>Close the connection</li>
 * </ul>
 */
public interface DisposableConnectionHandler<T> {
    /**
     * Closes the current connection.
     */
    void disconnect();

    /**
     * Sends data through the current connection.
     *
     * @param data to be sent
     */
    void sendData(T data);
}
