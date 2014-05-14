package server;

import java.net.Socket;

public class User {
    public String name = null;
    public Socket socket = null;
    public ClientListener listener = null;
    public ClientSender sender = null;
}