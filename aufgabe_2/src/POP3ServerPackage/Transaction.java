package POP3ServerPackage;

import java.util.Map;

/**
 * Created by Allquantor on 20.04.14.
 */
class Transaction {


    private Authentication authentication;
    private Mailbox mailbox;

    public Transaction(Authentication authentication) {
        this.authentication = authentication;
        mailbox = authentication.getMailbox();
    }

   
    //finished
    String quit() {
    	mailbox.quitAndSaveChanges();
        return ServerCodes.success("QUIT Signed off.");
    }


    //liefert den Status der Mailbox,
    // u. a. die Anzahl aller E-Mails im Postfach und deren Gesamtgröße (in Byte).

    //finished
    String stat() {
        return ServerCodes.success(mailbox.getEmailCount() + " " + mailbox.getTotalEmailSize() + "octats");
    }

    //finished
    String noop() {
        return ServerCodes.success("noop");
    }

    //finished
    String rset() {
        mailbox.unmarkAllMarked();
        return stat();
    }

    //finished
    String retr(int secondPart) {
        return list(secondPart) + ServerCodes.NEWLINE + mailbox.getEmailValue(secondPart) + ServerCodes.MULTI_LINE_TERMINATOR;
    }

    //finished

    String list(int key) {
        return fromInfoMapWithKey(key, mailbox.getInboxInfo());
    }

    //finished

    String list() {

        return stat() + fromInfoMap(mailbox.getInboxInfo());
    }


    String dele(int messageID) {
        if (mailbox.markDeleted(messageID)) {
            return ServerCodes.success("MESSAGE:" + messageID + " DELETED");
        } else {
            return ServerCodes.fail("NO SUCH MESSAGE OR ALREADY DELEATED");
        }
    }

    String uidl(int messageID) {
        return fromInfoMapWithKey(messageID, mailbox.getInboxUIDLs());
    }

    String uidl() {
        return ServerCodes.success(fromInfoMap(mailbox.getInboxUIDLs()));
    }

    private String fromInfoMap(Map<Integer, ?> mapsen) {
        String accu = "";
        for (int k : mapsen.keySet()) {
            accu += k + " " + mapsen.get(k) + ServerCodes.NEWLINE;
        }
        return ServerCodes.NEWLINE + accu + ServerCodes.MULTI_LINE_TERMINATOR;
    }

    private String fromInfoMapWithKey(int key, Map<Integer, ?> mapsen) {
        if (!mapsen.containsKey(key)) {
            return ServerCodes.fail("MESSAGE WITH ID:" + key + " DOES NOT EXIST");
        } else {
            return ServerCodes.success(key + " " + mapsen.get(key));
        }
    }
}
