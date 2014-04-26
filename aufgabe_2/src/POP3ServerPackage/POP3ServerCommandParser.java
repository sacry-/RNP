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

    private POP3ServerCommandImpl_Authetication commandImpl_authetication = new POP3ServerCommandImpl_Authetication();
    private POP3ServerCommandImpl_Transaction commandImpl_transaction;


    protected String success(String message) {
        return (OK + " " + message);
    }

    protected String fail(String message) {
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
            return  commandImpl_authetication.user(secondPart);

        } else if (firstPart == PASS) {
            return  commandImpl_authetication.pass(secondPart);

        } else if (firstPart == QUIT) {
            return  commandImpl_transaction.quit(secondPart);
        } else if (firstPart == STAT) {
            return  commandImpl_transaction.stat(secondPart);
        } else if (firstPart == LIST) {
            return  commandImpl_transaction.list(secondPart);
        } else if (firstPart == RETR) {
            return  commandImpl_transaction.retr(secondPart);
        } else if (firstPart == DELE) {
            return commandImpl_transaction.dele(secondPart);
        } else if (firstPart == NOOP) {
            return commandImpl_transaction.noop(secondPart);
        } else if (firstPart == RSET) {
            return commandImpl_transaction.rset(secondPart);
        } else if (firstPart == UIDL) {
           return commandImpl_transaction.uidl(secondPart);
        } else {
            return "NOP!";
        }
    }







    public boolean isAuthorized() {
        return commandImpl_authetication.isOK();
    }

    public void initializeTransactionState() {
        POP3ServerCommandImpl_Transaction commandImpl_transaction = new POP3ServerCommandImpl_Transaction(commandImpl_authetication.username(),
                commandImpl_authetication.password());

    }
}



