package server;

import java.util.*;

public class ServerDispatcher extends Thread {
    private Vector messageQueue = new Vector();
    private Vector<ServerUser> activeServerUsers = new Vector();

    public synchronized void addClient(ServerUser serverUser) {
        activeServerUsers.add(serverUser);
    }

    public synchronized boolean contains(ServerUser serverUser){
        for(ServerUser u : activeServerUsers)
            if(u.name.equals(serverUser.name))
                return true;
        return false;
    }

    public synchronized void deleteClient(ServerUser serverUser) {
        int clientIndex = activeServerUsers.indexOf(serverUser);
        if (clientIndex != -1)
            activeServerUsers.removeElementAt(clientIndex);
    }

    public synchronized void queueMessage(ServerUser serverUser, String aMessage) {
        aMessage = serverUser.name + ": " + aMessage;
        messageQueue.add(aMessage);
        notify();
    }

    private synchronized String dequeMessage()
            throws InterruptedException {
        while (messageQueue.size() == 0)
            wait();
        String message = (String) messageQueue.get(0);
        messageQueue.removeElementAt(0);
        return message;
    }

    private synchronized void sendMessageToAllUsers(String aMessage) {
        for (int i = 0; i < activeServerUsers.size(); i++) {
            ServerUser serverUser = activeServerUsers.get(i);
            serverUser.sender.queueMessage(aMessage);
        }
    }

    public void run() {
        try {
            while (true) {
                String message = dequeMessage();
                sendMessageToAllUsers(message);
            }
        } catch (InterruptedException e) {
            // Thread interrupted. Stop its execution
        }
    }

    public List<ServerUser> getAllActiveUsers() {
        return new ArrayList<ServerUser>(this.activeServerUsers);
    }

}