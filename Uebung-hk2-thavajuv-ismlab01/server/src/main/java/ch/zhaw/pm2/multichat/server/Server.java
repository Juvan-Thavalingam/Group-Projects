package ch.zhaw.pm2.multichat.server;

import java.io.IOException;
import java.net.SocketException;

import ch.zhaw.pm2.multichat.protocol.Message;
import ch.zhaw.pm2.multichat.protocol.MultichatLogger;
import ch.zhaw.pm2.multichat.protocol.NetworkHandler;
import ch.zhaw.pm2.multichat.protocol.NetworkHandler.NetworkConnection;
import ch.zhaw.pm2.multichat.protocol.NetworkHandler.NetworkServer;

/**
 * Handles the multichat server. It listens to incoming connection requests and
 * handles these connections. Once the server is shutdown it disconnects all
 * active connections.
 */
public class Server {

    private static final ConnectionRegistry connectionRegistry = new ConnectionRegistry();
    private final NetworkServer<Message> networkServer;
    private static final String LOGNAME = "server.log";

    /**
     * Starts the multichat server and prepares the shutdown process to execute.
     *
     * @param args can contain a port number to use.
     */
    public static void main(String[] args) {
        MultichatLogger.createLog(LOGNAME);
        try {
            int port = getPortNumberFromArgs(args);
            final Server server = new Server(port);
            Runtime.getRuntime().addShutdownHook(new Thread(() -> shutdown(server)));
            server.start();
        } catch (IOException | IllegalArgumentException e) {
            MultichatLogger.error("Error while starting server." + e.getMessage());
        }
    }

    /**
     * Gets the port from the passed arguments. If no argument is passed then the
     * {@link NetworkHandler#DEFAULT_PORT} will be used.
     *
     * @param args to get the port number from
     * @return the port number to use
     * @throws IllegalArgumentException if the number of arguments is neither zero
     *                                  nor one
     */
    private static int getPortNumberFromArgs(String[] args) {
        int port;
        switch (args.length) {
            case 0:
                port = NetworkHandler.DEFAULT_PORT;
                break;
            case 1:
                port = Integer.parseInt(args[0]);
                break;
            default:
                throw new IllegalArgumentException("Illegal number of arguments:  [<ServerPort>]");
        }

        return port;
    }

    /**
     * This adds a shutdown hook running a cleanup task if the JVM is terminated
     * (kill -HUP, Ctrl-C,...)
     *
     * @param server to terminate
     */
    private static void shutdown(Server server) {
        MultichatLogger.info("Shutdown initiated...");
        server.terminate();
        MultichatLogger.info("Shutdown complete.");
    }

    /**
     * Creates a new instance of a server.
     *
     * @param serverPort to open on the server host.
     * @throws IOException if an error occurred opening the port, e.g. the port
     *                     number is already used.
     */
    public Server(int serverPort) throws IOException {
        MultichatLogger.info("Create server connection");
        networkServer = NetworkHandler.createServer(serverPort);
        MultichatLogger.info("Listening on " + networkServer.getHostAddress() + ":" + networkServer.getHostPort());
    }

    /**
     * Starts the server instance. And listens to incoming connections until the
     * server is stopped or an exception occurs.
     */
    private void start() {
        MultichatLogger.info("Server started.");
        try {
            while (!networkServer.isClosed()) {
                NetworkConnection<Message> connection = networkServer.waitForConnection();
                ServerConnectionHandler connectionHandler = new ServerConnectionHandler(connection, connectionRegistry);

                MultichatLogger.info(String.format("Connected new Client %s with IP:Port <%s:%d>",
                    connectionHandler.getUserName(), connection.getRemoteHost(), connection.getRemotePort()));
            }
        } catch (SocketException e) {
            MultichatLogger.error("Server connection terminated " + e.getMessage());
        } catch (IOException e) {
            MultichatLogger.error("Communication error " + e.getMessage());
        } finally {
            MultichatLogger.info("Server Stopped.");
        }
    }

    /**
     * Removes all active connections and closes the network server.
     */
    private void terminate() {
        try {
            MultichatLogger.info("Close server port.");
            connectionRegistry.removeAllConnections();
            MultichatLogger.info("Close server port.");
            networkServer.close();
        } catch (IOException e) {
            MultichatLogger.error("Failed to close server connection: " + e.getMessage());
        }
    }
}
