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
package org.fiware.apps.repository.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;
import org.fiware.apps.repository.dao.*;
import org.fiware.apps.repository.dao.impl.*;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.*;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;



@RunWith(PowerMockRunner.class)
@PrepareForTest({DAOFactory.class, VirtuosoDAOFactory.class})
public class CollectionServiceTest {
    
    @Mock private MongoDAOFactory mongoFactory;
    @Mock private MongoCollectionDAO mongoCollectionDAO;
    @Mock private MongoResourceDAO mongoResourceDAO;
    @Mock private VirtuosoResourceDAO virtuosoResourceDAO;
    private CollectionService toTest;
    
    public CollectionServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws Exception {
        
        PowerMockito.mockStatic(DAOFactory.class);
        PowerMockito.mockStatic(VirtuosoDAOFactory.class);
        mongoFactory = mock(MongoDAOFactory.class);
        mongoResourceDAO = mock(MongoResourceDAO.class);
        mongoCollectionDAO = mock(MongoCollectionDAO.class);
        virtuosoResourceDAO = mock(VirtuosoResourceDAO.class);
        when(DAOFactory.getDAOFactory(eq(1))).thenReturn(mongoFactory);
        when(VirtuosoDAOFactory.getVirtuosoResourceDAO()).thenReturn(virtuosoResourceDAO);
        when(mongoFactory.getResourceDAO()).thenReturn(mongoResourceDAO);
        when(mongoFactory.getCollectionDAO()).thenReturn(mongoCollectionDAO);
        PowerMockito.whenNew(VirtuosoResourceDAO.class).withNoArguments().thenReturn(virtuosoResourceDAO);
        toTest = new CollectionService();
        
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void getResourceRootTest() {
        Response returned;
        
        returned = toTest.getResourceRoot();
        
        assertEquals(returned.getStatus(), 404);
    }
    
    @Test
    public void getResourceVirtuosoTest() throws DatasourceException, URISyntaxException{
        Response returned;
        String path = "a/b/c";
        String accept = "application/rdf+xml";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a/b/c"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        Resource resource = generateResource(path, null, true);
        resource.setContentMimeType("text/n3");
        when(mongoResourceDAO.getResource(eq(path))).thenReturn(resource);
        when(virtuosoResourceDAO.getResource(eq(path), anyString())).thenReturn(resource);
        
        returned = toTest.getResource(uriInfo, accept, path);
        
        assertEquals(returned.getStatus(), 200);
    }
    
    @Test
    public void getResourceVirtuosoNoContentTest() throws DatasourceException, URISyntaxException{
        Response returned;
        String path = "a/b/c";
        String accept = "application/rdf+xml";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a/b/c"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        Resource resource = generateResource(path, null, true);
        resource.setContentMimeType("text/n3");
        resource.setContent(null);
        when(mongoResourceDAO.getResource(eq(path))).thenReturn(resource);
        when(virtuosoResourceDAO.getResource(eq(path), anyString())).thenReturn(resource);
        
        returned = toTest.getResource(uriInfo, accept, path);
        
        assertEquals(returned.getStatus(), 204);
    }
    
    @Test
    public void getResourceMongoTest() throws DatasourceException, URISyntaxException{
        Response returned;
        String path = "a/b/c";
        String accept = "application/rdf+xml";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a/b/c"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        Resource resource = generateResource(path, null, true);
        resource.setContentMimeType(accept);
        when(mongoResourceDAO.getResource(eq(path))).thenReturn(resource);
        when(mongoResourceDAO.getResourceContent(eq(path))).thenReturn(resource);
        
        returned = toTest.getResource(uriInfo, accept, path);
        
        assertEquals(returned.getStatus(), 200);
    }
    
    @Test
    public void getResourceMongoNoContentTest() throws DatasourceException, URISyntaxException{
        Response returned;
        String path = "a/b/c";
        String accept = "application/rdf+xml";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a/b/c"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        Resource resource = generateResource(path, null, true);
        resource.setContentMimeType(accept);
        resource.setContent(null);
        when(mongoResourceDAO.getResource(eq(path))).thenReturn(resource);
        when(mongoResourceDAO.getResourceContent(eq(path))).thenReturn(resource);
        
        returned = toTest.getResource(uriInfo, accept, path);
        
        assertEquals(returned.getStatus(), 204);
    }
    
    @Test
    public void getResourceCollectionTest() throws DatasourceException, URISyntaxException{
        Response returned;
        String path = "a/b/c";
        String accept = "text/plain";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a/b/c"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        Resource resource = generateResource(path, null, true);
        ResourceCollection collection = generateResourceCollection(path, null, true);
        resource.setContentMimeType(accept);
        when(mongoResourceDAO.getResource(eq(path))).thenReturn(null);
        when(mongoCollectionDAO.getCollection(eq(path))).thenReturn(collection);
        
        returned = toTest.getResource(uriInfo, accept, path);
        
        assertEquals(returned.getStatus(), 200);
    }
    
    @Test
    public void getResourceCollectionNullTest() throws DatasourceException, URISyntaxException{
        Response returned;
        String path = "a/b/c";
        String accept = "text/plain";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a/b/c"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        Resource resource = generateResource(path, null, true);
        resource.setContentMimeType(accept);
        when(mongoResourceDAO.getResource(eq(path))).thenReturn(null);
        when(mongoCollectionDAO.getCollection(eq(path))).thenReturn(null);
        
        returned = toTest.getResource(uriInfo, accept, path);
        
        assertEquals(returned.getStatus(), 404);
    }
    
    @Test
    public void getResourceMetaTest() throws DatasourceException, JAXBException, URISyntaxException{
        Response returned;
        String path = "a/b/c";
        String accept = "text/plain";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a/b/c"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        Resource resource = generateResource(path, null, true);
        resource.setContentMimeType("text/n3");
        when(mongoResourceDAO.getResource(eq(path))).thenReturn(resource);
        
        returned = toTest.getResourceMeta(uriInfo, accept, path);
        
        assertEquals(returned.getStatus(), 200);
    }
    
    @Test
    public void getResourceCollectionMetaTest() throws DatasourceException, URISyntaxException{
        Response returned;
        String path = "a/b/c";
        String accept = "text/plain";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a/b/c"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        ResourceCollection collection = generateResourceCollection(path, null, true);
        when(mongoResourceDAO.getResource(eq(path))).thenReturn(null);
        when(mongoCollectionDAO.getCollection(eq(path))).thenReturn(collection);
        
        returned = toTest.getResourceMeta(uriInfo, accept, path);
        
        assertEquals(returned.getStatus(), 404);
    }
    
    @Test
    public void getResourceMetaExceptionTest() throws DatasourceException, URISyntaxException{
        Response returned;
        String path = "a/b/c";
        String accept = "application/rdf+xml";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a/b/c"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        Resource resource = generateResource(path, null, true);
        resource.setContentMimeType(accept);
        resource.setContent(null);
        when(mongoResourceDAO.getResource(eq(path))).thenThrow(DatasourceException.class);
        
        returned = toTest.getResourceMeta(uriInfo, accept, path);
        
        assertEquals(returned.getStatus(), 500);
    }
    
    @Test
    public void postResourceTest() throws URISyntaxException {
        Response returned;
        String path = "a";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        AbstractResource resource = generateResource(path, null, true);
        
        returned = toTest.postResource(uriInfo, resource);
        
        assertEquals(returned.getStatus(), 201);
    }
    
    @Test
    public void postResourceConflictTest() throws URISyntaxException, DatasourceException, SameIdException {
        Response returned;
        String path = "a";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        AbstractResource resource = generateResource(path, null, true);
        
        //doThrow(SameIdException.class);
        doThrow(SameIdException.class).when(mongoResourceDAO).insertResource(eq((Resource) resource));
        
        returned = toTest.postResource(uriInfo, resource);
        
        assertEquals(returned.getStatus(), 409);
    }
    
    @Test
    public void postResourceErrorTest() throws URISyntaxException, DatasourceException, SameIdException {
        Response returned;
        String path = "a";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        AbstractResource resource = generateResource(path, null, true);
        
        //doThrow(SameIdException.class);
        doThrow(DatasourceException.class).when(mongoResourceDAO).insertResource(eq((Resource) resource));
        
        returned = toTest.postResource(uriInfo, resource);
        
        assertEquals(returned.getStatus(), 500);
    }
    
    @Test
    public void postResourceCollectionTest() throws URISyntaxException {
        Response returned;
        String path = "a";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        AbstractResource resource = generateResourceCollection(path, null, true);
        
        returned = toTest.postResource(uriInfo, resource);
        
        assertEquals(returned.getStatus(), 201);
    }
    
    @Test
    public void postResourceCollectionConflictTest() throws URISyntaxException, DatasourceException, SameIdException {
        Response returned;
        String path = "a";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        AbstractResource resource = generateResourceCollection(path, null, true);
        
        doThrow(SameIdException.class).when(mongoCollectionDAO).insertCollection(eq((ResourceCollection) resource));
        
        returned = toTest.postResource(uriInfo, resource);
        
        assertEquals(409, returned.getStatus());
    }
    
    @Test
    public void postResourceCollectionErrorTest() throws URISyntaxException, DatasourceException, SameIdException {
        Response returned;
        String path = "a";
        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/a"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));
        AbstractResource resource = generateResourceCollection(path, null, true);
        
        doThrow(DatasourceException.class).when(mongoCollectionDAO).insertCollection(eq((ResourceCollection) resource));
        
        returned = toTest.postResource(uriInfo, resource);
        
        assertEquals(500, returned.getStatus());
    }
    
    @Test
    public void putResourceTest() throws URISyntaxException, DatasourceException {
        Response returned;
        String path = "a/b/c";
        String accept = "application/rdf+xml";
        
        when(mongoResourceDAO.getResource(eq(path))).thenReturn(null);
        
        returned = toTest.putResource(accept, path, "content");
        
        assertEquals(200, returned.getStatus());
    }
    
    @Test
    public void putResourceConflictTest() throws URISyntaxException, DatasourceException, SameIdException {
        Response returned;
        String path = "a/b/c";
        String accept = "application/rdf+xml";
        
        doThrow(SameIdException.class).when(mongoResourceDAO).insertResource(anyObject());
        
        returned = toTest.putResource(accept, path, "content");
        
        assertEquals(409, returned.getStatus());
    }
    
    @Test
    public void putResourceErrorTest() throws URISyntaxException, DatasourceException, SameIdException {
        Response returned;
        String path = "a/b/c";
        String accept = "application/rdf+xml";
        
        doThrow(DatasourceException.class).when(mongoResourceDAO).insertResource(anyObject());
        
        returned = toTest.putResource(accept, path, "content");
        
        assertEquals(500, returned.getStatus());
    }
    
    @Test
    public void putResourceMetaTest() throws DatasourceException, SameIdException {
        Response returned;
        String path = "a/b/c";
        String accept = "application/rdf+xml";
        Resource oldResource = generateResource("old", null, true);
        Resource newResource = generateResource("new", null, true);
        
        when(mongoResourceDAO.getResourceContent(eq(path))).thenReturn(oldResource);
        when(mongoResourceDAO.insertResource(anyObject())).thenReturn(null);
        
        returned = toTest.putResource(accept, path, newResource);

        assertEquals(200, returned.getStatus());
    }
    
    @Test
    public void putResourceMetaConflictTest() throws DatasourceException, SameIdException {
        Response returned;
        String path = "a/b/c";
        String accept = "application/rdf+xml";
        Resource oldResource = generateResource("old", null, true);
        Resource newResource = generateResource("new", null, true);
        
        when(mongoResourceDAO.getResourceContent(eq(path))).thenReturn(oldResource);
        doThrow(SameIdException.class).when(mongoResourceDAO).insertResource(anyObject());
        
        returned = toTest.putResource(accept, path, newResource);
        
        assertEquals(200, returned.getStatus());
    }
    
    @Test
    public void putResourceMetaErrorTest() throws DatasourceException, SameIdException {
        Response returned;
        String path = "a/b/c";
        String accept = "application/rdf+xml";
        Resource oldResource = generateResource("old", null, true);
        Resource newResource = generateResource("new", null, true);
        
        when(mongoResourceDAO.getResourceContent(eq(path))).thenReturn(oldResource);
        doThrow(DatasourceException.class).when(mongoResourceDAO).updateResourceContent(anyObject());
        
        returned = toTest.putResource(accept, path, newResource);
        
        assertEquals(200, returned.getStatus());
    }
    
    @Test
    public void putResourceCollectionMetaTest() {
        Response returned;
        String path = "a/b/c";
        String accept = "application/rdf+xml";
        AbstractResource collection = generateResourceCollection(path, null, true);
        
        returned = toTest.putResource(accept, path, collection);
        
        assertEquals(403, returned.getStatus());
    }
    
    
    @Test
    public void deleteResourceTest() throws DatasourceException {
        Response returned;
        String path = "a/b/c";
        
        when(mongoResourceDAO.isResource(eq(path))).thenReturn(true);
        
        returned = toTest.delete(path);
        
        assertEquals(202, returned.getStatus());
    }
    
    @Test
    public void deleteResourceCollectionTest() throws DatasourceException {
        Response returned;
        String path = "a/b/c";
        
        when(mongoResourceDAO.isResource(eq(path))).thenReturn(false);
        
        returned = toTest.delete(path);
        
        assertEquals(202, returned.getStatus());
    }
    
    @Test
    public void deleteResourceErrorTest() throws DatasourceException {
        Response returned;
        String path = "a/b/c";
        
        when(mongoResourceDAO.isResource(eq(path))).thenReturn(true);
        doThrow(DatasourceException.class).when(mongoResourceDAO).deleteResource(eq(path));
        
        returned = toTest.delete(path);
        
        assertEquals(500, returned.getStatus());
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
        ResourceCollection collection = new ResourceCollection();
        if (string == null)
            string = "string";
        if (date == null)
            date = new Date();
        if (creationDate)
            collection.setCreationDate(date);
        
        collection.setCreator(string + "Creator");
        collection.setModificationDate(date);
        collection.setId(string + "Id");
        
        return collection;
    }
    
}
