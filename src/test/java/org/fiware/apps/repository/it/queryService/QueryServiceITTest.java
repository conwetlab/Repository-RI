package org.fiware.apps.repository.it.queryService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.DatatypeConverter;
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

    private IntegrationTestHelper client;

    public QueryServiceITTest() {
        client = new IntegrationTestHelper();
    }

    @BeforeClass
    public static void setUpClass() throws IOException {
        IntegrationTestHelper client = new IntegrationTestHelper();
        Resource resource = IntegrationTestHelper.generateResource(null, "fileName", null, "http://appTest", null, "Me", null, null, "queryResourceTest");

        List <Header> headers = new LinkedList<>();
        client.deleteCollection("queryCollection", headers);

        //Create a resource
        headers.add(new BasicHeader("Content-Type", "application/json"));
        HttpResponse response = client.postResourceMeta("queryCollection", client.resourceToJson(resource), headers);
        assertEquals(201, response.getStatusLine().getStatusCode());

        String auxString = "";
        FileReader file = new FileReader("src/test/resources/storeRDF.rdf");
        BufferedReader buffer = new BufferedReader(file);
        while(buffer.ready()) {
            auxString = auxString.concat(buffer.readLine()+"\n");
        }
        buffer.close();

        headers = new LinkedList<>();
        headers.add(new BasicHeader("Content-Type", "application/rdf+xml"));
        response = client.putResourceContent("queryCollection/queryResourceTest", auxString, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @AfterClass
    public static void tearDownClass() throws IOException {

    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getQuerySelectXmlTest() throws IOException {
        String query = "SELECT ?s ?p ?o WHERE {?s ?p ?o}";
        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getQuerySelectJsonTest() throws IOException {
        String query = "SELECT ?s ?p ?o WHERE {?s ?p ?o}";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/xml"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getQuerySelectNotAcceptableTest() throws IOException {
        String query = "SELECT ?s ?p ?o WHERE {?s ?p ?o}";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(406, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getBadQuerySelectTest() throws IOException {
        String query = "SELECT ?s ?p ?o WHERE GRAPH {?s ?p ?o}";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryConstructRdfXmlTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> CONSTRUCT { ?X app:relaseYear ?year . } FROM <http://appTest> WHERE { OPTIONAL { ?X app:year ?year . } }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryConstructRdfJsonTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> CONSTRUCT { ?X app:relaseYear ?year . } FROM <http://appTest> WHERE { OPTIONAL { ?X app:year ?year . } }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryConstructRdfN3Test() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> CONSTRUCT { ?X app:relaseYear ?year . } FROM <http://appTest> WHERE { OPTIONAL { ?X app:year ?year . } }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "text/N3"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryConstructRdfTurtleTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> CONSTRUCT { ?X app:relaseYear ?year . } FROM <http://appTest> WHERE { OPTIONAL { ?X app:year ?year . } }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "text/turtle"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postConstructSelectNotAcceptableTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> CONSTRUCT { ?X app:relaseYear ?year . } FROM <http://appTest> WHERE { OPTIONAL { ?X app:year ?year . } }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(406, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postBadQueryConstructTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> CONSTRUCT { ?X app:relaseYear ?year . } <http://appTest> WHERE { OPTIONAL { ?X app:year ?year . } }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryDescribeRdfXmlTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> DESCRIBE ?app WHERE { ?app app:name \"Halo\" . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryDescribeRdfJsonTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> DESCRIBE ?app WHERE { ?app app:name \"Halo\" . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryDescribeRdfN3Test() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> DESCRIBE ?app WHERE { ?app app:name \"Halo\" . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "text/N3"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryDescribeRdfTurtleTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> DESCRIBE ?app WHERE { ?app app:name \"Halo\" . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "text/turtle"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryDescribeNotAcceptableTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> DESCRIBE ?app WHERE { ?app app:name \"Halo\" . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(406, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postBadQueryDescribeTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> DESCRIBE ?app  ?app app:name \"Halo\" . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryAskXmlTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> ASK { <http://www.app.fake/app/halo> app:price ?halo . <http://www.app.fake/app/uno> app:price ?uno . FILTER(?halo > ?uno) . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/xml"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryAskJsonTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> ASK { <http://www.app.fake/app/halo> app:price ?halo . <http://www.app.fake/app/uno> app:price ?uno . FILTER(?halo > ?uno) . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postQueryAskNotAcceptableTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> ASK { <http://www.app.fake/app/halo> app:price ?halo . <http://www.app.fake/app/uno> app:price ?uno . FILTER(?halo > ?uno) . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(406, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postBadQueryAskTest() throws IOException {
        String query = "PREFIX app: <http://www.app.fake/app#> ASK <http://www.app.fake/app/halo> app:price ?halo . <http://www.app.fake/app/uno> app:price ?uno . FILTER(?halo > ?uno) . }";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));
        headers.add(new BasicHeader("Content-Type", "text/plain"));

        HttpResponse response = client.getQuery(OAuthEncoder.encode(query), headers);
        assertEquals(400, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceByUrlContentRDFXmlTest() throws IOException {
        String contentUrl = "http://appTest";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+xml"));

        HttpResponse response = client.getResourceByUrlContent(contentUrl, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceByUrlContentRDFJsonTest() throws IOException {
        String contentUrl = "http://appTest";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));

        HttpResponse response = client.getResourceByUrlContent(contentUrl, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceByUrlContentRDFTurtleTest() throws IOException {
        String contentUrl = "http://appTest";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "text/turtle"));

        HttpResponse response = client.getResourceByUrlContent(contentUrl, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceByUrlContentRDFN3Test() throws IOException {
        String contentUrl = "http://appTest";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "text/N3"));

        HttpResponse response = client.getResourceByUrlContent(contentUrl, headers);
        assertEquals(200, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceByUrlContentNotAcceptableTest() throws IOException {
        String contentUrl = "http://appTest";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/json"));

        HttpResponse response = client.getResourceByUrlContent(contentUrl, headers);
        assertEquals(406, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getResourceByUrlContentNotFoundTest() throws IOException {
        String contentUrl = "http://appTest1";

        List <Header> headers = new LinkedList<>();
        headers.add(new BasicHeader("Accept", "application/rdf+json"));

        HttpResponse response = client.getResourceByUrlContent(contentUrl, headers);
        assertEquals(404, response.getStatusLine().getStatusCode());
    }
}
