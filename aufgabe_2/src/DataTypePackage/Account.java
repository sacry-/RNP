package DataTypePackage;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Allquantor on 20.04.14.
 */
public class Account {

    private Integer uid;
    private HashMap<String, String> eAddress;
    private String storageDir;

    private Account(HashMap<String, String> eAddress, String storageDir, Integer uid) {
        this.eAddress = eAddress;
        this.storageDir = storageDir;
        this.uid = uid;
    }

    public static Account valueOf(HashMap<String, String> eAddress, String storageDir, Integer uid) {
        return new Account(eAddress, storageDir, uid);
    }

    public HashMap<String, String> eAddress() {
        return eAddress;
    }

    public String storageDir() {
        return storageDir;
    }

    public Integer uid() {
        return uid;
    }

    public void put(String address, String pw) {
        eAddress.put(address, pw);
    }

    public void delete(String address) {
        eAddress.remove(address);
    }
}