package server;

/**
 * Created by sacry on 16/05/14.
 */
public class ServerUtil {

    public static final String ERROR = "ERROR";
    public static final String OK = "OK";
    private ServerUtil(){

    }

    public static String error(String msg){
        return ERROR + " " + msg;
    }
}
