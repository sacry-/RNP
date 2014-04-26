package POP3ServerPackage;

import java.util.Scanner;

/**
 * Created by Allquantor on 26.04.14.
 */
public class POP3ServerCommandParser {

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


    public static final String OK = "+OK";
    public static final String ERROR = "-ERR";

    private POP3ServerCommandImpl commandImpl = new POP3ServerCommandImpl();


    private static String success(String message) {
        return (OK + " " + message);
    }

    private static String fail(String message) {
        return (ERROR + " " + message);
    }


    public  String parseCommand(String command) {

        Scanner scanner = new Scanner(command);
        String firstPart = scanner.next();
        String secondPart = "";

        if (scanner.hasNext()) {
            secondPart = scanner.next();
        }

        switch (firstPart) {
            case USER:
                commandImpl.user(secondPart);
            case PASS:
                commandImpl.pass(secondPart);
            case QUIT:
            case STAT:
            case LIST:
            case RETR:
            case DELE:
            case NOOP:
            case RSET:
            case UIDL:
            default:
                return null;
        }
    }




}
