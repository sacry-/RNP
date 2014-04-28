package POP3ServerPackage;

/**
 * Created by sacry on 28/04/14.
 */
public class ServerCodes {

    public static final String OK = "+OK";
    public static final String ERROR = "-ERR";

    public static final String USER = "USER";
    public static final String PASS = "PASS";
    public static final String QUIT = "QUIT";
    public static final String STAT = "STAT";
    public static final String LIST = "LIST";
    public static final String RETR = "RETR";
    public static final String DELE = "DELE";
    public static final String NOOP = "NOOP";
    public static final String RSET = "RSET";
    public static final String UIDL = "UIDL";

    private ServerCodes(){

    }

    public static String success(String message) {
        return (OK + " " + message);
    }

    public static String fail(String message) {
        return (ERROR + " " + message);
    }
}
