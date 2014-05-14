package client;

import java.io.*;
import java.net.*;

public class ChatClient {

    public static final String SERVER_HOSTNAME = "localhost";
    public static final int SERVER_PORT = 50000;

    public static void main(String[] args) {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            Socket socket = new Socket(SERVER_HOSTNAME, SERVER_PORT);
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Connected to server " +
                    SERVER_HOSTNAME + ":" + SERVER_PORT);
        } catch (IOException ioe) {
            System.err.println("Can not establish connection to " +
                    SERVER_HOSTNAME + ":" + SERVER_PORT);
            ioe.printStackTrace();
            System.exit(-1);
        }

        new ClientAllThread(out, in);
    }
}
