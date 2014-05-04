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

	private Socket socket;

	private InputStream inputStream;
	private OutputStream outputStream;

	private BufferedReader reader;
	private DataOutputStream writer;

	private static final boolean DEBUGMODE = true;
	private final int WAITMS = 30000;

	private final static List<ClientUser> users = new ArrayList<ClientUser>();

	// TODO: documenting comments

	public ClientHelper() {
		users.add(new ClientUser("lol2", "fuck", "127.0.0.1",
				ServerStateService.PORT));
	}

	private static void debugLog(String msg) {
		if (DEBUGMODE)
			System.out.println(msg);
		logger.write(msg);
	}

	public static void main(String[] args) {
		new ClientHelper().runClient();
	}

	public void runClient() {
		// bei all diesen try-catches wäre Either Error oder Maybe Monad nett...
		
		for (ClientUser localUser : users) {
			try {
				resetConnectionVars();
				// Connect to host
				socket = new Socket(localUser.host, localUser.port);
				inputStream = socket.getInputStream();
				outputStream = socket.getOutputStream();
				reader = new BufferedReader(new InputStreamReader(inputStream));
				writer = new DataOutputStream(outputStream);

				// connection successful?
				if (responseWasFailure("Denied connection.")) continue;

				// USER Authorisation
				send(mkRequest(USER, localUser.user));
				if (responseWasFailure("User was not found.")) continue;

				// PASSWORD checking
				send(mkRequest(PASS, localUser.pw));
				if (responseWasFailure("Pasword was wrong.")) continue;

				debugLog("Connection and authentication successful!");

				// Ask for all Mails
				send(LIST);
				// Response is f.e.: +OK 14 messages 2343 octets
				if (responseWasFailure("List command not regnized by server.")) continue;

				// builds the list of IDs form the remaining lines of the response.
				List<Integer> mailIDs = makeMailIDsFromServerResponses();

				debugLog("Recieved mailIDs: " + mailIDs.toString());

				// Request and Read all the mails.
				for (Integer id : mailIDs) {
					
					send(mkRequest(RETR, id));
					if (responseWasFailure("Mail not found on server.")) continue;
					
					String content = readMail();
					
					// we could save the content somewhere now. but we're just
					// simply writing it into a log.
					logger.write(content);
				}
				
				closeSession();
			} catch (IOException e) {
				debugLog("IOException during server Connection" + e.toString());
			}
		}// all users have been tried out now.
		
		try {
			Thread.sleep(WAITMS);
		} catch (InterruptedException e) {
			debugLog("Client waiting disturbed");
		}
		
		runClient();	// after 30 seconds, repeat from beginning
	}
	
	// reads the lines containing the mail content until an "." appears
	private String readMail() throws IOException {
		StringBuilder content = new StringBuilder();
		String serverResp = recieve();
		content.append(serverResp);
		while (!serverResp.startsWith(TERMINTATOR)) {
			serverResp = recieve();
			content.append(serverResp + NEWLINE);
		}
		return content.toString();
	}
	
	// reads the lines containing mail IDs of the list command
	// and returns a List of mailIDs.
	private List<Integer> makeMailIDsFromServerResponses() throws IOException {
		String serverResp = recieve();
		List<Integer> mailIDs = new ArrayList<>();
		// read all the mailIDs until the terminator has appeared.
		while (!serverResp.startsWith(TERMINTATOR)) {
			Scanner line = new Scanner(serverResp);
			Integer msgId = Integer.parseInt(line.next());
			mailIDs.add(msgId);
			line.close();
			serverResp = recieve();
		}
		return mailIDs;
	}

	// if an error message appeared, then we close connections and return true.
	// else we return false. this captures a common error case pattern often
	// occured in code.
	private boolean responseWasFailure(String errMSG) throws IOException {
		if (!recieve().startsWith(OK)) {
			debugLog(errMSG);
			closeSession();
			return true;
		}
		return false;
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
		recieve();	// get +OK response
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

	private void resetConnectionVars() {
		socket = null;
		outputStream = null;
		inputStream = null;
		reader = null;
		writer = null;
	}
}
