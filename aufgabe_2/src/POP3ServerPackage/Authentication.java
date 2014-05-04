package POP3ServerPackage;

import DataTypePackage.Account;
import ServicePackage.MailboxImpl;
import ServicePackage.ReadFcWriteFs;
import ServicePackage.StorageService;

import java.util.Scanner;

import static POP3ServerPackage.POP3Server.logger;

/**
 * Created by Allquantor on 26.04.14.
 */
public class Authentication {

    private static final Account account = Account.valueOf(1);
    private String username;
    private String password;
    private ReadFcWriteFs stream;

    public Account getAccount(){
        return this.account;
    }
    public Authentication(ReadFcWriteFs stream) {
        this.stream = stream;
    }
    public Mailbox getMailbox() {
        return new MailboxImpl(this);
    }

    public boolean authenticate() {
        String line = "";

        line = stream.readFromClient();
        // logger.write("Server gelesen: " + line);
        // System.out.println("DAS IST DIE LINE: " + line);


        Scanner scanner = new Scanner(line);
        String cmd = ServerCodes.getNextLine(scanner);
      //  System.out.println("DAS IST CMD_1: " + cmd);

        if (cmd.equals(ServerCodes.USER)) {
            String user = ServerCodes.getNextLine(scanner);
          //  System.out.println("DAS IST CMD_2:  " + user);
            scanner.close();
            if (StorageService.checkIfExistst(account, user)) {
                this.username = user;
                stream.sendToClient(ServerCodes.success("Hello " + user));
                return isPwd(user);
            }
        }


        if (cmd.equals(ServerCodes.NULL_STRING)) {
            stream.sendToClient(ServerCodes.fail(ServerCodes.NULL_STRING));

            return authenticate();
        }

        if (cmd.equals(ServerCodes.QUIT)) {
            stream.sendToClient(ServerCodes.QUIT);
            return false;
        }
        scanner.close();
        stream.sendToClient(ServerCodes.fail(line));
        return authenticate();
    }

    public boolean isPwd(String user) {
        String line = stream.readFromClient();
        Scanner scanner = new Scanner(line);
        String cmd = ServerCodes.getNextLine(scanner);

        if (cmd.equals(ServerCodes.PASS)) {
            String password = ServerCodes.getNextLine(scanner);
            scanner.close();
            System.out.println("Password:" + password);
            if (StorageService.checkIfExists(account, user, password)) {
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



    public String username() {
        return username;
    }

    public String password() {
        return password;
    }
}
