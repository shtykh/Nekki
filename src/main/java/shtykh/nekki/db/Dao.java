package shtykh.nekki.db;


import org.hibernate.Session;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shtykh on 27/02/15.
 */
public interface Dao<EntryType, IdType extends Serializable> {

	void persist(EntryType entity);

	void update(EntryType entity);

	EntryType findById(IdType id);

	void delete(EntryType entity);

	List<EntryType> findAll();

	void deleteAll();

	Session openCurrentSessionWithTransaction();

	void closeCurrentSessionWithTransaction();

	Session openCurrentSession();

	void closeCurrentSession();
}
