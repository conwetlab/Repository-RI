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
import org.fiware.apps.repository.model.ResourceCollection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionServicePostITTest {

    private IntegrationTestHelper helper;
    private static final String collection = "collectionTestPost";

    public CollectionServicePostITTest() throws IOException, ServletException {
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
    }

    @After
    public void tearDown() throws Exception {
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        helper.deleteCollection(collection, headers);

        helper.stopEnviroment();
        helper.destroyEnviroment();
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
        Resource resource = helper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.postResourceMeta(collection, helper.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResource2JsonTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost2";
        String creator = "Me";
        String name = "resourceTestPost2";
        Resource resource = helper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.postResourceMeta(collection + "/" + collection, helper.resourceToJson(resource), headers);
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
        Resource resource = helper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/xml"));
        HttpResponse response = helper.postResourceMeta(collection, helper.resourceToXML(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResource2XmlTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost4";
        String creator = "Me";
        String name = "resourceTestPost4";
        Resource resource = helper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/xml"));
        HttpResponse response = helper.postResourceMeta(collection + "/" + collection, helper.resourceToXML(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResourceConflictTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost5";
        String creator = "Me";
        String name = "resourceTestPost5";
        Resource resource = helper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.postResourceMeta(collection, helper.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        response = helper.postResourceMeta(collection, helper.resourceToJson(resource), headers);
        assertEquals(409, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResourceConflictUrlTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost6";
        String creator = "Me";
        String name = "resourceTestPost6";
        Resource resource = helper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.postResourceMeta(collection, helper.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        resource.setName("resourceTestPost7");
        response = helper.postResourceMeta(collection, helper.resourceToJson(resource), headers);
        assertEquals(409, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResourceBadRequestTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost7";
        String creator = "Me";
        String name = "resourceTestPost7.meta";
        Resource resource = helper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.postResourceMeta(collection, helper.resourceToJson(resource), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResourceNoCollectionTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost8";
        String creator = "Me";
        String name = "resourceTestPost8";
        Resource resource = helper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.postResourceMeta("", helper.resourceToJson(resource), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postResourceBadCollectionTest() throws IOException {
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestPost9";
        String creator = "Me";
        String name = "resourceTestPost9";
        Resource resource = helper.generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.postResourceMeta(collection+"/"+collection+".a", helper.resourceToJson(resource), headers);
        assertEquals(409, response.getStatusLine().getStatusCode());
    }

    /*
    Collection tests
    */
    @Test
    public void postCollectionJsonTest() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTest1";
        ResourceCollection resourceCollection = helper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        helper.deleteCollection(name, headers);
        HttpResponse response = helper.postCollection("", helper.collectionToJson(resourceCollection), headers);
        helper.deleteCollection(name, headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postCollectionJsonTest2() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTest2";
        ResourceCollection resourceCollection = helper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        helper.deleteCollection(name, headers);
        HttpResponse response = helper.postCollection(collection+"/"+collection, helper.collectionToJson(resourceCollection), headers);
        helper.deleteCollection(name, headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postCollectionXmlTest() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTest3";
        ResourceCollection resourceCollection = helper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/xml"));
        helper.deleteCollection(name, headers);
        HttpResponse response = helper.postCollection("", helper.collectionToXml(resourceCollection), headers);
        helper.deleteCollection(name, headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postCollectionXmlTest2() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTest4";
        ResourceCollection resourceCollection = helper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/xml"));
        helper.deleteCollection(name, headers);
        HttpResponse response = helper.postCollection(collection+"/"+collection, helper.collectionToXml(resourceCollection), headers);
        helper.deleteCollection(name, headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postCollectionInACollection() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTestN";
        ResourceCollection resourceCollection = helper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        helper.deleteCollection("resourceCollectionIn", headers);
        HttpResponse response = helper.postCollection("resourceCollectionIn/A/B/C", helper.collectionToJson(resourceCollection), headers);
        helper.deleteCollection("resourceCollectionIn", headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
        assertEquals("http://localhost:"+helper.getTomcatPort()+"/FiwareRepository/v2/collec/resourceCollectionIn/A/B/C/resourceCollectionTestN", response.getHeaders("Content-Location")[0].getValue());
    }

    @Test
    public void postCollectionBadNameTest() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTest5.meta";
        ResourceCollection resourceCollection = helper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        helper.deleteCollection(name, headers);
        HttpResponse response = helper.postCollection("", helper.collectionToJson(resourceCollection), headers);
        helper.deleteCollection(name, headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postCollectionConflictTest() throws IOException {
        String creator = "Me";
        String name = "resourceCollectionTest6";
        ResourceCollection resourceCollection = helper.generateResourceCollection(null, name, creator, null, null);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        helper.deleteCollection(name, headers);
        helper.postCollection("", helper.collectionToJson(resourceCollection), headers);
        HttpResponse response = helper.postCollection("", helper.collectionToJson(resourceCollection), headers);
        helper.deleteCollection(name, headers);
        assertEquals(409, response.getStatusLine().getStatusCode());
    }

}
