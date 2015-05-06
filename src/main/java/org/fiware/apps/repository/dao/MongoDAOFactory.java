package org.fiware.apps.repository.dao;

import java.net.UnknownHostException;

import org.fiware.apps.repository.dao.impl.MongoCollectionDAO;
import org.fiware.apps.repository.dao.impl.MongoResourceDAO;
import org.fiware.apps.repository.settings.RepositorySettings;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoDAOFactory extends DAOFactory {	
	
	static DB db = null;
	
	public ResourceDAO getResourceDAO() {
		return new MongoResourceDAO();	
	}
	
	
	public static DB createConnection() {
		if(db !=null){
			return db;
		}

		
		Mongo m;
		try {
			m = new Mongo(RepositorySettings.getProperty("mongodb.host") , 
                                Integer.parseInt(RepositorySettings.getProperty("mongodb.port")));
                        db = m.getDB(RepositorySettings.getProperty("mongodb.db"));			

		} catch (UnknownHostException e) {
			System.out.println("Unknown Host: " + e.getMessage());
			e.printStackTrace();
		} catch (MongoException e) {
			System.out.println("MongoDB Error: " + e.getMessage());

		}
		return db;		
		
	}


	@Override
	public CollectionDAO getCollectionDAO() {
		return new MongoCollectionDAO();
	}
	
	
}
