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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.fiware.apps.repository.dao.CollectionDAO;
import org.fiware.apps.repository.dao.MongoDAOFactory;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(DBCollection.class)
public class MongoResourceDAOTest {

    @Mock private DB db;
    @Mock private MongoCollection mongoCollection;
    @Mock private CollectionDAO collectionDAO;
    @Mock private MongoDAOFactory mongoFactory;

    @InjectMocks MongoResourceDAO toTest;

    public MongoResourceDAOTest() {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() {

    }

    private DBObject generateDBObject(String path, Date date) {
        DBObject dBObject = new BasicDBObject();
        if(date != null) {
            dBObject.put("creationDate", date);
            dBObject.put("modificationDate", date);
        }
        dBObject.put("id", path + "11111");
        dBObject.put("name", path + "TestResource");
        dBObject.put("creator", path + "TestCreator");
        dBObject.put("contentUrl", "http://testcontenturl.com");
        dBObject.put("contentMimeType", "application/json");
        dBObject.put("contentFileName", path + "TestFile.json");
        dBObject.put("content", path.getBytes());
        return dBObject;
    }

    private Resource generateResource(String path, Date date) {
        Resource resource = new Resource();
        resource.setId(path + "11111");
        resource.setName(path + "TestResource");
        resource.setCreator(path + "TestCreator");
        resource.setContentUrl("http://testcontenturl.com");
        resource.setContentMimeType("application/json");
        resource.setContentFileName(path + "TestFile.json");
        resource.setContent(path.getBytes());

        if (date != null) {
            resource.setCreationDate(date);
            resource.setModificationDate(date);
        }

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

    private void assertEqualsResources(Resource expected, Resource actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getCreator(), actual.getCreator());
        assertEquals(expected.getContentUrl(), actual.getContentUrl());
        assertEquals(expected.getContentMimeType(), actual.getContentMimeType());
        assertEquals(expected.getContentFileName(), actual.getContentFileName());
        assertEquals(expected.getCreationDate(), actual.getCreationDate());
        assertEquals(expected.getModificationDate(), actual.getModificationDate());
    }

    private void testGetResources(ResourceFilter filter) throws DatasourceException {
        Date date = new Date();

        // Mock database access
        FindIterable it = mock(FindIterable.class);
        MongoCursor dBCursor = mock(MongoCursor.class);

        PowerMockito.when(mongoCollection.find(any(BasicDBObject.class))).thenReturn(it);

        when(it.skip(eq(0))).thenReturn(it);
        when(it.limit(eq(0))).thenReturn(it);
        when(it.iterator()).thenReturn(dBCursor);

        when(dBCursor.hasNext()).thenReturn(true, false);
        when(dBCursor.next()).thenReturn(this.generateDBObject("/path1", date));

        // Build list with expected result
        Resource expected = this.generateResource("/path1", date);

        List<Resource> result = null;
        if (filter != null) {
            result = toTest.getResources("", filter);
        } else {
            result = toTest.getResources("");
        }

        // Check result
        assertEquals(1, result.size());
        for (Resource act: result) {
            this.assertEqualsResources(expected, act);
        }

        // Check method calls
        if (filter != null) {
            ArgumentCaptor<BasicDBObject> captor = ArgumentCaptor.forClass(BasicDBObject.class);

            verify(mongoCollection).find(captor.capture());
            
            Pattern exp = Pattern.compile("^/[a-zA-Z0-9_\\.\\-\\+]*$");
            Pattern act = (Pattern) captor.getValue().get("id");
            assertEquals(exp.pattern(), act.pattern());
        }

        verify(it).skip(eq(0));
        verify(it).limit(eq(0));
        verify(it).iterator();
    }

    @Test
    public void getResourcesTest() throws DatasourceException {
        this.testGetResources(null);
    }

    @Test
    public void getResourcesFilterTest() throws Exception {
        this.testGetResources(new ResourceFilter(0, 0, "/[a-zA-Z0-9_\\.\\-\\+]*$"));
    }

    @Test(expected = DatasourceException.class)
    public void getResourcesExceptionTest() throws DatasourceException {
        String string = "/string";
        ResourceFilter resourceFilter = new ResourceFilter(0, 0, "");
        PowerMockito.when(mongoCollection.find(any(BasicDBObject.class))).thenThrow(Exception.class);

        toTest.getResources(string, resourceFilter);
    }

    private void mockMongoCollection(Date date, Boolean hasNextF, Boolean... hasNexts) {

        FindIterable it = mock(FindIterable.class);
        MongoCursor cursor = mock(MongoCursor.class);

        when(mongoCollection.find(any(BasicDBObject.class))).thenReturn(it);

        when(it.iterator()).thenReturn(cursor);

        if (hasNexts.length > 0) {
            when(cursor.hasNext()).thenReturn(hasNextF, hasNexts);
        } else {
            when(cursor.hasNext()).thenReturn(hasNextF);
        }

        when(cursor.next()).thenReturn(this.generateDBObject("getResource", date));
    }

    private void testGetResource(Date date, boolean withContent) {
        String string = "getResource";

        this.mockMongoCollection(date, true, false);

        Resource result = null;
        try {
            if (withContent) {
                result = toTest.getResourceContent("id");
            } else {
                result = toTest.getResource("id");
            }
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        // Validate result
        Resource expected = this.generateResource(string, date);
        this.assertEqualsResources(expected, result);
    }
    @Test
    public void getResourceTest() {
        this.testGetResource(new Date(), false);
    }

    @Test
    public void getResourceNullDateTest() {
        this.testGetResource(null, false);
    }

    private void mockFind() {
        FindIterable it = mock(FindIterable.class);
        MongoCursor cursor = mock(MongoCursor.class);
        
        when(cursor.hasNext()).thenReturn(false);
        when(it.iterator()).thenReturn(cursor);

        when(mongoCollection.find(isA(BasicDBObject.class))).thenReturn(it);
    }

    @Test
    public void getResourceNullTest() throws DatasourceException {
        this.mockFind();
        Resource res = toTest.getResource("id");
        assertNull(res);
    }

    @Test(expected = DatasourceException.class)
    public void getResourceExceptionTest() throws DatasourceException{
        when(mongoCollection.find(any(BasicDBObject.class))).thenThrow(DatasourceException.class);
        toTest.getResource("id");
    }

    @Test
    public void getResourceContentTest() {
        this.testGetResource(new Date(), true);
    }

    @Test
    public void getResourceContentTest2() {
        this.testGetResource(null, true);
    }

    @Test
    public void getResourceContentNullTest() throws DatasourceException {
        this.mockFind();
        Resource res = toTest.getResourceContent("id");
        assertNull(res);
    }

    @Test(expected = DatasourceException.class)
    public void getResourceContentExceptionTest() throws DatasourceException {
        when(mongoCollection.find(any(BasicDBObject.class))).thenThrow(DatasourceException.class);
        toTest.getResourceContent("id");

    }

    private void testIsResource(boolean expected) {
        String id = "id";
        boolean actual = !expected;

        this.mockMongoCollection(null, expected);

        try {
            actual = toTest.isResource(id);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        assertEquals(expected, actual);
    }

    @Test
    public void isResourceTest1() {
        this.testIsResource(false);
    }

    @Test
    public void isResourceTest2() {
        this.testIsResource(true);
    }

    @Test(expected = DatasourceException.class)
    public void isResourceException() throws DatasourceException {
        String id = "id";
        when(mongoCollection.find(any(BasicDBObject.class))).thenThrow(Exception.class);
        toTest.isResource(id);
    }

    @Test
    public void insertResourceTest() throws DatasourceException, SameIdException {
        String string = "/string/";
        Date date = new Date();
        Resource resource = this.generateResource(string, date);

        this.mockMongoCollection(date, false);
        when(collectionDAO.getCollection(anyString())).thenReturn(null);

        toTest.insertResource(resource);

        verify(mongoCollection).insertOne(any(BasicDBObject.class));
    }

    @Test
    public void insertResourceCollectionTest() throws DatasourceException, SameIdException {
        String string = "/string/";
        Date date = new Date();
        Resource resource = this.generateResource(string, date);

        this.mockMongoCollection(date, false);
        when(collectionDAO.getCollection("/string")).thenReturn(null);

        toTest.insertResource(resource);

        ArgumentCaptor<ResourceCollection> resCaptor = ArgumentCaptor.forClass(ResourceCollection.class);
        verify(collectionDAO).insertCollection(resCaptor.capture());
        assertEquals("/string", resCaptor.getValue().getId());
        assertEquals(resource.getCreator(), resCaptor.getValue().getCreator());

        verify(mongoCollection).insertOne(any(BasicDBObject.class));
    }

    @Test(expected = SameIdException.class)
    public void insertResourceSameIdExceptionTest() throws SameIdException, DatasourceException{
        String string = "/a/b/string";
        Date date = new Date();
        Resource resource = MongoResourceDAOTest.this.generateResource(string, date);

        this.mockMongoCollection(date, true, false);

        toTest.insertResource(resource);
    }

    @Test(expected = DatasourceException.class)
    public void insertResourceDatasourceExceptionTest() throws DatasourceException, SameIdException {
        String string = "/string";
        Date date = new Date();
        Resource resource = this.generateResource(string, date);

        this.mockMongoCollection(date, false);
        doThrow(Exception.class).when(mongoCollection).insertOne(any(BasicDBObject.class));

        toTest.insertResource(resource);
    }

    private void testFindResource(Date date) {
        this.mockMongoCollection(date, true, false);

        Resource actual = null;
        try {
            actual = toTest.findResource("id");
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        this.assertEqualsResources(this.generateResource("getResource", date), actual);
    }
    
    @Test
    public void findResourceTest() {
        this.testFindResource(new Date());
    }

    @Test
    public void findResourceTest2() {
        this.testFindResource(null);
    }

    @Test(expected = DatasourceException.class)
    public void findResourceExceptionTest() throws DatasourceException {
        when(mongoCollection.find(any(BasicDBObject.class))).thenThrow(DatasourceException.class);
        toTest.getResource("id");
    }

    private void testUpdateResource(boolean existsCollection) throws DatasourceException, SameIdException{
        String string = "/string";
        Date date = new Date();
        boolean result = false;
        Resource resource = this.generateResource(string, date);

        this.mockMongoCollection(date, true, false, true, false);

        if (existsCollection) {
            when(collectionDAO.getCollection(anyString())).thenReturn(null);
        } else {
            ResourceCollection collection = generateResourceCollection(string, date, true);
            when(collectionDAO.getCollection(anyString())).thenReturn(collection);
            when(collectionDAO.insertCollection(any(ResourceCollection.class))).thenReturn(true);
        }

        try {
            result = toTest.updateResource(string, resource);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }

        assertTrue(result);
        if (existsCollection) {
            verify(collectionDAO).insertCollection(any(ResourceCollection.class));
        }
    }

    @Test
    public void updateResourceTest() throws DatasourceException, SameIdException {
        this.testUpdateResource(true);
    }

    @Test
    public void updateResourceCollectionTest() throws DatasourceException, SameIdException {
        this.testUpdateResource(false);
    }

    @Test
    public void updateResourceNullTest() throws DatasourceException, SameIdException {
        String string = "/string";
        Date date = new Date();
        boolean result = true;

        Resource resource = this.generateResource(string, date);

        this.mockMongoCollection(date, false);

        try {
            result = toTest.updateResource(string, resource);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        assertFalse(result);
    }

    @Test(expected = DatasourceException.class)
    public void updateResourceExceptionCollectionTest() throws DatasourceException, SameIdException {
        String string = "/string";
        Date date = new Date();
        Resource resource = this.generateResource(string, date);

        this.mockMongoCollection(date, true, false);
        when(collectionDAO.getCollection(anyString())).thenReturn(null);
        when(collectionDAO.insertCollection(any(ResourceCollection.class))).thenThrow(SameIdException.class);

        toTest.updateResource(string, resource);
    }

    @Test(expected = DatasourceException.class)
    public void updateResourceExceptionTest() throws DatasourceException, SameIdException {
        String string = "/string/";
        Date date = new Date();
        Resource resource = this.generateResource(string, date);

        this.mockMongoCollection(date, true, false, true, false);
        when(collectionDAO.getCollection(anyString())).thenReturn(null);
        when(collectionDAO.insertCollection(isA(ResourceCollection.class))).thenReturn(true);
        doThrow(IllegalArgumentException.class).when(mongoCollection).updateOne(isA(BasicDBObject.class), isA(BasicDBObject.class));

        toTest.updateResource(string, resource);
    }

    @Test
    public void updateResourceContentTest() {
        String string = "string";
        Date date = new Date();
        boolean result = false;
        Resource resource = this.generateResource(string, date);

        this.mockMongoCollection(date, true, false, true, false);

        try {
            result = toTest.updateResourceContent(resource);
        } catch (DatasourceException ex) {
            fail("Exception not expected:\n" + ex.getLocalizedMessage());
        }
        assertTrue(result);
    }

    @Test(expected = DatasourceException.class)
    public void updateResourceContentExceptionTest() throws DatasourceException {
        String string = "string";
        Date date = new Date();
        Resource resource = this.generateResource(string, date);

        this.mockMongoCollection(date, true, false, true, false);
        doThrow(IllegalArgumentException.class).when(mongoCollection).updateOne(any(BasicDBObject.class), any(BasicDBObject.class));

        toTest.updateResourceContent(resource);
    }

    private void mockDeleteOne(boolean wasAck) {
        DeleteResult res = mock(DeleteResult.class);
        when(res.wasAcknowledged()).thenReturn(wasAck);
        when(mongoCollection.deleteOne(isA(BasicDBObject.class))).thenReturn(res);
    }
    @Test
    public void deleteResourceTest1() throws DatasourceException {
        String string = "string";
        boolean returned = false;

        this.mockDeleteOne(true);

        this.mockMongoCollection(null, true, false);

        returned = toTest.deleteResource(string);
        
        verify(mongoCollection).deleteOne(any(BasicDBObject.class));
        assertTrue(returned);

    }

    @Test
    public void deleteResourceTest2() throws DatasourceException {
        String string = "string";
        boolean returned;

        this.mockMongoCollection(null, false);

        returned = toTest.deleteResource(string);
        assertFalse(returned);
    }

    @Test(expected = DatasourceException.class)
    public void deleteResourceExceptionTest() throws DatasourceException {
        String string = "string";

        this.mockMongoCollection(null, true, false);
        doThrow(IllegalArgumentException.class).when(mongoCollection).deleteOne(any(BasicDBObject.class));

        toTest.deleteResource(string);
    }
}
