package client;

import server.ServerUser;

import java.io.*;
import java.net.*;

/**
 * Created by sacry on 18/05/14.
 */
public class UDPClient extends Thread {

    public static final int PORT = 50001;
    private TCPToServer tcpServer;
    private MessageQueue messageQueue;
    private ClientGUI gui;

    private int udpPort;

    private Listener listener;
    private Sender sender;

    public UDPClient(TCPToServer tcpServer, int udpPort, MessageQueue messageQueue, ClientGUI gui) {
        this.tcpServer = tcpServer;
        this.messageQueue = messageQueue;
        this.gui = gui;
        this.udpPort = udpPort;

        sender = new Sender();
        sender.setDaemon(true);
        sender.start();

        listener = new Listener(gui);
        listener.setDaemon(true);
        listener.start();

        start();
    }


    @Override
    public void run() {
        // while deamons alive, will be dead if main thread on socket is killed
        while (sender.isAlive() && listener.isAlive()) {
            if (!gui.isRunning) // early callback
                break;
        }
    }

    class Sender extends Thread {

        public Sender() {
        }

        public void run() {
            System.out.println("something started");
            while (gui.isRunning) {
                try {
                    String input = messageQueue.dequeMessage();
                    if (input == null)
                        break;
                    sendToAll(input);
                } catch (Exception e) {
                    System.out.println("Eroor sending datagram " + e);
                }
            }
        }

        private void sendToAll(String input) {
            byte[] data = input.getBytes();
            System.out.println("Sending...");
            for (ClientUser user : tcpServer.activeUsers) {
                try {
                    DatagramSocket theSocket = new DatagramSocket();
                    DatagramPacket theOutput = new DatagramPacket(data,
                            data.length, InetAddress.getByName(user.host), user.port);
                    System.out.println(theOutput.getAddress());
                    System.out.println(theOutput.getPort());
                    System.out.println(theOutput.getData());
                    System.out.println(user.port);
                    theSocket.send(theOutput);
                } catch (Exception e) {
                    System.out.println("Error sending datagram " + e);
                }
            }
        }

    }

    class Listener extends Thread {

        private ClientGUI gui;

        public Listener(ClientGUI gui) {
            this.gui = gui;
        }

        private synchronized void updateGUI(String msg) {
            this.gui.chatBox.append(msg);
        }

        public void run() {
            try {
                DatagramSocket ds = new DatagramSocket(udpPort);
                while (gui.isRunning) {
                    byte[] buffer = new byte[65507];
                    DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                    ds.receive(dp);
                    String msg = new String(dp.getData(), 0, dp.getLength());
                    System.out.println(msg);
                    updateGUI(msg);
                }
            } catch (SocketException se) {
                System.err.println("chat error " + se);
            } catch (IOException se) {
                System.err.println("chat error " + se);
            }
            this.interrupt();
            System.exit(1);
        }

    }

}
