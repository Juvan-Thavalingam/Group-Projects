package ch.zhaw.pm2.multichat.server;

    import java.util.Collection;
    import java.util.HashMap;

    import ch.zhaw.pm2.multichat.protocol.Message;
    import ch.zhaw.pm2.multichat.protocol.MultichatLogger;

/**
 * Manages all active connections. By providing functionality to add, remove
 * connections as well as stopping all connections.
 */
public class ConnectionRegistry {
    private final HashMap<String, DisposableConnectionHandler<Message>> connections = new HashMap<>();

    /**
     * Adds a new connection if there isn't a connection with the same {@code name}.
     *
     * @param name              of the connection
     * @param connectionHandler of the connection
     */
    public synchronized void addConnection(String name, DisposableConnectionHandler<Message> connectionHandler) {
        if (!connections.containsKey(name)) {
            connections.put(name, connectionHandler);
        }
    }

    /**
     * Removes a connection by name if it exists.
     *
     * @param name of the connection to remove
     */
    public synchronized void removeConnection(String name) {
        MultichatLogger.info("Close connection " + name);
        if (connections.containsKey(name)) {
            connections.get(name).disconnect();
            connections.remove(name);
            MultichatLogger.info("Connection " + name + " disconnected");
        }
    }

    /**
     * Removes all connections.
     */
    public synchronized void removeAllConnections() {
        for (String conn : connections.keySet()) {
            removeConnection(conn);
        }
    }

    /**
     * @param name of the connection
     * @return if a connection with the given name exists.
     */
    public synchronized boolean connectionNameExists(String name) {
        return connections.containsKey(name);
    }

    /**
     * @return all connections
     */
    public synchronized Collection<DisposableConnectionHandler<Message>> getAllConnections() {
        return connections.values();
    }

    /**
     * @param name of the connection to get
     * @return get a connection by name
     */
    public DisposableConnectionHandler<Message> getConnection(String name) {
        return connections.get(name);
    }
}
