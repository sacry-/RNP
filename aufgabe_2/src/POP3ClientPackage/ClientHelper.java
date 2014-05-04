package POP3ClientPackage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ServicePackage.Logger;
import ServicePackage.ServerStateService;
import POP3ClientPackage.ClientUser;
import static POP3ServerPackage.ServerCodes.*;

public class ClientHelper {
	
	private final static Logger logger = new Logger("clientlog.txt");
	
	private final int WAITMS = 30000;
	
	private Socket socket;
	
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private BufferedReader reader;
	private DataOutputStream writer;
	
	private static final boolean DEBUGMODE = true;
	
	private final static List<ClientUser> users = new ArrayList();	// TODO: insert account
	// TODO: insert mkRequest(RETR, msgNo);
	// TODO: documenting comments
	
	public ClientHelper() {
		users.add(new ClientUser("lol2", "fuck", "127.0.0.1", ServerStateService.PORT));
	}
	
	private static void debugLog(String msg) {
		if(DEBUGMODE)
			System.out.println(msg);
		logger.write(msg);
	}
	
	public static void main(String[] args) {
		new ClientHelper().runClient();
	}
	
	public void runClient() {
		
		String serverResp;
		
		String buffer;
		
		while(true) {
			
			run:
			for(ClientUser localUser : users) {
				
				resetConnectionVars();
				
				// Connect to host
				try {
					socket = new Socket(localUser.host, localUser.port);
					outputStream = socket.getOutputStream();
					inputStream = socket.getInputStream();
					reader = new BufferedReader(new InputStreamReader(inputStream));
					writer = new DataOutputStream(outputStream);
				} catch (Exception e) {
					debugLog("Connection failure.");
					continue;	// skip to next user
				}
				
				
				// authorize with server
				try {
					serverResp = recieve();
					if(failureResponse(serverResp)) {
						debugLog("Denied connection.");
						closeSession();
						continue;
					}
					
					debugLog("Connection established.");
					
					// USER Authorisation
					send(mkRequest(USER, localUser.user));
					serverResp = recieve();
					
					if(failureResponse(serverResp)) {
						debugLog("User was not found.");
						closeSession();
						continue;
					}
					
					// PASSWORD checking
					send(mkRequest(PASS, localUser.pw));
					
					//Erwarte "+OK"
					serverResp = recieve();
					
					if(failureResponse(serverResp)) {
						debugLog("Pasword was wrong.");
						closeSession();
						continue;
					}
					
					
					//Schreibe "LIST" an den Server
					send("LIST");
					
					//Erwarten +OK gefolgt von einer i langen Aufzählung an "nachrichtenNummer nachrichtengröße"
					serverResp = recieve();
					
					if(failureResponse(serverResp)) {
						debugLog("Der Server " + localUser.host + " hat eine unbekannte Nachricht verschickt");
						closeSession();
						continue run;
					}
					
					List<Integer> availableMessages = new ArrayList<Integer>();
					
					serverResp = recieve();
					
					//Solange entweder das erste Zeichen ungleich oder die beiden ersten Zeichen gleich Punkt sind
					while(!serverResp.startsWith(TERMINTATOR)) {
						Scanner line = new Scanner(serverResp);
						Integer msgId = Integer.parseInt(line.next());
						line.close();
						availableMessages.add(msgId);
						serverResp = recieve();
					}
					
					
					//Empfange alle E-Mails
					mailSchleife:
					for(int messageNum : availableMessages) {
						//Schreibe "RETR messageNum" an den Server
						send("RETR " + messageNum);
						
						//Erwarte "+OK" vom Server
						serverResp = recieve();
						
						if(failureResponse(serverResp)) {
							debugLog("Fehler beim Auslesen von Nachricht Nummer " + messageNum + " vom Server " + localUser.host);
						}
						else {
							serverResp = recieve();
							buffer = serverResp;
							while (!serverResp.startsWith(TERMINTATOR)) {
								serverResp = recieve();
								buffer += serverResp + NEWLINE;
							}
							
							// we could save the content somewhere now.
							// but we're just simply writing it into a log.
							logger.write(buffer);
							
						}
					}
					
					closeSession();
					
				
				} catch (IOException e) {
					debugLog("Fehler beim lesen oder schreiben zu " + localUser.host);
					continue run;
				}
			}
			
			try {
				Thread.sleep(WAITMS);
			} catch (InterruptedException e) {
				debugLog("Client waiting disturbed");
			}
		}
	}
	private void send(String request) throws IOException {
		writer.writeBytes(request + "\n");
		debugLog("Reqqust line :" + request);
	}
	
	private String recieve() throws IOException {
		String request = reader.readLine();
		debugLog("Resp line    :" + request);
		return request;
	}
	
	private boolean failureResponse(String resp) {
		return !resp.startsWith(OK);
	}
	
	private void closeSession() throws IOException {
		send(QUIT);
		recieve();
        try {
            if (!socket.isClosed()) {
            	socket.shutdownInput();
            	socket.shutdownOutput();
            	socket.close();
            }
        } catch (IOException e) {
        	debugLog("Closing Connection unsucccessful.");
        }
	}
	
	private void resetConnectionVars(){
		socket = null;
		outputStream = null;
		inputStream = null;
		reader = null;
		writer = null;
	}
}

