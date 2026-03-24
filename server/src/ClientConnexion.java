import java.io.*;
import java.net.Socket;
import java.util.HashSet;


public class ClientConnection extends Thread {
    public static HashSet<ClientConnection> clientList = new HashSet<ClientConnexion>();
    public static HashSet<String> aliasesList = new HashSet<String>();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String alias;

    public ClientConnection(Socket s) throws IOException {
        this.out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.alias = null;
    }

    @Override
    public void run() {
        String s;
        while (true) {
            if (alias == null) {   // get the alias
                out.println("Choose an alias : ");
                try {
                    s = in.readLine();
                    System.out.println("chaine : " + s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (s == null || s.isEmpty()){
                    out.println("This alias isn't valid");
                    continue;
                }
                //check duplicates
                if (!aliasesList.contains(s)) {
                    alias = s;
                    aliasesList.add(s);
                    Broadcast((s + " has joined the chat."));
                } else {
                    out.println("This alias is already taken.");
                }
            } else {
                try {// waiting for messages or exit
                    s = in.readLine();
                    if (s.equals("exit")) {
                        clientList.remove(socket);
                        socket.close();
                    } else {
                        Broadcast(alias, s);
                    }
                } catch (IOException e){
                    System.out.println("Soucis Serveur: "+e);
                }
            }
        }
    }

    private static void Broadcast(String alias, String s) {
        for (ClientConnexion c : clientList) {
            if (c.alias!=null) c.out.println(alias +" : "+s);
        }
    }

    private static void Broadcast(String message) {
        for (ClientConnexion c : clientList)
            if (c.alias != null) c.out.println(message);
    }
}

