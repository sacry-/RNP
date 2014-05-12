package POP3ClientPackage;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static ServicePackage.StorageService.*;

import ServicePackage.Logger;
import ServicePackage.ServerStateService;
import DataTypePackage.ClientUser;

import static POP3ServerPackage.ServerCodes.*;

public class POP3Client {

    private final static Logger logger = new Logger("clientlog.txt");
    private final String BASE = "/home/stud23/abl563/Downloads/aufgabe_2/src/ServicePackage/storage/email/1";

    private Socket socket;

    private InputStream inputStream;
    private OutputStream outputStream;

    private BufferedReader reader;
    private DataOutputStream writer;

    private static final boolean DEBUGMODE = true;
    private final int TIMEOUT = 30000;

    private final static List<ClientUser> users = new ArrayList<>();

    public POP3Client() {
        /*users.add(new ClientUser("lol2", "omg", "127.0.0.1",
                ServerStateService.PORT));*/
        users.add(new ClientUser("bai4rnpJ", "Fyi1vzX7", "lab30.cpt.haw-hamburg.de",
                ServerStateService.PORT));
        users.add(new ClientUser("bai4rnpJ", "Fyi1vzX7", "lab31.cpt.haw-hamburg.de",
                ServerStateService.PORT));
    }

    private static void debugLog(String msg) {
        if (DEBUGMODE)
            System.out.println(msg);
        logger.write(msg);
    }

    public static void main(String[] args) {
        new POP3Client().runClient();
    }

    public void runClient() {
        // Simple polling loop. Every correct response lets the loop progress
        for (ClientUser localUser : users) {
            try {
                resetConnectionVars();
                // Connect to host
                connectToHost(localUser);

                // connection successful?
                if (responseFailure("Denied connection."))
                    continue;

                // USER Authorisation
                if (reactionFailure(USER, localUser.user, "User was not found."))
                    continue;

                // PASSWORD checking
                if (reactionFailure(PASS, localUser.pw, "Pasword was wrong."))
                    continue;

                debugLog("Connection and authentication successful!");

                // Ask for all Mails
                // Response is f.e.: +OK 14 messages 2343 octets
                if (react(LIST, "List command not regnized by server."))
                    continue;

                // builds the list of IDs form the remaining lines of the response.
                processAllMails(localUser);

                // all work finished for this user :)
                closeSession();
            } catch (IOException e) {
                debugLog("IOException during server Connection" + e.toString());
            }
        }
        // all users have been tried out now.

        try {
            Thread.sleep(TIMEOUT);    // after 30 seconds
        } catch (InterruptedException e) {
            debugLog("Client waiting disturbed");
        }

        runClient();    // repeat from beginning
    }

    private void connectToHost(ClientUser localUser) throws IOException {
        socket = new Socket(localUser.host, localUser.port);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
        reader = new BufferedReader(new InputStreamReader(inputStream));
        writer = new DataOutputStream(outputStream);
    }

    private void processAllMails(ClientUser localUser) throws IOException {
        List<Integer> mailIDs = makeMailIDs();
        debugLog("Recieved mailIDs: " + mailIDs.toString());
        // Request and Read all the mails.
        for (Integer id : mailIDs) {
            if (reactionFailure(RETR, id, "Mail not found on server."))
                continue;

            String content = readMail();
            saveContentToFile(localUser, content);
        }
    }

    private void saveContentToFile(ClientUser localUser, String content) {
        // save the content into a new file.
        try {
            PrintWriter writer = new PrintWriter(BASE + SL + localUser.user + "_" + localUser.pw + SL + getUIDL(content), "UTF-8");
            writer.println(content);
            writer.close();
        } catch (Exception e) {
            logger.write("Mail creation failed.");
        }
    }

    private String getUIDL(String content) {
        Scanner lines = new Scanner(content);
        String myID = "asasdasd";
        while (lines.hasNextLine()) {
            String line = lines.nextLine();
            if (line.startsWith("Message-ID")) {
                myID = line.substring(line.indexOf('<') + 1, line.indexOf('@'));
            }
        }
        System.out.println("ID WAS:========================================" + myID);
        return myID;
    }

    private boolean reactionFailure(String command, Object arg, String msgIfError) throws IOException {
        send(mkRequest(command, arg));
        boolean hasFailed = responseFailure(msgIfError);
        return hasFailed;
    }

    private boolean react(String command, String msgIfError) throws IOException {
        send(mkRequest(command));
        boolean hasFailed = responseFailure(msgIfError);
        return hasFailed;
    }

    // reads the lines containing the mail content until an "." appears
    private String readMail() throws IOException {
        StringBuilder content = new StringBuilder();
        String serverResp = receive();
        while (!serverResp.equals(TERMINTATOR)) {
            if (serverResp.startsWith(TERMINTATOR)) {
                serverResp = serverResp.substring(1);
            }
            content.append(serverResp + NEWLINE);
            serverResp = receive();
        }
        return content.toString();
    }

    // reads the lines containing mail IDs of the list command
    // and returns a List of mailIDs.
    private List<Integer> makeMailIDs() throws IOException {
        String serverResp = receive();
        List<Integer> mailIDs = new ArrayList<>();
        // read all the mailIDs until the terminator has appeared.
        while (!serverResp.startsWith(TERMINTATOR)) {
            Scanner line = new Scanner(serverResp);
            Integer msgId = Integer.parseInt(line.next());
            mailIDs.add(msgId);
            line.close();
            serverResp = receive();
        }
        return mailIDs;
    }

    // if an error message appeared, then we close connections and return true.
    // else we return false. this captures a common error case pattern often
    // occured in code.
    private boolean responseFailure(String errMSG) throws IOException {
        if (!receive().startsWith(OK)) {
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

    private String receive() throws IOException {
        String request = reader.readLine();
        debugLog("Resp line    :" + request);
        return request;
    }

    private void closeSession() throws IOException {
        send(QUIT);
        receive();    // get +OK response
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
