package ServicePackage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataTypePackage.Account;
import DataTypePackage.Email;

/**
 * Created by Allquantor on 20.04.14.
 */
public class StorageService {

    private static final String BASE = "storage/";
    private static final String EMAIL = BASE + "email/";

    public StorageService() {
    }

    private static List<String> walkin(File dir, List<String> paths) {
        String pattern = ".html";

        File listFile[] = dir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].isDirectory()) {
                    walkin(listFile[i], paths);
                } else {
                    if (listFile[i].getName().endsWith(pattern)) {
                        paths.add(listFile[i].getPath());
                    }
                }
            }
        }
        return paths;
    }

    public static void main(String[] args) {
        System.out.println(walkin(new File(EMAIL), new ArrayList<String>()));
    }

}
