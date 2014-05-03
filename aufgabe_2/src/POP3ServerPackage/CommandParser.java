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
        String arg = null;
        if (scanner.hasNext()) {    // only assign a argument, if it exists. scanner.next() throws exception otherwise.
            arg = scanner.next();
        }
        scanner.close();

        boolean argGiven = arg != null;

        if (cmd.equals(QUIT)) {
            return transaction.quit();
        } else if (cmd.equals(STAT)) {
            return transaction.stat();
        } else if (cmd.equals(LIST)) {
            return handleList(argGiven, arg, transaction);
        } else if (cmd.equals(RETR)) {
            try {
                int i = Integer.parseInt(arg);
                return transaction.retr(i);
            } catch (NumberFormatException e) {
                return ServerCodes.failWithInvalidMsgNo(arg);
            }

        } else if (cmd.equals(DELE)) {
            try {
                int i = Integer.parseInt(arg);
                return transaction.dele(i);
            } catch (NumberFormatException e) {
                return ServerCodes.failWithInvalidMsgNo(arg);
            }

        } else if (cmd.equals(NOOP)) {
            return transaction.noop();
        } else if (cmd.equals(RSET)) {
            return transaction.rset();
        } else if (cmd.equals(UIDL)) {
            return handleUIDL(argGiven, arg, transaction);
        } else {
            return ServerCodes.fail("Unknown Command " + cmd);
        }
    }


    // boilerplate code to parse Ints and handle the optional argument commands.
    // I dont know, whether both of these methods can be refactored.....
    // they're identical except for exchanging "list" <-> "uidl" !!
    private static String handleList(boolean argGiven, String arg, Transaction transaction) {
        if (argGiven) {    // if an argument if given, then parse and process it.
            try {
                int i = Integer.parseInt(arg);
                return transaction.list(i);
            } catch (NumberFormatException e) {
                return ServerCodes.failWithInvalidMsgNo(arg);
            }
        } else {    // else call the argumentless version.
            return transaction.list();
        }
    }

    private static String handleUIDL(boolean argGiven, String arg, Transaction transaction) {
        if (argGiven) {    // if an argument if given, then parse and process it.
            try {
                int i = Integer.parseInt(arg);
                return transaction.uidl(i);
            } catch (NumberFormatException e) {
                return ServerCodes.failWithInvalidMsgNo(arg);
            }
        } else {    // else call the argumentless version.
            return transaction.uidl();
        }
    }

}



