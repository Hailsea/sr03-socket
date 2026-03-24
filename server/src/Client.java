import java.io.*;
import java.net.Socket;
import java.util.HashSet;


public class Client extends Thread {
    public static HashSet<Client> clientList = new HashSet<Client>();
    public static HashSet<String> aliasesList = new HashSet<String>();
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String alias;

    public Client(Socket s) throws IOException {
        this.out = new PrintWriter(new OutputStreamWriter(s.getOutputStream()), true);
        this.in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.alias = null;
    }

    @Override
    public void run() {
        String s;
        while (true) {
            if (alias == null) {   // get the alias
                out.println("Choose an alias : ");
                try {
                    s = in.readLine();
                    System.out.println("chaine : " + s);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //check duplicates
                if (s.isEmpty() && !aliasesList.contains(s)) {
                    alias = s;
                    aliasesList.add(s);
                    Broadcast((s + " has joined the chat."));
                } else {
                    out.println("This alias is already taken.");
                }
            } else {
                try {// waiting for messages or exit
                    s = in.readLine();
                    if (s.equals("exit")) {
                        clientList.remove(socket);
                        socket.close();
                    } else {
                        Broadcast(alias, s);
                    }
                } catch (IOException e){
                    System.out.println("Soucis Serveur: "+e);
                }
            }
        }
    }

    private static void Broadcast(String alias, String s) {
        for (Client c : clientList) {
            if (c.alias!=null) c.out.println(alias +" : "+s);
        }
    }

    private static void Broadcast(String message) {
        for (Client c : clientList)
            if (c.alias != null) c.out.println(message);
    }
}
//    @Override
//    public void run(){
//        byte[] b=new byte[200];
//        String chaine="";
//        int bytesRead=0;
//        try {
//            while (true){
//                if (alias==null){   // get the alias
//                    out.write("Choose an alias : ".getBytes());
//                    while (bytesRead==-1){
//                        bytesRead=in.read(b);
//                    }
//                    chaine=new String(b,0,bytesRead);
//                    System.out.println("chaine : "+chaine);
//                    //check duplicates
//                    /*
//                    if (chaine.isEmpty() && !aliasesList.contains(chaine)) {
//                        alias = chaine;
//                        aliasesList.add(chaine);
//                        Broadcast((chaine + " has joined the chat."));
//                    }
//                    else{*/
//                        out.write("This alias is already taken.".getBytes());
//                   // }
//                }
//                else{               // waiting for messages or exit
//                    while (bytesRead==-1){
//                        bytesRead=in.read(b);
//                    }
//                    chaine=new String(b,0,bytesRead);
//                    if (chaine.equals("exit")) {
//                        clientList.remove(socket);
//                        socket.close();
//                    }
//                    else{
//                        Broadcast(alias, chaine);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            System.err.println("Error during communication with client : " + e.getMessage());
//        }
//    }
        //public static void Broadcast(String message) throws IOException {
//        for (Client c : clientList) {
//            if (c.alias!=null) c.out.write((message).getBytes());
//        }
    }


//    public static void Broadcast(String message,String name) throws IOException {
//        for (Client c : clientList) { //check later
//            if (c.alias!=null) c.out.write((name +" : "+message).getBytes());
//        }
    }

    public Socket getSocket() {
        return socket;
    }
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public OutputStream getOut() {
        return out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}

