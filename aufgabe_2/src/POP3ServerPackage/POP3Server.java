package POP3ServerPackage;

import java.net.Socket;

/**
 * Created by Allquantor on 20.04.14.
 */
public class POP3Server {


    public void serverRun(){
        Socket welcomeSocket = new Socket();
        POP3ServerCommandParser commandParser = new POP3ServerCommandParser();

        while(true) {

            //unser input
            String command = "";
            //response
            String response = "";

            response =  commandParser.parseCommand(command);

            if(commandParser.isAuthorized()){
                commandParser.initializeTransactionState();
                new POP3ServerThread(welcomeSocket, 1, commandParser).start();
                commandParser = new POP3ServerCommandParser();
            }
        }
    }


    //MAIN LOOP
        //IF AUTHETHICATION STATE == OK ?
             //
            //CREATE NEW TRANSACTION_STATE
            //CREATE NEW THREAD 4 CONNECTION (TRANSACTION_STATE)
    //LOOP END



}

