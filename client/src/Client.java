import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;

public class Client {
    private static final String HOST = "localhost";
    private static final Integer PORT = 10000;

    public static void main(String[] args) {
        try {
            Socket socket=new Socket(HOST, PORT);
            System.out.println("Connected to server on "+HOST+": "+PORT);
            Listener l=new Listener(socket);
            Sender s=new Sender(socket);
            l.start();
            s.start();
        } catch (IOException e){
            System.err.println("Could not connect to server: "+e.getMessage());
        }
    }
}