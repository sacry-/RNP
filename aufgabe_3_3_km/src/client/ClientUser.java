package client;

import server.Listener;
import server.Sender;

import java.net.Socket;

public class ClientUser {
    public String name = null;
    public String host = null;
    public int port;

    public ClientUser(String name, String host, int port) {
        this.name = name;
        this.host = host;
        this.port = port;
    }

    @Override
    public String toString() {
        return this.host + ":" + this.port + " " + this.name;
    }
}