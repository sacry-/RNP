package POP3ServerPackage;

import java.util.Map;
import java.util.Set;

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


    String quit() {
        mailbox.quitAndSaveChanges();
        return ServerCodes.success(ServerCodes.SIGN_OFF);
    }


    String stat() {
        StringBuilder sb = new StringBuilder();
        sb.append(mailbox.getEmailCount());
        sb.append(ServerCodes.WIDESPACE);
        sb.append(mailbox.getTotalEmailSize());
        sb.append(ServerCodes.WIDESPACE);
        sb.append(ServerCodes.OCTATS);

        return ServerCodes.success(String.valueOf(sb));
    }

    String noop() {
        return ServerCodes.success(ServerCodes.NOOP);
    }

    String rset() {
        mailbox.unmarkAllMarked();
        return stat();
    }

    String retr(int secondPart) {
        String content = mailbox.getEmailValue(secondPart);
        StringBuilder sb = new StringBuilder();

        if (!content.equals(ServerCodes.NULL_STRING)) {
            sb.append(list(secondPart));
            sb.append(content);
            sb.append(ServerCodes.MULTI_LINE_TERMINATOR);
        } else {
            sb.append(ServerCodes.fail("NO SUCH MESSAGE"));
        }
        return String.valueOf(sb);
    }


    String list(int key) {
        return fromInfoMapWithKey(key, mailbox.getInboxInfo());
    }


    String list() {
        return stat() + fromInfoMap(mailbox.getInboxInfo());
    }


    String dele(int messageID) {
        if (mailbox.markDeleted(messageID)) {
            return ServerCodes.success("MESSAGE:" + messageID + " DELETED");
        } else {
            return ServerCodes.fail("NO SUCH MESSAGE OR ALREADY DELETED");
        }
    }

    String uidl(int messageID) {
        return fromInfoMapWithKey(messageID, mailbox.getInboxUIDLs());
    }

    String uidl() {
        return ServerCodes.success(fromInfoMap(mailbox.getInboxUIDLs()));
    }


    private String fromInfoMap(Map<Integer, ?> mapsen) {

        StringBuilder sb = new StringBuilder();

        sb.append(ServerCodes.NEWLINE);
        Set<Integer> keys = mapsen.keySet();
        int size = mapsen.keySet().size();
        int i = 1;
        for (int key : keys) {
            sb.append(key);
            sb.append(ServerCodes.WIDESPACE);
            sb.append(mapsen.get(key));
            if(i != size){	//dont add a newline after the last one
            	sb.append(ServerCodes.NEWLINE);
            }
            i += 1;
        }
        sb.append(ServerCodes.MULTI_LINE_TERMINATOR);
        return String.valueOf(sb);
    }

    private String fromInfoMapWithKey(int key, Map<Integer, ?> mapsen) {
        StringBuilder sb = new StringBuilder();
        if (!mapsen.containsKey(key)) {
            sb.append(ServerCodes.fail("MESSAGE WITH ID:" + key + " DOES NOT EXIST"));
        } else {
            sb.append(ServerCodes.success(key + ServerCodes.WIDESPACE + mapsen.get(key)));
        }
        return String.valueOf(sb);
    }
}
