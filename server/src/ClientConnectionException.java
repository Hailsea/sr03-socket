public class ClientConnectionException extends Exception {

    public ClientConnectionException() {
        super("Unnamed client connection crashed.");
    }

    public ClientConnectionException(String alias) {
        super(alias + " disappeared. Client left unexpectedly.");
    }
}