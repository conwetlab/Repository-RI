/*
Modified BSD License
====================

Copyright (c) 2012, SAP AG
Copyright (c) 2015, CoNWeTLab, Universidad Politecnica Madrid
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
* Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
* Neither the name of the SAP AG nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL SAP AG BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.fiware.apps.repository.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.fiware.apps.repository.dao.CollectionDAO;
import org.fiware.apps.repository.dao.MongoDAOFactory;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.ResourceCollection;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.ObjectId;
import java.util.Objects;
import org.fiware.apps.repository.dao.DAOFactory;
import org.fiware.apps.repository.dao.VirtModelFactory;
import org.fiware.apps.repository.dao.VirtuosoQueryExecutionFactory;
import org.fiware.apps.repository.settings.RepositorySettings;
import virtuoso.jena.driver.VirtGraph;

public class MongoCollectionDAO implements CollectionDAO{
    
    public static final String MONGO_COLL_NAME = "ResourceCollection";
    private DB db;
    private DBCollection mongoCollection;
    private VirtuosoResourceDAO virtuosoResourceDAO;
    
    public MongoCollectionDAO(){
        db = MongoDAOFactory.createConnection();
        mongoCollection = db.getCollection(MONGO_COLL_NAME);
        virtuosoResourceDAO = new VirtuosoResourceDAO();
    }
    
    public MongoCollectionDAO(DB dbIn, DBCollection dBCollectionIn, VirtuosoResourceDAO virtuosoResourceDAOIn) {
        this.db = Objects.requireNonNull(dbIn);
        this.mongoCollection = Objects.requireNonNull(dBCollectionIn);
        this.virtuosoResourceDAO = Objects.requireNonNull(virtuosoResourceDAOIn);
    }
    
    
    
    @Override
    public ResourceCollection findCollection(String id) throws DatasourceException {
        return getCollection(id);
    }
    
    @Override
    public Boolean updateCollection(String id, ResourceCollection r) throws DatasourceException {
        db.requestStart();
        try{
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
            
            
            mongoCollection.update(new BasicDBObject().append("_id", new ObjectId(internalId)), obj, false,false);
            db.requestDone();
            return true;
            
        }catch (Exception e){
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
                virtuosoResourceDAO.deleteResource(obj.get("id").toString());
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
            e.printStackTrace();
            db.requestDone();
            throw new DatasourceException("Error deleting Collection with ID " + id + " " + e.getMessage(), ResourceCollection.class );
        }
    }
    
    @Override
    public ResourceCollection getCollection(String id) throws DatasourceException{
        ResourceCollection r = new ResourceCollection();
        db.requestStart();
        DBObject obj =null;
        MongoResourceDAO resourceDAO = new MongoResourceDAO(db, mongoCollection, new MongoDAOFactory(), this);
        try{
            Pattern pat = Pattern.compile(id);
            BasicDBObject query = new BasicDBObject("id", id);
            obj = mongoCollection.findOne(query);
        }catch (Exception e){
            db.requestDone();
            throw new DatasourceException("Error parsing " + id + " " + e.getMessage(), ResourceCollection.class );
        }
        
        if(obj == null){
            db.requestDone();
            return null;
        }
        
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
