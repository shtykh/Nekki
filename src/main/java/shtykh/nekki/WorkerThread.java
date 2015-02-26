package shtykh.nekki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

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
		log.info(Thread.currentThread().getName()+" Start. File = "+ filePath);
		processFile();
		log.info(Thread.currentThread().getName()+" End.");
	}

	private void processFile() {
		File file = new File(filePath);
		try {
			Document document = Parser.parse(file.getAbsolutePath());
			Entry entry = parser.toEntry(document);
			log.info(entry.toString());
			moveToDirectory(file, doneDirectory);

		} catch (Exception e) {
			moveToDirectory(file, badFilesDirectory);
			log.error(e.getMessage());
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
