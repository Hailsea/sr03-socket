import java.io.*;
import java.net.Socket;

public class Listener extends Thread{
    private final BufferedReader reader;

    public Listener(Socket socket) throws IOException{
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run(){
        try {
            while(true) {
                String line = reader.readLine();
                if (line == null)
                    return;
                System.out.println(line);
            }
        } catch (IOException e){
            System.out.println("Error Listener: " + e);
        }
    }
}
