package POP3ServerPackage;

import java.util.Map;

/**
 * Created by Allquantor on 03.05.14.
 */
// Interface for the POP3 server.
// TODO: mailbox implementation.
// TODO: The mailbox implementation also has to support logging of events.
public interface Mailbox {

    public int getEmailCount();

    public int getTotalEmailSize();

    public void unmarkAllMarked();

    public Map<Integer, Integer> getInboxInfo();
    public Map<Integer, String> getInboxUIDLs();

    public String getEmailValue(int EmailID);

    public boolean markDeleted(int messageID);

	public void quitAndSaveChanges();

}
