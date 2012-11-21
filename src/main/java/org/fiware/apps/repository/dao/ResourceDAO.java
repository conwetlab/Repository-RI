package org.fiware.apps.repository.dao;

import java.util.List;

import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.ParentNotExistsException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceFilter;


public interface ResourceDAO {

	public Resource insertResource(Resource r)throws DatasourceException, SameIdException;

	public List<Resource> getResources(String id, ResourceFilter filter) throws DatasourceException;
	public List<Resource> getResources(String id) throws DatasourceException;
	
	public Boolean isResource (String id) throws DatasourceException;
	public Resource getResource(String id)  throws DatasourceException;
	public Resource findResource(String id) throws DatasourceException;
	public Boolean updateResourceContent(Resource r) throws DatasourceException;
	public Resource getResourceContent(String id) throws DatasourceException;
	public Boolean updateResource(String path, Resource r) throws DatasourceException;
	public Boolean deleteResource(String id) throws DatasourceException ;	
	
	
	
}
