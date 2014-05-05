package POP3ServerPackage;

import ServicePackage.ReadFcWriteFs;
import ServicePackage.ServerStateService;

import java.net.Socket;

import static POP3ServerPackage.CommandParser.parseCommand;

/**
 * Created by Allquantor on 20.04.14.
 */
public class POP3ServerThread extends Thread {
    Socket socket;
    private final int threadID;
    Transaction transaction;
    Authentication authentication;
    ReadFcWriteFs stream;


    public POP3ServerThread(Socket socket, ReadFcWriteFs stream, int threadID) {
        this.socket = socket;
        this.threadID = threadID;
        this.stream = stream;
    }

    private boolean authorization() {
        authentication = new Authentication(stream);
        boolean authed = authentication.authenticate();
        if (authed) {
            transaction = new Transaction(authentication);
            return true;
        } else {
            // System.out.println("Not closed!");
            closeConnection();
            return false;
        }
    }

    private void transaction() {
        if (!socket.isClosed() && socket.isConnected()) {
            String input = null;
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
        if (authorization()) {
            transaction();
        }
    }

    private void closeConnection() {
        stream.closeConnection();
        ServerCodes.closeSocketAndHisStream(socket);
        //decrease thread count
        ServerStateService.threadAnzahl--;
    }


    boolean isConnectionClosed(String resp) {
        return resp.startsWith(ServerCodes.QUIT);
    }

}
