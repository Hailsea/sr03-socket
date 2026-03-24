import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class Listener extends Thread{
    private final BufferedReader reader;
    private String line;

    public Listener(Socket socket) throws IOException{
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.line = "";
        this.start();
    }

    @Override
    public void run(){
        try {
            while((line = reader.readLine()) != null)
                System.out.println(line);
        } catch (SocketException e){
            System.out.println("Disconnect: " + e);
        } catch (IOException e){
            System.out.println("Soucis Listener: " + e);
        }
    }
}
