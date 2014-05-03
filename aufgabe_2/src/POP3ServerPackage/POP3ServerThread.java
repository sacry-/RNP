package POP3ServerPackage;

import ServicePackage.ReadFcWriteFs;

import java.io.IOException;
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
        boolean authed = authentication.isAuthorized();
        if (authed) {
            transaction = new Transaction(authentication);
            return true;
        } else {
            System.out.println("Not closed!");
            closeConnection();
            return false;
        }
    }


    private void transaction() {
        while (true) {
            String input = stream.readFromClient();
            String out = parseCommand(input, transaction);
            stream.sendToClient(out);
            if(input.equals(ServerCodes.QUIT)){
                System.out.println("w00t-" + out + "-w00t");
                break;
            }
        }

        closeConnection();
    }

    public void run() {

        if (authorization()) {
            transaction();
        }
    }

    private void closeConnection() {
        stream.closeConnection();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
