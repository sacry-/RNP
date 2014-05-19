package client;

/**
 * Created by sacry on 16/05/14.
 */
public class Client {

    public Client(String host, int tcpPort, int udpPort) {
        MyHandler handler = new MyHandler();
        ClientGUI gui = new ClientGUI(handler);
        TCPToServer tcpServer = new TCPToServer(host, tcpPort, handler);
        new UDPClient(tcpServer, udpPort, handler, gui); // gui needed to append items...
    }

    public static void main(String[] args) {
        String host = "localhost";
        int tcpPort = 50000;
        int updPort = 50001;
        new Client(host, tcpPort, updPort);
    }
}
