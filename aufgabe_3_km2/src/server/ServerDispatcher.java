package server;

import java.util.*;

public class ServerDispatcher extends Thread
{
    private Vector messageQueue = new Vector();
    private Vector activeUsers = new Vector();

    /**
     * Adds given client to the server's client list.
     */
    public synchronized void addClient(User user)
    {
        activeUsers.add(user);
    }

    /**
     * Deletes given client from the server's client list
     * if the client is in the list.
     */
    public synchronized void deleteClient(User user)
    {
        int clientIndex = activeUsers.indexOf(user);
        if (clientIndex != -1)
            activeUsers.removeElementAt(clientIndex);
    }

    /**
     * Adds given message to the dispatcher's message queue and notifies this
     * thread to wake up the message queue reader (getNextMessageFromQueue method).
     * dispatchMessage method is called by other threads (ClientListener) when
     * a message is arrived.
     */
    public synchronized void queueMessage(User user, String aMessage)
    {
        aMessage = user.name + ": " + aMessage;
        messageQueue.add(aMessage);
        notify();
    }

    /**
     * @return and deletes the next message from the message queue. If there is no
     * messages in the queue, falls in sleep until notified by dispatchMessage method.
     */
    private synchronized String dequeMessage()
            throws InterruptedException
    {
        while (messageQueue.size()==0)
            wait();
        String message = (String) messageQueue.get(0);
        messageQueue.removeElementAt(0);
        return message;
    }

    /**
     * Sends given message to all clients in the client list. Actually the
     * message is added to the client sender thread's message queue and this
     * client sender thread is notified.
     */
    private synchronized void sendMessageToAllUsers(String aMessage)
    {
        for (int i=0; i< activeUsers.size(); i++) {
            User user = (User) activeUsers.get(i);
            user.sender.queueMessage(aMessage);
        }
    }

    /**
     * Infinitely reads messages from the queue and dispatch them
     * to all clients connected to the server.
     */
    public void run()
    {
        try {
            while (true) {
                String message = dequeMessage();
                sendMessageToAllUsers(message);
            }
        } catch (InterruptedException ie) {
            // Thread interrupted. Stop its execution
        }
    }

    public List<User> getAllActiveUsers(){
        return new ArrayList<User>(this.activeUsers);
    }

}