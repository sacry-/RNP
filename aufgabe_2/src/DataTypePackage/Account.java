package DataTypePackage;

/**
 * Created by Allquantor on 20.04.14.
 */
public class Account {

    private Integer uid;

    private Account(Integer uid) {
        this.uid = uid;
    }

    public static Account valueOf(Integer uid) {
        return new Account(uid);
    }

    public Integer uid() {
        return uid;
    }

    @Override
    public String toString() {
        return "Account(" + this.uid() + ")";
    }
}