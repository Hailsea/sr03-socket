import java.io.*;
import java.net.Socket;

/**
 * Class handling client-side output. This thread reads user input from the standard input and sends it to the server.
 */
public class Sender extends Thread {
    private final PrintWriter out;
    private final BufferedReader keyboard;
    private String line = "";

    public Sender(Socket socket) throws IOException {
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        this.keyboard = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted() && !line.equals("exit")) {
                if (keyboard.ready()) {
                    line = keyboard.readLine();
                    if (!line.isEmpty()) {
                        out.println(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error Sender: " + e.getMessage());
        }
    }
}
