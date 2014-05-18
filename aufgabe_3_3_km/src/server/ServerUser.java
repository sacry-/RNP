package server;

import java.net.Socket;

public class ServerUser {
    public String name = null;
    public Socket socket = null;
    public Listener listener = null;
    public Sender sender = null;

    @Override
    public String toString() {
        if(this.name.equals("Matze"))
            return this.socket.getInetAddress().toString().substring(1) + ":" + "50001" + " " + this.name;
        return this.socket.getInetAddress().toString().substring(1) + ":" + "50002" + " " + this.name;
    }
}