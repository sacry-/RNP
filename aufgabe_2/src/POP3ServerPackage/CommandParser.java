package POP3ServerPackage;

import java.util.Scanner;

import static POP3ServerPackage.ServerCodes.*;

/**
 * Created by Allquantor on 26.04.14.
 */
public class CommandParser {

    private CommandParser() {

    }

    public static String parseCommand(String command, Transaction transaction) {
        Scanner scanner = new Scanner(command);
        String firstPart = scanner.next();
        String secondPart = "";

        if (scanner.hasNext()) {
            secondPart = scanner.next();
        }
        if (firstPart == QUIT) {
            return transaction.quit(secondPart);
        } else if (firstPart == STAT) {
            return transaction.stat(secondPart);
        } else if (firstPart == LIST) {
            return transaction.list(secondPart);
        } else if (firstPart == RETR) {
            return transaction.retr(secondPart);
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

}



