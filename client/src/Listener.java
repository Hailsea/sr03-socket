import java.io.*;
import java.net.Socket;

public class Listener extends Thread{
    private final BufferedReader reader;
    private String line;

    public Listener(Socket socket) throws IOException{
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.line = "";
    }

    @Override
    public void run(){
        try {
            while(true) {
                line = reader.readLine();
                System.out.println(line);
            }
        } catch (IOException e){
            System.out.println("Soucis Listener: " + e);
        }
    }
}
