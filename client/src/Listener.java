import java.io.*;
import java.net.Socket;

/**
 * Class handling client-side input. This thread listens to the socket and prints any received messages to the client console
 */
public class Listener extends Thread {
    private final BufferedReader reader;

    public Listener(Socket socket) throws IOException {
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run(){
        try {
            while() {
                String line = reader.readLine();
                if (line!=null)  {
                    System.out.println(line);
                }
            }
            return;
        }
        catch (IOException e){
            System.err.println("Error Listener: " + e);
        }
    }
}
