import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;


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
                            DeleteTheClient();
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

                        Broadcast((s + " has joined the chat."));
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
                            DeleteTheClient();
                            System.out.println("Client disconnected: "+alias);
                            return;
                        } else {
                            Broadcast(alias, s);
                            System.out.println(alias+": "+s);
                        }
                    } catch (NullPointerException e){ //broken socket
                        DeleteTheClient();
                        System.err.println(alias+" disappeared. Client left unexpectedly.");
                        return;
                    } catch (IOException e) {
                        System.err.println("Error Server: "+e.getMessage());
                        return;
                    }
                }
            }
        }

    private static void Broadcast(String alias, String s) {
        for (ClientConnection c : clientList)
            if (c.alias!=null) c.out.println(alias +" : "+s);
    }

    private static void Broadcast(String message) {
        for (ClientConnection c : clientList)
            if (c.alias != null) c.out.println(message);
    }
    private void DeleteTheClient(){
        clientList.remove(this);
        aliasesList.remove(alias);
        Broadcast(alias+" quit the chat.");
    }
}
