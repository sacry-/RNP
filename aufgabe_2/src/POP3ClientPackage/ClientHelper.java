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
import POP3ServerPackage.ServerCodes;
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
		
		String inputFromServer;
		
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
					continue run;
				}
				
				
				// authorize with server
				try {
					//Erwarte nach Aufbau der Verbindung ein "+OK"
					inputFromServer = recieve();
					
					if(!inputFromServer.startsWith("+OK")) {
						debugLog("Denied connection.");
						closeSession();
						continue run;
					}
					
					debugLog("POP3-Verbindung zu " + localUser.host + " wurde erfolgreich aufgebaut");
					
					//Schreibe "USER username" an den Server
					send("USER " + localUser.user);
					
					//Erwarte "+OK"
					inputFromServer = recieve();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						debugLog("Der User " + localUser.user + " ist bei dem Server " + localUser.host + " nicht bekannt");
						closeSession();
						continue run;
					}
					
					//Schreibe "PASS userpasswort" an den Server
					send("PASS " + localUser.pw);
					
					//Erwarte "+OK"
					inputFromServer = recieve();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						debugLog("Das Passwort von " + localUser.user + " war falsch");
						closeSession();
						continue run;
					}
					
					
					//Schreibe "LIST" an den Server
					send("LIST");
					
					//Erwarten +OK gefolgt von einer i langen Aufzählung an "nachrichtenNummer nachrichtengröße"
					inputFromServer = recieve();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						debugLog("Der Server " + localUser.host + " hat eine unbekannte Nachricht verschickt");
						closeSession();
						continue run;
					}
					
					List<Integer> availableMessages = new ArrayList<Integer>();
					
					inputFromServer = recieve();
					
					//Solange entweder das erste Zeichen ungleich oder die beiden ersten Zeichen gleich Punkt sind
					while(!inputFromServer.startsWith(TERMINTATOR)) {
						Scanner line = new Scanner(inputFromServer);
						Integer msgId = Integer.parseInt(line.next());
						line.close();
						availableMessages.add(msgId);
						inputFromServer = recieve();
					}
					
					
					//Empfange alle E-Mails
					mailSchleife:
					for(int messageNum : availableMessages) {
						//Schreibe "RETR messageNum" an den Server
						send("RETR " + messageNum);
						
						//Erwarte "+OK" vom Server
						inputFromServer = recieve();
						
						if(!inputFromServer.startsWith(OK)) {
							debugLog("Fehler beim Auslesen von Nachricht Nummer " + messageNum + " vom Server " + localUser.host);
						}
						else {
							//Gefolgt vom Inhalt der Email in mehreren Zeilen
							inputFromServer = recieve();
							buffer = inputFromServer;
							
							//Bis das Terminalsymbol, alleine in einer Zeile kommt
							while (!inputFromServer.startsWith(TERMINTATOR)) {
								inputFromServer = recieve();
								buffer += inputFromServer + "\n";
							}
							
							// hier könnte man es dann lokal speicheren.
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

