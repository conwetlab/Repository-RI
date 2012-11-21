package org.fiware.apps.repository.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.fiware.apps.repository.dao.CollectionDAO;
import org.fiware.apps.repository.dao.DAOFactory;
import org.fiware.apps.repository.dao.MongoDAOFactory;
import org.fiware.apps.repository.dao.ResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.ParentNotExistsException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;
import org.fiware.apps.repository.model.ResourceFilter;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.ObjectId;

public class MongoCollectionDAO implements CollectionDAO{

	public static final String MONGO_COLL_NAME = "ResourceCollection";
	private DB db;
	private DBCollection mongoCollection;
	
	public MongoCollectionDAO(){
		db = MongoDAOFactory.createConnection();
		mongoCollection = db.getCollection(MONGO_COLL_NAME);
	}
	


	@Override
	public ResourceCollection findCollection(String id) throws DatasourceException {
		ResourceCollection r = new ResourceCollection();
		db.requestStart();
		DBObject obj =null;
		try{		
			Pattern pat = Pattern.compile(id);
			BasicDBObject query = new BasicDBObject("id", pat);			
			obj = mongoCollection.findOne(query);	

		}catch (Exception e){
			System.out.println(e.getMessage());
			db.requestDone();	
			throw new DatasourceException("Error parsing " + r.getId() + " " + e.getMessage(), ResourceCollection.class );		
		}

		if(obj == null){				
			return null;	
		}
		r.setId(obj.get("id").toString());
		r.setCreator(obj.get("creator").toString());	
		if(obj.get("creationDate")!=null){
			r.setCreationDate((Date) obj.get("creationDate"));
		}
		
		db.requestDone();
		return r;

	}

	@Override
	public Boolean updateCollection(String id, ResourceCollection r) throws DatasourceException {	
		db.requestStart();	
	
		Pattern pat = Pattern.compile(id);
		BasicDBObject query = new BasicDBObject("id", pat);			
		DBObject obj = mongoCollection.findOne(query);		

		if(obj==null){
			db.requestDone();
			return false;		
		}

		obj.put("creator", r.getCreator());
		obj.put("id", r.getId());
		if(r.getCreationDate()!=null){
			obj.put("creationDate", r.getCreationDate()); 
		}else{
			obj.put("creationDate", new Date()); 
		}
		String internalId = obj.get("_id").toString();

		try{
			mongoCollection.update(new BasicDBObject().append("_id", new ObjectId(internalId)), obj, false,false);
			db.requestDone();
			return true;

		}catch (IllegalArgumentException e){
			db.requestDone();	
			throw new DatasourceException("Error updating Collection with ID " + r.getId() + " " + e.getMessage(), ResourceCollection.class );	
		}


	}

