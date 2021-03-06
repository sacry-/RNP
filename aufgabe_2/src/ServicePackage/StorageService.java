package ServicePackage;

import DataTypePackage.Account;
import DataTypePackage.Email;
import POP3ServerPackage.Authentication;
import POP3ServerPackage.ServerCodes;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Allquantor on 20.04.14.
 */
public class StorageService {
    
	// take the upper one, if you're unix. else take the bottom one for windows.
    // private static final String SL = "/";
    public static final String SL = ServerCodes.FILE_SEPARATOR;
    
    //+ "/src/ServicePackage/storage/email/";
    public static String base = new File(System.getProperty("user.dir")).getAbsolutePath()
    	/*	+ SL + "aufgabe_2"*/ + SL + "src" + SL + "ServicePackage" + SL + "storage" + SL + "email" + SL;	// change this depending on the operating system.
    
    
    private StorageService() {
    }

    public static Map<String, Email> inbox(Authentication auth) {
        List<File> mails = getEmailsForUser(auth.getAccount(), auth.username(), auth.password());
        HashMap<String, Email> inbox = new HashMap<>();
        for (File f : mails) {
            String uidl = stripMailName(f);
            int size = getByteSize(f);
            Email mail = new Email(uidl, mailContent(f), size, false);
            inbox.put(uidl, mail);
        }
        return inbox;
    }

    public static boolean deleteEmail(Authentication auth, Email mail) {
        if (checkIfExists(auth.getAccount(), auth.username(), auth.password())) {
            try {
                String userPath =  getUserPath(auth.getAccount(), auth.username(), auth.password());
                String path = base + userPath + SL + mail.uidl();
                return new File(path).delete();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static Integer getByteSize(File f) {
        return (int) f.length();
    }

    private static String mailContent(File f) {
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(f.getPath()));
            return new String(encoded, "utf-8");
        } catch (Exception e) {

        }
        return "";
    }

    private static String stripMailName(File f) {
        int lastIndex = f.toString().lastIndexOf(SL);
        return f.toString().substring(lastIndex + 1);
    }

    public static boolean saveAccount(Account a) {
        if (!checkIfExists(a)) {
            new File(base + a.uid().toString()).mkdirs();
            return true;
        }
        return false;
    }

    public static List<File> getEmailsForUser(Account a, String user, String pw) {
        if (checkIfExists(a, user, pw))
            return filterFiles(getFilesFromDirectory(a.uid().toString() + SL + user + "_" + pw));
        return null;
    }

    public static Map<String, String> getAllUsersForAccount(Account a) {
        if (checkIfExists(a)) {
            Map<String, String> result = new HashMap<>();
            List<File> resultSet = filterDirectories(getFilesFromDirectory(a.uid().toString()));
            for (File f : resultSet) {
                String path = f.toString();
                int l = path.lastIndexOf(SL) + 1;
                String[] key_val = path.substring(l).split("_");
                result.put(key_val[0], key_val[1]);
            }
            return result;
        }
        return null;
    }

    private static String getUserPath(Account a, String user, String pw) {
        return a.uid().toString() + SL + user + "_" + pw;
    }

    private static List<File> getFilesFromDirectory(String dir) {
        return Arrays.asList(new File(base + dir).listFiles());
    }

    public static boolean checkIfExists(Account a) {
    	System.out.println(new File(base + a.uid().toString()));
        return new File(base + a.uid().toString()).isDirectory();
    }

    public static boolean checkIfExists(Account a, String user, String pw) {
        if (checkIfExists(a))
            return new File(base + a.uid().toString() + SL + user + "_" + pw).isDirectory();
        return false;
    }

    public static boolean checkIfExistst(Account a, String user) {
        return getAllUsersForAccount(a).containsKey(user);
    }

    private static List<File> filterDirectories(List<File> l) {
        List<File> l2 = new ArrayList<>();
        for (File f : l) {
            if (f.isDirectory())
                l2.add(f);
        }
        return l2;
    }


    private static List<File> filterFiles(List<File> l) {
        List<File> l2 = new ArrayList<>();
        for (File f : l) {
            if (f.isFile())
                l2.add(f);
        }
        return l2;
    }

    // deprecated
    public static List<Account> getAllAccounts() {
        List<File> resultSet = filterDirectories(getFilesFromDirectory(""));
        List<Account> result = new ArrayList<>();
        for (File f : resultSet) {
            String path = f.toString();
            int l = path.lastIndexOf(SL) + 1;
            String id = path.substring(l);
            result.add(Account.valueOf(new Integer(id)));
        }
        return result;
    }

    public static List<File> getAllEmails(Account a) {
        if (checkIfExists(a))
            return filterFiles(getFilesFromDirectory(a.uid().toString()));
        return null;
    }


    public static boolean saveUser(Account a, String user, String pw) {
        saveAccount(a);
        if (!checkIfExists(a, user, pw)) {
            new File(base + getUserPath(a, user, pw)).mkdirs();
            return true;
        }
        return false;
    }

    public static boolean saveEmail(Account a, String user, String pw, Email email) {
        if (checkIfExists(a, user, pw)) {
            try {
                String path = base + getUserPath(a, user, pw) + SL + email.uidl() + ".txt";
                new File(path).createNewFile();
                PrintWriter printWriter = new PrintWriter(path, "UTF-8");
                printWriter.println(email.content());
                printWriter.close();
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
    	System.out.println("<" + SL + ">");
    }


}