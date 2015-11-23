/*
Modified BSD License
====================

Copyright (c) 2015, CoNWeTLab, Universidad Politecnica Madrid
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
* Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
* Neither the name of UPM nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL UPM BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.fiware.apps.repository.dao.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.fiware.apps.repository.dao.MongoDAOFactory;
import org.fiware.apps.repository.dao.VirtuosoDAOFactory;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({MongoCollection.class, MongoDatabase.class, MongoDAOFactory.class, VirtuosoDAOFactory.class})
public class MongoCollectionDAOTest {

    @Mock private MongoDatabase db;
    @Mock private MongoCollection mongoCollection;
    @Mock private VirtuosoResourceDAO virtuosoResourceDAO;
    @Mock private MongoDAOFactory mongoDAOFactory;
    @InjectMocks private MongoCollectionDAO toTest;

    public MongoCollectionDAOTest() {
    }

    @Before
    public void setUp() {
        toTest = new MongoCollectionDAO(db, mongoCollection, virtuosoResourceDAO);
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void updateCollectionTest() throws DatasourceException {
        String id = "/id";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, true);
        boolean returned;

        when(mongoCollection.findOneAndUpdate(isA(BasicDBObject.class), isA(BasicDBObject.class))).thenReturn(new Object());

        returned = toTest.updateCollection(id, resourceCollection);
        
        // Validate result
        assertTrue(returned);
        
        // Validate calls
        ArgumentCaptor<BasicDBObject> captorQuery = ArgumentCaptor.forClass(BasicDBObject.class);
        ArgumentCaptor<BasicDBObject> captorUpdate = ArgumentCaptor.forClass(BasicDBObject.class);
        verify(mongoCollection).findOneAndUpdate(captorQuery.capture(), captorUpdate.capture());

        assertEquals(id, captorQuery.getValue().get("id"));
        
        BasicDBObject actualUpdate = captorUpdate.getValue();
        assertEquals(resourceCollection.getId(), actualUpdate.get("id"));
        assertEquals(resourceCollection.getCreator(), actualUpdate.get("creator"));
        assertEquals(resourceCollection.getName(), actualUpdate.get("name"));
    }

    @Test
    public void updateCollectionNullTest() {
        String id = "/id";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, true);
        boolean returned = true;

        when(mongoCollection.findOneAndUpdate(any(BasicDBObject.class), any(BasicDBObject.class))).thenReturn(null);

        try {
            returned = toTest.updateCollection(id, resourceCollection);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        assertFalse(returned);
    }

    @Test(expected = DatasourceException.class)
    public void updateCollectionExceptionTest1() throws DatasourceException {
        String id = "/id";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, true);

        when(mongoCollection.findOneAndUpdate(any(BasicDBObject.class), any(BasicDBObject.class))).thenThrow(IllegalArgumentException.class);

        toTest.updateCollection(id, resourceCollection);
    }

    private void mockDeleteOne(boolean result) {
        DeleteResult delRes = mock(DeleteResult.class);
        when(delRes.wasAcknowledged()).thenReturn(result);

        when(mongoCollection.deleteOne(isA(BasicDBObject.class))).thenReturn(delRes);
    }

    private void attachMockCursor(MongoCollection collection, Date date, BasicDBObject colObject) {
        FindIterable it = mock(FindIterable.class);
        MongoCursor cursor = mock(MongoCursor.class);
        when(it.iterator()).thenReturn(cursor);

        when(cursor.hasNext()).thenReturn(true, false);
        when(cursor.next()).thenReturn(colObject);
        
        when(collection.find(isA(BasicDBObject.class))).thenReturn(it);
    }
    @Test
    public void deleteCollectionTest() throws DatasourceException {
        String id = "/id";
        Date date = new Date();
        Boolean returned = false;

        this.mockDeleteOne(true);

        // Mock Mongo Collection
        MongoCollection mongoResource = mock(MongoCollection.class);
        when(db.getCollection(isA(String.class))).thenReturn(mongoResource);

        BasicDBObject colObject = generateDBCollection("/collection", date);
        this.attachMockCursor(mongoResource, date, colObject);
        this.attachMockCursor(mongoCollection, date, colObject);

        returned = toTest.deleteCollection(id);

        assertTrue(returned);
        ArgumentCaptor<BasicDBObject> colCaptor = ArgumentCaptor.forClass(BasicDBObject.class);
        verify(mongoResource).deleteOne(colCaptor.capture());
        assertEquals(colObject, colCaptor.getValue());

        verify(virtuosoResourceDAO).deleteResource(colObject.getString("id"));
        
        ArgumentCaptor<BasicDBObject> mongoCaptor = ArgumentCaptor.forClass(BasicDBObject.class);
        verify(mongoCollection, times(2)).deleteOne(mongoCaptor.capture());
        assertEquals(id, mongoCaptor.getAllValues().get(0).getString("id"));
        assertEquals(colObject, mongoCaptor.getAllValues().get(1));
    }

    @Test
    public void deleteCollectionNotDeleted() {
        String id = "/id";
        Boolean returned = false;
        
        this.mockDeleteOne(false);
        try {
            returned = toTest.deleteCollection(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        assertFalse(returned);
    }

    @Test(expected = DatasourceException.class)
    public void deleteCollectionExceptionTest1() throws DatasourceException {
        String id = "/id";
        Date date = new Date();

        this.mockDeleteOne(true);
        when(db.getCollection(anyString())).thenThrow(Exception.class);

        toTest.deleteCollection(id);

        //fail();
    }

    @Test(expected = DatasourceException.class)
    public void deleteCollectionExceptionTest2() throws DatasourceException {
        String id = "/id";

        when(mongoCollection.deleteOne(isA(BasicDBObject.class))).thenThrow(IllegalArgumentException.class);
        toTest.deleteCollection(id);

        //fail();

    }

    private void mockFind(BasicDBObject dbObject) {
        FindIterable it = mock(FindIterable.class);
        MongoCursor cursor = mock(MongoCursor.class);

        when(it.iterator()).thenReturn(cursor);
        if (dbObject != null) {
            when(cursor.hasNext()).thenReturn(true, false);
            when(cursor.next()).thenReturn(dbObject);
        } else {
            when(cursor.hasNext()).thenReturn(false);
        }

        when(mongoCollection.find(isA(BasicDBObject.class))).thenReturn(it);
    }

    @Test
    public void getCollectionTest() throws DatasourceException {
        String id = "/id";
        Date date = new Date();
        ResourceCollection returned = null;
        BasicDBObject dBObject = generateDBCollection(id, date);
    
        this.mockFind(dBObject);
        this.mockMongoDaoFactory();
        try {
            returned = toTest.findCollection(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        ArgumentCaptor<BasicDBObject> queryCaptor = ArgumentCaptor.forClass(BasicDBObject.class);
        verify(mongoCollection, times(2)).find(queryCaptor.capture());

        assertEquals(id, queryCaptor.getAllValues().get(0).getString("id"));

        assertEquals(id+"Id", returned.getId());
        assertEquals(id+"Creator", returned.getCreator());
    }

    @Test
    public void getCollectionNullTest() {
        String id = "/id";
        Date date = new Date();
        ResourceCollection returned = null;

        this.mockFind(null);

        try {
            returned = toTest.findCollection(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        assertNull(returned);
    }

    @Test(expected = DatasourceException.class)
    public void getCollectionExceptionTest() throws DatasourceException {
        String id = "/id";
        when(mongoCollection.find(isA(BasicDBObject.class))).thenThrow(Exception.class);
        toTest.findCollection(id);
    }

    @Test
    public void insertCollectionTest() {
        String id = "/id/hola";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, true);
        boolean returned = false;

        this.mockFind(null);

        try {
            returned = toTest.insertCollection(resourceCollection);
        } catch (Exception ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        ArgumentCaptor<BasicDBObject> insertCaptor = ArgumentCaptor.forClass(BasicDBObject.class);
        verify(mongoCollection, times(2)).insertOne(insertCaptor.capture());
        List<BasicDBObject> actual = insertCaptor.getAllValues();

        // Check inserted collection
        assertEquals(resourceCollection.getId(), actual.get(0).getString("id"));
        assertEquals(resourceCollection.getCreator(), actual.get(0).getString("creator"));
        assertEquals(resourceCollection.getName(), actual.get(0).getString("name"));
        assertEquals(resourceCollection.getCreationDate(), actual.get(0).getDate("creationDate"));

        // Check previous insertions
        assertEquals("/id", actual.get(1).getString("id"));
        
        assertTrue(returned);
    }

    @Test(expected = DatasourceException.class)
    public void insertCollectionRecursiveExceptionTest() throws DatasourceException, SameIdException {
        String id = "/id/hola";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, false);

        this.mockFind(null);
        doThrow(Exception.class).when(mongoCollection).insertOne(isA(BasicDBObject.class));

        toTest.insertCollection(resourceCollection);
    }

    private void mockMongoDaoFactory() throws DatasourceException {
        MongoResourceDAO resourceDao = mock(MongoResourceDAO.class);
        List<Resource> res = new ArrayList<>();
        when(resourceDao.getResources(isA(String.class))).thenReturn(res);
        when(mongoDAOFactory.getResourceDAO()).thenReturn(resourceDao);
    }
    @Test(expected = SameIdException.class)
    public void insertCollectionSameIdExceptionTest() throws DatasourceException, SameIdException {
        String id = "/id";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, false);

        this.mockMongoDaoFactory();
        this.mockFind(this.generateDBCollection(id, date));
        

        toTest.insertCollection(resourceCollection);
    }

    @Test
    public void getCollectionsTest() {
        String id = "/id";
        Date date = new Date();
        List<ResourceCollection> actual = null;
        BasicDBObject expCol = this.generateDBCollection(id, date);
        this.mockFind(expCol);

        try {
            actual = toTest.getCollections(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        assertEquals(1, actual.size());
        assertEquals(expCol.getString("id"), actual.get(0).getId());
        assertEquals(expCol.getString("creator"), actual.get(0).getCreator());
        assertEquals(expCol.getString("name"), actual.get(0).getName());
        assertEquals(expCol.getDate("creationDate"), actual.get(0).getCreationDate());
    }

    @Test
    public void getCollectionsTest2() {
        String id = "/id";
        List<ResourceCollection> actual = null;

        this.mockFind(null);

        try {
            actual = toTest.getCollections(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        assertTrue(actual.isEmpty());
    }

    @Test
    public void getCollectionsTest3() {
        String id = "/id";
        Date date = new Date();
        List<ResourceCollection> actual = null;
        BasicDBObject expCol = this.generateDBCollection(id, date);
        expCol.put("id", null);

        this.mockFind(expCol);

        try {
            actual = toTest.getCollections(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        assertTrue(actual.isEmpty());
    }

    @Test(expected = DatasourceException.class)
    public void getCollectionsExceptionTest() throws DatasourceException {
        String id = "/id";
        doThrow(Exception.class).when(mongoCollection).find(isA(BasicDBObject.class));
        toTest.getCollections(id);

        //fail();
    }

    private BasicDBObject generateDBCollection(String string, Date date) {
        BasicDBObject dBObject = new BasicDBObject();

        dBObject.put("creationDate", date);

        dBObject.put("id", string + "Id");
        dBObject.put("name", string + "name");
        dBObject.put("_id", "507f1f77bcf86cd799439011");
        dBObject.put("creator", string + "Creator");

        return dBObject;
    }

    private ResourceCollection generateResourceCollection(String string, Date date, boolean creationDate) {
        ResourceCollection resourceCollection = new ResourceCollection();
        resourceCollection.setId(string+"Id");
        resourceCollection.setCreator(string+"Creator");
        if(creationDate)
            resourceCollection.setCreationDate(date);
        return resourceCollection;
    }

}
