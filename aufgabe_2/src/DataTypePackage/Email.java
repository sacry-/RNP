package DataTypePackage;

/**
 * Created by Allquantor on 20.04.14.
 */
public class Email {

    private String addressFrom;
    private String content;

    private Email(String addressFrom, String content) {
        this.addressFrom = addressFrom;
        this.content = content;
    }

    public static Email valueOf(String addressFrom, String content) {
        return new Email(addressFrom, content);
    }

}
