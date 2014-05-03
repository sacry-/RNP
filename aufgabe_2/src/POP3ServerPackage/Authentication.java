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

    public Authentication(ReadFcWriteFs stream) {
        this.stream = stream;
    }
    
    public Mailbox getMailbox(){
    	return new MailboxImpl(this);
    }
    
    public boolean isAuthorized() {
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
        	return false;
        }
        scanner.close();
        stream.sendToClient(ServerCodes.fail(line));
        return isAuthorized();
    }

    public boolean isPwd(String user) {
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
