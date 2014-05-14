package server;

import java.io.*;
import java.util.*;

public class ClientSender extends Thread {

    private Vector mMessageQueue = new Vector();

    private ServerDispatcher mServerDispatcher;
    private User user;
    private PrintWriter out;

    public ClientSender(User user, ServerDispatcher aServerDispatcher, PrintWriter out)
            throws IOException {
        this.user = user;
        mServerDispatcher = aServerDispatcher;
        this.out = out;
    }

    public synchronized void queueMessage(String aMessage) {
        mMessageQueue.add(aMessage);
        notify();
    }

    // notify(), wait()
    private synchronized String dequeMessage() throws InterruptedException {
        while (mMessageQueue.size() == 0)
            wait();
        String message = (String) mMessageQueue.get(0);
        mMessageQueue.removeElementAt(0);
        return message;
    }

    private void sendMessage(String aMessage) {
        out.println(aMessage);
        out.flush();
    }

    /**
     * Until interrupted, reads messages from the message queue
     * and sends them to the client's socket.
     */
    public void run() {
        try {
            while (!isInterrupted()) {
                String message = dequeMessage();
                sendMessage(message);
            }
        } catch (Exception e) {
            // Commuication problem
        }
        communicationBroken();
    }

    private void communicationBroken() {
        user.listener.interrupt();
        mServerDispatcher.deleteClient(user);
    }

}