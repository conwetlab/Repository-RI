/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package org.fiware.apps.repository.it.collectionService;

import org.fiware.apps.repository.it.IntegrationTestHelper;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionServicePostITTest {

    private IntegrationTestHelper client;
    private static final String collection = "collectionTestPost";

    public CollectionServicePostITTest() {
        client = new IntegrationTestHelper();
    }

    @BeforeClass
    public static void setUpClass() throws IOException {
        IntegrationTestHelper client = new IntegrationTestHelper();
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));

        //Delete the collection
        client.deleteCollection(collection, headers);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /*
    Resource tests
    */
    @Test
    public void postResourceJsonTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost";
        String creator = "Me";
        String name = "resourceTestPost";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collection, client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResource2JsonTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost2";
        String creator = "Me";
        String name = "resourceTestPost2";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collection + "/" + collection, client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    //TODO: json mal construido

    @Test
    public void postResourceXmlTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost3";
        String creator = "Me";
        String name = "resourceTestPost3";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/xml"));
        HttpResponse response = client.postResourceMeta(collection, client.resourceToXML(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResource2XmlTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost4";
        String creator = "Me";
        String name = "resourceTestPost4";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/xml"));
        HttpResponse response = client.postResourceMeta(collection + "/" + collection, client.resourceToXML(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResourceConflictTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost5";
        String creator = "Me";
        String name = "resourceTestPost5";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collection, client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        response = client.postResourceMeta(collection, client.resourceToJson(resource), headers);
        assertEquals(409, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResourceConflictUrlTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost6";
        String creator = "Me";
        String name = "resourceTestPost6";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collection, client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        resource.setName("resourceTestPost7");
        response = client.postResourceMeta(collection, client.resourceToJson(resource), headers);
        assertEquals(409, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResourceBadRequestTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost7";
        String creator = "Me";
        String name = "resourceTestPost7.meta";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collection, IntegrationTestHelper.resourceToJson(resource), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResourceNoCollectionTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost8";
        String creator = "Me";
        String name = "resourceTestPost8";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta("", IntegrationTestHelper.resourceToJson(resource), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResourceBadCollectionTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost9";
        String creator = "Me";
        String name = "resourceTestPost9";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collection+"/"+collection+".a", IntegrationTestHelper.resourceToJson(resource), headers);
        assertEquals(409, response.getStatusLine().getStatusCode());
    }

    /*
    Collection tests
    */
    @Test
    public void postCollectionJsonTest() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTest1";
        ResourceCollection resourceCollection = IntegrationTestHelper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        client.deleteCollection(name, headers);
        HttpResponse response = client.postCollection("", IntegrationTestHelper.collectionToJson(resourceCollection), headers);
        client.deleteCollection(name, headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postCollectionJsonTest2() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTest2";
        ResourceCollection resourceCollection = IntegrationTestHelper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        client.deleteCollection(name, headers);
        HttpResponse response = client.postCollection(collection+"/"+collection, IntegrationTestHelper.collectionToJson(resourceCollection), headers);
        client.deleteCollection(name, headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postCollectionXmlTest() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTest3";
        ResourceCollection resourceCollection = IntegrationTestHelper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/xml"));
        client.deleteCollection(name, headers);
        HttpResponse response = client.postCollection("", IntegrationTestHelper.collectionToXml(resourceCollection), headers);
        client.deleteCollection(name, headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postCollectionXmlTest2() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTest4";
        ResourceCollection resourceCollection = IntegrationTestHelper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/xml"));
        client.deleteCollection(name, headers);
        HttpResponse response = client.postCollection(collection+"/"+collection, IntegrationTestHelper.collectionToXml(resourceCollection), headers);
        client.deleteCollection(name, headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postCollectionBadNameTest() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTest5.meta";
        ResourceCollection resourceCollection = IntegrationTestHelper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        client.deleteCollection(name, headers);
        HttpResponse response = client.postCollection("", IntegrationTestHelper.collectionToJson(resourceCollection), headers);
        client.deleteCollection(name, headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postCollectionConflictTest() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTest6";
        ResourceCollection resourceCollection = IntegrationTestHelper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        client.deleteCollection(name, headers);
        client.postCollection("", IntegrationTestHelper.collectionToJson(resourceCollection), headers);
        HttpResponse response = client.postCollection("", IntegrationTestHelper.collectionToJson(resourceCollection), headers);
        client.deleteCollection(name, headers);
        assertEquals(409, response.getStatusLine().getStatusCode());
    }

}
