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
package org.fiware.apps.repository.it.queryService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.fiware.apps.repository.it.IntegrationTestHelper;
import org.fiware.apps.repository.model.Resource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.scribe.utils.OAuthEncoder;

public class QueryServiceITTest {

    private IntegrationTestHelper helper;
    private String rdfXmlExample;

    public QueryServiceITTest() throws IOException, ServletException {
        helper = new IntegrationTestHelper();

        rdfXmlExample = helper.readRDFFile("src/test/resources/storeRDF.rdf");
    }

    @BeforeClass
    public static void setUpClass() throws IOException {

    }

    @AfterClass
    public static void tearDownClass() throws IOException {

    }

    @Before
    public void setUp() throws Exception {
        helper.createEnviroment();
        helper.startEnviroment();

        Resource resource = helper.generateResource(null, "fileName", null, "http://appTest", null, "Me", null, null, "queryResourceTest");

        List <Header> headers = new LinkedList<>();
        helper.deleteCollection("queryCollection", headers);

        //Create a resource
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = helper.postResourceMeta("queryCollection", helper.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        //Insert rdf content in the resource
        headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/rdf+xml"));
        response = helper.putResourceContent("queryCollection/queryResourceTest", rdfXmlExample, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @After
    public void tearDown() throws Exception {
        List <Header> headers = new LinkedList<>();
        helper.deleteCollection("queryCollection", headers);

        helper.stopEnviroment();
        helper.destroyEnviroment();
    }

    @Test
    public void getQuerySelectXmlTest() throws IOException {
        String query = "SELECT ?s ?p ?o WHERE {?s ?p ?o}";
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getQuerySelectJsonTest() throws IOException {
        String query = "SELECT ?s ?p ?o WHERE {?s ?p ?o}";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/xml"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getQuerySelectNotAcceptableTest() throws IOException {
        String query = "SELECT ?s ?p ?o WHERE {?s ?p ?o}";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(406, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getBadQuerySelectTest() throws IOException {
        String query = "SELECT ?s ?p ?o WHERE GRAPH {?s ?p ?o}";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryConstructRdfXmlTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> CONSTRUCT { ?X app:relaseYear ?year . } FROM <http://appTest> WHERE { OPTIONAL { ?X app:year ?year . } }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryConstructRdfJsonTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> CONSTRUCT { ?X app:relaseYear ?year . } FROM <http://appTest> WHERE { OPTIONAL { ?X app:year ?year . } }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryConstructRdfN3Test() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> CONSTRUCT { ?X app:relaseYear ?year . } FROM <http://appTest> WHERE { OPTIONAL { ?X app:year ?year . } }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "text/N3"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryConstructRdfTurtleTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> CONSTRUCT { ?X app:relaseYear ?year . } FROM <http://appTest> WHERE { OPTIONAL { ?X app:year ?year . } }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "text/turtle"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postConstructSelectNotAcceptableTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> CONSTRUCT { ?X app:relaseYear ?year . } FROM <http://appTest> WHERE { OPTIONAL { ?X app:year ?year . } }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(406, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postBadQueryConstructTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> CONSTRUCT { ?X app:relaseYear ?year . } <http://appTest> WHERE { OPTIONAL { ?X app:year ?year . } }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryDescribeRdfXmlTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> DESCRIBE ?app WHERE { ?app app:name \"Halo\" . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryDescribeRdfJsonTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> DESCRIBE ?app WHERE { ?app app:name \"Halo\" . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryDescribeRdfN3Test() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> DESCRIBE ?app WHERE { ?app app:name \"Halo\" . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "text/N3"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryDescribeRdfTurtleTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> DESCRIBE ?app WHERE { ?app app:name \"Halo\" . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "text/turtle"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryDescribeNotAcceptableTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> DESCRIBE ?app WHERE { ?app app:name \"Halo\" . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(406, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postBadQueryDescribeTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> DESCRIBE ?app  ?app app:name \"Halo\" . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryAskXmlTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> ASK { <http://www.app.fake/app/halo> app:price ?halo . <http://www.app.fake/app/uno> app:price ?uno . FILTER(?halo > ?uno) . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/xml"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryAskJsonTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> ASK { <http://www.app.fake/app/halo> app:price ?halo . <http://www.app.fake/app/uno> app:price ?uno . FILTER(?halo > ?uno) . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryAskNotAcceptableTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> ASK { <http://www.app.fake/app/halo> app:price ?halo . <http://www.app.fake/app/uno> app:price ?uno . FILTER(?halo > ?uno) . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(406, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postBadQueryAskTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> ASK <http://www.app.fake/app/halo> app:price ?halo . <http://www.app.fake/app/uno> app:price ?uno . FILTER(?halo > ?uno) . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = helper.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceByUrlContentRDFXmlTest() throws IOException {
        String contentUrl = "http://appTest";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));

        HttpResponse response = helper.getResourceByUrlContent(contentUrl, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceByUrlContentRDFJsonTest() throws IOException {
        String contentUrl = "http://appTest";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));

        HttpResponse response = helper.getResourceByUrlContent(contentUrl, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceByUrlContentRDFTurtleTest() throws IOException {
        String contentUrl = "http://appTest";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "text/turtle"));

        HttpResponse response = helper.getResourceByUrlContent(contentUrl, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceByUrlContentRDFN3Test() throws IOException {
        String contentUrl = "http://appTest";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "text/N3"));

        HttpResponse response = helper.getResourceByUrlContent(contentUrl, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceByUrlContentNotAcceptableTest() throws IOException {
        String contentUrl = "http://appTest";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));

        HttpResponse response = helper.getResourceByUrlContent(contentUrl, headers);
        assertEquals(406, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceByUrlContentNotFoundTest() throws IOException {
        String contentUrl = "http://appTest1";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));

        HttpResponse response = helper.getResourceByUrlContent(contentUrl, headers);
        assertEquals(404, response.getStatusLine().getStatusCode());
    }
}
