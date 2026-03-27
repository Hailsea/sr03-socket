import java.io.*;
import java.net.Socket;
import java.util.HashSet;

//TODO thread-safe List
/**
 * Represents a connection with a client. Each client runs on a dedicated thread with its own input and output streams.
 * It manages the client's alias and handles chat communication.
 */
public class ClientConnection extends Thread {
    public static HashSet<ClientConnection> clientList = new HashSet<>();
    public static HashSet<String> aliasesList = new HashSet<>(); //TODO welcomeback
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private String alias;

    public ClientConnection(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            this.alias = registerAlias();

            broadcast(alias + " has joined the chat.");
            out.println("-------------------------");
            System.out.println("Client connected: "+alias);

            if (this.alias != null) {
                chat();
            }
        } catch (ClientConnectionException e) {
            System.out.println(e.getMessage());
        } finally {
            disconnectClient();
        }
    }

    private String registerAlias() throws ClientConnectionException {
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
            } catch (NullPointerException e){ //broken socket
                throw new ClientConnectionException(this.alias);
            } catch (IOException e) { // quand un client crash c'est ca qui catch
                throw new ClientConnectionException(this.alias);
            }
        }
    }

    private void disconnectClient() {
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error closing socket.");
        }

        clientList.remove(this);
        aliasesList.remove(alias);
        broadcast(alias + " quit the chat.");
    }

    /**
     * @param alias
     * @param s
     * Broadcast a message s from a client to all client in the chat
     */
    private static void broadcast(String alias, String s) {
        for (ClientConnection c : clientList)
            if (c.alias != null) c.out.println(alias + " : " + s);
    }

    /**
     * @param message
     * Broadcast a message from the server to all client in the chat
     */
    private static void broadcast(String message) {
        for (ClientConnection c : clientList)
            if (c.alias != null) c.out.println(message);
    }
}
