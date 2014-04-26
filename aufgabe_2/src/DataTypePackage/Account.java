package DataTypePackage;

/**
 * Created by Allquantor on 20.04.14.
 */
public class Account {

    private String pw;
    private String email;

    private Account(String pw, String email) {
        this.pw = pw;
        this.email = email;
    }

    public static Account valueOf(String pw, String email) {
        return new Account(pw, email);
    }


}
