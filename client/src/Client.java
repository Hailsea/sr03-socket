import java.io.IOException;
import java.net.Socket;

/**
 * Client-side class that connects to the server, listens for server responses, and sends messages to the server.
 */
public class Client {
    private static final String HOST = "localhost";
    private static final Integer PORT = 10000;

    public static void main(String[] args) {
        try {
            Socket socket=new Socket(HOST, PORT);
            System.out.println("Connected to server on "+HOST+": "+PORT);
            Listener l=new Listener(socket);
            Sender s=new Sender(socket);
            s.start();
            l.start();
            while (l.isAlive() || s.isAlive()){

                if (!s.isAlive()) {
                    socket.close();
                    l.interrupt();
                    System.out.println(l.getState());
                    System.out.println(s.getState());
                    System.out.println(l.isAlive());
                    System.out.println(s.isAlive());
                    System.out.println("Client exit");
                }
                if (!l.isAlive()) {
                    socket.close();
                    s.interrupt();
                    System.out.println(l.isAlive());
                    System.out.println(s.isAlive());
                    System.err.println("Lost connection to server.");
                }
            }
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());

        }
    }
}