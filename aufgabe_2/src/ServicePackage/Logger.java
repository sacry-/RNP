package ServicePackage;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private PrintWriter writer;

	public Logger(String fileName) {
		try {
			writer = new PrintWriter(fileName);
		} catch (IOException e) {
			System.err.println("Logfile creation failed.");
		}
	}

	public void write(String message) {
		String currendDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());
		writer.println(currendDate + " "  +message);
		writer.flush();
	}
}