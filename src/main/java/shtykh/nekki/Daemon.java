package shtykh.nekki;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static shtykh.nekki.Util.loadProperties;

/**
 * Created by shtykh on 24/02/15.
 */
public class Daemon {
	final static Logger log = Logger.getLogger(Daemon.class);
	private static int THREAD_POOL_SIZE = 20;
	private static String INPUT_PATH = "files/input/";
	private static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static int MAX_CONTENT_SIZE = 1024;

	static {
		Properties properties;
		try {
			properties = loadProperties(Daemon.class, "nekki.properties");
			initParameters(properties);
		} catch (IOException e) {
			log.info(e.getMessage());
		}
		new File(INPUT_PATH).mkdirs();
	}

	private static void initParameters(Properties properties) {
		INPUT_PATH = properties.getProperty("INPUT_PATH", INPUT_PATH);
		DATE_FORMAT = properties.getProperty("DATE_FORMAT", DATE_FORMAT);
		THREAD_POOL_SIZE = Integer.parseInt(properties.getProperty("THREAD_POOL_SIZE", String.valueOf(THREAD_POOL_SIZE)));
		MAX_CONTENT_SIZE = Integer.parseInt(properties.getProperty("MAX_CONTENT_SIZE", String.valueOf(MAX_CONTENT_SIZE)));
	}

	public static void main(String[] args) {
		log.info("Daemon is started");
		BasicConfigurator.configure();
		Parser parser = new Parser(DATE_FORMAT, MAX_CONTENT_SIZE);
		EntryPersister entryPersister = new EntryPersister();
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		try{
			while (true) {
				File input = new File(INPUT_PATH);
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
			System.exit(1);
		}
	}
	
}
