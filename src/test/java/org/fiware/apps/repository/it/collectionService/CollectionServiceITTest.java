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
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.fiware.apps.repository.model.ResourceCollection;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionServiceITTest {

    private IntegrationTestHelper client;
    private static String rdfxmlExample;
    private static String rdfjsonExample;

    public CollectionServiceITTest() {
        client = new IntegrationTestHelper();
    }

    @BeforeClass
    public static void setUpClass() throws IOException {
        //Delete test collection.
        IntegrationTestHelper client = new IntegrationTestHelper();
        List <Header> headers = new LinkedList<>();
        client.deleteCollection("collectionTest1", headers);

        String auxString = "";
        FileReader file = new FileReader("src/test/resources/rdf+xml.rdf");
        BufferedReader buffer = new BufferedReader(file);
        while(buffer.ready()) {
            auxString = auxString.concat(buffer.readLine());
        }
        buffer.close();
        rdfxmlExample = auxString;

        auxString = "";
        file = new FileReader("src/test/resources/rdf+json.rdf");
        buffer = new BufferedReader(file);
        while(buffer.ready()) {
            auxString = auxString.concat(buffer.readLine());
        }
        buffer.close();
        rdfjsonExample = auxString;

    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        //Delete test collection.
        IntegrationTestHelper client = new IntegrationTestHelper();
        List <Header> headers = new LinkedList<>();
        client.deleteCollection("collectionTest1", headers);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /*
    Test que crea un recurso
    */
    @Test
    public void createResourceTest() throws JsonProcessingException, IOException {
        String collections = "collectionTest1";
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTest1";
        String creator = "Me";
        String name = "resourceTest1";
        Resource resource = generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collections, client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    /*
    Test que crea un recurso, comprueba que existe y lo elimina
    */
    @Test
    public void createAndDeleteResourceTest() throws JsonProcessingException, IOException {
        String collections = "collectionTest1";
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTest2";
        String creator = "";
        String name = "resourceTest2";
        Resource resource = generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create un resource in the repository
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collections, client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        //Get the resource meta
        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        response = client.getResourceMeta(collections+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        //Delete the resource
        response = client.deleteResource(collections+"/"+name, headers);
        assertEquals(204, response.getStatusLine().getStatusCode());
    }

    /*
    Test que crea un recurso e inserta un contenido
    */
    @Test
    public void createAndinsertResourceTest() throws JsonProcessingException, IOException {
        String collections = "collectionTest1";
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTest3";
        String creator = "";
        String name = "resourceTest3";
        Resource resource = generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create un resource in the repository
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collections, client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        //Get the resource meta
        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        response = client.getResourceMeta(collections+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        //Insert a RDF content in the repository
        headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/rdf+xml"));
        response = client.putResourceContent(collections+"/"+name, rdfxmlExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    /*
    Test que crea un recurso y lo modifica
    */
    @Test
    public void createAndEditResourceTest() throws IOException {
        String collections = "collectionTest1";
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTest4";
        String creator = "";
        String name = "resourceTest4";
        Resource resource = generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Add a resource in the repository
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collections, client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        resource = generateResource(null, fileName, null, contentUrl, null, creator, null, null, name+".1");

        //Modify the resource in the repository
        response = client.putResourceMeta(collections+"/"+name, client.resourceToJson(resource), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        //Check the old resource is not in the repository
        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        response = client.getResourceMeta(collections+"/"+name, headers);
        assertEquals(404, response.getStatusLine().getStatusCode());

        //Check the new resource is in the repository
        response = client.getResourceMeta(collections+"/"+name+".1", headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    /*
    Test que crea un recurso inserta contenido y lo modifica
    */
    @Test
    public void createInsertAndModifyResourceTest() throws IOException {
        String collections = "collectionTest1";
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTest5";
        String creator = "";
        String name = "resourceTest5";
        Resource resource = generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource in the repository
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collections, client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        //Insert a RDF content in the repository
        headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/rdf+xml"));
        response = client.putResourceContent(collections+"/"+name, rdfxmlExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        //Get the resource content
        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        response = client.getResourceContent(collections+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        //Update de the resource content
        headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/rdf+json"));
        response = client.putResourceContent(collections+"/"+name, rdfjsonExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());

        //Get the new resource content
        headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        response = client.getResourceContent(collections+"/"+name, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    /*
    Test que crea un recurso lo elimina y lo vuelve a crear
    */
    @Test
    public void createDeleteAndCreateResourceTest() throws IOException {
        String collections = "collectionTest1";
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTest6";
        String creator = "";
        String name = "resourceTest6";
        Resource resource = generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Add a resource in the repository
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta(collections, client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        //Delete the resource in the repository
        response = client.deleteResource(collections+"/"+name, headers);
        assertEquals(204, response.getStatusLine().getStatusCode());

        //Add the resource again in the repository
        response = client.postResourceMeta(collections, client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());
    }

    @Test
    public void createResourceConflictTest() throws IOException {
        String collections = "collectionTest1";
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/resourceTestConflict";
        String creator = "Me";
        String name = "resourceTestConflict";
        Resource resource = generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        client.postResourceMeta(collections, client.resourceToJson(resource), headers);
        HttpResponse response = client.postResourceMeta(collections, client.resourceToJson(resource), headers);
        assertEquals(409, response.getStatusLine().getStatusCode());
    }

    @Test
    public void createResourceFailTest() throws IOException {
        String collections = "collectionTest1";
        String fileName = "";
        String mimeType = "";
        String contentUrl = "http://localhost:8080/contentUrl/badName";
        String creator = "Me";
        String name = "badname.meta";
        Resource resource = generateResource(null, fileName, mimeType, contentUrl, null, creator, null, null, name);

        //Create a resource
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        client.postResourceMeta(collections, client.resourceToJson(resource), headers);
        HttpResponse response = client.postResourceMeta(collections, client.resourceToJson(resource), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceNotFoundTest() throws IOException {
        String collections = "collectionTest1";
        String name = "notfound";

        //Get a resource that not exist
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        client.getResourceMeta(collections, headers);
        HttpResponse response = client.getResourceMeta(collections+"/"+name, headers);
        assertEquals(404, response.getStatusLine().getStatusCode());
    }

    private Resource generateResource(String content, String fileName, String mimeType, String contentUrl,
        Date creationDate, String creator, String id, Date modificationDate, String name) {
        Resource resource = new Resource();
        if (content == null) {
            resource.setContent(null);
        } else {
            resource.setContent(content.getBytes());
        }
        resource.setContentFileName(fileName);
        resource.setContentMimeType(mimeType);
        resource.setContentUrl(contentUrl);
        resource.setCreationDate(creationDate);
        resource.setCreator(creator);
        resource.setId(id);
        resource.setModificationDate(modificationDate);
        resource.setName(name);
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
