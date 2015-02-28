package shtykh.nekki.db;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by shtykh on 27/02/15.
 */

public class EntryService {

	private EntryDao dao;

	public EntryService() {
		dao = new EntryDao();
	}

	public synchronized void persist(Entry entity) {
		dao.openCurrentSessionWithTransaction();
		dao.persist(entity);
		dao.closeCurrentSessionWithTransaction();
	}
	
	public synchronized void persistAll(Collection<Entry> entries){
		dao.openCurrentSessionWithTransaction();
		for (Entry entry : entries) {
			dao.persist(entry);
		}
		dao.closeCurrentSessionWithTransaction();
	}
	

	public synchronized void update(Entry entity) {
		dao.openCurrentSessionWithTransaction();
		dao.update(entity);
		dao.closeCurrentSessionWithTransaction();
	}

	public synchronized Entry findById(UUID id) {
		dao.openCurrentSession();
		Entry entry = dao.findById(id);
		dao.closeCurrentSession();
		return entry;
	}

	public synchronized void delete(UUID id) {
		dao.openCurrentSessionWithTransaction();
		Entry book = dao.findById(id);
		dao.delete(book);
		dao.closeCurrentSessionWithTransaction();
	}

	public synchronized List<Entry> findAll() {
		dao.openCurrentSession();
		List<Entry> books = dao.findAll();
		dao.closeCurrentSession();
		return books;
	}

	public synchronized void deleteAll() {
		dao.openCurrentSessionWithTransaction();
		dao.deleteAll();
		dao.closeCurrentSessionWithTransaction();
	}

	public EntryDao getDao() {
		return dao;
	}
}