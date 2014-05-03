package ServicePackage;

import java.util.Map;

import DataTypePackage.Account;
import POP3ServerPackage.Authentication;
import POP3ServerPackage.Mailbox;

// implementaion of the storage connection

public class MailboxImpl implements Mailbox {
	Authentication auth;
	public MailboxImpl(Authentication auth) {
		this.auth = auth;
	}

	@Override
	public int getEmailCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTotalEmailSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getEmailSizeToID(int ID) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void unmarkAllMarked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<Integer, Integer> getInboxInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, Integer> getInboxUIDLs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getEmailValue(int EmailID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean markDeleted(int messageID) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void quitAndSaveChanges() {
		// TODO Auto-generated method stub
		
	}
}
