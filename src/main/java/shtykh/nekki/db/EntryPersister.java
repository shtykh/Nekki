package shtykh.nekki.db;

import org.apache.log4j.Logger;
import shtykh.nekki.util.FileMover;
import shtykh.nekki.util.Receiver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

import static shtykh.nekki.util.FileMover.Destination.BAD;
import static shtykh.nekki.util.FileMover.moveFileToDirectory;

/**
 * Created by shtykh on 28/02/15.
 */
public class EntryPersister implements Receiver<Entry> {
	Logger log = Logger.getLogger(EntryPersister.class);
	private Collection<Entry> entries = new ConcurrentLinkedQueue<>();
	private EntryService entryService = new EntryService();

	@Override
	public void receive(Entry msg, Object sender) {
		entries.add(msg);
		log.info(msg + " is received from " + sender);
	}
	
	public void persistAll() {
		if (entries.isEmpty()) {
			return;
		}
		Collection<Entry> entriesCopy = new ArrayList<>(entries);
		try {
			entryService.persistAll(entriesCopy);
			entries.removeAll(entriesCopy);
			log.info(entriesCopy.size() + " entities have been persisted : ");
			log.info(entriesCopy.toString());
		} catch (Exception e) {
			try {
				for (Entry entry : entriesCopy) {
					moveFileToDirectory(entry.getFile(), BAD);
				}
			} catch (FileMover.FileMoveException fileMoveEx) {
				throw new RuntimeException(e.getMessage() + "\n" + fileMoveEx.getMessage(), e);
			}
			throw new RuntimeException(e);
		}
	}

	public void deleteAll() {
		log.info("Delete all entries");
		entryService.deleteAll();
	}
}
