package client;

/**
 * Created by sacry on 16/05/14.
 */
public class Client {

    public Client() {
        new TCPToServer();
    }

    public static void main(String[] args) {
        new Client();
    }
}
