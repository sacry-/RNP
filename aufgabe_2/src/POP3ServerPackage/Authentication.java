package POP3ServerPackage;
/**
 * Created by Allquantor on 26.04.14.
 */
public class Authentication {



    private CommandParser commandParser = new CommandParser();
    private String username;
    private String password;

    public String user(String command) {
        if (true) {
            username = command;
            return commandParser.success(command);
        } else {
             reset();
             return commandParser.fail(command);
        }
    }

    public String pass(String command) {
        if (true) {
            password = command;
            return commandParser.success(command);
        } else {
            reset();
             return commandParser.fail(command);
        }
    }

    public void reset(){
        username = null;
        password = null;
    }

    public boolean isOK(){
        return (username != null && password != null);
    }

    public String username() {
        return username;
    }
    public String password() {
        return password;
    }
}
