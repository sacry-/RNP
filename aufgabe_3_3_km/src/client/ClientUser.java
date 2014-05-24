package client;

public class ClientUser {
    public String name = null;
    public String host = null;
    public int port;

    public ClientUser(String name, String host) {
        this.name = name;
        this.host = host;
    }

    @Override
    public String toString() {
        return this.host + " " + this.name;
    }
}