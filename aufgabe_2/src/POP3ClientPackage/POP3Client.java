package POP3ClientPackage;

import static POP3ServerPackage.POP3Server.logger;
import POP3ServerPackage.ServerCodes;
import ServicePackage.ServerStateService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static java.lang.System.in;

/**
 * Created by Allquantor on 02.04.14.
 * RN_1
 */
public class POP3Client {

    //socket connection
    static Socket serverSocket;
    //send messages to server
    static PrintWriter writerSendOut;
    //read the input stream from server
    static BufferedReader bufferResponse;
    //read StdIn
    static BufferedReader bufferStdInput;

    public static void initialize(String hostName, int port) {

        try {
            serverSocket = new Socket(hostName, port);
            writerSendOut = new PrintWriter(serverSocket.getOutputStream(), true);
            bufferResponse = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            bufferStdInput = new BufferedReader(new InputStreamReader(in)); //System.in)); => Same shit
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void runClient() {
    	boolean keepAsking = true;
        String answer = null;
        try {

            do {
                // read the next input from the standart IO
                String line = bufferStdInput.readLine();

                // out send the line to the server
                writerSendOut.println(line);
                writerSendOut.flush();
                logger.write("Client:sent to server=" + line);
                if(line.startsWith(ServerCodes.QUIT)) {
                	keepAsking = false;
                }
                // we get a response from server
                if(serverSocket.isClosed()) break;
                answer = bufferResponse.readLine();
                logger.write("Client:answer from server=" + answer);

                //check response message
            } while (keepAsking);
            closeConnection();


        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.write("Connection succesfull refused");
    }

    private static void closeConnection() {
        try {
            writerSendOut.close();
            serverSocket.close();
            bufferStdInput.close();
            bufferResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {

        String hostName = "127.0.0.1";
        int port = ServerStateService.PORT;
        initialize(hostName, port);
        runClient();

    }

}
