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
package org.fiware.apps.repository.it.collectionService;

import org.fiware.apps.repository.it.IntegrationTestHelper;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
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

public class CollectionServiceGetITTest {

    private IntegrationTestHelper helper;
    private final String collection = "collectionTestGet";
    private static String rdfxmlExample;

    public CollectionServiceGetITTest() throws IOException, ServletException {
        helper = new IntegrationTestHelper();
        rdfxmlExample = helper.readRDFFile("src/test/resources/rdf+xml.rdf");
        
    }

    @BeforeClass
    public static void setUpClass() throws IOException {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
        helper.createEnviroment();
        helper.startEnviroment();

        String fileName = "fileNameExample";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestGet";
        String creator = "Me";
        String name = "resourceTest";
        Resource resource = helper.generateResource(null, fileName, null, contentUrl, null, creator, null, null, name);
        List <Header> headers = new LinkedList<>();

        //Create a resource in the repository
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.postResourceMeta(collection, helper.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        //Insert RDF content in the resource
        headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/rdf+xml"));
        response = helper.putResourceContent(collection+"/"+name, rdfxmlExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @After
    public void tearDown() throws Exception {
        //Delete the collection
        List <Header> headers = new LinkedList<>();
        helper.deleteCollection(collection, headers);

        helper.stopEnviroment();
        helper.destroyEnviroment();
    }

    /*
    Resource Meta Tests
    */
    private void getResourceMeta(String name, String type, String typeExpected, int status) throws IOException {
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", type));
        HttpResponse response = helper.getResourceMeta(name, headers);

        assertEquals(status, response.getStatusLine().getStatusCode());
        if (typeExpected != null) {
            assertEquals(type, response.getEntity().getContentType().getValue());
        }
    }

    @Test
    public void getResourceRootTest() throws IOException {
        String resource = "";
        String type = "application/xml";
        getResourceMeta(resource, type, type, 404);
    }

    @Test
    public void getResourceMetaXmlTest() throws IOException {
        String resource = collection + "/resourceTest";
        String type = "application/xml";
        getResourceMeta(resource, type, type, 200);
    }

    @Test
    public void getResourceMetaJsonTest() throws IOException {
        String resource = collection + "/resourceTest";
        String type = "application/json";
        getResourceMeta(resource, type, type, 200);
    }

    @Test
    public void getResourceMetaHtmlTest() throws IOException {
        String resource = collection + "/resourceTest";
        String type = "text/html";
        getResourceMeta(resource, type, type, 200);
    }

    @Test
    public void getResourceMetaTextTest() throws IOException {
        String resource = collection + "/resourceTest";
        String type = "text/plain";
        getResourceMeta(resource, type, type, 200);
    }

    @Test
    public void getResourceMetaRDFxmlTest() throws IOException {
        String resource = collection + "/resourceTest";
        String type = "application/rdf+xml";
        getResourceMeta(resource, type, type, 200);
    }

    @Test
    public void getResourceMetaTurtleTest() throws IOException {
        String resource = collection + "/resourceTest";
        String type = "text/turtle";
        getResourceMeta(resource, type, type, 200);
    }

    @Test
    public void getResourceMetaN3Test() throws IOException {
        String resource = collection + "/resourceTest";
        String type = "text/n3";
        getResourceMeta(resource, type, type, 200);
    }

    @Test
    public void getResourceMetaNotAcceptableTest() throws IOException {
        String resource = collection + "/resourceTest";
        String type = "not/acceptable";
        getResourceMeta(resource, type, null, 406);
    }

    /*
    Resource Content Tests
    */
    private void getResourceContent(String name, String type, String typeExpected, int status) throws IOException {
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", type));
        HttpResponse response = helper.getResourceContent(name, headers);

        assertEquals(status, response.getStatusLine().getStatusCode());
        if (typeExpected != null) {
            assertEquals(type, response.getEntity().getContentType().getValue());
        }
    }

    @Test
    public void getResourceContentRDFxmlTest() throws IOException {
        String resource = collection + "/resourceTest";
        String type = "application/rdf+xml";
        getResourceContent(resource, type, type, 200);
    }

    @Test
    public void getResourceContentTurtleTest() throws IOException {
        String resource = collection + "/resourceTest";
        String type = "text/turtle";
        getResourceContent(resource, type, type, 200);
    }

    @Test
    public void getResourceContentN3Test() throws IOException {
        String resource = collection + "/resourceTest";
        String type = "text/n3";
        getResourceContent(resource, type, type, 200);
    }

    @Test
    public void getResourceContentNotAcceptableTest() throws IOException {
        String resource = collection + "/resourceTest";
        String type = "not/acceptable";
        getResourceContent(resource, type, null, 406);
    }

    //TODO: Test para cuando el contenido no sea RDF

    /*
    Collection Meta Tests
    */
    private void getCollection(String name, String type, String typeExpected, int status) throws IOException {
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", type));
        HttpResponse response = helper.getCollection(name, headers);

        assertEquals(status, response.getStatusLine().getStatusCode());
        if (typeExpected != null) {
            assertEquals(type, response.getEntity().getContentType().getValue());
        }
    }

    @Test
    public void getCollectionMetaXmlTest() throws IOException {
        String type = "application/xml";
        getCollection(collection, type, type, 200);
    }

    @Test
    public void getCollectionMetaJsonTest() throws IOException {
        String type = "application/json";
        getCollection(collection, type, type, 200);
    }

    @Test
    public void getCollectionMetaHtmlTest() throws IOException {
        String type = "text/html";
        getCollection(collection, type, type, 200);
    }

    @Test
    public void getCollectionMetaTextTest() throws IOException {
        String type = "text/plain";
        getCollection(collection, type, type, 200);
    }

    @Test
    public void getCollectionMetaRDFxmlTest() throws IOException {
        String type = "application/rdf+xml";
        getCollection(collection, type, type, 200);
    }

    @Test
    public void getCollectionMetaTurtleTest() throws IOException {
        String type = "text/turtle";
        getCollection(collection, type, type, 200);
    }

    @Test
    public void getCollectionMetaN3Test() throws IOException {
        String type = "text/n3";
        getCollection(collection, type, type, 200);
    }

    @Test
    public void getCollectionMetaNotAcceptableTest() throws IOException {
        String type = "not/acceptable";
        getCollection(collection, type, null, 406);
    }
}
