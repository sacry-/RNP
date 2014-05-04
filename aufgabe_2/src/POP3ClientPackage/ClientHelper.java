package POP3ClientPackage;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ServicePackage.Logger;
import POP3ClientPackage.ClientUser;

public class ClientHelper extends Thread {
	
	private final static Logger logger = new Logger("Clientlog.txt");
	
	private final String OK = "OK ";
	private final String ERROR = "ERROR ";
	private final int ASCII_LINEFEED = 10;
	private final int STREAM_DEFAULT = -1;
	private final int INPUT_SIZE_BYTE = 65535;
	private final char SPACE = ' ';
	private final int POPPERIOD = 30000;
	
	private int name;
	private Socket socket;
	boolean running = true;
	
	private InputStream inputStream;
	private OutputStream outputStream;
	
	private BufferedReader br;
	private DataOutputStream da;
	
	private final static List<ClientUser> users = new ArrayList();	// TODO:
	
	public ClientHelper(int name) {
		this.name = name;
	}
	
	public void run() {
		
		String inputFromServer;
		String answerToServer;
		String command;
		String[] inputArray;
		
		int i;
		String buffer;
		
		
		while(running) {
			
			run:
			for(ClientUser mailKonto : Server.mailKontoList()) {
				
				//Ausnullen zur Sicherheit
				socket = null;
				outputStream = null;
				inputStream = null;
				
				//Verbindung zum Host herstellen
				try {
					socket = new Socket(mailKonto.host, mailKonto.port());
					outputStream = socket.getOutputStream();
					inputStream = socket.getInputStream();
					
					
					br = new BufferedReader(new InputStreamReader(inputStream));
					da = new DataOutputStream(outputStream);
					
					
				} catch (UnknownHostException e) {
					log.newWarning("Die Hostaddresse " + mailKonto.host + " konnte nicht aufgelöst werden");
					System.out.println("Die Hostaddresse " + mailKonto.host + " konnte nicht aufgelöst werden");
					continue run;
				} catch (IOException e) {
					log.newWarning("Der Socket zu " + mailKonto.host + " konnte nicht erstellt werden");
					System.out.println("Der Socket zu " + mailKonto.host + " konnte nicht erstellt werden");
					continue run;
				}
				
				
				//Nachrichtenaustausch mit dem Server
				try {
					//Erwarte nach Aufbau der Verbindung ein "+OK"
					inputFromServer = readFromServer();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						log.newWarning("Mailserver von " + mailKonto.host + " lehnt eine Verbindung ab");
						System.out.println("Mailserver von " + mailKonto.host + " lehnt eine Verbindung ab");
						terminateSession();
						continue run;
					}
					
					log.newInfo("POP3-Verbindung zu " + mailKonto.host + " wurde erfolgreich aufgebaut");
					System.out.println("POP3-Verbindung zu " + mailKonto.host + " wurde erfolgreich aufgebaut");
					
					//Schreibe "USER username" an den Server
					writeToServer("USER " + mailKonto.user);
					
					//Erwarte "+OK"
					inputFromServer = readFromServer();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						log.newWarning("Der User " + mailKonto.user + " ist bei dem Server " + mailKonto.host + " nicht bekannt");
						System.out.println("Der User " + mailKonto.user + " ist bei dem Server " + mailKonto.host + " nicht bekannt");
						terminateSession();
						continue run;
					}
					
					//Schreibe "PASS userpasswort" an den Server
					writeToServer("PASS " + mailKonto.passwort());
					
					//Erwarte "+OK"
					inputFromServer = readFromServer();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						log.newWarning("Das Passwort von " + mailKonto.user + " war falsch");
						System.out.println("Das Passwort von " + mailKonto.user + " war falsch");
						terminateSession();
						continue run;
					}
					
					
					//Schreibe "LIST" an den Server
					writeToServer("LIST");
					
					//Erwarten +OK gefolgt von einer i langen Aufzählung an "nachrichtenNummer nachrichtengröße"
					inputFromServer = readFromServer();
					
					if(inputFromServer.indexOf("+OK") != 0) {
						log.newWarning("Der Server " + mailKonto.host + " hat eine unbekannte Nachricht verschickt");
						System.out.println("Der Server " + mailKonto.host + " hat eine unbekannte Nachricht verschickt");
						terminateSession();
						continue run;
					}
					
					
					List<Integer> availableMessages = new ArrayList<Integer>();
					
					inputFromServer = readFromServer();
					
					//Solange entweder das erste Zeichen ungleich oder die beiden ersten Zeichen gleich Punkt sind
					while(/*inputFromServer.charAt(0) == '.' && inputFromServer.charAt(1) == '.' 
							|| */inputFromServer.indexOf('.') != 0) {
						
						buffer = inputFromServer.split(" ")[0];			//Speicher den ersten Teilstring in den buffer
						availableMessages.add(Integer.parseInt(buffer));//Parse den Buffer zu einem Integer und füge ihn zu den verfügbaren Nachrichten hinzu
						
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
							log.newWarning("Fehler beim Auslesen von Nachricht Nummer " + messageNum + " vom Server " + mailKonto.host);
							System.out.println("Fehler beim Auslesen von Nachricht Nummer " + messageNum + " vom Server " + mailKonto.host);
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
							log.newWarning("Fehler beim Löschen von Nachricht Nummer " + messageNum + " vom Server " + mailKonto.host);
							System.out.println("Fehler beim Lölschen von Nachricht Nummer " + messageNum + " vom Server " + mailKonto.host);
							continue mailSchleife;
						}
					}
					
					terminateSession();
					
				
				} catch (IOException e) {
					log.newWarning("Fehler beim lesen oder schreiben zu " + mailKonto.host);
					System.out.println("Fehler beim lesen oder schreiben zu " + mailKonto.host);
					continue run;
				}
			}
			
			try {
				synchronized (this) {
					this.wait(POPPERIOD);
				}
			} catch (InterruptedException e) {
				log.newWarning("Client Thread konnte nicht bis zu Ende warten");
				System.out.println("Client Thread konnte nicht bis zu Ende warten");
			}
			
		}
			
	}
	
	
	
	
	private void writeToServer(String request) throws IOException {
		da.writeBytes(request + "\n");
		logger.write("Req: " + request);
		System.out.println("Req: " + request);
	}
	
	private String readFromServer() throws IOException {
		String request = br.readLine();
		logger.write("Resp: " + request);
		System.out.println("Resp: " + request);
		return request;
	}
	
	private void terminateSession() throws IOException {
		writeToServer("QUIT");	//Beende die Sitzung mit dem Server
		readFromServer();		//Lese das "+OK" aus
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
}

