package ServicePackage;

import DataTypePackage.Account;
import DataTypePackage.Email;

/**
 * Created by Allquantor on 20.04.14.
 */
 public class  StorageService {


    //PATHS
    public static final String EMAIL_STORE_PATH = "";
    public static final String ACCOUNTDATA_STORE_PATH = "";
    
    private String storageDir;
    
    StorageService(String storageDir) {
    	this.storageDir = storageDir;
    }
    
    public static boolean save(Email a) {
    	return false;
    }

    public static boolean save(Account a) {
    	return false;
    }
    
    public static Email loadEmail() {
    	return null;
    }
    
    public static Account loadAccount() {
    	return null;
    }
    
}
