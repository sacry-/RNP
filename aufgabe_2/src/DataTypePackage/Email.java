package DataTypePackage;

/**
 * Created by Allquantor on 20.04.14.
 */
public class Email {

    private String addressFrom;
    private String content;
    private Integer uidl;
    private Long timeStamp;

    private Email(String addressFrom, String content, Integer uidl) {
        this.addressFrom = addressFrom;
        this.content = content;
        this.uidl = uidl;
        this.timeStamp = System.currentTimeMillis();
    }

    public static Email valueOf(String addressFrom, String content, Integer uidl) {
        return new Email(addressFrom, content, uidl);
    }

    public String content() {
        return content;
    }

    public Long timeStamp() {
        return timeStamp;
    }

    public Integer getUidl() {
        return uidl;
    }
}
