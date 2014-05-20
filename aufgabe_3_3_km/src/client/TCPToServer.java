package client;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Created by sacry on 16/05/14.
 */
public class TCPToServer extends Thread {

    public String host;
    public int tcpPort;
    public volatile ArrayList<ClientUser> activeUsers = new ArrayList<ClientUser>();

    private BufferedReader in = null;
    private PrintWriter out = null;
    private Socket socket;
    private Sender sender;
    private Receiver receiver;
    private NameHandler nameHandler;
    private ClientGUI gui;
    private ClientUser clientUser;

    public TCPToServer(String host, int tcpPort, NameHandler nameHandler, ClientGUI gui) {
        this.host = host;
        this.tcpPort = tcpPort;
        this.nameHandler = nameHandler;
        this.gui = gui;

        try {

            this.socket = new Socket(host, tcpPort);

            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            System.err.println("Can not establish connection to " +
                    host + ":" + tcpPort);
            e.printStackTrace();
            System.exit(-1);
        }

        sender = new Sender(out);
        sender.setDaemon(true);
        sender.start();

        receiver = new Receiver(in);
        receiver.setDaemon(true);
        receiver.start();

        start();
    }

    public String getUserName() {
        return clientUser.name;
    }

    @Override
    public void run() {
        // while deamons alive, will be dead if main thread on socket is killed
        while (sender.isAlive() && receiver.isAlive()) {
            if (!gui.isRunning) { // callback
                out.println(ClientProtocol.BYE);
                out.flush();
                break;
            }
        }
    }

    class Receiver extends Thread {

        private BufferedReader in;

        public Receiver(BufferedReader in) {
            this.in = in;
        }

        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {

                    String response = ClientProtocol.normalizeCommand(message);
                    if (response.equals(ClientProtocol.BYE)) {
                        System.out.println(response);
                        break;
                    }
                    if (response.equals(ClientProtocol.LIST)) {
                        activeUsers = new ArrayList<ClientUser>();
                        activeUsers.addAll(ClientProtocol.list(message));
                        System.out.println(activeUsers);
                    }
                    if (response.equals(ClientProtocol.ERROR)) {
                        System.out.println(message);
                        nameHandler.response.updateResponse(response);
                    }
                    if (response.equals(ClientProtocol.OK)) {
                        System.out.println(message);
                        nameHandler.response.updateResponse(response);
                    }

                }
            } catch (IOException e) {
                System.err.println("Connection to server broken: " + e.toString());
            }
        }
    }

    class Sender extends Thread {
        private PrintWriter out;

        public Sender(PrintWriter out) {
            this.out = out;
        }

        public void run() {
            guiName();
            try {
                BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
                while (!isInterrupted()) {
                    String message = r.readLine();
                    out.println(message);
                    out.flush();
                }
            } catch (Exception e) {
                System.err.println("Connection to server broken: " + e.toString());
            }
            out.println(ClientProtocol.BYE);
            out.flush();
        }

        private void guiName() {
            String name = null;
            while (!ClientProtocol.isNameValid(name)) {
                try {
                    name = nameHandler.name.getName();
                    out.println("NEW " + name);
                    out.flush();
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
            clientUser = new ClientUser(name, host, tcpPort);
        }
    }

}

