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
* Neither the name of the copyright holders nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL COPYRIGHT HOLDERS BE LIABLE FOR ANY
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
import com.mongodb.bulk.DeleteRequest;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;


public class MongoCollectionDAO implements CollectionDAO{

    public static final String MONGO_COLL_NAME = "ResourceCollection";
    private MongoDatabase db;

    @Autowired
    private MongoDAOFactory mongoDAOFactory;

    private MongoCollection mongoCollection;
    private VirtuosoResourceDAO virtuosoResourceDAO;

    public MongoCollectionDAO(MongoDatabase db, VirtuosoResourceDAO virtuosoResourceDAO){
        this.db = db;
        this.mongoCollection = db.getCollection(MONGO_COLL_NAME);
        this.virtuosoResourceDAO = virtuosoResourceDAO;
    }

    public MongoCollectionDAO (MongoDatabase db, MongoCollection mongoCollection, VirtuosoResourceDAO virtuosoResourceDAO) {
        this.db = db;
        this.mongoCollection = mongoCollection;
        this.virtuosoResourceDAO = virtuosoResourceDAO;
    }

    @Override
    public ResourceCollection findCollection(String id) throws DatasourceException {
        return getCollection(id);
    }

    @Override
    public Boolean updateCollection(String id, ResourceCollection r) throws DatasourceException {
        try{
            // Buid query
            BasicDBObject query = new BasicDBObject("id", id);

            // Set values to update
            BasicDBObject update = new BasicDBObject();
            update.put("creator", r.getCreator());
            update.put("id", r.getId());
            update.put("name", r.getName());

            if(r.getCreationDate()!=null){
                update.put("creationDate", r.getCreationDate());
            }else{
                update.put("creationDate", new Date());
            }

            // Update document
            Object result = mongoCollection.findOneAndUpdate(query, update);

            if (result == null) {
                return false;
            }
            return true;

        } catch (Exception e){
            throw new DatasourceException("Error updating Collection with ID " + r.getId() + " " + e.getMessage(), ResourceCollection.class );
        }
    }

    private void removeEntries(String id, MongoCollection collection, boolean hasVirtEntry) {
        BasicDBObject query = new BasicDBObject();
        Pattern p = Pattern.compile("^" + id + "/[a-zA-Z0-9_\\.\\-\\+]*");
        query.put("id", p);

        FindIterable objs = collection.find(query);
        MongoCursor it = objs.iterator();

        while(it.hasNext()){
            BasicDBObject obj = (BasicDBObject) it.next();

            if (hasVirtEntry) {
                virtuosoResourceDAO.deleteResource(obj.get("id").toString());
            }

            collection.deleteOne(obj);
        }
    }
    
    @Override
    public Boolean deleteCollection(String id) throws DatasourceException {
        boolean result = false;
        try{
            // Delete the given collection
            BasicDBObject query = new BasicDBObject("id", id);
            result = mongoCollection.deleteOne(query).wasAcknowledged();

            if (result) {
                // Delete Resources contained in the collection
                this.removeEntries(id,
                        db.getCollection(MongoResourceDAO.MONGO_COLL_NAME),
                        true);

                // Delete Collections contained in the given one
                this.removeEntries(id, mongoCollection, false);
            }
        } catch (IllegalArgumentException e){
            throw new DatasourceException(
                    "Error deleting Collection with ID " + 
                            id + " " + e.getMessage(), ResourceCollection.class);
        } catch (Exception e){
            throw new DatasourceException(e.getMessage(), ResourceCollection.class);
        }
        return result;
    }

    private ResourceCollection getResourceCollection(
            BasicDBObject obj, String id) throws DatasourceException {

        ResourceCollection r = new ResourceCollection();

        r.setId(obj.get("id").toString());
        r.setName(obj.get("name").toString());
        r.setCreator(obj.get("creator").toString());
        if(obj.get("creationDate")!=null){
            r.setCreationDate((Date) obj.get("creationDate"));
        }

        if (!id.isEmpty()) {
            r.setResources(
                this.mongoDAOFactory.
                        getResourceDAO().
                        getResources(id));

            r.setCollections(getCollections(id));
        }
        
        return r;
    }

