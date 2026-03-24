import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public class Client extends Thread {
    public static HashMap<Client,String> clientList=new HashMap<Client,String>();
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private String alias;

    public Client(Socket s){
        this.socket=s;
        try {
            this.in=this.socket.getInputStream();
            this.out=this.socket.getOutputStream();
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
                in.read();
                chaine=new String(b,0,bytesRead);
            }
        } catch (Exception e) {
            System.err.println("Error during communication with client : " + e.getMessage());
        }
    }

    public static void Broadcast(String message) throws IOException {
        for (Client c : clientList.keySet()) { //check later
            c.out.write(message.getBytes());
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