package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class ServerDispatcher extends Thread {
    private Vector<ServerUser> activeServerUsers = new Vector();

    public synchronized void addClient(ServerUser serverUser) {
        activeServerUsers.add(serverUser);
    }

    public synchronized boolean contains(String name) {
        for (ServerUser u : activeServerUsers)
            if (u.name.equals(name))
                return true;
        return false;
    }

    public synchronized void deleteClient(ServerUser serverUser) {
        int clientIndex = activeServerUsers.indexOf(serverUser);
        if (clientIndex != -1)
            activeServerUsers.removeElementAt(clientIndex);
    }

    public synchronized void sendInfoToAllUsers() {
        for (int i = 0; i < activeServerUsers.size(); i++) {
            ServerUser serverUser = activeServerUsers.get(i);
            serverUser.sender.queueMessage(ServerProtocol.info(activeServerUsers));
        }
    }

    public synchronized void quit() {
        for (int i = 0; i < activeServerUsers.size(); i++) {
            ServerUser serverUser = activeServerUsers.get(i);
            serverUser.sender.queueMessage(ServerProtocol.BYE);
        }
    }

    public void run() {
        try {
            while (!interrupted()) {
            }
        } catch (Exception e) {
        }
    }

    public List<ServerUser> getAllActiveUsers() {
        return new ArrayList<ServerUser>(this.activeServerUsers);
    }

}