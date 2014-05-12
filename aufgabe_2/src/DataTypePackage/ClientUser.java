package DataTypePackage;

public class ClientUser {
    public String user;
    public String host;
    public String pw;
    public int port;

    public ClientUser(String user, String pw, String host, int port) {
        this.user = user;
        this.pw = pw;
        this.host = host;
        this.port = port;
    }

}
