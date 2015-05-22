package org.fiware.apps.repository.dao;

import java.util.List;

import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.ResourceCollection;


public interface CollectionDAO {


	public ResourceCollection findCollection(String id) throws DatasourceException;
	public Boolean insertCollection(ResourceCollection r) throws DatasourceException, SameIdException;
	public Boolean updateCollection(String id, ResourceCollection r) throws DatasourceException;
	public Boolean deleteCollection(String id) throws DatasourceException;

	public ResourceCollection getCollection(String id) throws DatasourceException;
	List<ResourceCollection> getCollections(String id) throws DatasourceException; 


}
