package ServicePackage;

import POP3ServerPackage.POP3Server;
import POP3ServerPackage.ServerCodes;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by Allquantor on 26.04.14.
 */


public class ReadFcWriteFs {

    private Socket clientSocket;
    InputStream inputStream;
    OutputStream outputStream;


    public ReadFcWriteFs(Socket socket) {
        clientSocket = socket;
        try {
            this.inputStream = clientSocket.getInputStream();
            this.outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFromClient() {
        Scanner scanner = new java.util.Scanner(inputStream).useDelimiter(ServerCodes.NEWLINE);
        String erg = scanner.hasNext() ? scanner.next() : "";
        POP3Server.logger.write("Incoming:  " + erg);
        return erg;
    }

    public void sendToClient(String message) {
        try {
            if (!clientSocket.isClosed()) {
                byte[] byteArray = (message + ServerCodes.NEWLINE).getBytes("UTF-8");
                outputStream.write(byteArray, 0, byteArray.length);
                ServerStateService.debug(message);
                POP3Server.logger.write("Outgoing: " + message);
            }

        } catch (Exception e) {
            closeConnection();
            // e.printStackTrace();
        }
    }

    public void closeConnection() {
        ServerCodes.closeSocketAndHisStream(clientSocket);
        // inputStream.close();
        // outputStream.close();
        //clientSocket.close();
    }

}
