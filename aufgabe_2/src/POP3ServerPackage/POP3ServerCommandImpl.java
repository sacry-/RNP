package POP3ServerPackage;



/**
 * Created by Allquantor on 20.04.14.
 */
public class POP3ServerCommandImpl {
    //TODO Javadoc Comments 4 each functions



    public  String stat() {
        return null;
    }

    public  String list() {
        return null;
    }

    public  String retr() {
        return null;
    }

    public  String dele() {
        return null;
    }

    public  String noop() {
        return null;
    }

    public  String rset() {
        return null;
    }

    public  String uidl() {
        return null;
    }




    //USER name
    //Arguments:
    //a string identifying a mailbox (required), which is of
    //significance ONLY to the server
    public String user(String command) {
        if (true) {
          //  return success(command);
        } else {
           // return fail(command);
        }
        return null;
    }

    public  String pass(String command) {
        if (true) {
          //  return success(command);
        } else {
           // return fail(command);
        }
        return null;
    }


    /*
    Restrictions:
    may only be given in the AUTHORIZATION state after the POP3
    greeting or after an unsuccessful USER or PASS command
    Discussion:
    To authenticate using the USER and PASS command
    combination, the client must first issue the USER
    command. If the POP3 server responds with a positive
    status indicator ("+OK"), then the client may issue
    either the PASS command to complete the authentication,
    or the QUIT command to terminate the POP3 session. If
    the POP3 server responds with a negative status indicator
            ("-ERR") to the USER command, then the client may either
    issue a new authentication command or may issue the QUIT
    command.
    The server may return a positive response even though no
    such mailbox exists. The server may return a negative
    response if mailbox exists, but does not permit plaintext

    Examples:
    C: USER frated
    S: -ERR sorry, no mailbox for frated here
    ...
    C: USER mrose
    S: +OK mrose is a real hoopy frood
    */




    /*
    Here is the summary for the QUIT command when used in the
    AUTHORIZATION state:
    QUIT
    Arguments: none
    Restrictions: none
    Possible Responses:
    +OK
    Examples:
    C: QUIT
    S: +OK dewey POP3 server signing off
    */

    public static String quit() {
        return null;
    }


}
