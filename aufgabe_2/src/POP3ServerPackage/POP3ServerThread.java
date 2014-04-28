package POP3ServerPackage;

import java.net.Socket;
import POP3ServerPackage.Transaction;
import static POP3ServerPackage.CommandParser.parseCommand;
import ServicePackage.Maybe;
import ServicePackage.ReadFcWriteFs;

/**
 * Created by Allquantor on 20.04.14.
 */
public class POP3ServerThread extends Thread {
    Socket socket;
    private final int threadID;
    Transaction transaction;
    Authentication authentication;
    ReadFcWriteFs stream;
    
    public POP3ServerThread(Socket socket, ReadFcWriteFs stream, int threadID){
        this.socket = socket;
        this.threadID = threadID;
        this.stream = stream;
    }

    private boolean authorization() {
        authentication = new Authentication(stream);
        Maybe<Boolean> authed = authentication.isAuthorized();
        if(authed.isJust()) {
        	transaction = new Transaction(authentication);  
        	return true;
        }
        else {
        	closeConnection();
        	return false;
        }
    }

    
    private void transaction() {
    	while(true) parseCommand("from Socket", transaction);
    }

    public void run() {
    	if(authorization()){
    		transaction();
    	}
    }
    
    private void closeConnection() {
        stream.closeConnection();	// the stream also closes the socket.
    }

}
