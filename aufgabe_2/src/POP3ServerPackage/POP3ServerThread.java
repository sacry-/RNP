package POP3ServerPackage;

import java.net.Socket;

/**
 * Created by Allquantor on 20.04.14.
 */
public class POP3ServerThread extends Thread {
    Socket socket;
    private final int threadID;
    Transaction transaction;


    public POP3ServerThread(Socket socket,int threadID, Transaction transaction){
        this.socket = socket;
        this.threadID = threadID;
        this.transaction = transaction;


    }

    public void run(){
        //here the thread abfuck
        CommandParser.parseCommand("from Socket", transaction);
    }

}
