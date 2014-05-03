package POP3ServerPackage;

import java.util.Map;

/**
 * Created by Allquantor on 03.05.14.
 */
public interface MailboxInterface {

    public int getEmailCount();

    public int getTotalEmailSize();

    public int getEmailSizeToID(int ID);

    public void unmarkAllMarked();


    public Map<Integer, Integer> getInboxInfo();
    public Map<Integer,Integer> getInboxUIDLs();

    public String getEmailValue(int EmailID);

    public boolean markDeleted(int messageID);

}
