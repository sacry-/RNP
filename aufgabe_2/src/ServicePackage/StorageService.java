package ServicePackage;

import java.util.HashMap;
import java.util.Map;

import DataTypePackage.Account;
import DataTypePackage.Email;

/**
 * Created by Allquantor on 20.04.14.
 */
 public class  StorageService {
    
    private String storageDir;
    
    // dummy storage
    //Map<Integer, Email> emails = new HashMap();
    private Map<Integer, Account> accounts = new HashMap();
    
    StorageService(String storageDir) {
    	this.storageDir = storageDir;
    }
    
    /*public static boolean saveEmail(Email a) {
    	
    }*/
    
    public static boolean saveAccount(Account a) {
    	accounts.put(a., a)
    }
    
    public static Account loadAccount(Integer id) {
    	return null;	// dummy
    }
    
    /*public static Email loadEmail(Integer uidl) {
    	return null;
    }*/
    
}
