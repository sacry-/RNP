package ServicePackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Created by Allquantor on 26.04.14.
 */


public class ReadFcWriteFs {

    private Socket clientSocket;
    InputStream inputStream;
    OutputStream outputStream;


    public ReadFcWriteFs(Socket socket){
        clientSocket = socket;
        try {
            this.inputStream = clientSocket.getInputStream();
            this.outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String readFromClient() {
        int read;
        byte[] byteArray = new byte[512];
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

    public void sendToClient(String message) {
        try {
            if (!clientSocket.isClosed()) {
                byte[] byteArray = (message + "\n").getBytes("UTF-8");
                outputStream.write(byteArray, 0, byteArray.length);
            }

        } catch (Exception e) {
            closeConnection();
            // e.printStackTrace();
        }
    }

    public void closeConnection() {
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
