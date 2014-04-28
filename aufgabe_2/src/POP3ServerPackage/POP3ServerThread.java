package POP3ServerPackage;

import java.net.Socket;
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

    private void initialize() {
        authentication = new Authentication(stream);
        authentication.isAuthorized();
        transaction = new Transaction(authentication.username(),
                authentication.password());
    }

    public void run(){
        //here the thread abfuck
        initialize();
        CommandParser.parseCommand("from Socket", transaction);
    }

}
