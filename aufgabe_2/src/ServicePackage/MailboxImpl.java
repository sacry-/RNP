package ServicePackage;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import DataTypePackage.Account;
import DataTypePackage.Email;
import POP3ServerPackage.Authentication;
import POP3ServerPackage.Mailbox;

// implementaion of the storage connection

public class MailboxImpl implements Mailbox {
	Authentication auth;
	Map<Integer, Email> emails;	// maps from IDs to Emails
	Map<String, Email> UIDLmails;	// maps from UDILs to Emails
	public MailboxImpl(Authentication auth) {
		this.auth = auth;
		UIDLmails = StorageService.inbox(auth);
		
		int i = 1;
		Map<Integer, Email> emails = new HashMap<Integer, Email>();
		for(Email uidl:UIDLmails.values()){
			emails.put(i, uidl);
			i+=1;
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
		for(Email eml:emails.values()) {
			sum+=eml.size();
		}
		return sum;
	}

	@Override
	public void unmarkAllMarked() {
		for(Email eml:emails.values()){
			eml.toBeDeleted(false);
		}
	}

	@Override
	public Map<Integer, Integer> getInboxInfo() {
		Map<Integer, Integer> accu = new HashMap<>();
		for(Entry<Integer, Email> entry:emails.entrySet()) {
			accu.put(entry.getKey(), entry.getValue().size());
		}
		return accu;
	}

	@Override
	public Map<Integer, Integer> getInboxUIDLs() {
		return UIDLmails.keySet();
	}

	@Override
	public String getEmailValue(int EmailID) {
		return emails.get(EmailID).content();
	}

	@Override
	public boolean markDeleted(int messageID) {
		emails.get(messageID).toBeDeleted(true);
	}

	@Override
	public void quitAndSaveChanges() {
		// TODO Auto-generated method stub
		for(Email eml:emails.values()) {
			if(eml.isMarkedAsDeleted()) {
				deleteEmail(auth, eml);
			}
		}
	}
}
