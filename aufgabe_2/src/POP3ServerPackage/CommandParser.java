package POP3ServerPackage;

import java.util.Scanner;

/**
 * Created by Allquantor on 26.04.14.
 */
public class CommandParser {

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

    private Authentication authentication = new Authentication();
    private Transaction transaction;


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
            return  authentication.user(secondPart);

        } else if (firstPart == PASS) {
            return  authentication.pass(secondPart);

        } else if (firstPart == QUIT) {
            return  transaction.quit(secondPart);
        } else if (firstPart == STAT) {
            return  transaction.stat(secondPart);
        } else if (firstPart == LIST) {
            return  transaction.list(secondPart);
        } else if (firstPart == RETR) {
            return  transaction.retr(secondPart);
        } else if (firstPart == DELE) {
            return transaction.dele(secondPart);
        } else if (firstPart == NOOP) {
            return transaction.noop(secondPart);
        } else if (firstPart == RSET) {
            return transaction.rset(secondPart);
        } else if (firstPart == UIDL) {
           return transaction.uidl(secondPart);
        } else {
            return "NOP!";
        }
    }


    public boolean isAuthorized() {
        return authentication.isOK();
    }

    public void initializeTransactionState() {
        Transaction commandImpl_transaction = new Transaction(authentication.username(),
                authentication.password());

    }
}



