package POP3ClientPackage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import ServicePackage.Logger;
import ServicePackage.ServerStateService;
import POP3ClientPackage.ClientUser;
import POP3ServerPackage.ServerCodes;

public class ClientHelper {
	
	private final static Logger logger = new Logger("Clientlog.txt");
	
	private final int WAITMS = 30000;
	
	private Socket socket;
	boolean running = true;
	
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private BufferedReader reader;
	private DataOutputStream writer;
	
	private static final boolean DEBUGMODE = true;
	
	private final static List<ClientUser> users = new ArrayList();	// TODO: insert account
	// TODO: insert ServerCodes.mkRequest(RETR, msgNo);
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
		
		while(running) {
			
			run:
			for(ClientUser localUser : users) {
				
				//Ausnullen zur Sicherheit
				resetConnectionVars();
				
				//Verbindung zum Host herstellen
				try {
					socket = new Socket(localUser.host, localUser.port);
					outputStream = socket.getOutputStream();
					inputStream = socket.getInputStream();
					reader = new BufferedReader(new InputStreamReader(inputStream));
					writer = new DataOutputStream(outputStream);
					
					
				} catch (UnknownHostException e) {
					debugLog("Die Hostaddresse " + localUser.host + " konnte nicht aufgelöst werden");
					continue run;
				} catch (IOException e) {
					debugLog("Der Socket zu " + localUser.host + " konnte nicht erstellt werden");
					continue run;
				}
				
				
				//Nachrichtenaustausch mit dem Server
				try {
					//Erwarte nach Aufbau der Verbindung ein "+OK"
					inputFromServer = recieve();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						debugLog("Mailserver von " + localUser.host + " lehnt eine Verbindung ab");
						terminateSession();
						continue run;
					}
					
					debugLog("POP3-Verbindung zu " + localUser.host + " wurde erfolgreich aufgebaut");
					
					//Schreibe "USER username" an den Server
					send("USER " + localUser.user);
					
					//Erwarte "+OK"
					inputFromServer = recieve();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						debugLog("Der User " + localUser.user + " ist bei dem Server " + localUser.host + " nicht bekannt");
						terminateSession();
						continue run;
					}
					
					//Schreibe "PASS userpasswort" an den Server
					send("PASS " + localUser.pw);
					
					//Erwarte "+OK"
					inputFromServer = recieve();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						debugLog("Das Passwort von " + localUser.user + " war falsch");
						terminateSession();
						continue run;
					}
					
					
					//Schreibe "LIST" an den Server
					send("LIST");
					
					//Erwarten +OK gefolgt von einer i langen Aufzählung an "nachrichtenNummer nachrichtengröße"
					inputFromServer = recieve();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						debugLog("Der Server " + localUser.host + " hat eine unbekannte Nachricht verschickt");
						terminateSession();
						continue run;
					}
					
					List<Integer> availableMessages = new ArrayList<Integer>();
					
					inputFromServer = recieve();
					
					//Solange entweder das erste Zeichen ungleich oder die beiden ersten Zeichen gleich Punkt sind
					while(inputFromServer.indexOf('.') != 0) {
						buffer = inputFromServer.split(" ")[0];			//Speicher den ersten Teilstring in den buffer
						availableMessages.add(Integer.parseInt(buffer));//Parse den Buffer zu einem Integer und füge ihn zu den verfügbaren Nachrichten hinzu
						
						inputFromServer = recieve();
					}
					
					
					//Empfange alle E-Mails
					mailSchleife:
					for(int messageNum : availableMessages) {
						//Schreibe "RETR messageNum" an den Server
						send("RETR " + messageNum);
						
						//Erwarte "+OK" vom Server
						inputFromServer = recieve();
						
						if(!inputFromServer.startsWith(ServerCodes.OK)) {
							debugLog("Fehler beim Auslesen von Nachricht Nummer " + messageNum + " vom Server " + localUser.host);
						}
						else {
							//Gefolgt vom Inhalt der Email in mehreren Zeilen
							inputFromServer = recieve();
							buffer = inputFromServer;
							
							//Bis das Terminalsymbol, alleine in einer Zeile kommt
							while (!terminition(inputFromServer)) {
								inputFromServer = recieve();
								buffer += inputFromServer + "\n";
							}
							
							logger.write("Read Mail: ");
							logger.write(buffer);
							logger.write("====================================");
							
							/*Server.addMail(buffer, Server.getId());
							
							send("DELE " + messageNum);
							inputFromServer = recieve();
							
							if(inputFromServer.indexOf("+OK") != 0) {
								debugLog("Fehler beim Löschen von Nachricht Nummer " + messageNum + " vom Server " + mailKonto.host);
								continue mailSchleife;
							}
							*/
						}
					}
					
					terminateSession();
					
				
				} catch (IOException e) {
					debugLog("Fehler beim lesen oder schreiben zu " + localUser.host);
					continue run;
				}
			}
			
			try {
				synchronized (this) {
					this.wait(WAITMS);
				}
			} catch (InterruptedException e) {
				debugLog("Client Thread konnte nicht bis zu Ende warten");
			}
			
		}
			
	}
	
	
	
	
	private void send(String request) throws IOException {
		writer.writeBytes(request + "\n");
		debugLog("Req: " + request);
	}
	
	private String recieve() throws IOException {
		String request = reader.readLine();
		debugLog("Resp: " + request);
		return request;
	}
	
	private void terminateSession() throws IOException {
		send("QUIT");
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
	
	private boolean terminition(String message) {
		if(message.length() > 1) {										//Wenn die Zeile länger als ein Zeichen ist
			return message.charAt(0) == '.' && message.charAt(1) != '.';//	True -> wenn das erste Zeichen = '.' und das zweite Zeichen != '.' ist | False -> sonst
		} else if(message.length() == 1) {								//Wenn die Nachricht genau ein Zeichen lang ist
			return message.charAt(0) == '.';							//	True -> wenn das Zeichen = '.' ist | False -> sonst
		} else {														//Sonst (Nachricht ist genau 0 Zeichen lang)
			return false;												//	False
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

