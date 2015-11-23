package org.fiware.apps.repository.it.collectionService;

import org.fiware.apps.repository.it.IntegrationTestHelper;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
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

    private IntegrationTestHelper helper;
    private final String collection = "collectionTestDelete";

    public CollectionServiceDeleteITTest() throws IOException, ServletException {
        helper = new IntegrationTestHelper();
        
    }

    @BeforeClass
    public static void setUpClass() {
        
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        helper.createEnviroment();
        helper.startEnviroment();

        String fileName = "fileName";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestDelete";
        String creator = "Me";
        String name = "resourceTestDelete";
        Resource resource = helper.generateResource(null, fileName, null, contentUrl, null, creator, null, null, name);

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));

        //Delete the collection
        helper.deleteCollection(collection, headers);

        //Create a resource
        HttpResponse response = helper.postResourceMeta(collection, helper.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @After
    public void tearDown() throws LifecycleException {
        helper.stopEnviroment();
        helper.destroyEnviroment();
    }

    /*
    Resource tests
    */

    @Test
    public void deleteResource() throws IOException {
        String name = "resourceTestDelete";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.deleteResource(collection+"/"+name, headers);
        assertEquals(204, response.getStatusLine().getStatusCode());

        response = helper.getResourceMeta(collection+"/"+name, headers);
        assertEquals(404, response.getStatusLine().getStatusCode());

        response = helper.getCollection(collection, headers);
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
        HttpResponse response = helper.deleteCollection(collection, headers);
        assertEquals(204, response.getStatusLine().getStatusCode());

        response = helper.getResourceMeta(collection+"/"+name, headers);
        assertEquals(404, response.getStatusLine().getStatusCode());

        response = helper.getCollection(collection, headers);
        assertEquals(404, response.getStatusLine().getStatusCode());
    }
}
