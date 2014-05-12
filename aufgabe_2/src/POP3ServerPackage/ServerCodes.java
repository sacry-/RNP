package POP3ServerPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by sacry on 28/04/14.
 */
public class ServerCodes {

    public static final String OK = "+OK";
    public static final String ERROR = "-ERR";

    public static final String USER = "USER";
    public static final String PASS = "PASS";
    public static final String QUIT = "QUIT";
    public static final String STAT = "STAT";
    public static final String LIST = "LIST";
    public static final String RETR = "RETR";
    public static final String DELE = "DELE";
    public static final String NOOP = "NOOP";
    public static final String RSET = "RSET";
    public static final String UIDL = "UIDL";
    public static final String SIGN_OFF = "QUIT - SIGNED OFF";

    public static final String NEWLINE = "\r\n";
    public static final String TERMINTATOR = ".";
    public static final String MULTI_LINE_TERMINATOR = NEWLINE + TERMINTATOR;
    public static final String EMPTY_STRING = "";
    public static final String WHITESPACE = " ";
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String OCTATS = "Octats";

    private ServerCodes() {
    }

    public static void closeClientSocketAndStream(Socket client) {
        try {
            if (!client.isClosed()) {
                client.shutdownInput();
                client.shutdownOutput();
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeSocket(ServerSocket welcomeSocket) {
        try {
            welcomeSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getNextLine(Scanner scanner) {
        if (scanner.hasNext()) {
            return scanner.next();
        }
        return EMPTY_STRING;
    }

    public static String mkRequest(String cmd) {
    	return (cmd);
    }
    public static String mkRequest(String cmd, Object msg) {
    	return (cmd + WHITESPACE + msg.toString());
    }
    

    public static String success(String message) {
        return (OK + WHITESPACE + message);
    }

    public static String fail(String message) {
        return (ERROR + WHITESPACE + message);
    }

    public static String failWithInvalidMsgNo(String arg) {
        return fail("Invalid Msg No " + arg);
    }


}
