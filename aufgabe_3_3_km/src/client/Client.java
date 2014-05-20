package client;

/**
 * Created by sacry on 16/05/14.
 */
public class Client {

    public Client(String host, int tcpPort, int udpPort) {
        MessageQueue messageQueue = new MessageQueue();
        NameHandler nameHandler = new NameHandler();
        ClientGUI gui = new ClientGUI(messageQueue, nameHandler);
        TCPToServer tcpServer = new TCPToServer(host, tcpPort, nameHandler, gui);
        new UDPClient(tcpServer, udpPort, messageQueue, gui);
    }

    public static void main(String[] args) {
        String host = "localhost";
        int tcpPort = 50000;
        int updPort = 50002;
        new Client(host, tcpPort, updPort);
    }
}
