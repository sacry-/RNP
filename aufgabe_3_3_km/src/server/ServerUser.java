package server;

import java.net.Socket;

public class ServerUser {
    public String name = null;
    public Socket socket = null;
    public Listener listener = null;
    public Sender sender = null;

    @Override
    public String toString() {
        return this.socket.getInetAddress().toString().substring(1) + " " + this.name;
    }
}