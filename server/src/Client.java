import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashSet;


public class Client extends Thread {
    public static HashSet<Client> clientList=new HashSet<Client>();
    public static HashSet<String> aliasesList=new HashSet<String>();
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private String alias;

    public Client(Socket s){
        this.socket=s;
        try {
            in=this.socket.getInputStream();
            out=this.socket.getOutputStream();
        } catch (IOException e) {
            System.out.println("Error while getting data streams from socket.");
        }
        this.alias=null;
    }
    @Override
    public void run(){
        byte[] b=new byte[200];
        String chaine="";
        int bytesRead=0;
        try {
            while (true){
                if (alias==null){   // get the alias
                    out.write("Choose an alias : ".getBytes());
                    in.read();
                    chaine=new String(b,0,bytesRead);
                    //check duplicates
                    if (!aliasesList.contains(chaine)) {
                        alias = chaine;
                        aliasesList.add(chaine);
                        Broadcast((chaine + " has joined the chat."));
                    }
                    else{
                        out.write("This alias is already taken.".getBytes());
                    }
                }
                else{               // waiting for messages or exit
                    in.read();
                    chaine=new String(b,0,bytesRead);
                    if (chaine.equals("exit")) {
                        clientList.remove(socket);
                        socket.close();
                    }
                    else{
                        Broadcast(alias, chaine);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error during communication with client : " + e.getMessage());
        }
    }

    public static void Broadcast(String message) throws IOException {
        for (Client c : clientList) {
            if (c.alias!=null) c.out.write((message).getBytes());
        }
    }

    public static void Broadcast(String message,String name) throws IOException {
        for (Client c : clientList) { //check later
            if (c.alias!=null) c.out.write((name +" : "+message).getBytes());
        }
    }

    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public OutputStream getOut() {
        return out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}

