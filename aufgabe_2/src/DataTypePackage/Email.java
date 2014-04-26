package DataTypePackage;

/**
 * Created by Allquantor on 20.04.14.
 */
public class Email {

    private String addressFrom;
    private String content;
    private Integer uidl;

    private Email(String addressFrom, String content, Integer uidl) {
        this.addressFrom = addressFrom;
        this.content = content;
        this.uidl = uidl;
    }

    public static Email valueOf(String addressFrom, String content, Integer uidl) {
        return new Email(addressFrom, content, uidl);
    }

}
