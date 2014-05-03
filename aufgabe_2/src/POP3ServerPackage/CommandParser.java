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
            return transaction.stat();
        } else if (cmd == LIST) {
            //check arg given or not
            //if given parse int
            return transaction.list();
        } else if (cmd == RETR) {
            //parse int out
            //if not int error
            return transaction.retr(1);
        } else if (cmd == DELE) {
        	// parse int
        	// if not int then error.
            return transaction.dele(1);
        } else if (cmd == NOOP) {
            return transaction.noop(arg);
        } else if (cmd == RSET) {
            return transaction.rset();
        } else if (cmd == UIDL) {
            //check arg given or not
            //if given parse int
            return transaction.uidl(1);
        } else {
            return ServerCodes.fail("Unknown Command " + cmd);
        }
    }

}



