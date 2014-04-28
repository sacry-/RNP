package POP3ServerPackage;

import ServicePackage.ReadFcWriteFs;
import ServicePackage.ServerStateService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static ServicePackage.ServerStateService.*;

/**
 * Created by Allquantor on 20.04.14.
 */
public class POP3Server {

    ReadFcWriteFs stream;
    static ServerSocket welcomeSocket;
    Socket clientSocket;

    public void initializeServer() {
        try {
            welcomeSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void intializeStreams() {
        try {
            clientSocket = welcomeSocket.accept();
            stream = new ReadFcWriteFs(clientSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void serverRun() {
        CommandParser commandParser = new CommandParser();

        while (ServerStateService.isRunning() && threadAnzahl <= MAX_CONNECTIONS) {

            intializeStreams();

            String command = stream.readFromClient();

            //response
            String response = commandParser.parseCommand(command);
            stream.sendToClient(response);


            if (commandParser.isAuthorized()) {
                commandParser.initializeTransactionState();
                new POP3ServerThread(clientSocket, threadAnzahl++, commandParser).start();
                commandParser = new CommandParser();

            }
        }
    }


}




