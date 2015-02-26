package shtykh.nekki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by shtykh on 26/02/15.
 */
public class WorkerThread implements Runnable {
	final static Logger log = LoggerFactory.getLogger(WorkerThread.class);
	private String filePath;
	private Parser parser;
	private String doneDirectory;
	private String badFilesDirectory;

	public WorkerThread(String filePath, String doneDirectory, String badFilesDirectory, Parser parser){
		this.badFilesDirectory = badFilesDirectory;
		this.filePath = filePath;
		this.doneDirectory = doneDirectory;
		this.parser = parser;
	}

	@Override
	public void run() {
		log.info(filePath + " Start");
		processFile();
		log.info(filePath + " End");
	}

	private void processFile() {
		File file = new File(filePath);
		try {
			Entry entry = parser.parse(file.getAbsolutePath());
			log.info(entry.toString() + " parsed");
			moveToDirectory(file, doneDirectory);
		} catch (Exception e) {
			log.error(e.getMessage());
			try {
				moveToDirectory(file, badFilesDirectory);
			} catch (Exception e1) {
				log.error(e1.getMessage());
				throw new RuntimeException(e.getMessage() + "\n" + e1.getMessage(), e);
			}
			throw new RuntimeException(e);
		}
	}

	private void moveToDirectory(File file, String directory) {
		if (!file.renameTo(new File(directory + file.getName()))) {
			throw new RuntimeException("File " + file.getAbsolutePath() + " is failed to move to " + directory + "!");
		}
	}

	@Override
	public String toString(){
		return this.filePath;
	}
}