	@Override
	public Boolean deleteCollection(String id) throws DatasourceException {		
		
		db.requestStart();	
		
		try{
			//delete Resources
			DBCollection mongoResource = db.getCollection(MongoResourceDAO.MONGO_COLL_NAME);			
			BasicDBObject query = new BasicDBObject();	
			Pattern p = Pattern.compile("^"+id+"/[a-zA-Z0-9_\\.\\-\\+]*");			
			query.put("id", p);			
			List <DBObject> objs = mongoResource.find(query).toArray();
			
			for(DBObject obj : objs){
				mongoResource.remove(obj);
			}			
			
			//delete Collections
			BasicDBObject queryC = new BasicDBObject();	
			Pattern pC = Pattern.compile("^"+id+"/[a-zA-Z0-9_\\.\\-\\+]*");			
			queryC.put("id", pC);			
			List <DBObject> objsC = mongoCollection.find(queryC).toArray();
			
			for(DBObject obj : objsC){
				mongoCollection.remove(obj);
			}	
		}
		catch (Exception e){
			db.requestDone();
			e.printStackTrace();
			throw new DatasourceException(e.getMessage(), ResourceCollection.class );	
		}		
		
		db.requestDone();		
		
		try{
			db.requestStart();		
			Pattern pat = Pattern.compile(id);
			BasicDBObject query = new BasicDBObject("id", pat);			
			DBObject obj = mongoCollection.findOne(query);	
			if(obj==null){
				db.requestDone();
				return false;		
			}
			mongoCollection.remove(obj);
			db.requestDone();
			return true;

		}catch (IllegalArgumentException e){
			db.requestDone();	
			throw new DatasourceException("Error deleting Collection with ID " + id + " " + e.getMessage(), ResourceCollection.class );	
		}
	}

		
	public ResourceCollection getCollection(String id) throws DatasourceException{
	
		ResourceCollection r = new ResourceCollection();
		db.requestStart();
		DBObject obj =null;
		try{			
			
			Pattern pat = Pattern.compile(id);
			BasicDBObject query = new BasicDBObject("id", id);			
			//System.out.println("id "+id +" " +query.toString());
			
			obj = mongoCollection.findOne(query);	

		}catch (Exception e){
			System.out.println(e.getMessage());
			db.requestDone();	
			throw new DatasourceException("Error parsing " + id + " " + e.getMessage(), ResourceCollection.class );		
		}

		if(obj == null){			
			return null;	
		}

		MongoResourceDAO resourceDAO = new MongoResourceDAO();
		r.setId(obj.get("id").toString());
		r.setCreator(obj.get("creator").toString());	
		if(obj.get("creationDate")!=null){
			r.setCreationDate((Date) obj.get("creationDate"));
		}
		r.setResources(resourceDAO.getResources(id));
		r.setCollections(getCollections(id));		
		db.requestDone();
		return r;	
	}
	
	
	@Override
	public Boolean insertCollection(ResourceCollection r) throws DatasourceException, SameIdException {
		
		if (getCollection(r.getId()) != null){
			throw new SameIdException(r.getId(), ResourceCollection.class);
		}	
		
		try{
			db.requestStart();		
			BasicDBObject obj = new BasicDBObject();
			obj.put("id", r.getId());
			obj.put("creator", r.getCreator());		
			if(r.getCreationDate()!=null){
				obj.put("creationDate", r.getCreationDate()); 
			}else{
				obj.put("creationDate", new Date()); 
			}
			mongoCollection.insert(obj);		
			insertCollectionRecursive(r);			
			
			db.requestDone();
			return true;
		}catch (Exception e){			
			db.requestDone();			
			throw new DatasourceException("Error parsing " + r.getId() + " " + e.getMessage(), ResourceCollection.class );		
		}
		
	}
	
	private Boolean insertCollectionRecursive(ResourceCollection r) throws DatasourceException{		
	
		if((r.getId().contains("/"))&&(getCollection(r.getId().substring(0, r.getId().lastIndexOf("/"))) == null)){
			r.setId(r.getId().substring(0, r.getId().lastIndexOf("/")));	
			try{
				db.requestStart();		
				BasicDBObject obj = new BasicDBObject();
				obj.put("id", r.getId());
				obj.put("creator", r.getCreator());	
				if(r.getCreationDate()!=null){
					obj.put("creationDate", r.getCreationDate()); 
				}else{
					obj.put("creationDate", new Date()); 
				}
				mongoCollection.insert(obj);				
				db.requestDone();
				return insertCollectionRecursive(r);
			}catch (Exception e){			
				db.requestDone();			
				throw new DatasourceException("Error parsing " + r.getId() + " " + e.getMessage(), ResourceCollection.class );		
			}			
		}else{
			return true;
		}
	}

	
	@Override
	public List<ResourceCollection> getCollections(String path) throws DatasourceException {
		
		List <ResourceCollection> resourceCollections = new ArrayList<ResourceCollection>();
		db.requestStart();
	
		
		try{
			BasicDBObject query = new BasicDBObject();	
			Pattern p = Pattern.compile("^"+path+"/[a-zA-Z0-9_\\.\\-\\+]*$");	
			
			query.put("id", p);

			List <DBObject> objs = mongoCollection.find(query).toArray();

			
			for(DBObject obj : objs){
				if((obj!=null)&&(obj.get("id")!=null)){
					ResourceCollection rcol = new ResourceCollection();			
					rcol.setId(obj.get("id").toString());
					rcol.setCreator(obj.get("creator").toString());
					if(obj.get("creationDate")!=null){
						rcol.setCreationDate((Date) obj.get("creationDate"));
					}
					resourceCollections.add(rcol);
				}
			}	
		}
		catch (Exception e){
			db.requestDone();
			e.printStackTrace();
			throw new DatasourceException(e.getMessage(), ResourceCollection.class );	
		}
		db.requestDone();
		return resourceCollections;
	}

}
