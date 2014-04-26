package DataTypePackage;

import java.util.HashMap;

/**
 * Created by Allquantor on 20.04.14.
 */
public class Account {

    //what i need here:
    //email amout in the inbox

    //email byte weight of all emails in the in box

    //email weight of each email

    //all messages must have a unique number

    //function to delete a message to an argument (message number)

    //function to get message to message number
    //need to look like :
    //func(1)
    // Date: Mon, 18 Oct 2004 04:11:45 +0200
    // From: Someone <someone@example.com>
    // To: wiki@example.com
    // Subject: Test-E-Mail
    // Content-Type: text/plain; charset=us-ascii; format=flowed
    // Content-Transfer-Encoding: 7bit

    // Dies ist eine Test-E-Mail


    private Integer uid;

    private Account(Integer uid) {
        this.uid = uid;
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

    public void put(String address, String pw) {
        //eAddress.put(address, pw);
    }

    public void delete(String address) {
        //eAddress.remove(address);
    }

    @Override
    public String toString() {
        return "Account(" + this.uid() + ")";
    }
}