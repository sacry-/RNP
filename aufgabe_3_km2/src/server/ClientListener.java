package server;

import java.io.*;

public class ClientListener extends Thread {
    private ServerDispatcher serverDispatcher;
    private User user;
    private BufferedReader in;

    public ClientListener(User user, ServerDispatcher serverDispatcher, BufferedReader in)
            throws IOException {
        this.user = user;
        this.serverDispatcher = serverDispatcher;
        this.in = in;
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                String message = in.readLine();
                if (message == null)
                    break;
                serverDispatcher.queueMessage(user, message);
            }
        } catch (IOException e) {
            System.out.println(e);
        }

        connectionBroken();
    }

    private void connectionBroken() {
        user.sender.interrupt();
        serverDispatcher.deleteClient(user);
    }
}