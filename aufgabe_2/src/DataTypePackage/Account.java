package DataTypePackage;

/**
 * Created by Allquantor on 20.04.14.
 */
public class Account {

    private String pw;
    private String email;
    private String storageDir;

    private Account(String pw, String email, String storageDir) {
        this.pw = pw;
        this.email = email;
    }

    public static Account valueOf(String pw, String email, String storageDir) {
        return new Account(pw, email, storageDir);
    }

    public String password() {
        return pw;
    }

    public String email() {
        return email;
    }

    public String storageDir(){
        return storageDir;
    }
}
