package server;

import java.net.*;
import java.io.*;
import java.util.List;

public class ChatServer {

    public static final int PORT = 50000;
    public static ServerSocket serverSocket = null;
    public static volatile ServerDispatcher serverDispatcher;

    public static ChatServer instance = null;

    public static ChatServer getInstance() {
        if (instance == null) {
            instance = new ChatServer();
        }
        return instance;
    }

    private ChatServer() {
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
                new ChatServerThread(socket).start();
                new ServerSignaler(serverDispatcher).start();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) {
        ChatServer.getInstance().startServer();
    }
}

class ServerSignaler extends Thread {

    private ServerDispatcher serverDispatcher;

    public ServerSignaler(ServerDispatcher serverDispatcher) {
        this.serverDispatcher = serverDispatcher;
    }

    public List<User> getAllActiveUsers() {
        return serverDispatcher.getAllActiveUsers();
    }
}

class ChatServerThread extends Thread {

    private Socket socket;
    private ClientListener listener;
    private ClientSender sender;
    private User user = new User();
    private BufferedReader in;
    private PrintWriter out;

    public ChatServerThread(Socket socket) {
        this.socket = socket;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void run() {
        user.socket = this.socket;

        try {

            listener = new ClientListener(user, ChatServer.getInstance().serverDispatcher, this.in);
            sender = new ClientSender(user, ChatServer.getInstance().serverDispatcher, this.out);

            user.listener = listener;
            user.sender = sender;

            IOUserName();

            listener.start();
            sender.start();

            ChatServer.getInstance().serverDispatcher.addClient(user);

            sender.queueMessage(user.name + " entered the room");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private void IOUserName() {
        try {
            out.println("Name: ");
            out.flush();
            String name = in.readLine();
            while (nameIsInvalid(name)) {
                name = in.readLine();
            }
            user.name = name;
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private boolean nameIsInvalid(String name) {
        return name == null || name == "" || name.length() > 20;
    }
}