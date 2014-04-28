package POP3ServerPackage;

import DataTypePackage.Account;
import ServicePackage.ReadFcWriteFs;
import ServicePackage.StorageService;

import java.util.Scanner;

/**
 * Created by Allquantor on 26.04.14.
 */
public class Authentication {

    private Account account = Account.valueOf(1);
    private String username;
    private String password;
    private ReadFcWriteFs stream;

    public Authentication(ReadFcWriteFs stream) {
        this.stream = stream;
    }

    public boolean isAuthorized() {
        String line = stream.readFromClient();
        Scanner scanner = new Scanner(line);
        String cmd = scanner.next();
        if (cmd.equals(ServerCodes.USER)) {//persistenz zugriff.
            String user = scanner.next();
            if (StorageService.checkIfExistst(account, user)) {
                this.username = user;
                stream.sendToClient(ServerCodes.success("Hello " + user));
                return isPwd(user);
            }
        }
        stream.sendToClient(ServerCodes.fail(line));
        return isAuthorized();
    }

    public boolean isPwd(String user) {
        String line = stream.readFromClient();
        Scanner scanner = new Scanner(line);
        String cmd = scanner.next();
        if (cmd.equals(ServerCodes.PASS)) {   //persistenz zugriff.
            String password = scanner.next();
            System.out.println("Password:" + password);
            if(StorageService.checkIfExists(account, username, password)) {
                this.password = password;
                stream.sendToClient(ServerCodes.success("pwd " + password));
                return true;
            }
        }
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
