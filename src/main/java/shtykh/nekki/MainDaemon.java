package shtykh.nekki;

import org.apache.log4j.Logger;
import shtykh.nekki.db.EntryPersister;
import shtykh.nekki.parse.Parser;
import shtykh.nekki.parse.ParserThread;
import shtykh.nekki.util.Parameters;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static shtykh.nekki.util.Parameters.*;

/**
 * Created by shtykh on 24/02/15.
 */
public class MainDaemon {
	final static Logger log = Logger.getLogger(MainDaemon.class);
	
	public static void main(String[] args) {
		Parameters.init();
		log.info("Daemon is started");
		Parser parser = new Parser(DATE_FORMAT, MAX_CONTENT_SIZE);
		EntryPersister entryPersister = new EntryPersister();
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		entryPersister.deleteAll();
		try{
			File input = new File(INPUT);
			while (true) {
				if (input.isDirectory()) {
					File[] filesInInput = input.listFiles();
					for (File file : filesInInput != null ? filesInInput : new File[0]) {
						Runnable worker = new ParserThread(file, parser, entryPersister);
						executor.execute(worker);
					}
				}
				entryPersister.persistAll();
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			log.info("Daemon is stopped");
			System.exit(1);
		}
	}
	
}
