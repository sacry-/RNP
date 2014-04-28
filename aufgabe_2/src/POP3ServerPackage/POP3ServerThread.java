package POP3ServerPackage;

import java.net.Socket;

/**
 * Created by Allquantor on 20.04.14.
 */
public class POP3ServerThread extends Thread {
    Socket socket;
    private final int threadID;
    CommandParser commandParser ;



    public POP3ServerThread(Socket socket,int threadID, CommandParser commandParser){
        this.socket = socket;
        this.threadID = threadID;
        this.commandParser = commandParser;

    }

    public void run(){
        //here the thread abfuck
    }

}