    @Override
    public ResourceCollection getCollection(String id) throws DatasourceException{
        
        BasicDBObject obj = null;
        try{
            Pattern pat = Pattern.compile(id);
            BasicDBObject query = new BasicDBObject("id", pat);
            MongoCursor objs = mongoCollection.find(query).iterator();
            while (objs.hasNext()) {
                obj = (BasicDBObject) objs.next();
            }
        }catch (Exception e){
            throw new DatasourceException("Error parsing " + id + " " + e.getMessage(), ResourceCollection.class);
        }

        if(obj == null){
            return null;
        }
        return this.getResourceCollection(obj, id);
    }


    @Override
    public Boolean insertCollection(ResourceCollection r) throws DatasourceException, SameIdException {
        if (getCollection(r.getId()) != null){
            throw new SameIdException(r.getId(), ResourceCollection.class);
        }

        try{
            BasicDBObject obj = new BasicDBObject();
            obj.put("id", r.getId());
            obj.put("creator", r.getCreator());
            obj.put("name", r.getName());
            if(r.getCreationDate()!=null){
                obj.put("creationDate", r.getCreationDate());
            }else{
                obj.put("creationDate", new Date());
            }
            mongoCollection.insertOne(obj);
            insertCollectionRecursive(r);

            return true;
        }catch (Exception e){
            throw new DatasourceException("Error parsing " + r.getId() + " " + e.getMessage(), ResourceCollection.class);
        }

    }

    private Boolean insertCollectionRecursive(ResourceCollection r) throws DatasourceException{
        ResourceCollection res = new ResourceCollection();

        if((r.getId().contains("/")) && (getCollection(r.getId().substring(0, r.getId().lastIndexOf("/"))) == null)){
            
            String id = r.getId().substring(0, r.getId().lastIndexOf("/"));

            if (id.isEmpty()) {
                return false;
            }
            res.setId(id);

            if (res.getId().contains("/")) {
                res.setName(res.getId().substring(res.getId().lastIndexOf("/")+1));
            } else {
                res.setName(res.getId());
            }

            res.setCreator(r.getCreator());
            res.setCreationDate(r.getCreationDate());

            try{
                BasicDBObject obj = new BasicDBObject();
                obj.put("id", res.getId());
                obj.put("creator", res.getCreator());
                obj.put("name", res.getName());
                if(res.getCreationDate()!=null){
                    obj.put("creationDate", res.getCreationDate());
                }else{
                    obj.put("creationDate", new Date());
                }
                mongoCollection.insertOne(obj);
                return insertCollectionRecursive(res);
            }catch (Exception e){
                throw new DatasourceException("Error parsing " + res.getId() + " " + e.getMessage(), ResourceCollection.class);
            }
        }else{
            return true;
        }
    }


    @Override
    public List<ResourceCollection> getCollections(String path) throws DatasourceException {

        List <ResourceCollection> resourceCollections = new ArrayList<>();

        try{
            BasicDBObject query = new BasicDBObject();
            Pattern p = Pattern.compile("^" + path + "/[a-zA-Z0-9_\\.\\-\\+]*$");

            query.put("id", p);
            MongoCursor objs = mongoCollection.find(query).iterator();

            while (objs.hasNext()){
                BasicDBObject obj = (BasicDBObject) objs.next();

                if((obj!=null)&&(obj.get("id")!=null)){
                    resourceCollections.add(this.getResourceCollection(obj, ""));
                }
            }
        }
        catch (Exception e){
            throw new DatasourceException(e.getMessage(), ResourceCollection.class);
        }

        return resourceCollections;
    }

}
