package server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sacry on 16/05/14.
 */
public class ServerProtocol {

    public static final String NEW = "NEW";
    public static final String INFO = "INFO";
    public static final String BYE = "BYE";
    public static final String LIST = "LIST";

    public static final String INVALID_NAME = "@@";

    private ServerProtocol() {
    }

    public static String newName(String command) {
        ArrayList<String> tokens = tokenize(command);
        if (tokens.size() == 2 && tokens.get(0).equals(NEW)) {
            String name = tokens.get(1);
            System.out.println("NAME " + name);
            if (name != null && name != "" && name.length() < 20) {
                return name;
            }
        }
        return INVALID_NAME;
    }

    public static String normalizeCommand(String command) {
        ArrayList<String> tokens = tokenize(command);
        String cmd = tokens.get(0);
        if (cmd.equals(INFO)) {
            return INFO;
        }
        if (cmd.equals(BYE)) {
            return BYE;
        }
        return ServerUtil.error("invalid command");
    }

    public static String info(List<ServerUser> userlist) {
        String users = LIST + " " + userlist.size();
        for (ServerUser u : userlist) {
            users += " " + u.toString();
        }
        return users;
    }

    private static ArrayList<String> tokenize(String word) {
        if (word == null || word == "") {
            return new ArrayList<String>();
        }
        String[] tokens = word.trim().split(" ");
        return new ArrayList<String>(Arrays.asList(tokens));
    }
}
