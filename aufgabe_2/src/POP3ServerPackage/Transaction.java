package POP3ServerPackage;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import static POP3ServerPackage.ServerCodes.*;

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
        return success(SIGN_OFF);
    }


    String stat() {
        StringBuilder sb = new StringBuilder();
        sb.append(mailbox.getEmailCount());
        sb.append(WHITESPACE);
        sb.append(mailbox.getTotalEmailSize());
        sb.append(WHITESPACE);
        sb.append(OCTATS);

        return success(String.valueOf(sb));
    }

    String noop() {
        return success(NOOP);
    }

    String rset() {
        mailbox.unmarkAllMarked();
        return stat();
    }

    String retr(int secondPart) {
        String content = mailbox.getEmailValue(secondPart);

        content = handleSingletonPoint(content);
        StringBuilder sb = new StringBuilder();

        if (!content.equals(EMPTY_STRING)) {
            sb.append(list(secondPart));
            sb.append(NEWLINE);
            sb.append(content);
            sb.append(MULTI_LINE_TERMINATOR);
        } else {
            sb.append(fail("NO SUCH MESSAGE"));
        }
        return String.valueOf(sb);
    }

    private String handleSingletonPoint(String content) {
        Scanner lines = new Scanner(content);
        StringBuilder sb = new StringBuilder();
        while (lines.hasNextLine()){
            String line = lines.nextLine();
            if(line.startsWith(TERMINTATOR)){
                line = "." + line;
            }
            sb.append(line + NEWLINE);
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
            return success("MESSAGE:" + messageID + " DELETED");
        } else {
            return fail("NO SUCH MESSAGE OR ALREADY DELETED");
        }
    }

    String uidl(int messageID) {
        return fromInfoMapWithKey(messageID, mailbox.getInboxUIDLs());
    }

    String uidl() {
        return success(fromInfoMap(mailbox.getInboxUIDLs()));
    }


    private String fromInfoMap(Map<Integer, ?> inbox) {
        StringBuilder sb = new StringBuilder();

        sb.append(NEWLINE);
        Set<Integer> keys = inbox.keySet();
        int size = inbox.keySet().size();
        int i = 1;
        for (int key : keys) {
            sb.append(key);
            sb.append(WHITESPACE);
            sb.append(inbox.get(key));
            if(i != size){	//dont add a newline after the last one
            	sb.append(NEWLINE);
            }
            i += 1;
        }
        sb.append(MULTI_LINE_TERMINATOR);
        return String.valueOf(sb);
    }

    private String fromInfoMapWithKey(int key, Map<Integer, ?> inbox) {
        StringBuilder sb = new StringBuilder();
        if (!inbox.containsKey(key)) {
            sb.append(fail("MESSAGE WITH ID:" + key + " DOES NOT EXIST"));
        } else {
            sb.append(success(key + WHITESPACE + inbox.get(key)));
        }
        return String.valueOf(sb);
    }
}
