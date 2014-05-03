package ServicePackage;

/**
 * Created by Allquantor on 26.04.14.
 */
public class ServerStateService {
    //Utility class
    public static boolean serverStateRunning = true;
    public static boolean updateState = false;

    public static final int PORT = 1100;
    public final static int MAX_CONNECTIONS = 100;
    
    private static final boolean DEBUGMODE = true; 
    
    public static void debug(String msg) {
    	if(DEBUGMODE) System.out.println(msg);
    }
    
    public static int threadAnzahl = 0;

    public static boolean isRunning(){
        return serverStateRunning;
    }
    public static void setShutdown(){
        serverStateRunning = false;
    }
    public static void setRunning(){
        serverStateRunning = true;
    }



}
