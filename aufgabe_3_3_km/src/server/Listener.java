package server;

import java.io.*;

public class Listener extends Thread {
    private ServerDispatcher serverDispatcher;
    private ServerUser serverUser;
    private BufferedReader in;

    public Listener(ServerUser serverUser, ServerDispatcher serverDispatcher, BufferedReader in)
            throws IOException {
        this.serverUser = serverUser;
        this.serverDispatcher = serverDispatcher;
        this.in = in;
    }

    public void run() {
        try {
            while (!isInterrupted()) {

                String message = in.readLine();
                System.out.println(message);
                if (message == null)
                    break;

                String response = ServerProtocol.normalizeCommand(message);
                if (response.startsWith(ServerProtocol.ERROR)) {
                    serverUser.sender.queueMessage(response);
                }
                if (response.startsWith(ServerProtocol.BYE)) {
                    serverUser.sender.queueMessage(response);
                    break;
                }
                if (response.startsWith(ServerProtocol.INFO)) {
                    String activeUsers = ServerProtocol.info(serverDispatcher.getAllActiveUsers());
                    serverUser.sender.queueMessage(activeUsers);
                }

            }
        } catch (IOException e) {

        }

        stopNow();
    }

    private void stopNow() {
        serverUser.sender.interrupt();
        serverUser.listener.interrupt();
        serverDispatcher.deleteClient(serverUser);
    }
}