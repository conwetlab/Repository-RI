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
* Neither the name of the UPM nor the
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

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.fiware.apps.repository.dao.CollectionDAO;
import org.fiware.apps.repository.dao.DAOFactory;
import org.fiware.apps.repository.dao.impl.MongoResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;
import org.fiware.apps.repository.model.ResourceFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(DBCollection.class)
public class MongoResourceDAOTest {

    @Mock private DB db;
    @Mock private DBCollection mongoCollection;
    @Mock private DBObject dBObject;
    @Mock private DAOFactory mongoFactory;
    @Mock private CollectionDAO collectionDAO;
    MongoResourceDAO toTest;

    public MongoResourceDAOTest() {
    }

    @Before
    public void setUp() {

        db = mock(DB.class);
        mongoCollection = mock(DBCollection.class);
        dBObject = mock(DBObject.class);
        mongoFactory = mock(DAOFactory.class);
        collectionDAO = mock(CollectionDAO.class);

        toTest = new MongoResourceDAO(db, mongoCollection, mongoFactory, collectionDAO);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void getResourcesTest() throws Exception {
        String string = "/string";
        Date date = new Date();
        ResourceFilter resourceFilter = new ResourceFilter(0, 0, "");
        List list = new LinkedList();
        DBCursor dBCursor = mock(DBCursor.class);

        mongoCollection = PowerMockito.mock(DBCollection.class);
        toTest = new MongoResourceDAO(db, mongoCollection, mongoFactory, collectionDAO);
        list.add(dBObject);
        list.add(dBObject);

        rulesdbObject(string, date);
        PowerMockito.when(mongoCollection.find(any(DBObject.class))).thenReturn(dBCursor);
        when(dBCursor.skip(eq(0))).thenReturn(dBCursor);
        when(dBCursor.limit(eq(0))).thenReturn(dBCursor);
        when(dBCursor.toArray()).thenReturn(list);

        try {
            toTest.getResources(string, resourceFilter);
        } catch (DatasourceException ex) {
            fail(ex.getLocalizedMessage());
        }

        verify(dBCursor).skip(eq(0));
        verify(dBCursor).limit(eq(0));
        verify(dBCursor).toArray();
        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test
    public void getResources2Test() {
        String string = "/string";
        Date date = new Date();
        List list = new LinkedList();
        DBCursor dBCursor = mock(DBCursor.class);

        mongoCollection = PowerMockito.mock(DBCollection.class);
        toTest = new MongoResourceDAO(db, mongoCollection, mongoFactory, collectionDAO);
        list.add(dBObject);
        list.add(dBObject);

        rulesdbObject(string, null);
        PowerMockito.when(mongoCollection.find(any(DBObject.class))).thenReturn(dBCursor);
        when(dBCursor.skip(eq(0))).thenReturn(dBCursor);
        when(dBCursor.limit(eq(0))).thenReturn(dBCursor);
        when(dBCursor.toArray()).thenReturn(list);

        try {
            toTest.getResources(string);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(dBCursor).skip(eq(0));
        verify(dBCursor).limit(eq(0));
        verify(dBCursor).toArray();
        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test(expected = DatasourceException.class)
    public void getResourcesExceptionTest() throws DatasourceException {
        String string = "/string";
        ResourceFilter resourceFilter = new ResourceFilter(0, 0, "");

        mongoCollection = PowerMockito.mock(DBCollection.class);
        toTest = new MongoResourceDAO(db, mongoCollection, mongoFactory, collectionDAO);
        PowerMockito.when(mongoCollection.find(any(DBObject.class))).thenThrow(Exception.class);

        toTest.getResources(string, resourceFilter);

        //fail();
    }

    @Test
    public void getResourceTest1() {
        String string = "string";
        Date date = new Date();
        List list = new LinkedList();
        list.add(dBObject);

        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());

        try {
            toTest.getResource("id");
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test
    public void getResourceTest2() {
        String string = "string";
        Date date = new Date();
        List list = new LinkedList();
        list.add(dBObject);

        rulesdbObject(string, null);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());

        try {
            toTest.getResource("id");
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test
    public void getResourceNullTest() {
        String string = "string";
        Date date = new Date();

        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);


        try {
            toTest.getResource("id");
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test(expected = DatasourceException.class)
    public void getResourceExceptionTest() throws DatasourceException{
        String string = "string";
        Date date = new Date();

        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenThrow(DatasourceException.class);

        toTest.getResource("id");

        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test
    public void getResourceContentTest1() {
        Date date = new Date();
        String string = "String";
        List list = new LinkedList();
        list.add(dBObject);

        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());

        try {
            toTest.getResourceContent("id");
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(dBObject, times(11)).get(anyString());
        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test
    public void getResourceContentTest2() {
        Date date = new Date();
        String string = "String";
        List list = new LinkedList();
        list.add(dBObject);

        rulesdbObject(string, null);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());

        try {
            toTest.getResourceContent("id");
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(dBObject, times(9)).get(anyString());
        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test
    public void getResourceContentNullTest() {
        Date date = new Date();
        String string = "String";

        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);

        try {
            toTest.getResourceContent("id");
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test(expected = DatasourceException.class)
    public void getResourceContentExceptionTest() throws DatasourceException {
        Date date = new Date();
        String string = "String";

        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenThrow(Exception.class);

        toTest.getResourceContent("id");

        //fail();
    }

    @Test
    public void isResourceTest1() {
        String id = "id";
        List list = new LinkedList();
        Boolean expected = false;

        list.add(dBObject);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());

        try {
            expected = toTest.isResource(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db).requestStart();
        verify(db).requestDone();
        assertTrue(expected);
    }

    @Test
    public void isResourceTest2() {
        String id = "id";
        Boolean expected = false;

        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);

        try {
            expected = toTest.isResource(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db).requestStart();
        verify(db).requestDone();
        assertFalse(expected);
    }

    @Test(expected = DatasourceException.class)
    public void isResourceException() throws DatasourceException {
        String id = "id";

        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenThrow(Exception.class);

        toTest.isResource(id);

        //fail();
    }

    @Test
    public void insertResourceTest1() {
        String string = "/string";
        Date date = new Date();
        Resource resource = generateResource(string, date, true);

        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        when(mongoCollection.getCollection(anyString())).thenReturn(null);

        try {
            toTest.insertResource(resource);
        } catch (Exception ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db, times(2)).requestStart();
        verify(db, times(2)).requestDone();
    }

    @Test
    public void insertResourceTest2() throws DatasourceException {
        String string = "/string";
        Date date = new Date();
        Resource resource = generateResource(string, date, true);

        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        when(mongoCollection.getCollection(anyString())).thenReturn(null);
        when(collectionDAO.getCollection(anyString())).thenReturn(null);

        try {
            toTest.insertResource(resource);
        } catch (Exception ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db, times(2)).requestStart();
        verify(db, times(2)).requestDone();
    }

    @Test(expected = SameIdException.class)
    public void insertResourceSameIdExceptionTest() throws SameIdException, DatasourceException{
        String string = "/a/b/string";
        Date date = new Date();
        Resource resource = generateResource(string, date, true);
        List list = new LinkedList();

        list.add(dBObject);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());

        toTest.insertResource(resource);

        //fail();
    }

    @Test(expected = DatasourceException.class)
    public void insertResourceDatasourceExceptionTest() throws DatasourceException, SameIdException {
        String string = "/string";
        Date date = new Date();
        Resource resource = generateResource(string, date, false);

        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        when(mongoCollection.getCollection(anyString())).thenReturn(null);
        doThrow(Exception.class).when(mongoCollection).insert(any(DBObject.class));

        toTest.insertResource(resource);

        //fail();
    }

    @Test
    public void findResourceTest1() {
        String string = "string";
        Date date = new Date();
        List list = new LinkedList();
        list.add(dBObject);

        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());

        try {
            toTest.findResource("id");
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test
    public void findResourceTest2() {
        String string = "string";
        Date date = new Date();
        List list = new LinkedList();
        list.add(dBObject);

        rulesdbObject(string, null);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());

        try {
            toTest.findResource("id");
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test(expected = DatasourceException.class)
    public void findResourceExceptionTest() throws DatasourceException {
        String string = "string";
        Date date = new Date();

        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenThrow(DatasourceException.class);

        toTest.getResource("id");

        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test
    public void updateResourceTest1() throws DatasourceException, SameIdException {
        String string = "/string";
        Date date = new Date();
        Resource resource = generateResource(string, date, true);
        List list = new LinkedList();

        list.add(dBObject);
        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        when(mongoCollection.getCollection(anyString())).thenReturn(null);

        when(collectionDAO.getCollection(anyString())).thenReturn(null);
        when(collectionDAO.insertCollection(any(ResourceCollection.class))).thenReturn(true);
        doNothing().when(mongoCollection).update(any(DBObject.class), any(DBObject.class), anyBoolean(), anyBoolean());

        try {
            toTest.updateResource(string, resource);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db).requestStart();
        verify(db).requestDone();

    }

    @Test
    public void updateResourceTest2() throws DatasourceException, SameIdException {
        String string = "/string";
        Date date = new Date();
        Resource resource = generateResource(string, date, false);
        ResourceCollection collection = generateResourceCollection(string, date, true);
        List list = new LinkedList();

        list.add(dBObject);
        rulesdbObject(string, null);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        when(mongoCollection.getCollection(anyString())).thenReturn(null);

        when(collectionDAO.getCollection(anyString())).thenReturn(collection);
        when(collectionDAO.insertCollection(any(ResourceCollection.class))).thenReturn(true);
        doNothing().when(mongoCollection).update(any(DBObject.class), any(DBObject.class), anyBoolean(), anyBoolean());

        try {
            toTest.updateResource(string, resource);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db).requestStart();
        verify(db).requestDone();

    }

    @Test
    public void updateResourceNullTest() throws DatasourceException, SameIdException {
        String string = "/string";
        Date date = new Date();
        Resource resource = generateResource(string, date, true);

        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);

        try {
            toTest.updateResource(string, resource);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test(expected = DatasourceException.class)
    public void updateResourceExceptionTest1() throws DatasourceException, SameIdException {
        String string = "/string";
        Date date = new Date();
        Resource resource = generateResource(string, date, true);
        List list = new LinkedList();

        list.add(dBObject);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        when(mongoCollection.getCollection(anyString())).thenReturn(null);
        when(collectionDAO.insertCollection(any(ResourceCollection.class))).thenThrow(SameIdException.class);

        toTest.updateResource(string, resource);

        //fail();
    }

    @Test(expected = DatasourceException.class)
    public void updateResourceExceptionTest2() throws DatasourceException, SameIdException {
        String string = "/string";
        Date date = new Date();
        Resource resource = generateResource(string, date, true);
        List list = new LinkedList();

        list.add(dBObject);
        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        when(mongoCollection.getCollection(anyString())).thenReturn(null);

        when(collectionDAO.getCollection(anyString())).thenReturn(null);
        when(collectionDAO.insertCollection(any(ResourceCollection.class))).thenReturn(true);
        doThrow(IllegalArgumentException.class).when(mongoCollection).update(any(DBObject.class), any(DBObject.class), anyBoolean(), anyBoolean());

        toTest.updateResource(string, resource);

        //fail();
    }

    @Test
    public void updateResourceContentTest1() {
        String string = "string";
        Date date = new Date();
        Resource resource = generateResource(string, date, true);
        List list = new LinkedList();

        list.add(dBObject);
        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        doNothing().when(mongoCollection).update(any(DBObject.class), any(DBObject.class), anyBoolean(), anyBoolean());

        try {
            toTest.updateResourceContent(resource);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test
    public void updateResourceContentTest2() {
        String string = "string";
        Date date = new Date();
        Resource resource = generateResource(string, date, false);
        List list = new LinkedList();

        list.add(dBObject);
        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        doNothing().when(mongoCollection).update(any(DBObject.class), any(DBObject.class), anyBoolean(), anyBoolean());

        try {
            toTest.updateResourceContent(resource);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test(expected = DatasourceException.class)
    public void updateResourceContentExceptionTest() throws DatasourceException {
        String string = "string";
        Date date = new Date();
        Resource resource = generateResource(string, date, false);
        List list = new LinkedList();

        list.add(dBObject);
        rulesdbObject(string, date);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        doThrow(IllegalArgumentException.class).when(mongoCollection).update(any(DBObject.class), any(DBObject.class), anyBoolean(), anyBoolean());

        toTest.updateResourceContent(resource);

        //fail();
    }

    @Test
    public void deleteResourceTest1() {
        String string = "string";
        boolean returned = false;
        List list = new LinkedList();

        list.add(dBObject);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        doNothing().when(mongoCollection).remove(eq(dBObject));

        try {
            returned = toTest.deleteResource(string);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        assertTrue(returned);
        verify(db).requestStart();
        verify(db).requestDone();

    }

    @Test
    public void deleteResourceTest2() {
        String string = "string";
        boolean returned = true;

        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(null);
        doNothing().when(mongoCollection).remove(eq(dBObject));

        try {
            returned = toTest.deleteResource(string);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        assertFalse(returned);
        verify(db).requestStart();
        verify(db).requestDone();
    }

    @Test(expected = DatasourceException.class)
    public void deleteResourceExceptionTest() throws DatasourceException {
        String string = "string";
        List list = new LinkedList();

        list.add(dBObject);
        when(mongoCollection.find(any(DBObject.class), any(DBObject.class), anyInt(), anyInt(), anyInt())).thenReturn(list.iterator());
        doThrow(IllegalArgumentException.class).when(mongoCollection).remove(eq(dBObject));


        toTest.deleteResource(string);

        //fail();
    }

    private Resource generateResource(String string, Date date, boolean creationDate) {
        Resource resource = new Resource();
        if (string == null)
            string = "string";
        if (date == null)
            date = new Date();
        if (creationDate)
            resource.setCreationDate(date);

        resource.setCreator(string + "Creator");
        resource.setModificationDate(date);
        resource.setContent(string.getBytes());
        resource.setContentFileName(string + "ContentFileName");
        resource.setContentMimeType(string + "ContentMimeType");
        resource.setContentUrl(string + "ContentUrl");
        resource.setId(string + "Id");
        resource.setName(string + "Name");

        return resource;
    }

    private ResourceCollection generateResourceCollection(String string, Date date, boolean creationDate) {
        ResourceCollection resourceCollection = new ResourceCollection();
        resourceCollection.setId(string+"Id");
        resourceCollection.setCreator(string+"Creator");
        if(creationDate)
            resourceCollection.setCreationDate(date);
        return resourceCollection;
    }

    private void rulesdbObject(String string, Date date) {
        if(date != null) {
            when(this.dBObject.get("creationDate")).thenReturn(date);
            when(this.dBObject.get("modificationDate")).thenReturn(date);
        }
        when(this.dBObject.get("id")).thenReturn(string + "Id");
        when(this.dBObject.get("_id")).thenReturn("507f1f77bcf86cd799439011");
        when(this.dBObject.get("name")).thenReturn(string + "Name");
        when(this.dBObject.get("creator")).thenReturn(string + "Creator");
        when(this.dBObject.get("contentUrl")).thenReturn(string + "ContentUrl");
        when(this.dBObject.get("contentMimeType")).thenReturn(string + "ContentMimeType");
        when(this.dBObject.get("contentFileName")).thenReturn(string + "ContentFileName");
        when(dBObject.get(eq("content"))).thenReturn(string.getBytes());
    }
}
