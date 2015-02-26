package shtykh.nekki;

import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shtykh on 24/02/15.
 */
public class Daemon {
	final static Logger log = LoggerFactory.getLogger(Daemon.class);
	private static final int THREAD_POOL_SIZE = 20;
	private static final String INPUT_PATH = System.getProperty("user.dir") + "/input/";
	private static final String DONE_PATH = System.getProperty("user.dir") + "/done/";
	private static final String BAD_PATH = System.getProperty("user.dir") + "/bad/";

	private final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final static int DEFAULT_MAX_CONTENT_SIZE = 1024;

	static {
		new File(INPUT_PATH).mkdirs();
		new File(DONE_PATH).mkdirs();
		new File(BAD_PATH).mkdirs();
	}

	public static void main(String[] args) {
		log.info("Daemon is started");
		BasicConfigurator.configure();
		Parser parser = new Parser(DEFAULT_DATE_FORMAT, DEFAULT_MAX_CONTENT_SIZE);
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
		while (true) {
			File input = new File(INPUT_PATH);
			if (input.isDirectory()) {
				for (File file : input.listFiles()) {
					Runnable worker = new WorkerThread(file.getAbsolutePath(), DONE_PATH, BAD_PATH, parser);
					executor.execute(worker);
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.info(e.getMessage());
			}
		}
	}
}
