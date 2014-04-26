package DataTypePackage;

import java.util.List;
import java.util.Set;

/**
 * Created by Allquantor on 20.04.14.
 */
public class Account {

    private Integer uid;
    private String pw;
    private Set<String> eAddress;
    private String storageDir;

    private Account(String pw, Set<String> eAddress, String storageDir, Integer uid) {
        this.pw = pw;
        this.eAddress = eAddress;
        this.storageDir = storageDir;
        this.uid = uid;
    }

    public static Account valueOf(String pw, Set<String> email, String storageDir, Integer uid) {
        return new Account(pw, email, storageDir, uid);
    }

    public String password() {
        return pw;
    }

    public Set<String> eAddress() {
        return eAddress;
    }

    public String storageDir() {
        return storageDir;
    }

    public Integer uid() {
        return uid;
    }

    public boolean add(String address) {
        return eAddress.add(address);
    }

    public boolean addAll(Set<String> address) {
        return eAddress.addAll(address);
    }

    public boolean delete(String address) {
        return eAddress.remove(address);
    }
}
