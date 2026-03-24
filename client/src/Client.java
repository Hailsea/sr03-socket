import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;

public class Client {
    private static final String HOST = "localhost";
    private static final Integer PORT = 10000;

    public static void main(String[] args) {
        try {
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Connected to server on " + HOST + ": " + PORT);
            new Listener(socket);
            new Sender(socket);
        } catch (IOException e){
            System.err.println("Could not connect to server: " + e.getMessage());
        }
    }
}