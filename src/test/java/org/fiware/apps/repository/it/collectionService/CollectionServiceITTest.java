/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package org.fiware.apps.repository.it.collectionService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jortiz
 */
public class GetResourceMetaTest {

    /*private CollectionServiceTestClient collectionServiceTestClient;

    public GetResourceMetaTest() {
        collectionServiceTestClient = new CollectionServiceTestClient();
    }

    @BeforeClass
    public static void setUpClass() {
        CollectionServiceTestClient client = new CollectionServiceTestClient();
        //Crear los recursos que sean necesarios para los diferentes test

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));

        //client.postResourceMeta("", "", headers);

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

    @Test
    public void getResourceMetaXMLTest() {
        String resourceId = "";
        HttpResponse response = null;
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/xml"));

        try {
            response = collectionServiceTestClient.getResourceMeta(resourceId, headers);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        assertEquals(200, response.getStatusLine().getStatusCode());
        //TODO:compare
        //TODO:compare response.
    }

    @Test
    public void getResourceMetaJSONTest() {
        String resourceId = "";
        HttpResponse response = null;
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));

        try {
            response = collectionServiceTestClient.getResourceMeta(resourceId, headers);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        assertEquals(200, response.getStatusLine().getStatusCode());

        //TODO:compare response.
    }

    @Test
    public void getResourceMetaRDFXMLTest() {
        String resourceId = "";
        HttpResponse response = null;
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));

        try {
            response = collectionServiceTestClient.getResourceMeta(resourceId, headers);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        assertEquals(200, response.getStatusLine().getStatusCode());
        //TODO:compare
        //TODO:compare response.
    }

    @Test
    public void getResourceMetaRDFJSONTest() {
        String resourceId = "";
        HttpResponse response = null;
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));

        try {
            response = collectionServiceTestClient.getResourceMeta(resourceId, headers);
        } catch (IOException ex) {
            fail(ex.getMessage());
        }

        assertEquals(200, response.getStatusLine().getStatusCode());
        //TODO:compare
        //TODO:compare response.
    }*/
}
