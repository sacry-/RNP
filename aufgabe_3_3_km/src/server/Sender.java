package server;

import java.io.*;
import java.util.*;

public class Sender extends Thread {

    public Vector messageQueue = new Vector();

    private ServerDispatcher serverDispatcher;
    private ServerUser serverUser;
    private PrintWriter out;

    public Sender(ServerUser serverUser, ServerDispatcher aServerDispatcher, PrintWriter out)
            throws IOException {
        this.serverUser = serverUser;
        this.serverDispatcher = aServerDispatcher;
        this.out = out;
    }

    public synchronized void queueMessage(String aMessage) {
        messageQueue.add(aMessage);
        notify();
    }

    private synchronized String dequeMessage() throws InterruptedException {
        while (messageQueue.size() == 0)
            wait();
        String message = (String) messageQueue.get(0);
        messageQueue.removeElementAt(0);
        return message;
    }

    public void sendMessage(String aMessage) {
        out.println(aMessage);
        out.flush();
    }

    public void run() {
        try {
            while (!isInterrupted()) {
                String message = dequeMessage();
                sendMessage(message);
            }
        } catch (Exception e) {
        }

        stopNow();
    }

    private void stopNow() {
        serverUser.sender.interrupt();
        serverUser.listener.interrupt();
        serverDispatcher.deleteClient(serverUser);
    }

}