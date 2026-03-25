import java.io.*;
import java.net.Socket;
import java.util.HashSet;


public class ClientConnection extends Thread {
    public static HashSet<ClientConnection> clientList = new HashSet<ClientConnection>();
    public static HashSet<String> aliasesList = new HashSet<String>();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
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
                        System.out.println("client : " + s);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    if (s == null || s.isEmpty()) {
                        out.println("This alias isn't valid");
                        continue;
                    }
                    //check duplicates
                    if (!aliasesList.contains(s)) {
                        alias = s;
                        aliasesList.add(s);
                        Broadcast((s + " has joined the chat."));
                        out.println("-------------------------");
                    } else {
                        out.println("This alias is already taken.");
                    }
                } else {
                    try {// waiting for messages or exit
                        s = in.readLine();
                        if (s.equals("exit")) {
                            clientList.remove(this);
                            aliasesList.remove(this.alias);
                            socket.close();
                            Broadcast(alias + " quite the chat.");
                            System.out.println("A client is gone");
                            return;
                        } else {
                            Broadcast(alias, s);
                            System.out.println(alias + ": " + s);
                        }
                    } catch (IOException e) {
                        System.out.println("Soucis Serveur: " + e);
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

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void setOut(PrintWriter out) {
        this.out = out;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}


