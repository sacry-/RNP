package ServicePackage;

import java.util.HashMap;
import java.util.Map;

import DataTypePackage.Account;
import DataTypePackage.Email;

/**
 * Created by Allquantor on 20.04.14.
 */
 public class  StorageService {
    
    private final String BASE = "storage/";
    private final String ACCOUNT = "account/";
    private final String EMAIL = "email/";
    private String storageDir;
    
    // dummy storage
    private static Map<Integer, Account> accounts = new HashMap();
    
    StorageService(String storageDir) {
    	this.storageDir = storageDir;
    }
    
    public static void saveAccount(Account a) {
    	accounts.put(a.uid(), a);
    }
    
    public static Account loadAccount(Integer id) {
    	return accounts.get(id);	// dummy
    }
    
    private static Account mkAccountMap() {
    	
    }
    
}
