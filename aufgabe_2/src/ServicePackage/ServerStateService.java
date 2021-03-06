package ServicePackage;

/**
 * Created by Allquantor on 26.04.14.
 */
public class ServerStateService {
    //Utility class
    public static boolean serverStateRunning = true;

    public static final int PORT = 11000;
    public final static int MAX_CONNECTIONS = 100;
    
    public static final boolean DEBUGMODE = true;
    
    public static void debug(String msg) {
    	if(DEBUGMODE) System.out.println(msg);
    }
    
    public static int threadAnzahl = 0;

    public static boolean isRunning(){
        return serverStateRunning;
    }

}
