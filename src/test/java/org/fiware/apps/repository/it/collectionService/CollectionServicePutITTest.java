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

public class CollectionServicePutITTest {

    private IntegrationTestHelper helper;
    private final static String collection = "collectionTestPut";
    private final String rdfXmlExample;
    private final String rdfJsonExample;
    private final String rdfN3Example;
    private final String rdfTurttleExample;
    private final String dataExample;

    public CollectionServicePutITTest() throws IOException, ServletException {
        helper = new IntegrationTestHelper();

        rdfXmlExample = helper.readRDFFile("src/test/resources/rdf+xml.rdf");
        rdfJsonExample = helper.readRDFFile("src/test/resources/rdf+json.rdf");
        rdfN3Example = helper.readRDFFile("src/test/resources/N3.rdf");
        rdfTurttleExample = helper.readRDFFile("src/test/resources/turttle.rdf");
        
        dataExample = "Data example with nothing inside.";
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

        String fileName = "fileNameExample";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPut";
        String creator = "Me";
        String name = "resourceTestPut";
        Resource resource = helper.generateResource(null, fileName, null, contentUrl, null, creator, null, null, name);

        List <Header> headers = new LinkedList<>();

        //Create a resource in the repository
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.postResourceMeta(collection, helper.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        resource.setName(name+"New");
        resource.setContentUrl(contentUrl+"New");
        response = helper.postResourceMeta(collection, helper.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
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
    Resource meta tests
    */
    @Test
    public void putResourceMetaJsonTest() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String name = "resourceTestPut";
        Resource resource = helper.generateResource(null, fileName, null, null, null, creator, null, null, name);

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.putResourceMeta(collection+"/"+name, helper.resourceToJson(resource), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceMetaXmlTest() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String name = "resourceTestPut";
        Resource resource = helper.generateResource(null, fileName, null, null, null, creator, null, null, name);

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/xml"));
        HttpResponse response = helper.putResourceMeta(collection+"/"+name, helper.resourceToXML(resource), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceMetaNewNameTest() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String name = "resourceTestPutname";
        Resource resource = helper.generateResource(null, fileName, null, null, null, creator, null, null, "nombre");

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.putResourceMeta(collection+"/"+name, helper.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceMetaForbiddenTest() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String name = "resourceTestPut";
        Resource resource = helper.generateResource(null, fileName, "application/xml", null, null, creator, null, null, name);

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.putResourceMeta(collection+"/"+name, helper.resourceToJson(resource), headers);
        assertEquals(403, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceMetaForbiddenTest2() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String name = "resourceTestPut";
        Resource resource = helper.generateResource(null, fileName, null, "newUrlContent", null, creator, null, null, name);

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.putResourceMeta(collection+"/"+name, helper.resourceToJson(resource), headers);
        assertEquals(403, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putNewResourceMetaNewNameTest() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPut"+"New";
        String name = "resourceTestPut"+"New";
        Resource resource = helper.generateResource(null, fileName, null, contentUrl, null, creator, null, null, name);

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.putResourceMeta(collection+"/"+name, helper.resourceToJson(resource), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putNewResourceMetaBadNameTest() throws IOException {
        String fileName = "fileName";
        String creator = "Me";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPut2";
        String name = "resourceTestPut2";
        Resource resource = helper.generateResource(null, fileName, null, contentUrl, null, creator, null, null, "name.meta");

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.putResourceMeta(collection+"/"+name, helper.resourceToJson(resource), headers);
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
        HttpResponse response = helper.putResourceContent(collection+"/"+name, rdfXmlExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        response = helper.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));
        response = helper.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceContentRDFJsonTest() throws IOException {
        String name = "resourceTestPut";

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/rdf+json"));
        HttpResponse response = helper.putResourceContent(collection+"/"+name, rdfJsonExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers.add(new BasicHeader("Accept", "application/rdf+json"));
        response = helper.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        response = helper.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceContentRDFTurtleTest() throws IOException {
        String name = "resourceTestPut";

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "text/turtle"));
        HttpResponse response = helper.putResourceContent(collection+"/"+name, rdfTurttleExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers.add(new BasicHeader("Accept", "text/turtle"));
        response = helper.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        response = helper.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceContentRDFN3Test() throws IOException {
        String name = "resourceTestPut";

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "text/N3"));
        HttpResponse response = helper.putResourceContent(collection+"/"+name, rdfN3Example, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers.add(new BasicHeader("Accept", "text/N3"));
        response = helper.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        response = helper.getResourceContent(collection+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceContentNoRDFTest() throws IOException {
        String name = "resourceTestPut";

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/data"));
        HttpResponse response = helper.putResourceContent(collection+"/"+name, dataExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        response = helper.getResourceContent(collection+"/"+name, headers);
        assertEquals(204, response.getStatusLine().getStatusCode());
    }

    @Test
    public void putResourceContentRDFErrorTest() throws IOException {
        String name = "resourceTestPut";

        //Update resource meta data
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/rdf+xml"));
        HttpResponse response = helper.putResourceContent(collection+"/"+name, rdfJsonExample, headers);
        assertEquals(500, response.getStatusLine().getStatusCode());
    }
}
