/*
Modified BSD License
====================

Copyright (c) 2012, SAP AG
Copyright (c) 2015, CoNWeT Lab., Universidad Politecnica Madrid
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of the copyright holders, nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS BE LIABLE FOR ANY
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
import org.fiware.apps.repository.dao.ResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;
import org.fiware.apps.repository.model.ResourceFilter;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;

public class MongoResourceDAO implements ResourceDAO{

    public static final String MONGO_COLL_NAME = "Resource";

    private MongoDatabase db;
    private MongoCollection mongoCollection;
    private CollectionDAO collectionDAO;

    @Autowired
    private MongoDAOFactory mongoDAOFactory;

    public MongoResourceDAO(MongoDatabase db){
        this.db = db;
        this.mongoCollection = db.getCollection(MONGO_COLL_NAME);
        this.collectionDAO = mongoDAOFactory.getCollectionDAO();
    }

    public MongoResourceDAO(
            MongoDatabase db, MongoCollection mongoCollection, CollectionDAO collectionDao) {
        this.db = db;
        this.mongoCollection = mongoCollection;
        this.collectionDAO = collectionDao;
    }

    private Resource buildResource(BasicDBObject obj, boolean hasContent) {
        Resource res = new Resource();
        res.setId(obj.get("id").toString());
        res.setName(obj.get("name").toString());
        res.setCreator(obj.get("creator").toString());
        res.setContentUrl(obj.get("contentUrl").toString());
        res.setContentMimeType(obj.get("contentMimeType").toString());
        res.setContentFileName(obj.get("contentFileName").toString());

        if (hasContent) {
            res.setContent((byte[]) obj.get("content"));
        }

        if(obj.get("creationDate")!=null){
            res.setCreationDate((Date) obj.get("creationDate"));
        }
        if(obj.get("modificationDate")!=null){
            res.setModificationDate((Date) obj.get("modificationDate"));
        }
        return res;
    }

    @Override
    public List<Resource> getResources(String id) throws DatasourceException {
        return getResources (id,  new ResourceFilter (0, 0, ""));

    }

    @Override
    public List<Resource> getResources(String path, ResourceFilter filter) throws DatasourceException {
        List <Resource> resources = new ArrayList<>();

        try{

            BasicDBObject query = filter.parseFilter();
            Pattern p = Pattern.compile("^" + path + "/[a-zA-Z0-9_\\.\\-\\+]*$");

            query.put("id", p);

            MongoCursor objs = mongoCollection.
                    find(query).
                    skip(filter.getOffset()).
                    limit(filter.getLimit()).
                    iterator();

            while (objs.hasNext()) {
                BasicDBObject obj = (BasicDBObject) objs.next();
                resources.add(this.buildResource(obj, false));
            }
        }
        catch (Exception e){
            throw new DatasourceException(e.getMessage(), Resource.class );
        }
        return resources;
    }

    private MongoCursor searchResource(String field, String value) {
        BasicDBObject query = new BasicDBObject();
        query.put(field, value);
        return mongoCollection.find(query).iterator();
    }

    private BasicDBObject getResourceInfo(String id) throws DatasourceException {
        BasicDBObject obj = null;
        try{

            MongoCursor objs = this.searchResource("id", id);

            while (objs.hasNext()) {
                obj = (BasicDBObject) objs.next();
            }
        }catch (Exception e){
            throw new DatasourceException("Error parsing " + id + " " + e.getMessage(), Resource.class );
        }

        return obj;
    }

    @Override
    public Resource getResource(String id) throws DatasourceException {
        Resource res = null;
        BasicDBObject obj = getResourceInfo(id);
        if (obj != null) {
            res = this.buildResource(obj, false);
        }
        return res;
    }

    @Override
    public Resource getResourceContent(String id) throws DatasourceException {
        Resource res = null;
        BasicDBObject obj = getResourceInfo(id);
        if (obj != null) {
            res = this.buildResource(obj, true);
        }
        return res;
    }

    @Override
    public Boolean isResource (String id) throws DatasourceException {
        try{
            return this.searchResource("id", id).hasNext();
        }catch (Exception e){
            throw new DatasourceException("Error parsing " + id + " " + e.getMessage(), Resource.class );
        }

    }

    @Override
    public Boolean isResourceByContentUrl (String contentUrl) throws DatasourceException{
        try{
            return this.searchResource("contentUrl", contentUrl).hasNext();
        }catch (Exception e){
            throw new DatasourceException("Error parsing " + contentUrl + " " + e.getMessage(), Resource.class );
        }
    }

    private void fillDBObjectContent(BasicDBObject obj, Resource r, boolean withContent) {
        if (withContent) {
            obj.put("content", r.getContent());
        }
        obj.put("contentMimeType", r.getContentMimeType());
        obj.put("contentFileName", r.getContentFileName());

        if(r.getCreationDate()!=null){
            obj.put("creationDate", r.getCreationDate());
        }else{
            obj.put("creationDate", new Date());
        }
        obj.put("modificationDate", new Date());
    }

    private void fillBDObjectInfo(BasicDBObject obj, Resource r) {
        obj.put("id", r.getId());
        obj.put("name", r.getName());
        obj.put("creator", r.getCreator());
        obj.put("contentUrl", r.getContentUrl());
        this.fillDBObjectContent(obj, r, false);
    }

    @Override
    public Resource insertResource(Resource r)throws DatasourceException, SameIdException{
        //Check if resource exist.
        if (isResource(r.getId())){
            throw new SameIdException(r.getId(), Resource.class);
        }

        //Insert collections if they do not exist.
        if(collectionDAO.getCollection(r.getId().substring(0, r.getId().lastIndexOf("/"))) == null){
            ResourceCollection col = new ResourceCollection();
            col.setCreator(r.getCreator());
            col.setId(r.getId().substring(0, r.getId().lastIndexOf("/")));
            collectionDAO.insertCollection(col);
        }

        try{
            BasicDBObject obj = new BasicDBObject();
            this.fillBDObjectInfo(obj, r);
            
            mongoCollection.insertOne(obj);
            return r;
        }catch (Exception e){
            throw new DatasourceException("Error parsing " + r.getId() + " " + e.getMessage(), Resource.class );
        }

    }

    @Override
    public Resource findResource(String id) throws DatasourceException {
        return getResource(id);
    }

    @Override
    public Boolean updateResource(String path, Resource r) throws DatasourceException {

        BasicDBObject oldObj = this.getResourceInfo(path);
        BasicDBObject newObj = this.getResourceInfo(path);

        if(oldObj == null){
            return false;
        }

        if(collectionDAO.getCollection(r.getId().substring(0, r.getId().lastIndexOf("/"))) == null){
            ResourceCollection col = new ResourceCollection();
            col.setCreator(r.getCreator());
            col.setId(r.getId().substring(0, r.getId().lastIndexOf("/")));
            try {
                collectionDAO.insertCollection(col);
            } catch (SameIdException ex) {
                throw new DatasourceException(ex.getLocalizedMessage(), ResourceCollection.class);
            }
        }

        this.fillBDObjectInfo(newObj, r);

        try{
            mongoCollection.updateOne(oldObj, newObj);
            return true;

        }catch (IllegalArgumentException e){
            throw new DatasourceException("Error updating Resource with ID " + r.getId() + " " + e.getMessage(), Resource.class );
        }
    }

    @Override
    public Boolean updateResourceContent(Resource r) throws DatasourceException{
        try{
            BasicDBObject oldObj = this.getResourceInfo(r.getId());
            BasicDBObject newObj = this.getResourceInfo(r.getId());

            this.fillDBObjectContent(newObj, r, true);
            this.mongoCollection.updateOne(oldObj, newObj);
        }catch (IllegalArgumentException e){
            throw new DatasourceException("Error updating Resource with ID " + r.getId() + " " + e.getMessage(), Resource.class );
        }

        return true;
    }

    @Override
    public Boolean deleteResource(String id) throws DatasourceException {

        boolean result = false;
        try{
            BasicDBObject db = this.getResourceInfo(id);
            if (db != null) {
                result = mongoCollection.deleteOne(db).wasAcknowledged();
            }
        } catch (IllegalArgumentException e){
            throw new DatasourceException("Error deleting Collection with ID " + id + " " + e.getMessage(), Resource.class );
        }
        return result;
    }
}
