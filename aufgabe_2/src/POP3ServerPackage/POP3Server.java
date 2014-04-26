package POP3ServerPackage;

import ServicePackage.ServerStateService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;

import static ServicePackage.ServerStateService.*;

/**
 * Created by Allquantor on 20.04.14.
 */
public class POP3Server {

    static ServerSocket welcomeSocket;
    static Socket clientSocket;

    InputStream inputStream;
    OutputStream outputStream;

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
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void serverRun() {
        POP3ServerCommandParser commandParser = new POP3ServerCommandParser();

        while (ServerStateService.isRunning() && threadAnzahl <= MAX_CONNECTIONS) {

            intializeStreams();
            String command = readFromClient();

            //response
            String response = commandParser.parseCommand(command);
            sendToClient(response);


            if (commandParser.isAuthorized()) {
                commandParser.initializeTransactionState();
                new POP3ServerThread(clientSocket, threadAnzahl++, commandParser).start();
                commandParser = new POP3ServerCommandParser();

            }
        }
    }

    String readFromClient() {
        int read;
        byte[] byteArray = new byte[255];
        boolean keepGo = true;

        for (int i = 0; i < byteArray.length && keepGo == true; i++) {
            try {
                read = inputStream.read();

                if (read == -1 || read == 10) {
                    keepGo = false;
                } else {
                    byteArray[i] = (byte) read;
                }
            } catch (IOException e) {
                keepGo = false;
                return null;
            }
        }

        try {
            return (new String(byteArray, "UTF-8")).trim();
        } catch (UnsupportedEncodingException e) {
            return null;
            //e.printStackTrace();
        }
    }

    void sendToClient(String message) {
        try {


            if (!clientSocket.isClosed()) {
                byte[] byteArray = (message + "\n").getBytes("UTF-8");
                outputStream.write(byteArray, 0, byteArray.length);
            }

        } catch (Exception e) {
            closeConnectionAndStopThread();
            // e.printStackTrace();
        }
    }

    void closeConnectionAndStopThread() {
        try {
            inputStream.close();
            outputStream.close();
            clientSocket.close();
            //decrease thread anzahl
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}




