package org.fiware.apps.repository.it.collectionService;

import org.fiware.apps.repository.it.IntegrationTestHelper;
import java.io.IOException;
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

public class CollectionServiceDeleteITTest {

    private IntegrationTestHelper client;
    private final String collection = "collectionTestDelete";

    public CollectionServiceDeleteITTest() {
        client = new IntegrationTestHelper();
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IOException {

        String fileName = "fileName";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestDelete";
        String creator = "Me";
        String name = "resourceTestDelete";
        Resource resource = IntegrationTestHelper.generateResource(null, fileName, null, contentUrl, null, creator, null, null, name);

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));

        //Delete the collection
        HttpResponse response = client.deleteCollection(collection, headers);

        //Create a resource
        response = client.postResourceMeta(collection, client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @After
    public void tearDown() {
    }

    /*
    Resource tests
    */

    @Test
    public void deleteResource() throws IOException {
        String name = "resourceTestDelete";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.deleteResource(collection+"/"+name, headers);
        assertEquals(204, response.getStatusLine().getStatusCode());

        response = client.getResourceMeta(collection+"/"+name, headers);
        assertEquals(404, response.getStatusLine().getStatusCode());

        response = client.getCollection(collection, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    /*
    Collection tests
    */

    @Test
    public void deleteCollection() throws IOException {
        String name = "resourceTestDelete";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.deleteCollection(collection, headers);
        assertEquals(204, response.getStatusLine().getStatusCode());

        response = client.getResourceMeta(collection+"/"+name, headers);
        assertEquals(404, response.getStatusLine().getStatusCode());

        response = client.getCollection(collection, headers);
        assertEquals(404, response.getStatusLine().getStatusCode());
    }
}
