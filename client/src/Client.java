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
            Socket socket = new Socket(HOST, PORT);
            System.out.println("Connected to server on "+HOST+": "+PORT);
            Listener listener = new Listener(socket);
            Sender sender = new Sender(socket);

            listener.start();
            sender.start();
            int i=0;
            while (listener.isAlive() || sender.isAlive()){
                if (!sender.isAlive() && !listener.isInterrupted()) { //stop listener when sender is stopped
                    listener.interrupt();
                    Thread.sleep(10);
                }
                else if (!listener.isAlive() && !sender.isInterrupted()) { //stop sender when listener is stopped
                    System.err.println("Lost connection to server.");
                    sender.interrupt();
                    Thread.sleep(10);
                }
            }
            socket.close();

        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
        } catch (InterruptedException e){
            System.err.println("Sleep error");
        }
    }
}