package POP3ServerPackage;

import ServicePackage.ReadFcWriteFs;
import ServicePackage.ServerStateService;

import java.net.Socket;

import static POP3ServerPackage.CommandParser.parseCommand;

/**
 * Created by Allquantor on 20.04.14.
 */
public class POP3ServerThread extends Thread {
    Socket clientSocket;
    private final int threadID;
    Transaction transaction;
    Authentication authentication;
    ReadFcWriteFs stream;


    public POP3ServerThread(Socket socket, ReadFcWriteFs stream, int threadID) {
        this.clientSocket = socket;
        this.threadID = threadID;
        this.stream = stream;
    }

    private void transaction() {
        transaction = new Transaction(authentication);
        if (!clientSocket.isClosed() && clientSocket.isConnected()) {
            String input = "";
            do {
                input = stream.readFromClient();
                String out = parseCommand(input, transaction);
                stream.sendToClient(out);
               
            } while (!isConnectionClosed(input));
        }
        closeConnection();
    }

    public void run() {
        stream.sendToClient(ServerCodes.success("Server ready!"));
        authentication = new Authentication(stream);
        boolean authenticated = authentication.whileNotAuthenticatedState();
        if (authenticated) {
            transaction();
        } else {
            closeConnection();
        }
    }

    private void closeConnection() {
        stream.closeConnection();
        ServerCodes.closeClientSocketAndStream(clientSocket);
        ServerStateService.threadAnzahl--;
    }

    boolean isConnectionClosed(String resp) {
        return resp.startsWith(ServerCodes.QUIT);
    }

}
