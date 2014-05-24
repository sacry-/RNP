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
                        updateGUI(activeUsers);
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

        private synchronized void updateGUI(ArrayList<ClientUser> aUsers) {
            String users = "";
            for (ClientUser user : aUsers) {
                users += user.name + "\n";
            }
            if (gui.activeBox != null)
                gui.activeBox.setText(users);
        }
    }

    class Sender extends Thread {
        private PrintWriter out;

        public Sender(PrintWriter out) {
            this.out = out;
        }

        public void run() {
            guiName();
            new RequestInfoTask().start();
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
            String name;
            while (true) {
                try {
                    name = nameHandler.name.getName();
                    out.println("NEW " + name);
                    out.flush();
                    String response = nameHandler.response.getResponse();
                    if (response.equals(ClientProtocol.OK)) {
                        gui.preFrame.setVisible(false);
                        gui.display();
                        break;
                    } else {
                        gui.usernameChooser.setText("");
                        gui.usernameChooser.requestFocusInWindow();
                    }
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        }

        class RequestInfoTask extends Thread {
            public RequestInfoTask() {
            }

            @Override
            public void run() {
                try {
                    Long timestamp = System.currentTimeMillis();
                    while (!isInterrupted()) {
                        while ((System.currentTimeMillis() - timestamp) <= 5000L) {
                        }
                        timestamp = System.currentTimeMillis();
                        out.println(ClientProtocol.INFO);
                        out.flush();
                    }
                } catch (Exception e) {
                    System.err.println("Connection to server broken: " + e.toString());
                }
            }


        }
    }
}