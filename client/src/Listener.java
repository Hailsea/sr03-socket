import java.io.*;
import java.net.Socket;

public class Listener extends Thread{
    private final BufferedReader reader;

    public Listener(Socket socket) throws IOException{
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run(){
        while(true){
            try {
                String message = reader.readLine();
                System.out.println(message);
            } catch (IOException e) {
                System.out.println("Soucis");
                break;
            }
        }
    }
}
