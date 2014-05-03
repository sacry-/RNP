package ServicePackage;

import DataTypePackage.Email;
import POP3ServerPackage.Authentication;
import POP3ServerPackage.Mailbox;
import POP3ServerPackage.ServerCodes;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

// implementaion of the storage connection

public class MailboxImpl implements Mailbox {
    Authentication auth;
    Map<Integer, Email> emails;    // maps from IDs to Emails
    Map<String, Email> UIDLmails;    // maps from UDILs to Emails

    public MailboxImpl(Authentication auth) {
        this.auth = auth;
        UIDLmails = StorageService.inbox(auth);

        int i = 1;
        Map<Integer, Email> emails = new HashMap<Integer, Email>();
        for (Email uidl : UIDLmails.values()) {
            emails.put(i, uidl);
            i++;
        }
        this.emails = emails;
    }

    @Override
    public int getEmailCount() {
        return emails.keySet().size();
    }

    @Override
    public int getTotalEmailSize() {
        int sum = 0;
        for (Email eml : emails.values()) {
            sum += eml.size();
        }
        return sum;
    }

    @Override
    public void unmarkAllMarked() {
        for (Email eml : emails.values()) {
            eml.toBeDeleted(false);
        }
    }

    @Override
    public Map<Integer, Integer> getInboxInfo() {
        Map<Integer, Integer> accu = new HashMap<>();
        for (Entry<Integer, Email> entry : emails.entrySet()) {
            accu.put(entry.getKey(), entry.getValue().size());
        }
        return accu;
    }

    @Override
    public Map<Integer, String> getInboxUIDLs() {
        Map<Integer, String> accu = new HashMap<>();
        for (int uidl : emails.keySet()) {
            accu.put(uidl, emails.get(uidl).uidl());
        }
        return accu;
    }

    @Override
    public String getEmailValue(int emailID) {
        if (emails.containsKey(emailID)) {
            return emails.get(emailID).content();
        }
        return ServerCodes.NULL_STRING;
    }

    @Override
    public boolean markDeleted(int messageID) {
        if (emails.containsKey(messageID)) {
            return emails.get(messageID).toBeDeleted(true);
        } else {
            return false;
        }
    }

    @Override
    public void quitAndSaveChanges() {
        for (Email eml : emails.values()) {
            if (eml.isMarkedAsDeleted()) {
                StorageService.deleteEmail(auth, eml);
            }
        }
    }
}
