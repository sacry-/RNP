package client;

import utils.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.regex.Pattern;

/**
 * Created by sacry on 16/05/14.
 */
public class ClientProtocol {

    public static final String BYE = "BYE";
    public static final String LIST = "LIST";
    public static final String INFO = "INFO";
    public static final String OK = "OK";
    public static final String ERROR = "ERROR";

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
        if (cmd.startsWith(OK)) {
            return OK;
        }
        return ERROR;
    }

    public static boolean isBetween1and100Characters(String input) {
        int size = tokenize(input).size();
        return size > 0 && size <= 100;
    }

    public static ArrayList<ClientUser> list(String list) {
        ArrayList<String> users = new ArrayList<String>(Arrays.asList(list.split(" ")));
        ArrayList<ClientUser> clientUsers = new ArrayList<ClientUser>();
        ListIterator<String> iterator = users.subList(2, users.size()).listIterator();
        while (iterator.hasNext()) {
            String hostName = iterator.next();
            String name = iterator.next();
            clientUsers.add(new ClientUser(name, hostName));
        }
        return clientUsers;
    }

    private static ArrayList<String> tokenize(String word) {
        if (word == null || word.equals("")) {
            return new ArrayList<String>();
        }
        String[] tokens = word.trim().split(" ");
        return new ArrayList<String>(Arrays.asList(tokens));
    }
}