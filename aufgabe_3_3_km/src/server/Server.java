package server;

import java.net.*;
import java.io.*;

public class Server {

    public static final int PORT = 50000;
    public static ServerSocket serverSocket = null;
    public static volatile ServerDispatcher serverDispatcher;

    public static Server instance = null;

    public static Server getInstance() {
        if (instance == null) {
            instance = new Server();
        }
        return instance;
    }

    private Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("ChatServer started on port " + PORT);
        } catch (IOException se) {
            System.err.println("Can not start listening on port " + PORT);
            se.printStackTrace();
            System.exit(-1);
        }
        serverDispatcher = new ServerDispatcher();
        serverDispatcher.start();
    }

    public void startServer() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new TCPServer(socket).start();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) {
        Server.getInstance().startServer();
    }
}

class TCPServer extends Thread {

    private Socket socket;
    private Listener listener;
    private Sender sender;
    private ServerUser serverUser = new ServerUser();
    private BufferedReader in;
    private PrintWriter out;

    public TCPServer(Socket socket) {
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void run() {
        serverUser.socket = this.socket;

        try {
            listener = new Listener(serverUser, Server.getInstance().serverDispatcher, this.in);
            sender = new Sender(serverUser, Server.getInstance().serverDispatcher, this.out);
            serverUser.listener = listener;
            serverUser.sender = sender;

            IOUserName();

            listener.start();
            sender.start();

            Server.getInstance().serverDispatcher.addClient(serverUser);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void IOUserName() {
        try {
            String input = in.readLine();
            String name = ServerProtocol.newName(input);
            while (!ServerProtocol.isNameValid(name)) {
                out.println(ServerProtocol.error("invalid command or name. Hint: NEW <name>"));
                out.flush();
                input = in.readLine();
                name = ServerProtocol.newName(input);
            }
            System.out.println(name);
            serverUser.name = name;
            out.println(ServerProtocol.OK);
            out.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}