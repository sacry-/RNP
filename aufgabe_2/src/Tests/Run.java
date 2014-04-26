package Tests;

import java.io.File;
import java.io.IOException;

public class Run {
	
	public static void main(String[] args) {
		System.out.println("Connect to Server...");
		System.out.println("Send Emails...");
		System.out.println("Get Emails...");
		System.out.println("Disconnect from Server...");
		
	  String dir="user.dir"; // set to current directory
	  File b = null;
	  try {
		  dir=new File(System.getProperty(dir)).getCanonicalPath();
		  b = new File("C:\\Users\\Swaneet\\github\\RNP\\aufgabe_2\\src\\ServicePackage\\storage");
	  }
	  catch (IOException e1) { /*handler required but null */ }
	  System.out.println ("Current dir : " + dir);
	  System.out.println ("dir as File: " + (new File(dir)));
	  // System.out.println ("dir as File: " + b.list().length);
	}
	
}
