package shtykh.nekki;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import static shtykh.nekki.Util.loadProperties;

/**
 * Created by shtykh on 28/02/15.
 */
public class FileMover {
	final static Logger log = Logger.getLogger(FileMover.class);
	private static String DONE_PATH = "files/done/";
	private static String BAD_PATH = "files/bad/";

	static {
		Properties properties;
		try {
			properties = loadProperties(FileMover.class, "nekki.properties");
			initParameters(properties);
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		new File(DONE_PATH).mkdirs();
		new File(BAD_PATH).mkdirs();
	}

	private static void initParameters(Properties properties) {
		DONE_PATH = properties.getProperty("DONE_PATH", DONE_PATH);
		BAD_PATH = properties.getProperty("BAD_PATH", BAD_PATH);
	}
	
	public static File moveFileToDirectory(File file, Destination destination) throws FileMoveException {
		String newName = destination + file.getName();
		if (!file.renameTo(new File(newName))) {
			throw new FileMoveException(file, destination);
		} else {
			return new File(newName);
		}
	}
	
	static enum Destination {
		DONE(DONE_PATH),
		BAD(BAD_PATH);
		
		private final String path;

		Destination(String path) {
			this.path = path;
		}

		@Override
		public String toString() {
			return path;
		}
	}

	static class FileMoveException extends Exception {
		public FileMoveException(File file, Destination destination) {
			super("File " + file.getAbsolutePath() + " is failed to move to " + destination + "!");
		}
	}
}
