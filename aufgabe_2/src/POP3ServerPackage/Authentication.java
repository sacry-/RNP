package POP3ServerPackage;

import DataTypePackage.Account;
import ServicePackage.Just;
import ServicePackage.Maybe;
import ServicePackage.Nothing;
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
    
    public Maybe<Boolean> isAuthorized() {
        String line = stream.readFromClient();
        Scanner scanner = new Scanner(line);
        String cmd = scanner.next();
        if (cmd.equals(ServerCodes.USER)) {
            String user = scanner.next();
            scanner.close();
            if (StorageService.checkIfExistst(account, user)) {
                this.username = user;
                stream.sendToClient(ServerCodes.success("Hello " + user));
                return isPwd(user);
            }
        }
        if (cmd.equals(ServerCodes.QUIT)) {
        	return new Nothing();
        }
        scanner.close();
        stream.sendToClient(ServerCodes.fail(line));
        return isAuthorized();
    }

    public Maybe<Boolean> isPwd(String user) {
        String line = stream.readFromClient();
        Scanner scanner = new Scanner(line);
        String cmd = scanner.next();
        if (cmd.equals(ServerCodes.PASS)) {
            String password = scanner.next();
            scanner.close();
            System.out.println("Password:" + password);
            if(StorageService.checkIfExists(account, username, password)) {
                this.password = password;
                stream.sendToClient(ServerCodes.success("pwd " + password));
                return new Just<Boolean>(true);
            }
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
