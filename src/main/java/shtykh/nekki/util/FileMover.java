package shtykh.nekki.util;

import org.apache.log4j.Logger;

import java.io.File;

/**
 * Created by shtykh on 28/02/15.
 */
public class FileMover {
	final static Logger log = Logger.getLogger(FileMover.class);
	
	public static File moveFileToDirectory(File file, Destination destination) throws FileMoveException {
		String newName = destination + file.getName();
		if (!file.renameTo(new File(newName))) {
			throw new FileMoveException(file, destination);
		} else {
			return new File(newName);
		}
	}
	
	public static enum Destination {
		DONE(Parameters.DONE),
		BAD(Parameters.BAD);
		
		private final String path;

		Destination(String path) {
			this.path = path;
		}

		@Override
		public String toString() {
			return path;
		}
	}

	public static class FileMoveException extends Exception {
		public FileMoveException(File file, Destination destination) {
			super("File " + file.getAbsolutePath() + " is failed to move to " + destination + "!");
		}
	}
}
