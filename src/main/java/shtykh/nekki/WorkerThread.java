package shtykh.nekki;

import org.w3c.dom.Document;

import java.io.File;

/**
 * Created by shtykh on 26/02/15.
 */
public class WorkerThread implements Runnable {
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
		System.out.println(Thread.currentThread().getName()+" Start. File = "+ filePath);
		processFile();
		System.out.println(Thread.currentThread().getName()+" End.");
	}

	private void processFile() {
		File file = new File(filePath);
		try {
			Document document = Parser.parse(file.getAbsolutePath());
			Entry entry = parser.toEntry(document);
			System.out.println(entry);
			moveToDirectory(file, doneDirectory);

		} catch (Exception e) {
			moveToDirectory(file, badFilesDirectory);
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
