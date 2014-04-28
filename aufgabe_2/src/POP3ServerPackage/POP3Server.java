package POP3ServerPackage;

import ServicePackage.ReadFcWriteFs;
import ServicePackage.ServerStateService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

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

        while (ServerStateService.isRunning() && threadAnzahl <= MAX_CONNECTIONS) {

            intializeStreams();

            Authentication authentication = new Authentication(stream);
            authentication.isAuthorized();
            Transaction transaction = new Transaction(authentication.username(),
                    authentication.password());
            new POP3ServerThread(clientSocket, threadAnzahl++, transaction).start();

        }
    }


}




