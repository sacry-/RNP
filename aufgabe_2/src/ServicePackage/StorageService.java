package ServicePackage;

import DataTypePackage.Account;
import DataTypePackage.Email;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Allquantor on 20.04.14.
 */
public class StorageService {

    private static String base = new File(System.getProperty("user.dir")).getAbsolutePath()
            + "/src/ServicePackage/storage/email/";

    public StorageService() {
    }

    public static boolean saveAccount(Account a) {
        if (!checkIfExists(a)) {
            new File(base + a.uid().toString()).mkdirs();
            return true;
        }
        return false;
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
                String path = base + getUserPath(a, user, pw) + "/" + email.getUidl() + ".txt";
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

    public static boolean deleteEmail(Account a, String user, String pw, Integer uidl) {
        if (checkIfExists(a, user, pw)) {
            try {
                String path = base + getUserPath(a, user, pw) + "/" + uidl + ".txt";
                return new File(path).delete();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public static List<File> getEmailsForUser(Account a, String user, String pw) {
        if (checkIfExists(a, user, pw))
            return filterFiles(getFilesFromDirectory(a.uid().toString() + "/" + user + "_" + pw));
        return null;
    }

    public static Map<String, String> getAllUsersForAccount(Account a) {
        if (checkIfExists(a)) {
            Map<String, String> result = new HashMap<>();
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

    private static String getUserPath(Account a, String user, String pw) {
        return a.uid().toString() + "/" + user + "_" + pw;
    }

    private static List<File> getFilesFromDirectory(String dir) {
        return Arrays.asList(new File(base + dir).listFiles());
    }

    public static boolean checkIfExists(Account a) {
        return new File(base + a.uid().toString()).isDirectory();
    }

    public static boolean checkIfExists(Account a, String user, String pw) {
        if (checkIfExists(a))
            return new File(base + a.uid().toString() + "/" + user + "_" + pw).isDirectory();
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

    public static void main(String[] args) {
        System.out.println(getEmailsForUser(Account.valueOf(1), "lol@lol.de", "pwd"));
        System.out.println(checkIfExists(Account.valueOf(1)));
        System.out.println(checkIfExists(Account.valueOf(1), "lol@lol.de", "pwd"));
        System.out.println(checkIfExists(Account.valueOf(12)));
        System.out.println(checkIfExists(Account.valueOf(1), "lol@lol.ded", "pwd"));
        System.out.println(getAllUsersForAccount(Account.valueOf(1)));
        System.out.println(getAllUsersForAccount(Account.valueOf(12)));
        System.out.println(getAllAccounts());
        saveAccount(Account.valueOf(5));
        saveUser(Account.valueOf(5), "lol@lol.de", "pwd");
        saveUser(Account.valueOf(5), "lol2@lol.de", "pwd");
        saveEmail(Account.valueOf(5), "lol2@lol.de", "pwd",
                Email.valueOf("abc",
                        "asdjkjkasfjkajknadsjafnjkafjkdjknsad\ndslflksdkf\nlsdfkskdaslsdlsdgvlksvsodckdcnabckafujk\n",
                        2));
        deleteEmail(Account.valueOf(5), "lol2@lol.de", "pwd", 2);
    }


}