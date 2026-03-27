import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;

//TODO separate login from chat
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

    @Override
    public void run() {
            String s;
            while (true) {

                if (alias == null) {   // get the alias
                    out.println("Choose an alias : ");
                    try {
                        s = in.readLine();
                        if (s==null){
                            System.err.println("Unnamed client connection crashed.");
                            return;
                        }
                        else if (s.isEmpty()) {
                            out.println("This alias isn't valid.");
                            continue;
                        }
                        else if (s.equals("exit")) {
                            socket.close();
                            deleteTheClient();
                            System.out.println("Unnamed client disconnected.");
                            return;
                        }
                    } catch (IOException e) { //TODO faire une classe ClientConnectionException hihi
                        System.err.println("Unnamed client connection crashed.");
                        return;
                    }

                    //check duplicates
                    if (!aliasesList.contains(s)) {
                        alias = s;
                        aliasesList.add(s);

                        broadcast((s + " has joined the chat."));
                        out.println("-------------------------");

                        System.out.println("Client connected: "+alias);
                    } else {
                        out.println("This alias is already taken.");
                    }

                } else {
                    // waiting for messages or exit
                    try {
                        s = in.readLine();
                        if (s.equals("exit")) {
                            socket.close();
                            deleteTheClient();
                            System.out.println("Client disconnected: "+alias);
                            return;
                        } else {
                            broadcast(alias, s);
                            System.out.println(alias+": "+s);
                        }
                    } catch (NullPointerException e){ //broken socket
                        deleteTheClient();
                        System.err.println(alias+" disappeared. Client left unexpectedly.");
                        return;
                    } catch (IOException e) {
                        System.err.println("Error Server: "+e.getMessage());
                        return;
                    }
                }
            }
        }

    /**
     * Delete the client of clientList and aliasesList and Broadcast a message
     */
    private void deleteTheClient() {
        clientList.remove(this);
        aliasesList.remove(alias);
        broadcast(alias + " quit the chat.");
    }
}


