package client;

import utils.Tuple2;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

/**
 * Created by sacry on 16/05/14.
 */
public class ClientProtocol {

    public static final String BYE = "BYE";
    public static final String LIST = "LIST";

    private ClientProtocol() {
    }

    public static String normalizeCommand(String command) {
        ArrayList<String> tokens = tokenize(command);
        String cmd = tokens.get(0);
        if (cmd.startsWith(LIST)) {
            return LIST;
        }
        if (cmd.startsWith(BYE)) {
            return BYE;
        }
        return ClientUtil.ERROR;
    }

    public static ArrayList<ClientUser> list(String list) {
        ArrayList<String> users = new ArrayList<String>(Arrays.asList(list.split(" ")));
        ArrayList<ClientUser> clientUsers = new ArrayList<ClientUser>();
        ListIterator<String> iterator = users.subList(2, users.size()).listIterator();
        while (iterator.hasNext()) {
            String hostN = iterator.next();
            Tuple2<String, Integer> t = hostName(hostN);
            String name = iterator.next();
            clientUsers.add(new ClientUser(name, t._1(), t._2()));
        }
        return clientUsers;
    }

    private static Tuple2<String, Integer> hostName(String aSocket) {
        String[] host_port = aSocket.split(":");
        String host = host_port[0];
        int port = Integer.parseInt(host_port[1]);
        return Tuple2.valueOf(host, port);
    }

    private static ArrayList<String> tokenize(String word) {
        if (word == null || word == "") {
            return new ArrayList<String>();
        }
        String[] tokens = word.trim().split(" ");
        return new ArrayList<String>(Arrays.asList(tokens));
    }

}