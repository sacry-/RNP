package POP3ClientPackage;

public class ClientUser {
	public String user;
	public String host;
	public String pw;
	public int port;
	// Vertrauten, dass die sowieso nicht geändert werden.
	ClientUser(String user, String pw, String host, int port) {
		this.user=user; this.pw=pw; this.host=host; this.port=port;
	}
	
}
