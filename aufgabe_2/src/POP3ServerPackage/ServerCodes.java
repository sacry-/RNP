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
    public static final String NEWLINE = System.getProperty("line.separator");
    public static final String TERMINTATOR = ".";
    public static final String MULTI_LINE_TERMINATOR = NEWLINE + TERMINTATOR;
    public static final String NULL_STRING = "";
    public static final String WIDESPACE = " ";
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    public static final String OCTATS = "Octats";
    public static final String SIGN_OFF = "QUIT - SIGNED OFF";

    private ServerCodes() {

    }

    public static void closeSocketAndHisStream(Socket s) {
        try {
            if (!s.isClosed()) {
                s.shutdownInput();
                s.shutdownOutput();
                s.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeSocketAndHisStream(ServerSocket welcomeSocket) {
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
        return NULL_STRING;
    }
    
    public static String mkRequest(String cmd, String msg) {
    	return (cmd + WIDESPACE + msg);
    }

    public static String success(String message) {
        return (OK + WIDESPACE + message);
    }

    public static String fail(String message) {
        return (ERROR + WIDESPACE + message);
    }

    public static String failWithInvalidMsgNo(String arg) {
        return fail("Invalid Msg No " + arg);
    }


}
