package DataTypePackage;

/**
 * Created by Allquantor on 20.04.14.
 */
public class Email {

    private boolean markedAsDeleted;
    private String content;
    private String uidl;
    private Integer size;

    public Email(String uidl, String content, Integer size, boolean markedAsDeleted) {
        this.uidl = uidl;
        this.content = content;
        this.size = size;
        this.markedAsDeleted = markedAsDeleted;
    }

    public Integer size() {
        return this.size;
    }

    public String uidl() {
        return this.uidl;
    }

    public String content() {
        return this.content;
    }

    public boolean toBeDeleted(boolean markedAsDeleted) {
        this.markedAsDeleted = markedAsDeleted;
        return markedAsDeleted;
    }

    public boolean isMarkedAsDeleted() {
        return this.markedAsDeleted;
    }


}
