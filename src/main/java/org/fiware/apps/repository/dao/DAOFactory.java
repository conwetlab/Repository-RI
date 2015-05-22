package org.fiware.apps.repository.dao; 

public abstract class DAOFactory {

	public static final int MONGO = 1;

	public abstract ResourceDAO getResourceDAO();
	public abstract CollectionDAO getCollectionDAO();

	public static DAOFactory getDAOFactory(
			int factory) {

		switch (factory) {
		case MONGO:
			return new MongoDAOFactory();
		default:
			return null;
		}
	}

}
