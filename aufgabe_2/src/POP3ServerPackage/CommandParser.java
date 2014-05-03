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
        String cmd = scanner.next();
        String arg = "";

        if (scanner.hasNext()) {
            arg = scanner.next();
        }
        scanner.close();
        if (cmd == QUIT) {
            return transaction.quit();
        } else if (cmd == STAT) {
            return transaction.stat(arg);
        } else if (cmd == LIST) {
            return transaction.list(arg);
        } else if (cmd == RETR) {
            return transaction.retr(arg);
        } else if (cmd == DELE) {
            return transaction.dele(arg);
        } else if (cmd == NOOP) {
            return transaction.noop(arg);
        } else if (cmd == RSET) {
            return transaction.rset(arg);
        } else if (cmd == UIDL) {
            return transaction.uidl(arg);
        } else {
            return ServerCodes.fail("Unknown Command " + cmd);
        }
    }

}



