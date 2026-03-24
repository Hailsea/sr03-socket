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
            while ((line = reader.readLine()) != null && !line.equals("exit") )
                System.out.println(line);
        } catch (IOException _){
        }
    }
}
