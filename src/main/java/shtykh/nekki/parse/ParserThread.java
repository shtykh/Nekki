package shtykh.nekki.parse;

import org.apache.log4j.Logger;
import shtykh.nekki.util.FileMover;
import shtykh.nekki.util.Receiver;
import shtykh.nekki.db.Entry;

import java.io.File;

import static shtykh.nekki.util.FileMover.Destination.BAD;
import static shtykh.nekki.util.FileMover.Destination.DONE;
import static shtykh.nekki.util.FileMover.moveFileToDirectory;

/**
 * Created by shtykh on 26/02/15.
 */
public class ParserThread implements Runnable {
	final static Logger log = Logger.getLogger(ParserThread.class);
	private File file;
	private Parser parser;
	private final Receiver<Entry> receiver;

	public ParserThread(File file,
						Parser parser,
						Receiver<Entry> receiver) throws FileMover.FileMoveException {
		this.parser = parser;
		this.receiver = receiver;
		this.file = moveFileToDirectory(file, DONE);
	}

	@Override
	public void run() {
		log.info(file + " Start");
		try {
			Entry entry = parser.parse(file);
			entry.setFile(file);
			log.info(entry.toString() + " parsed");
			receiver.receive(entry, this);
		} catch (Exception e) {
			try {
				file = moveFileToDirectory(file, BAD);
			} catch (Exception e1) {
				log.info(file + " Ends with error");
				throw new RuntimeException(e.getMessage() + "\n" + e1.getMessage(), e);
			}
			log.info(file + " Ends with error");
			throw new RuntimeException(e);
		}
		log.info(file + " End");
	}

	@Override
	public String toString(){
		return "Thread_" + this.file;
	}
}
