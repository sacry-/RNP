package ServicePackage;

import java.io.File;
import java.util.*;

import DataTypePackage.Account;
import DataTypePackage.Email;

/**
 * Created by Allquantor on 20.04.14.
 */
public class StorageService {

    private static String base = new File(System.getProperty("user.dir")).getAbsolutePath()
            + "/src/ServicePackage/storage/email/";

    public StorageService() {
    }

    public static List<File> getEmailsForUser(Account a, String user, String pw) {
        if (checkIfExists(a, user, pw))
            return filterFiles(getFilesFromDirectory(a.uid().toString() + "/" + user + "_" + pw));
        return null;
    }

    public static HashMap<String, String> getAllUsersForAccount(Account a) {
        if (checkIfExists(a)) {
            HashMap<String, String> result = new HashMap<>();
            List<File> resultSet = filterDirectories(getFilesFromDirectory(a.uid().toString()));
            for (File f : resultSet) {
                String path = f.toString();
                int l = path.lastIndexOf("/") + 1;
                String[] key_val = path.substring(l).split("_");
                result.put(key_val[0], key_val[1]);
            }
            return result;
        }
        return null;
    }

    public static List<Account> getAllAccounts() {
        List<File> resultSet = filterDirectories(getFilesFromDirectory(""));
        List<Account> result = new ArrayList<>();
        for (File f : resultSet) {
            String path = f.toString();
            int l = path.lastIndexOf("/") + 1;
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

    private static List<File> walker(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                walker(file.getAbsolutePath(), files);
            }
        }
        return files;
    }

    private static List<File> getFilesFromDirectory(String dir) {
        return Arrays.asList(new File(base + dir).listFiles());
    }

    private static boolean checkIfExists(Account a) {
        return new File(base + a.uid().toString()).isDirectory();
    }

    private static boolean checkIfExists(Account a, String user, String pw) {
        if (checkIfExists(a))
            return new File(base + a.uid().toString() + "/" + user + "_" + pw).isDirectory();
        return false;
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

    public static void main(String[] args) {
        System.out.println(getEmailsForUser(Account.valueOf(1), "lol@lol.de", "pwd"));
        System.out.println(checkIfExists(Account.valueOf(1)));
        System.out.println(checkIfExists(Account.valueOf(1), "lol@lol.de", "pwd"));
        System.out.println(checkIfExists(Account.valueOf(12)));
        System.out.println(checkIfExists(Account.valueOf(1), "lol@lol.ded", "pwd"));
        System.out.println(getAllUsersForAccount(Account.valueOf(1)));
        System.out.println(getAllUsersForAccount(Account.valueOf(12)));
        System.out.println(getAllAccounts());
    }

}