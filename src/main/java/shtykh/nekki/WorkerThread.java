package shtykh.nekki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Created by shtykh on 26/02/15.
 */
public class WorkerThread implements Runnable {
	final static Logger log = LoggerFactory.getLogger(WorkerThread.class);
	private File file;
	private Parser parser;
	private String doneDirectory;
	private String badFilesDirectory;

	public WorkerThread(File file, 
						String doneDirectory, 
						String badFilesDirectory, 
						Parser parser){
		this.file = file;
		this.doneDirectory = doneDirectory;
		this.badFilesDirectory = badFilesDirectory;
		this.parser = parser;
	}

	@Override
	public void run() {
		log.info(file + " Start");
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
		log.info(file + " End");
	}

	private void moveToDirectory(File file, String directory) {
		if (!file.renameTo(new File(directory + file.getName()))) {
			throw new RuntimeException("File " + file.getAbsolutePath() + " is failed to move to " + directory + "!");
		}
	}

	@Override
	public String toString(){
		return "Thread_" + this.file;
	}
}
