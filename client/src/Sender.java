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
        this.start();
    }

    @Override
    public void run(){
        while(!line.equals("exit")){
            try {
                line = keyboard.readLine();
                out.println(line);
            } catch (IOException e) {
                System.out.println("Soucis Sender");
                break;
            }
        }
    }
}
