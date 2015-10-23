/*
Modified BSD License
====================

Copyright (c) 2015, CoNWeT Lab., Universidad Politecnica Madrid
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
package org.fiware.apps.repository.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;
import org.fiware.apps.repository.dao.*;
import org.fiware.apps.repository.dao.impl.*;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.*;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.jboss.resteasy.specimpl.ResteasyHttpHeaders;
import org.jboss.resteasy.spi.ResteasyUriInfo;
import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MongoDAOFactory.class, VirtuosoDAOFactory.class, CollectionService.class})
public class CollectionServiceTest {

    private MongoDAOFactory mongoFactory;
    private MongoCollectionDAO mongoCollectionDAO;
    private MongoResourceDAO mongoResourceDAO;
    private VirtuosoDAOFactory virtuosoDAOFactory;
    private VirtuosoResourceDAO virtuosoResourceDAO;
    private ServletContext servletContext;
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

        mongoFactory = mock(MongoDAOFactory.class);
        mongoResourceDAO = mock(MongoResourceDAO.class);
        mongoCollectionDAO = mock(MongoCollectionDAO.class);
        virtuosoDAOFactory = mock(VirtuosoDAOFactory.class);
        virtuosoResourceDAO = mock(VirtuosoResourceDAO.class);
        servletContext = mock(ServletContext.class);

        /*when(VirtuosoDAOFactory.getVirtuosoResourceDAO(anyObject())).thenReturn(virtuosoResourceDAO);*/
        when(servletContext.getInitParameter("propertiesFile")).thenReturn("");
        when(mongoFactory.getResourceDAO(anyObject())).thenReturn(mongoResourceDAO);
        when(mongoFactory.getCollectionDAO(anyObject())).thenReturn(mongoCollectionDAO);
        when(virtuosoDAOFactory.getVirtuosoResourceDAO(anyObject())).thenReturn(virtuosoResourceDAO);

        PowerMockito.whenNew(MongoDAOFactory.class).withAnyArguments().thenReturn(mongoFactory);
        PowerMockito.whenNew(VirtuosoDAOFactory.class).withAnyArguments().thenReturn(virtuosoDAOFactory);

        toTest = new CollectionService(servletContext);

    }

    @After
    public void tearDown() {
    }

    @Test
    public void getResourceRootTest() {
        Response returned;
        List <String> accepts = new LinkedList();
        accepts.add("application/xml");


        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        returned = toTest.getResourceRoot(headers);

        assertEquals(returned.getStatus(), 404);
    }

    @Test
    public void getResourceRootNoAcceptTest() {
        Response returned;
        List <String> accepts = new LinkedList();
        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        returned = toTest.getResourceRoot(headers);

        assertEquals(returned.getStatus(), 404);
    }

    private void getResourceOrCollection(String path, List <String> accepts, String typeResponse, int statusResponse)
            throws URISyntaxException {
        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/"+path),
                new URI("http://localhost:8080/FiwareRepository/v2/"));

        Response response = toTest.getResource(uriInfo, headers, path);

        if (typeResponse !=  null) {
            assertEquals(typeResponse, response.getMediaType().toString());
        }
        assertEquals(statusResponse, response.getStatus());
    }

    private void getResourceMeta(String path, List <String> accepts, String typeResponse, int statusResponse)
            throws URISyntaxException {
        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        UriInfo uriInfo = new ResteasyUriInfo(new URI("http://localhost:8080/FiwareRepository/v2/collec/"+path+".meta"),
                new URI("http://localhost:8080/FiwareRepository/v2/"));

        Response response = toTest.getResourceMeta(uriInfo, headers, path);

        if (typeResponse !=  null) {
            assertEquals(typeResponse, response.getMediaType().toString());
        }
        assertEquals(statusResponse, response.getStatus());
    }

    @Test
    public void getResourceMongoDefaultMediaTypeTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, "application/rdf+xml");
        List <String> accepts = new LinkedList<>();
        accepts.add("*/*");

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);
        when(mongoResourceDAO.getResourceContent(path)).thenReturn(resource);

        getResourceOrCollection(path, accepts, RestHelper.RdfDefaultType, 200);
    }

    @Test
    public void getResourceVirtuosoDefaultMediaTypeTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, "application/rdf+json");
        resource.setContent("content".getBytes());
        List <String> accepts = new LinkedList<>();
        accepts.add("*/*");

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);
        when(virtuosoResourceDAO.getResource(resource.getContentUrl(), "RDF/XML")).thenReturn(resource);

        getResourceOrCollection(path, accepts, RestHelper.RdfDefaultType, 200);
    }

    @Test
    public void getResourceMongoNoContentTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, "");
        resource.setContent("".getBytes());
        List <String> accepts = new LinkedList<>();
        accepts.add("application/rdf+xml");

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);
        when(mongoResourceDAO.getResourceContent(path)).thenReturn(resource);

        getResourceOrCollection(path, accepts, null, 204);
    }

    @Test
    public void getResourceMongoContentNullTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, "");
        resource.setContent(null);
        List <String> accepts = new LinkedList<>();
        accepts.add("application/rdf+xml");

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);
        when(mongoResourceDAO.getResourceContent(path)).thenReturn(resource);

        getResourceOrCollection(path, accepts, null, 204);
    }

    @Test
    public void getResourceVirtuosoNoContentTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, "application/rdf+json");
        resource.setContent("content".getBytes());
        List <String> accepts = new LinkedList<>();
        accepts.add("*/*");

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);
        when(virtuosoResourceDAO.getResource(resource.getContentUrl(), "RDF/XML")).thenReturn(null);

        getResourceOrCollection(path, accepts, null, 204);
    }

    @Test
    public void getResourceNoAceptableTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, "text/plain");
        resource.setContent("".getBytes());
        List <String> accepts = new LinkedList<>();
        accepts.add("test/fail");

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);

        getResourceOrCollection(path, accepts, null, 406);
    }

    @Test
    public void getResourceDatasourceExceptionTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        List <String> accepts = new LinkedList<>();
        accepts.add("*/*");

        when(mongoResourceDAO.getResource(anyString())).thenThrow(DatasourceException.class);

        getResourceOrCollection(path, accepts, null, 500);
    }

    @Test
    public void getResourceDatasourceExceptionTest2() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, "application/rdf+xml");
        List <String> accepts = new LinkedList<>();
        accepts.add("*/*");

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);
        when(mongoResourceDAO.getResourceContent(path)).thenThrow(DatasourceException.class);

        getResourceOrCollection(path, accepts, null, 500);
    }

    @Test
    public void getResourceMetaAnyAcceptTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, null);
        List <String> accepts = new LinkedList<>();
        accepts.add("*/*");

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);

        getResourceMeta(path, accepts, "application/json", 200);
    }

    @Test
    public void getResourceMetaNoAcceptTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, null);
        List <String> accepts = new LinkedList<>();

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);

        getResourceMeta(path, accepts, "application/json", 200);
    }

    @Test
    public void getResourceMetaTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, null);
        List <String> accepts = new LinkedList<>();
        accepts.add("application/xml");

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);

        getResourceMeta(path, accepts, "application/xml", 200);
    }

    @Test
    public void getResourceMetaNotFoundTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        List <String> accepts = new LinkedList<>();
        accepts.add("application/xml");

        when(mongoResourceDAO.getResource(path)).thenReturn(null);

        getResourceMeta(path, accepts, "application/xml", 404);
    }

    @Test
    public void getResourceMetaNotAcceptableTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, null);
        List <String> accepts = new LinkedList<>();
        accepts.add("test/fail");

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);

        getResourceMeta(path, accepts, null, 406);
    }

    @Test
    public void getResourceMetaDatasourceExceptionTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        List <String> accepts = new LinkedList<>();
        accepts.add("application/xml");

        when(mongoResourceDAO.getResource(path)).thenThrow(DatasourceException.class);

        getResourceMeta(path, accepts, "application/xml", 500);
    }

    @Test
    public void getResourceMetaJAXBExceptionTest() throws DatasourceException, URISyntaxException {
        String path = "a/b/c";
        List <String> accepts = new LinkedList<>();
        accepts.add("application/xml");

        when(mongoResourceDAO.getResource(path)).thenThrow(JAXBException.class);

        getResourceMeta(path, accepts, "application/xml", 500);
    }

    /* Get Collection Tests */
    @Test
    public void getCollectionTest() throws DatasourceException, URISyntaxException {
        String path = "a";
        ResourceCollection resourceCollection = generateResourceCollection(path, null, true);
        List <String> accepts = new LinkedList<>();
        accepts.add("application/xml");

        when(mongoResourceDAO.getResource(path)).thenReturn(null);
        when(mongoCollectionDAO.getCollection(path)).thenReturn(resourceCollection);

        getResourceOrCollection(path, accepts, "application/xml", 200);
    }

    @Test
    public void getCollectionNotAcceptableTest() throws DatasourceException, URISyntaxException {
        String path = "a";
        ResourceCollection resourceCollection = generateResourceCollection(path, null, true);
        List <String> accepts = new LinkedList<>();
        accepts.add("test/fail");

        when(mongoResourceDAO.getResource(path)).thenReturn(null);
        when(mongoCollectionDAO.getCollection(path)).thenReturn(resourceCollection);

        getResourceOrCollection(path, accepts, null, 406);
    }

    @Test
    public void getCollectionDefaultMediaTypeTest() throws DatasourceException, URISyntaxException {
        String path = "a";
        ResourceCollection resourceCollection = generateResourceCollection(path, null, true);
        List <String> accepts = new LinkedList<>();
        accepts.add("*/*");

        when(mongoResourceDAO.getResource(path)).thenReturn(null);
        when(mongoCollectionDAO.getCollection(path)).thenReturn(resourceCollection);

        getResourceOrCollection(path, accepts, "application/json", 200);
    }

    @Test
    public void getCollectionNoAcceptTest() throws DatasourceException, URISyntaxException {
        String path = "a";
        ResourceCollection resourceCollection = generateResourceCollection(path, null, true);
        List <String> accepts = new LinkedList<>();

        when(mongoResourceDAO.getResource(path)).thenReturn(null);
        when(mongoCollectionDAO.getCollection(path)).thenReturn(resourceCollection);

        getResourceOrCollection(path, accepts, "application/json", 200);
    }

    @Test
    public void getCollectionNotFoundTest() throws DatasourceException, URISyntaxException {
        String path = "a";
        List <String> accepts = new LinkedList<>();
        accepts.add("application/xml");

        when(mongoResourceDAO.getResource(path)).thenReturn(null);
        when(mongoCollectionDAO.getCollection(path)).thenReturn(null);

        getResourceOrCollection(path, accepts, null, 404);
    }

    @Test
    public void getCollectionDatasourceExceptionTest() throws DatasourceException, URISyntaxException {
        String path = "a";
        List <String> accepts = new LinkedList<>();

        when(mongoResourceDAO.getResource(path)).thenReturn(null);
        when(mongoCollectionDAO.getCollection(path)).thenThrow(DatasourceException.class);

        getResourceOrCollection(path, accepts, null, 500);
    }

    @Test
    public void getCollectionJAXBExceptionTest() throws DatasourceException, URISyntaxException {
        String path = "a";
        List <String> accepts = new LinkedList<>();

        when(mongoResourceDAO.getResource(path)).thenReturn(null);
        when(mongoCollectionDAO.getCollection(path)).thenThrow(JAXBException.class);

        getResourceOrCollection(path, accepts, null, 500);
    }

    //POST Resource

    private void postResource(String path, Resource resource, List <String> contentType, int status) throws URISyntaxException {
        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Content-Type", contentType);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        Response returned = toTest.postResource(headers, path, resource);

        assertEquals(status, returned.getStatus());
    }

    @Test
    public void postResourceTest() throws URISyntaxException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection";
        int status = 201;
        Resource resource = generateResource("a", null, true, null);

        postResource(path, resource, contentType, status);
    }

    @Test
    public void postResourceTest2() throws URISyntaxException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection/collection";
        int status = 201;
        Resource resource = generateResource("a", null, true, null);

        postResource(path, resource, contentType, status);
    }

    @Test
    public void postResourceBadRequestTest() throws URISyntaxException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "";
        int status = 400;
        Resource resource = generateResource("a", null, true, null);

        postResource(path, resource, contentType, status);
    }

    @Test
    public void postResourceBadRequestTest2() throws URISyntaxException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = null;
        int status = 400;
        Resource resource = generateResource("a", null, true, null);

        postResource(path, resource, contentType, status);
    }

    @Test
    public void postResourceConflictTest() throws URISyntaxException, DatasourceException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection/collection2";
        int status = 409;
        Resource resource = generateResource("a", null, true, null);

        when(mongoResourceDAO.isResource(path)).thenReturn(true);

        postResource(path, resource, contentType, status);
    }

    @Test
    public void postResourceConflictTest2() throws URISyntaxException, DatasourceException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection/collection2";
        int status = 409;
        Resource resource = generateResource("a", null, true, null);

        when(mongoResourceDAO.isResource(path)).thenReturn(true);
        when(mongoCollectionDAO.getCollection(path+resource.getId())).thenReturn(null);

        postResource(path, resource, contentType, status);
    }

    @Test
    public void postResourceDatasourceExceptionTest() throws URISyntaxException, DatasourceException, SameIdException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection/collection2";
        int status = 500;
        Resource resource = generateResource("a", null, true, null);

        doThrow(DatasourceException.class).when(mongoResourceDAO).insertResource(resource);

        postResource(path, resource, contentType, status);
    }

    @Test
    public void postResourceURISyntaxExceptionTest() throws URISyntaxException, DatasourceException, SameIdException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection/collection2";
        int status = 500;
        Resource resource = generateResource("a", null, true, null);

        doThrow(URISyntaxException.class).when(mongoResourceDAO).insertResource(resource);

        postResource(path, resource, contentType, status);
    }

    @Test
    public void postResourceSameIdExceptionTest() throws URISyntaxException, DatasourceException, SameIdException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection/collection2";
        int status = 409;
        Resource resource = generateResource("a", null, true, null);

        doThrow(SameIdException.class).when(mongoResourceDAO).insertResource(resource);

        postResource(path, resource, contentType, status);
    }

    private void postResourceCollection(String path, ResourceCollection resourceCollection,List <String> contentType, int status) {
        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Content-Type", contentType);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        Response response = toTest.postResource(headers, path, resourceCollection);

        assertEquals(status, response.getStatus());
    }

    @Test
    public void postResourceCollectionNoPathTest() {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        int status = 201;
        ResourceCollection resourceCollection = generateResourceCollection("collection", null, true);

        postResourceCollection("", resourceCollection, contentType, status);
    }

    @Test
    public void postResourceCollectionTest() {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "";
        int status = 201;
        ResourceCollection resourceCollection = generateResourceCollection("collection", null, true);

        postResourceCollection(path, resourceCollection, contentType, status);
    }

    @Test
    public void postResourceCollectionTest2() {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection";
        int status = 201;
        ResourceCollection resourceCollection = generateResourceCollection("collection2", null, true);

        postResourceCollection(path, resourceCollection, contentType, status);
    }

    @Test
    public void postResourceCollectionConflictTest() throws DatasourceException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection/collection3";
        int status = 409;
        ResourceCollection resourceCollection = generateResourceCollection("collection2", null, true);

        when(mongoResourceDAO.isResource(path)).thenReturn(true);

        postResourceCollection(path, resourceCollection, contentType, status);
    }

    @Test
    public void postResourceCollectionConflictTest2() throws DatasourceException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection/collection3";
        int status = 409;
        ResourceCollection resourceCollection = generateResourceCollection("collection2", null, true);

        when(mongoResourceDAO.isResource(path)).thenReturn(false);
        when(mongoCollectionDAO.getCollection(path+"/"+resourceCollection.getName())).thenReturn(resourceCollection);

        postResourceCollection(path, resourceCollection, contentType, status);
    }

    @Test
    public void postResourceCollectionDatasourceExceptionTest() throws DatasourceException, SameIdException, SameIdException, SameIdException, SameIdException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection/collection3";
        int status = 500;
        ResourceCollection resourceCollection = generateResourceCollection("collection2", null, true);

        doThrow(DatasourceException.class).when(mongoCollectionDAO).insertCollection(resourceCollection);

        postResourceCollection(path, resourceCollection, contentType, status);
    }

    @Test
    public void postResourceCollectionURISyntaxExceptionTest() throws DatasourceException, SameIdException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection/collection3";
        int status = 500;
        ResourceCollection resourceCollection = generateResourceCollection("collection2", null, true);

        doThrow(URISyntaxException.class).when(mongoCollectionDAO).insertCollection(resourceCollection);

        postResourceCollection(path, resourceCollection, contentType, status);
    }

    @Test
    public void postResourceCollectionSameIdExceptionTest() throws DatasourceException, SameIdException {
        List <String> contentType = new LinkedList<>();
        contentType.add("application/json");

        String path = "collection/collection3";
        int status = 409;
        ResourceCollection resourceCollection = generateResourceCollection("collection2", null, true);

        doThrow(SameIdException.class).when(mongoCollectionDAO).insertCollection(resourceCollection);

        postResourceCollection(path, resourceCollection, contentType, status);
    }

    private void putResourceContent(String path, List <String> accepts, String contentType, String content, int status) {
        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        Response returned = toTest.putResourceContent(headers, path, contentType, content);

        assertEquals(status, returned.getStatus());
    }

    @Test
    public void putResourceTest() throws URISyntaxException, DatasourceException {
        String path = "a/b/c";
        String contentType = "application/rdf+xml";
        Resource resource = generateResource(contentType, null, true, null);

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        when(mongoResourceDAO.getResourceContent(eq(path))).thenReturn(resource);

        putResourceContent(path, accpeted, contentType, "content", 200);
    }

    @Test
    public void putResourceNotRDFTest() throws URISyntaxException, DatasourceException {
        String path = "a/b/c";
        String contentType = "application/not-rdf";
        Resource resource = generateResource("application/not-rdf", null, true, null);

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        when(mongoResourceDAO.getResourceContent(eq(path))).thenReturn(resource);

        putResourceContent(path, accpeted, contentType, "content", 200);
    }

    @Test
    public void putResourceNullTest() throws URISyntaxException, DatasourceException {
        String path = "a/b/c";
        String contentType = "application/rdf+xml";

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        when(mongoResourceDAO.getResourceContent(eq(path))).thenReturn(null);

        putResourceContent(path, accpeted, contentType, "content", 404);

    }

    @Test
    public void putResourceNoContentTypeTest() throws URISyntaxException, DatasourceException {
        String path = "a/b/c";
        String contentType = null;
        Resource resource = generateResource("application/rdf+xml", null, true, null);

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        when(mongoResourceDAO.getResourceContent(eq(path))).thenReturn(resource);

        putResourceContent(path, accpeted, contentType, "content", 415);
    }

    @Test
    public void putResourceNoContentTypeTest2() throws URISyntaxException, DatasourceException {
        String path = "a/b/c";
        String contentType = "";
        Resource resource = generateResource("application/rdf+xml", null, true, null);

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        when(mongoResourceDAO.getResourceContent(eq(path))).thenReturn(resource);

        putResourceContent(path, accpeted, contentType, "content", 415);
    }

    @Test
    public void putResourceErrorTest() throws URISyntaxException, DatasourceException, SameIdException {
        String path = "a/b/c";
        String contentType = "application/xml";

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        doThrow(DatasourceException.class).when(mongoResourceDAO).getResourceContent(path);

        putResourceContent(path, accpeted, contentType, "content", 500);
    }

    private void putResource(String path, List <String> accepts, Resource resource, int status) {
        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        Response returned = toTest.putResource(headers, path, resource);

        assertEquals(status, returned.getStatus());
    }

    @Test
    public void putResourceMetaTest() throws DatasourceException, SameIdException {
        String path = "a/b/c";
        Resource oldResource = generateResource("old", null, true, null);
        Resource newResource = generateResource("new", null, true, null);
        newResource.setContentMimeType(oldResource.getContentMimeType());
        newResource.setContentUrl(oldResource.getContentUrl());

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        when(mongoResourceDAO.getResource(eq(path))).thenReturn(oldResource);
        when(mongoResourceDAO.insertResource(anyObject())).thenReturn(null);

        putResource(path, accpeted, newResource, 200);
    }

    @Test
    public void putResourceMetaUrlErrorTest() throws DatasourceException, SameIdException {
        String path = "a/b/c";
        Resource oldResource = generateResource("old", null, true, null);
        Resource newResource = generateResource("new", null, true, null);
        newResource.setContentUrl(oldResource.getContentUrl());

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        when(mongoResourceDAO.getResource(eq(path))).thenReturn(oldResource);

        putResource(path, accpeted, newResource, 403);

    }

    @Test
    public void putResourceMetaMimeErrorTest() throws DatasourceException, SameIdException {
        String path = "a/b/c";
        Resource oldResource = generateResource("old", null, true, null);
        Resource newResource = generateResource("new", null, true, null);
        newResource.setContentMimeType(oldResource.getContentMimeType());

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        when(mongoResourceDAO.getResource(eq(path))).thenReturn(oldResource);

        putResource(path, accpeted, newResource, 403);
    }

    @Test
    public void putResourceMetaErrorTest() throws DatasourceException {
        String path = "a/b/c";
        Resource oldResource = generateResource("old", null, true, null);
        Resource newResource = generateResource("new", null, true, null);
        newResource.setContentMimeType(oldResource.getContentMimeType());
        newResource.setContentUrl(oldResource.getContentUrl());

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        when(mongoResourceDAO.getResource(eq(path))).thenReturn(oldResource);
        doThrow(DatasourceException.class).when(mongoResourceDAO).updateResource(eq(path), anyObject());

        putResource(path, accpeted, newResource, 500);
    }

    private void deleteResource(String path, List <String> accepts, int status) {
        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        Response returned = toTest.delete(headers, path);

        assertEquals(status, returned.getStatus());
    }

    @Test
    public void deleteResourceTest() throws DatasourceException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, null);

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);

        deleteResource(path, accpeted, 204);
    }

    @Test
    public void deleteResourceCollectionTest() throws DatasourceException {
        String path = "a/b/c";
        ResourceCollection collection = generateResourceCollection(path, null, true);

        List <String> accpeted = new LinkedList<>();

        when(mongoResourceDAO.getResource(path)).thenReturn(null);
        when(mongoCollectionDAO.findCollection(path)).thenReturn(collection);

        deleteResource(path, accpeted, 204);
    }

    @Test
    public void deleteNotFoundTest() throws DatasourceException {
        String path = "a/b/c";

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        when(mongoResourceDAO.getResource(path)).thenReturn(null);
        when(mongoCollectionDAO.findCollection(path)).thenReturn(null);

        deleteResource(path, accpeted, 404);
    }

    @Test
    public void deleteResourceErrorTest() throws DatasourceException {
        String path = "a/b/c";
        Resource resource = generateResource(path, null, true, null);

        List <String> accpeted = new LinkedList<>();
        accpeted.add("application/json");

        when(mongoResourceDAO.getResource(path)).thenReturn(resource);
        doThrow(DatasourceException.class).when(mongoResourceDAO).deleteResource(eq(path));

        deleteResource(path, accpeted, 500);
    }

    private Resource generateResource(String string, Date date, boolean creationDate, String mediaType) {
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
        if (mediaType == null) {
            resource.setContentMimeType(string + "ContentMimeType");
        } else {
            resource.setContentMimeType(mediaType);
        }
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

        collection.setName(string);
        collection.setCreator(string + "Creator");
        collection.setModificationDate(date);
        collection.setId(string + "Id");

        return collection;
    }

}
