import java.io.*;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a connection with a client.
 * Each client runs on a dedicated thread with its own input and output streams.
 * It manages the client's alias and handles chat communication.
 */
public class ClientConnection extends Thread {

    public static Set<ClientConnection> clientList = ConcurrentHashMap.newKeySet(); //Thread safe set
    public static Set<String> aliasesList = ConcurrentHashMap.newKeySet(); // Thread safe set
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private String alias;

    public ClientConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    /**
     * Main execution loop of the thread.
     * Handles the initial alias setup and then starts the chat session.
     */
    @Override
    public void run() {
        try {
            this.alias = askAlias();

            if (this.alias != null) {
                broadcast(alias + " has joined the chat.");
                out.println("-------------------------");
                System.out.println("Client connected: "+alias);
                chat();
            }
        } catch (ClientConnectionException e) {
            System.out.println(e.getMessage());
        } finally {
            disconnectClient();
        }
    }

    /**
     * Asks the client for an alias and validates it.
     *
     * @return the chosen alias, or null if the client requests to exit.
     * @throws ClientConnectionException if a socket error occurs.
     */
    private String askAlias() throws ClientConnectionException {
        String alias;
        while(true) {
            out.println("Choose an alias : ");
            try {
                alias = in.readLine();

                if (alias == null) {
                    throw new ClientConnectionException();
                } else if (alias.isEmpty()) {
                    out.println("This alias isn't valid.");
                } else if (alias.equals("exit")) {
                    System.out.println("Unnamed client disconnected.");
                    return null;
                } else {
                    if (!aliasesList.contains(alias)) {
                        aliasesList.add(alias);
                        return alias;
                    } else {
                        out.println("This alias is already taken.");
                    }
                }
            } catch (IOException e){
                throw new ClientConnectionException();
            }
        }
    }

    /**
     * Chat that listens for messages from the client and broadcasts them.
     *
     * @throws ClientConnectionException if the connection is lost or an I/O error occurs.
     */
    private void chat() throws ClientConnectionException{
        String message;
        while(true){ // waiting for messages or exit
            try {
                message = in.readLine();
                if (message == null) {
                    throw new ClientConnectionException(this.alias);
                }
                if (message.equals("exit")) {
                    System.out.println("Client disconnected: "+this.alias);
                    return;
                } else {
                    broadcast(alias, message);
                    System.out.println(alias+": "+message);
                }
            } catch (IOException e) {
                throw new ClientConnectionException(this.alias);
            }
        }
    }

    /**
     * Closes the client socket and cleanup the client from the lists and broadcasting the departure.
     */
    private void disconnectClient() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket.");
        }

        clientList.remove(this);
        try{
            aliasesList.remove(alias);
            broadcast(alias + " quit the chat.");
        }
        catch(NullPointerException e){} // alias non défini
    }

    /**
     * Broadcasts a formatted message from a specific client to all connected clients.
     *
     * @param alias The alias of the sender.
     * @param message The message content to broadcast.
     */
    private static void broadcast(String alias, String message) {
        for (ClientConnection c : clientList) {
            if (c.alias != null) c.out.println(alias + " : " + message);
        }
    }

    /**
     * Broadcasts a message to all connected clients.
     *
     * @param message The server message to broadcast.
     */
    private static void broadcast(String message) {
        for (ClientConnection c : clientList){
            if (c.alias != null) c.out.println(message);
        }
    }
}
