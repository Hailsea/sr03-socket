import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Sender extends Thread{
    private final PrintWriter out;
    private final BufferedReader keyboard;

    public Sender(Socket socket) throws IOException {
        this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        this.keyboard = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run(){

        while(true){
            try {
                String message = keyboard.readLine();
                System.out.println(message);
                out.println(message);
            } catch (IOException e) {
                System.out.println("Soucis Sender");
                break;
            }
        }
    }
}
