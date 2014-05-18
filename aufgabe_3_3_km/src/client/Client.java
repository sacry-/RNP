package client;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by sacry on 16/05/14.
 */
public class Client {

    public Client(String host, int tcpPort, int udpPort) {
        MessageQueue queue = new MessageQueue();
        ClientGUI gui = new ClientGUI(queue);
        TCPToServer tcpServer = new TCPToServer(host, tcpPort, gui);
        new UDPClient(tcpServer, udpPort, gui, queue);
    }

    public static void main(String[] args) {
        String host = "localhost";
        int tcpPort = 50000;
        int updPort = 50002;
        new Client(host, tcpPort, updPort);
    }
}
