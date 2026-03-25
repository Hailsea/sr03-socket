import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private static final Integer PORT = 10000;

    public static void main(String[] args) {
        System.out.println("Starting server on port: " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)){
            while (true){
                Socket newClientSocket=serverSocket.accept();
                System.out.println("New client connected");
                ClientConnection newClient=new ClientConnection(newClientSocket);
                ClientConnection.clientList.add(newClient);
                newClient.start();
            }
        } catch (IOException e) {
            System.err.println("Error while starting the server: " + e.getMessage());
        }
    }
}