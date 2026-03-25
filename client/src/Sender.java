import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Sender extends Thread{
    private final PrintWriter out;
    private final BufferedReader keyboard;
    private String line = "";

    public Sender(Socket socket) throws IOException {
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        this.keyboard = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        while (!line.equals("exit") && !Thread.currentThread().isInterrupted()) {
            try {
                if (keyboard.ready()){
                    line = keyboard.readLine();
                    if (!line.isEmpty())
                        out.println(line);
                }

            } catch (IOException e) {
                System.out.println("Error Sender: " + e.getMessage());
                break;
            }
        }
    }
}
