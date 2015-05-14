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

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.fiware.apps.repository.dao.MongoDAOFactory;
import org.fiware.apps.repository.dao.VirtuosoDAOFactory;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.ResourceCollection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DBCollection.class, DB.class, MongoDAOFactory.class, VirtuosoDAOFactory.class})
public class MongoCollectionDAOTest {
    
    @Mock private DB db;
    @Mock private DBCollection mongoCollection;
    @Mock private VirtuosoResourceDAO virtuosoResourceDAO;
    @Mock private DBObject dBObject;
    private MongoCollectionDAO toTest;
    
    public MongoCollectionDAOTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        db = mock(DB.class);
        mongoCollection = mock(DBCollection.class);
        virtuosoResourceDAO = mock(VirtuosoResourceDAO.class);
        dBObject = mock(DBObject.class);
        toTest = new MongoCollectionDAO(db, mongoCollection, mongoCollection, virtuosoResourceDAO);
    }
    
    @After
    public void tearDown() {
        
    }
    
    //Tests 
    @Test
    public void voidConstructorTest()
    {
    /*    PowerMockito.mockStatic(MongoDAOFactory.class);
        PowerMockito.mockStatic(DB.class);
        PowerMockito.mockStatic(VirtuosoDAOFactory.class);
        
        PowerMockito.when(MongoDAOFactory.createConnection()).thenReturn(db);
        when(db.getCollection(anyString())).thenReturn(mongoCollection);
        PowerMockito.when(VirtuosoDAOFactory.getVirtuosoResourceDAO()).thenReturn(virtuosoResourceDAO);
        
        MongoCollectionDAO voidConstructor = new MongoCollectionDAO();
        MongoCollectionDAO notVoidConstructor = new MongoCollectionDAO(db, mongoCollection, virtuosoResourceDAO);*/
    }
    
    
    @Test
    public void findCollectionTest() {
        getCollectionTest();
    }
    
    @Test
    public void findCollectionNullTest() {
        getCollectionNullTest();
    }
    
    @Test(expected = DatasourceException.class)
    public void findCollectionExceptionTest() throws DatasourceException {
        getCollectionExceptionTest();
    }
    
    @Test
    public void updateCollectionTest() {
        String id = "/id";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, true);
        List list = new LinkedList();
        boolean returned = false;
        
        list.add(dBObject);
        rulesdbObjectCollection(id, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        doNothing().when(mongoCollection).update(any(DBObject.class), any(DBObject.class), anyBoolean(), anyBoolean());
        
        try {
            returned = toTest.updateCollection(id, resourceCollection);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        assertTrue(returned);
        verify(db).requestStart();
        verify(db).requestDone();
    }
    
    @Test
    public void updateCollectionNullTest() {
        String id = "/id";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, true);
        boolean returned = true;
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        
        try {
            returned = toTest.updateCollection(id, resourceCollection);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        assertFalse(returned);
        verify(db).requestStart();
        verify(db).requestDone();
    }
    
    @Test(expected = DatasourceException.class)
    public void updateCollectionExceptionTest1() throws DatasourceException {
        String id = "/id";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, true);
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenThrow(Exception.class);
        
        toTest.updateCollection(id, resourceCollection);
    }
    
    @Test(expected = DatasourceException.class)
    public void updateCollectionExceptionTest2() throws DatasourceException {
        String id = "/id";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, false);
        List list = new LinkedList();
        
        list.add(dBObject);
        rulesdbObjectCollection(id, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        doThrow(IllegalArgumentException.class).when(mongoCollection).update(any(DBObject.class), any(DBObject.class), anyBoolean(), anyBoolean());
        
        toTest.updateCollection(id, resourceCollection);
    }
    
    @Test
    public void deleteCollectionTest() {
        String id = "/id";
        Date date = new Date();
        DBCursor dBCursor = mock(DBCursor.class);
        List list = new LinkedList();
        Boolean returned = false;
        
        DBCollection mongoResource = PowerMockito.mock(DBCollection.class);
        db = PowerMockito.mock(DB.class);
        mongoCollection = PowerMockito.mock(DBCollection.class);
        toTest = new MongoCollectionDAO(db, mongoCollection, mongoCollection, virtuosoResourceDAO);
        
        list.add(dBObject);
        rulesdbObjectCollection(id, date);
        PowerMockito.when(db.getCollection(anyString())).thenReturn(mongoResource);
        PowerMockito.when(mongoResource.find(any(DBObject.class))).thenReturn(dBCursor);
        PowerMockito.when(dBCursor.toArray()).thenReturn(list);
        when(virtuosoResourceDAO.deleteResource(eq(id+"Id"))).thenReturn(true);
        PowerMockito.doNothing().when(mongoResource).remove(dBObject);
        PowerMockito.when(mongoCollection.find(any(DBObject.class))).thenReturn(dBCursor);
        PowerMockito.doNothing().when(mongoCollection).remove(dBObject);
        PowerMockito.when(mongoCollection.findOne(any(DBObject.class))).thenReturn(dBObject);
        
        try {
            returned = toTest.deleteCollection(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        assertTrue(returned);
        verify(db, times(2)).requestStart();
        verify(db, times(2)).requestDone();
        
    }
    
    @Test
    public void deleteCollectionNullTest() {
        String id = "/id";
        Date date = new Date();
        DBCursor dBCursor = mock(DBCursor.class);
        List list = new LinkedList();
        Boolean returned = false;
        
        DBCollection mongoResource = PowerMockito.mock(DBCollection.class);
        db = PowerMockito.mock(DB.class);
        mongoCollection = PowerMockito.mock(DBCollection.class);
        toTest = new MongoCollectionDAO(db, mongoCollection, mongoCollection, virtuosoResourceDAO);
        
        list.add(dBObject);
        rulesdbObjectCollection(id, date);
        PowerMockito.when(db.getCollection(anyString())).thenReturn(mongoResource);
        PowerMockito.when(mongoResource.find(any(DBObject.class))).thenReturn(dBCursor);
        PowerMockito.when(dBCursor.toArray()).thenReturn(list);
        when(virtuosoResourceDAO.deleteResource(eq(id+"Id"))).thenReturn(true);
        PowerMockito.doNothing().when(mongoResource).remove(dBObject);
        PowerMockito.when(mongoCollection.find(any(DBObject.class))).thenReturn(dBCursor);
        PowerMockito.doNothing().when(mongoCollection).remove(dBObject);
        PowerMockito.when(mongoCollection.findOne(any(DBObject.class))).thenReturn(null);
        
        try {
            returned = toTest.deleteCollection(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        assertFalse(returned);
        verify(db, times(2)).requestStart();
        verify(db, times(2)).requestDone();
        
    }
    
    @Test(expected = DatasourceException.class)
    public void deleteCollectionExceptionTest1() throws DatasourceException {
        String id = "/id";
        Date date = new Date();
        
        db = PowerMockito.mock(DB.class);
        toTest = new MongoCollectionDAO(db, mongoCollection, mongoCollection, virtuosoResourceDAO);
        
        rulesdbObjectCollection(id, date);
        PowerMockito.when(db.getCollection(anyString())).thenThrow(Exception.class);
        
        toTest.deleteCollection(id);
        
        //fail();
    }
    
    @Test(expected = DatasourceException.class)
    public void deleteCollectionExceptionTest2() throws DatasourceException {
        String id = "/id";
        Date date = new Date();
        DBCursor dBCursor = mock(DBCursor.class);
        List list = new LinkedList();
        
        DBCollection mongoResource = PowerMockito.mock(DBCollection.class);
        db = PowerMockito.mock(DB.class);
        mongoCollection = PowerMockito.mock(DBCollection.class);
        toTest = new MongoCollectionDAO(db, mongoCollection, mongoCollection, virtuosoResourceDAO);
        
        list.add(dBObject);
        rulesdbObjectCollection(id, date);
        PowerMockito.when(db.getCollection(anyString())).thenReturn(mongoResource);
        PowerMockito.when(mongoResource.find(any(DBObject.class))).thenReturn(dBCursor);
        PowerMockito.when(dBCursor.toArray()).thenReturn(list);
        when(virtuosoResourceDAO.deleteResource(eq(id+"Id"))).thenReturn(true);
        PowerMockito.doNothing().when(mongoResource).remove(dBObject);
        PowerMockito.when(mongoCollection.find(any(DBObject.class))).thenReturn(dBCursor);
        PowerMockito.doNothing().when(mongoCollection).remove(dBObject);
        PowerMockito.when(mongoCollection.findOne(any(DBObject.class))).thenThrow(IllegalArgumentException.class);
        
        toTest.deleteCollection(id);
        
        //fail();
        
    }
    
    
    
    @Test
    public void getCollectionTest() {
        String id = "/id";
        Date date = new Date();
        List list = new LinkedList();
        ResourceCollection returned = null;
        
        list.add(dBObject);
        rulesdbObjectCollection(id, null);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        
        try {
            returned = toTest.findCollection(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        assertEquals(id+"Id", returned.getId());
        assertEquals(id+"Creator", returned.getCreator());
        verify(db, times(3)).requestStart();
        verify(db, times(3)).requestDone();
    }
    
    @Test
    public void getCollectionNullTest() {
        String id = "/id";
        Date date = new Date();
        ResourceCollection returned = null;
        
        rulesdbObjectCollection(id, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        
        try {
            returned = toTest.findCollection(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        assertNull(returned);
        verify(db).requestStart();
        verify(db).requestDone();
    }
    
    @Test(expected = DatasourceException.class)
    public void getCollectionExceptionTest() throws DatasourceException {
        String id = "/id";
        
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenThrow(Exception.class);
        
        toTest.findCollection(id);
    }
    
    @Test
    public void insertCollectionTest() {
        String id = "/id/hola";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, true);
        boolean returned = false;
        
        rulesdbObjectCollection(id, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        doNothing().when(mongoCollection).insert(any(DBObject.class));
        
        try {
            returned = toTest.insertCollection(resourceCollection);
        } catch (Exception ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        assertTrue(returned);
        verify(db, atLeast(1)).requestStart();
        verify(db, atLeast(1)).requestDone();
    }
    
    @Test(expected = DatasourceException.class)
    public void insertCollectionRecursiveExceptionTest() throws DatasourceException, SameIdException {
        String id = "/id/hola";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, false);
        boolean returned = false;
        
        rulesdbObjectCollection(id, null);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        doNothing().doThrow(Exception.class).when(mongoCollection).insert(any(DBObject.class));
        
        
        returned = toTest.insertCollection(resourceCollection);
        
        
        assertTrue(returned);
        verify(db, atLeast(1)).requestStart();
        verify(db, atLeast(1)).requestDone();
    }
    
    @Test(expected = DatasourceException.class)
    public void insertCollectionDatasourceExceptionTest() throws DatasourceException, SameIdException {
        String id = "/id/hola";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, false);
        
        rulesdbObjectCollection(id, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        doThrow(Exception.class).when(mongoCollection).insert(any(DBObject.class));
        
        toTest.insertCollection(resourceCollection);
        
    }
    
    @Test(expected = SameIdException.class)
    public void insertCollectionSameIdExceptionTest() throws DatasourceException, SameIdException {
        String id = "/id";
        Date date = new Date();
        ResourceCollection resourceCollection = generateResourceCollection(id, date, false);
        List list = new LinkedList();
        
        list.add(dBObject);
        rulesdbObjectCollection(id, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        
        toTest.insertCollection(resourceCollection);
    }
    
    @Test
    public void getCollectionsTest() {
        String id = "/id";
        Date date = new Date();
        DBCursor dBCursor = mock(DBCursor.class);
        List list = new LinkedList();
        
        mongoCollection = PowerMockito.mock(DBCollection.class);
        toTest = new MongoCollectionDAO(db, mongoCollection, mongoCollection, virtuosoResourceDAO);
        list.add(dBObject);
        
        PowerMockito.when(mongoCollection.find(any())).thenReturn(dBCursor);
        rulesdbObjectCollection(id, date);
        when(dBCursor.toArray()).thenReturn(list);
        
        try {
            toTest.getCollections(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        verify(db).requestStart();
        verify(db).requestDone();
    }
    
    @Test
    public void getCollectionsTest2() {
        String id = "/id";
        Date date = new Date();
        DBCursor dBCursor = mock(DBCursor.class);
        List list = new LinkedList();
        
        mongoCollection = PowerMockito.mock(DBCollection.class);
        toTest = new MongoCollectionDAO(db, mongoCollection, mongoCollection, virtuosoResourceDAO);
        list.add(dBObject);
        list.add(null);
        
        PowerMockito.when(mongoCollection.find(any())).thenReturn(dBCursor);
        rulesdbObjectCollection(id, null);
        when(dBCursor.toArray()).thenReturn(list);
        
        try {
            toTest.getCollections(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        verify(db).requestStart();
        verify(db).requestDone();
    }
    
    @Test(expected = DatasourceException.class)
    public void getCollectionsExceptionTest() throws DatasourceException {
        String id = "/id";
        Date date = new Date();
        
        mongoCollection = PowerMockito.mock(DBCollection.class);
        toTest = new MongoCollectionDAO(db, mongoCollection, mongoCollection, virtuosoResourceDAO);
        
        PowerMockito.when(mongoCollection.find(any())).thenThrow(Exception.class);
        rulesdbObjectCollection(id, date);
        
        toTest.getCollections(id);
        
        //fail();
    }
    
    private void rulesdbObjectCollection(String string, Date date) {
        if(date != null)
            when(this.dBObject.get("creationDate")).thenReturn(date);
        else
            when(this.dBObject.get("creationDate")).thenReturn(null);
        when(this.dBObject.get("id")).thenReturn(string + "Id");
        when(this.dBObject.get("_id")).thenReturn("507f1f77bcf86cd799439011");
        when(this.dBObject.get("creator")).thenReturn(string + "Creator");
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
