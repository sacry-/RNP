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

    private POP3ServerCommandImpl commandImpl = new POP3ServerCommandImpl("bla","bla");


    private static String success(String message) {
        return (OK + " " + message);
    }

    private static String fail(String message) {
        return (ERROR + " " + message);
    }


    public String parseCommand(String command) {

        Scanner scanner = new Scanner(command);
        String firstPart = scanner.next();
        String secondPart = "";

        if (scanner.hasNext()) {
            secondPart = scanner.next();
        }


        if (firstPart == USER) {
            commandImpl.user(secondPart);
        } else if (firstPart == PASS) {
            commandImpl.pass(secondPart);
        } else if (firstPart == QUIT) {
            commandImpl.quit(secondPart);
        } else if (firstPart == STAT) {
            commandImpl.stat(secondPart);
        } else if (firstPart == LIST) {
            commandImpl.list(secondPart);
        } else if (firstPart == RETR) {
            commandImpl.retr(secondPart);
        } else if (firstPart == DELE) {
            commandImpl.dele(secondPart);
        } else if (firstPart == NOOP) {
            commandImpl.noop(secondPart);
        } else if (firstPart == RSET) {
            commandImpl.rset(secondPart);
        } else if (firstPart == UIDL) {
            commandImpl.uidl(secondPart);
        } else {
            return "NOP!";
        }
        return null;
    }


}


