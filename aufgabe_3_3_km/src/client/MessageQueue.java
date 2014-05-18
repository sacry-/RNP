package client;

import sun.plugin2.message.Message;

import java.util.Vector;

/**
 * Created by sacry on 18/05/14.
 */
public class MessageQueue {

    private Vector<String> messageQueue;

    public MessageQueue() {
        messageQueue = new Vector<String>();
    }

    public synchronized void queueMessage(String aMessage) {
        messageQueue.add(aMessage);
        notify();
    }

    public synchronized String dequeMessage()
            throws InterruptedException {
        while (messageQueue.size() == 0)
            wait();
        String message = messageQueue.get(0);
        messageQueue.removeElementAt(0);
        return message;
    }

}