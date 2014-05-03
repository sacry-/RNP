package POP3ServerPackage;

import DataTypePackage.Account;
import ServicePackage.MailboxImpl;
import ServicePackage.ReadFcWriteFs;
import ServicePackage.StorageService;

import java.util.Scanner;

/**
 * Created by Allquantor on 26.04.14.
 */
public class Authentication {
    // TODO: we need to insert locking user access here.
    // once a authentication is authed, it has seated the user and
    // no other may access it.
    // we have to lock the user here.
    // also, we have to write a unlock() methode here
    // which is then called when the lient quits.

    private Account account = Account.valueOf(1);
    private String username;
    private String password;
    private ReadFcWriteFs stream;
    private boolean authorized = false;

    public Authentication(ReadFcWriteFs stream) {
        this.stream = stream;
    }

    public Mailbox getMailbox() {
        return new MailboxImpl(this);
    }


    public void waitForAuthentication() {

        while (!authorized) {
            String line = stream.readFromClient();
            String command, args;
            if (line == null || line.length() == 0) {
                command = "";
                args = "";
            } else {
                String[] userIn = line.split(" ");
                command = userIn[0];
                args = userIn[1];
            }

            if (command.equals(ServerCodes.USER)) {

            } else if (command.equals(ServerCodes.PASS)) {

            } else if ((command.equals(ServerCodes.QUIT))) {

            } else {

            }
        }
    }

    public boolean authorized() {
        return this.authorized;
    }

    public boolean isAuthorized() {
        String line = "";

        line = stream.readFromClient();
        System.out.println("DAS IST DIE LINE: " + line);


        Scanner scanner = new Scanner(line);
        String cmd = getNextLine(scanner);
        System.out.println("DAS IST CMD_1: " + cmd);

        if (cmd.equals(ServerCodes.USER)) {
            String user = getNextLine(scanner);
            System.out.println("DAS IST CMD_2:  " + user);
            scanner.close();
            if (StorageService.checkIfExistst(account, user)) {
                this.username = user;
                stream.sendToClient(ServerCodes.success("Hello " + user));
                return isPwd(user);
            }
        }

        //
        if (cmd.equals("")) {
            stream.sendToClient(ServerCodes.fail(""));
            return isAuthorized();
        }

        if (cmd.equals(ServerCodes.QUIT)) {
            stream.sendToClient(ServerCodes.success("QUIT"));
            return false;
        }
        scanner.close();
        stream.sendToClient(ServerCodes.fail(line));
        return isAuthorized();
    }

    public boolean isPwd(String user) {
        String line = stream.readFromClient();
        Scanner scanner = new Scanner(line);
        String cmd = getNextLine(scanner);

        if (cmd.equals(ServerCodes.PASS)) {
            String password = getNextLine(scanner);
            scanner.close();
            System.out.println("Password:" + password);
            if (StorageService.checkIfExists(account, username, password)) {
                this.password = password;
                stream.sendToClient(ServerCodes.success("pwd " + password));
                return true;
            }
        }
        if (cmd.equals(ServerCodes.QUIT)) {
            return false;
        }
        scanner.close();
        stream.sendToClient(ServerCodes.fail(line));
        return isPwd(user);
    }

    private String getNextLine(Scanner scanner) {
        if (scanner.hasNext()) {
            return scanner.next();
        }
        return "";
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
