package shtykh.nekki.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.UUID;


/**
 * Created by shtykh on 27/02/15.
 */


public class EntryDao implements Dao<Entry, UUID> {

	private Session currentSession;
	private Transaction currentTransaction;

	public EntryDao() {
	}

	public Session openCurrentSession() {
		currentSession = getSessionFactory().openSession();
		return currentSession;
	}

	public Session openCurrentSessionWithTransaction() {
		currentSession = getSessionFactory().openSession();
		currentTransaction = currentSession.beginTransaction();
		return currentSession;
	}

	public void closeCurrentSession() {
		currentSession.close();
	}

	public void closeCurrentSessionWithTransaction() {
		try{
			currentTransaction.commit();
		} finally {
			currentSession.close();
		}
	}

	private static SessionFactory getSessionFactory() {
		Configuration configuration = new Configuration().configure();
		StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
				.applySettings(configuration.getProperties());
		SessionFactory sessionFactory = configuration.buildSessionFactory(builder.build());
		return sessionFactory;
	}

	public Session getCurrentSession() {
		return currentSession;
	}

	public void setCurrentSession(Session currentSession) {
		this.currentSession = currentSession;
	}

	public Transaction getCurrentTransaction() {
		return currentTransaction;
	}

	public void setCurrentTransaction(Transaction currentTransaction) {
		this.currentTransaction = currentTransaction;
	}

	public void persist(Entry entity) {
		getCurrentSession().save(entity);
	}

	public void update(Entry entity) {
		getCurrentSession().update(entity);
	}

	public Entry findById(UUID id) {
		Entry entry = (Entry) getCurrentSession().get(Entry.class, id);
		return entry;
	}

	public void delete(Entry entity) {
		getCurrentSession().delete(entity);
	}

	@SuppressWarnings("unchecked")
	public List<Entry> findAll() {
		List<Entry> entries = (List<Entry>) getCurrentSession().createQuery("from Entry").list();
		return entries;
	}

	public void deleteAll() {
		List<Entry> entityList = findAll();
		for (Entry entity : entityList) {
			delete(entity);
		}
	}
}