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

    public POP3Server(){
        initializeServer();
    }

    private void initializeServer() {
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
            new POP3ServerThread(clientSocket, stream, threadAnzahl++).start();
        }
    }

    public static void main(String[] args) {
        POP3Server s = new POP3Server();
        s.serverRun();
    }


}




