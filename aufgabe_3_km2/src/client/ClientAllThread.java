package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * Created by sacry on 14/05/14.
 */
public class ClientAllThread {

    public ClientAllThread(PrintWriter out, BufferedReader in) {
        Sender sender = new Sender(out);
        sender.setDaemon(true);
        sender.start();

        Receiver receiver = new Receiver(in);
        receiver.setDaemon(true);
        receiver.start();

        while (sender.isAlive() && receiver.isAlive()) {
        }
    }

    class Receiver extends Thread {

        private BufferedReader in;

        public Receiver(BufferedReader in) {
            this.in = in;
        }

        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException ioe) {
                System.err.println("Connection to server broken.");
                ioe.printStackTrace();
            }
        }
    }


    class Sender extends Thread {
        private PrintWriter out;

        public Sender(PrintWriter out) {
            this.out = out;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                while (!isInterrupted()) {
                    String message = in.readLine();
                    out.println(message);
                    out.flush();
                }
            } catch (IOException ioe) {
                // Communication is broken
            }
        }
    }
}
