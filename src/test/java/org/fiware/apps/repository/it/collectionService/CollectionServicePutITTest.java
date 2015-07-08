/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fiware.apps.repository.it.collectionService;

import org.fiware.apps.repository.it.IntegrationTestHelper;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.fiware.apps.repository.model.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionServicePutITTest {

    private IntegrationTestHelper client;
    private final static String collection = "collectionTestPut";
    private static String rdfXmlExample;
    private static String rdfJsonExample;
    private static String rdfN3Example;
    private static String rdfTurttleExample;
    private static String dataExample;

    public CollectionServicePutITTest() {
        client = new IntegrationTestHelper();
    }

    @BeforeClass
    public static void setUpClass() throws IOException {
        IntegrationTestHelper client = new IntegrationTestHelper();

        String fileName = "fileNameExample";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPut";
        String creator = "Me";
        String name = "resourceTestPut";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, null, contentUrl, null, creator, null, null, name);

        String auxString = "";
        FileReader file = new FileReader("src/test/resources/rdf+xml.rdf");
        BufferedReader buffer = new BufferedReader(file);
        while(buffer.ready()) {
            auxString = auxString.concat(buffer.readLine());
        }
        buffer.close();
        rdfXmlExample = auxString;

        auxString = "";
        file = new FileReader("src/test/resources/rdf+json.rdf");
        buffer = new BufferedReader(file);
        while(buffer.ready()) {
            auxString = auxString.concat(buffer.readLine());
        }
        buffer.close();
        rdfJsonExample = auxString;

        auxString = "";
        file = new FileReader("src/test/resources/N3.rdf");
        buffer = new BufferedReader(file);
        while(buffer.ready()) {
            auxString = auxString.concat(buffer.readLine());
        }
        buffer.close();
        rdfN3Example = auxString;

        auxString = "";
        file = new FileReader("src/test/resources/turttle.rdf");
        buffer = new BufferedReader(file);
        while(buffer.ready()) {
            auxString = auxString.concat(buffer.readLine());
        }
        buffer.close();
        rdfTurttleExample = auxString;

        dataExample = "Data example with nothing inside.";

        //Delete the collection
        List <Header> headers = new LinkedList<>();
        client.deleteCollection(collection, headers);

        //Create a resource in the repository
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collection, IntegrationTestHelper.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        resource.setName(name+"name");
        resource.setContentUrl(contentUrl+name);
        response = client.postResourceMeta(collection, IntegrationTestHelper.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
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
    Resource meta tests
    */

    @Test
    public void putResourceMetaJsonTest() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String name = "resourceTestPut";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, null, null, null, creator, null, null, name);

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.putResourceMeta(collection+"/"+name, IntegrationTestHelper.resourceToJson(resource), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceMetaXmlTest() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String name = "resourceTestPut";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, null, null, null, creator, null, null, name);

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/xml"));
        HttpResponse response = client.putResourceMeta(collection+"/"+name, IntegrationTestHelper.resourceToXML(resource), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceMetaNewNameTest() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String name = "resourceTestPutname";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, null, null, null, creator, null, null, "nombre");

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.putResourceMeta(collection+"/"+name, IntegrationTestHelper.resourceToJson(resource), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceMetaForbiddenTest() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String name = "resourceTestPut";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, "application/xml", null, null, creator, null, null, name);

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.putResourceMeta(collection+"/"+name, IntegrationTestHelper.resourceToJson(resource), headers);
        assertEquals(403, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceMetaForbiddenTest2() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String name = "resourceTestPut";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, null, "newUrlContent", null, creator, null, null, name);

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.putResourceMeta(collection+"/"+name, IntegrationTestHelper.resourceToJson(resource), headers);
        assertEquals(403, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putNewResourceMetaNewNameTest() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPut2";
        String name = "resourceTestPut2";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, null, contentUrl, null, creator, null, null, name);

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.putResourceMeta(collection+"/"+name, IntegrationTestHelper.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putNewResourceMetaBadNameTest() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPut2";
        String name = "resourceTestPut2";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, null, contentUrl, null, creator, null, null, "name.meta");

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.putResourceMeta(collection+"/"+name, IntegrationTestHelper.resourceToJson(resource), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    /*
    Resource content tests
    */

    @Test
    public void putResourceContentRDFXmlTest() throws IOException {
        String name = "resourceTestPut";

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/rdf+xml"));
        HttpResponse response = client.putResourceContent(collection+"/"+name, rdfXmlExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        response = client.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));
        response = client.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceContentRDFJsonTest() throws IOException {
        String name = "resourceTestPut";

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/rdf+json"));
        HttpResponse response = client.putResourceContent(collection+"/"+name, rdfJsonExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers.add(new BasicHeader("Accept", "application/rdf+json"));
        response = client.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        response = client.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceContentRDFTurtleTest() throws IOException {
        String name = "resourceTestPut";

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "text/turtle"));
        HttpResponse response = client.putResourceContent(collection+"/"+name, rdfTurttleExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers.add(new BasicHeader("Accept", "text/turtle"));
        response = client.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        response = client.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceContentRDFN3Test() throws IOException {
        String name = "resourceTestPut";

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "text/N3"));
        HttpResponse response = client.putResourceContent(collection+"/"+name, rdfN3Example, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers.add(new BasicHeader("Accept", "text/N3"));
        response = client.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        response = client.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceContentNoRDFTest() throws IOException {
        String name = "resourceTestPut";

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/data"));
        HttpResponse response = client.putResourceContent(collection+"/"+name, dataExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        response = client.getResourceContent(collection+"/"+name, headers);
        assertEquals(204, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceContentRDFErrorTest() throws IOException {
        String name = "resourceTestPut";

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/rdf+xml"));
        HttpResponse response = client.putResourceContent(collection+"/"+name, rdfJsonExample, headers);
        assertEquals(500, response.getStatusLine().getStatusCode());
    }
}
