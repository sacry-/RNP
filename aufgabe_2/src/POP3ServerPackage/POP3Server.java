package POP3ServerPackage;

import ServicePackage.Logger;
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
    public static Logger logger;

    public POP3Server() {
        initializeServer();
    }

    private void initializeServer() {
        try {
            welcomeSocket = new ServerSocket(PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger = new Logger("serverlog.txt");
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
        while (readyToAcceptNewConnection()) {
            intializeStreams();
            if (threadAnzahl <= MAX_CONNECTIONS) {
                new POP3ServerThread(clientSocket, stream, threadAnzahl++).start();
            }
        }
        closeConnection();
    }

    public static void main(String[] args) {
        new POP3Server().serverRun();
    }

    private boolean readyToAcceptNewConnection() {
        return ServerStateService.isRunning();
    }

    private void closeConnection() {
        System.out.println("Server: starting shutdown process");
        ServerCodes.closeSocket(welcomeSocket);
        ServerCodes.closeClientSocketAndStream(clientSocket);
        stream.closeConnection();
    }


}




