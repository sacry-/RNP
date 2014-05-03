package DataTypePackage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Allquantor on 20.04.14.
 */
public class Account {

    private DateFormat dataTime;
    private Integer uid;

    private Account(Integer uid) {
        this.uid = uid;
    }

    private void createTime() {
        Date dNow = new Date();
        SimpleDateFormat ft =
                new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss a zzz");
    }

    public static Account valueOf(Integer uid) {
        return new Account(uid);
    }

    public HashMap<String, String> eAddress() {
        return null;
    }

    public Integer uid() {
        return uid;
    }

    @Override
    public String toString() {
        return "Account(" + this.uid() + ")";
    }
}