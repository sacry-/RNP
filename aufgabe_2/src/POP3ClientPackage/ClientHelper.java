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
import POP3ClientPackage.ClientUser;

public class ClientHelper {
	
	private final static Logger logger = new Logger("Clientlog.txt");
	
	private final String OK = "OK ";
	private final String ERROR = "ERROR ";
	private final int ASCII_LINEFEED = 10;
	private final int STREAM_DEFAULT = -1;
	private final int INPUT_SIZE_BYTE = 65535;
	private final char SPACE = ' ';
	private final int POPPERIOD = 30000;
	
	private Socket socket;
	boolean running = true;
	
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private BufferedReader br;
	private DataOutputStream da;
	
	private static final boolean DEBUGMODE = true;
	
	private final static List<ClientUser> users = new ArrayList();	// TODO: insert account
	// TODO: insert ServerCodes.mkRequest(RETR, msgNo);
	
	public ClientHelper() {
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
		String answerToServer;
		String command;
		String[] inputArray;
		
		int i;
		String buffer;
		
		
		while(running) {
			
			run:
			for(ClientUser mailKonto : users) {
				
				//Ausnullen zur Sicherheit
				socket = null;
				outputStream = null;
				inputStream = null;
				
				//Verbindung zum Host herstellen
				try {
					socket = new Socket(mailKonto.host, mailKonto.port);
					outputStream = socket.getOutputStream();
					inputStream = socket.getInputStream();
					
					
					br = new BufferedReader(new InputStreamReader(inputStream));
					da = new DataOutputStream(outputStream);
					
					
				} catch (UnknownHostException e) {
					debugLog("Die Hostaddresse " + mailKonto.host + " konnte nicht aufgel�st werden");
					continue run;
				} catch (IOException e) {
					debugLog("Der Socket zu " + mailKonto.host + " konnte nicht erstellt werden");
					continue run;
				}
				
				
				//Nachrichtenaustausch mit dem Server
				try {
					//Erwarte nach Aufbau der Verbindung ein "+OK"
					inputFromServer = readFromServer();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						debugLog("Mailserver von " + mailKonto.host + " lehnt eine Verbindung ab");
						terminateSession();
						continue run;
					}
					
					debugLog("POP3-Verbindung zu " + mailKonto.host + " wurde erfolgreich aufgebaut");
					
					//Schreibe "USER username" an den Server
					writeToServer("USER " + mailKonto.user);
					
					//Erwarte "+OK"
					inputFromServer = readFromServer();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						debugLog("Der User " + mailKonto.user + " ist bei dem Server " + mailKonto.host + " nicht bekannt");
						terminateSession();
						continue run;
					}
					
					//Schreibe "PASS userpasswort" an den Server
					writeToServer("PASS " + mailKonto.pw);
					
					//Erwarte "+OK"
					inputFromServer = readFromServer();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						debugLog("Das Passwort von " + mailKonto.user + " war falsch");
						terminateSession();
						continue run;
					}
					
					
					//Schreibe "LIST" an den Server
					writeToServer("LIST");
					
					//Erwarten +OK gefolgt von einer i langen Aufz�hlung an "nachrichtenNummer nachrichtengr��e"
					inputFromServer = readFromServer();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						debugLog("Der Server " + mailKonto.host + " hat eine unbekannte Nachricht verschickt");
						terminateSession();
						continue run;
					}
					
					
					List<Integer> availableMessages = new ArrayList<Integer>();
					
					inputFromServer = readFromServer();
					
					//Solange entweder das erste Zeichen ungleich oder die beiden ersten Zeichen gleich Punkt sind
					while(inputFromServer.indexOf('.') != 0) {
						buffer = inputFromServer.split(" ")[0];			//Speicher den ersten Teilstring in den buffer
						availableMessages.add(Integer.parseInt(buffer));//Parse den Buffer zu einem Integer und f�ge ihn zu den verf�gbaren Nachrichten hinzu
						
						inputFromServer = readFromServer();
						
					}
					
					
					//Empfange alle E-Mails
					mailSchleife:
					for(int messageNum : availableMessages) {
						//Schreibe "RETR messageNum" an den Server
						writeToServer("RETR " + messageNum);
						
						//Erwarte "+OK" vom Server
						inputFromServer = readFromServer();
						
						if(inputFromServer.indexOf("+OK") != 0) {
							debugLog("Fehler beim Auslesen von Nachricht Nummer " + messageNum + " vom Server " + mailKonto.host);
							continue mailSchleife;
						}
						
						//Gefolgt vom Inhalt der Email in mehreren Zeilen
						inputFromServer = readFromServer();
						buffer = inputFromServer;
						
						//Bis das Terminalsymbol, alleine in einer Zeile kommt
						while (!terminition(inputFromServer)) {
							inputFromServer = readFromServer();
							buffer += inputFromServer + "\n";
						}
						
						Server.addMail(buffer, Server.getId());
						
						writeToServer("DELE " + messageNum);
					
						
						inputFromServer = readFromServer();
						
						if(inputFromServer.indexOf("+OK") != 0) {
							debugLog("Fehler beim L�schen von Nachricht Nummer " + messageNum + " vom Server " + mailKonto.host);
							continue mailSchleife;
						}
					}
					
					terminateSession();
					
				
				} catch (IOException e) {
					debugLog("Fehler beim lesen oder schreiben zu " + mailKonto.host);
					continue run;
				}
			}
			
			try {
				synchronized (this) {
					this.wait(POPPERIOD);
				}
			} catch (InterruptedException e) {
				debugLog("Client Thread konnte nicht bis zu Ende warten");
			}
			
		}
			
	}
	
	
	
	
	private void writeToServer(String request) throws IOException {
		da.writeBytes(request + "\n");
		debugLog("Req: " + request);
	}
	
	private String readFromServer() throws IOException {
		String request = br.readLine();
		debugLog("Resp: " + request);
		return request;
	}
	
	private void terminateSession() throws IOException {
		writeToServer("QUIT");
		readFromServer();
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
		if(message.length() > 1) {										//Wenn die Zeile l�nger als ein Zeichen ist
			return message.charAt(0) == '.' && message.charAt(1) != '.';//	True -> wenn das erste Zeichen = '.' und das zweite Zeichen != '.' ist | False -> sonst
		} else if(message.length() == 1) {								//Wenn die Nachricht genau ein Zeichen lang ist
			return message.charAt(0) == '.';							//	True -> wenn das Zeichen = '.' ist | False -> sonst
		} else {														//Sonst (Nachricht ist genau 0 Zeichen lang)
			return false;												//	False
		}
	}
}

