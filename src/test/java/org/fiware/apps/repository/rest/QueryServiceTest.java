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

package org.fiware.apps.repository.rest;

import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import org.fiware.apps.repository.dao.*;
import org.fiware.apps.repository.dao.impl.VirtuosoResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.SelectQueryResponse;
import org.jboss.resteasy.specimpl.MultivaluedMapImpl;
import org.jboss.resteasy.specimpl.ResteasyHttpHeaders;
import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({VirtuosoDAOFactory.class, QueryService.class})
public class QueryServiceTest {

    private VirtuosoResourceDAO virtuosoResourceDAO;
    private VirtuosoDAOFactory virtuosoDAOFactory;
    private ServletContext servletContext;
    private QueryService toTest;

    private SelectQueryResponse querySelect = new SelectQueryResponse();
    private String queryConstruct = "Construct query";
    private String queryDescribe = "Describe query";
    private int OK = 200;
    private int UNSOPORTED_MEDIA_TYPE = 415;
    private int NOT_ACCEPTABLE = 406;

    public QueryServiceTest() {
    }

    @Before
    public void setUp() throws Exception {
        virtuosoResourceDAO = mock(VirtuosoResourceDAO.class);
        virtuosoDAOFactory = mock(VirtuosoDAOFactory.class);
        servletContext = mock(ServletContext.class);

        when(servletContext.getInitParameter("propertiesFile")).thenReturn("");
        when(virtuosoDAOFactory.getVirtuosoResourceDAO(anyObject())).thenReturn(virtuosoResourceDAO);

        PowerMockito.whenNew(VirtuosoDAOFactory.class).withAnyArguments().thenReturn(virtuosoDAOFactory);

        toTest = new QueryService(servletContext);
    }

    @Test
    public void getVoidQueyTest() {
        List <String> accepts = new LinkedList();
        accepts.add("application/xml");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        Response returned = toTest.executeQuery(headers, null);

        assertEquals(400, returned.getStatus());
    }

    @Test
    public void postVoidQueyTest() {
        List <String> accepts = new LinkedList();
        accepts.add("application/xml");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        Response returned = toTest.executeLongQuery(headers, null);

        assertEquals(400, returned.getStatus());
    }

    private void getSelectTest(HttpHeaders headers, int response) {
        Response returned = toTest.executeQuery(headers, "select");

        assertEquals(response, returned.getStatus());
    }

    private void getConstructTest(HttpHeaders headers, int response) {
        Response returned = toTest.executeQuery(headers, "construct");

        assertEquals(response, returned.getStatus());
    }

    private void getDescribeTest(HttpHeaders headers, int response) {
        Response returned = toTest.executeQuery(headers, "describe");

        assertEquals(response, returned.getStatus());
    }

    private void getAskTest(HttpHeaders headers, int response, boolean askResponse) {
        when(virtuosoResourceDAO.executeQueryAsk(anyString())).thenReturn(askResponse);

        Response returned = toTest.executeQuery(headers, "ask");

        assertEquals(response, returned.getStatus());
    }

    private void postSelectTest(HttpHeaders headers, int response) {
        Response returned = toTest.executeLongQuery(headers, "select");

        assertEquals(response, returned.getStatus());
    }


    private void postConstructTest(HttpHeaders headers, int response) {
        Response returned = toTest.executeLongQuery(headers, "construct");

        assertEquals(response, returned.getStatus());
    }


    private void postDescribeTest(HttpHeaders headers, int response) {
        Response returned = toTest.executeLongQuery(headers, "describe");

        assertEquals(response, returned.getStatus());
    }

    private void postAskTest(HttpHeaders headers, int response, boolean askResponse) {
        when(virtuosoResourceDAO.executeQueryAsk(anyString())).thenReturn(askResponse);

        Response returned = toTest.executeLongQuery(headers, "ask");

        assertEquals(response, returned.getStatus());
    }

    @Test
    public void SelectXmlTest() {
        List <String> accepts = new LinkedList();
        accepts.add("application/xml");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getSelectTest(headers, OK);
        postSelectTest(headers, OK);
    }

    @Test
    public void SelectJsonTest() {
        List <String> accepts = new LinkedList();
        accepts.add("application/json");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getSelectTest(headers, OK);
        postSelectTest(headers, OK);
    }

    @Test
    public void SelectAnyMediaTypeTest() {
        List <String> accepts = new LinkedList();
        accepts.add("*/*");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getSelectTest(headers, OK);
        postSelectTest(headers, OK);
    }

    @Test
    public void SelectNotAceptableTest() {
        List <String> accepts = new LinkedList();
        accepts.add("test/fail");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getSelectTest(headers, NOT_ACCEPTABLE);
        postSelectTest(headers, NOT_ACCEPTABLE);
    }

    @Test
    public void ConstructRdfXmlTest() {
        List <String> accepts = new LinkedList();
        accepts.add("application/rdf+xml");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getConstructTest(headers, OK);
        postConstructTest(headers, OK);
    }

    @Test
    public void ConstructRdfJsonTest() {
        List <String> accepts = new LinkedList();
        accepts.add("application/rdf+json");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getConstructTest(headers, OK);
        postConstructTest(headers, OK);
    }

    @Test
    public void ConstructTurtleTest1() {
        List <String> accepts = new LinkedList();
        accepts.add("text/turtle");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getConstructTest(headers, OK);
        postConstructTest(headers, OK);
    }

    @Test
    public void ConstructTurtleTest2() {
        List <String> accepts = new LinkedList();
        accepts.add("application/x-turtle");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getConstructTest(headers, OK);
        postConstructTest(headers, OK);
    }

    @Test
    public void ConstructN3Test1() {
        List <String> accepts = new LinkedList();
        accepts.add("text/rdf+n3");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getConstructTest(headers, OK);
        postConstructTest(headers, OK);
    }

    @Test
    public void ConstructN3Test2() {
        List <String> accepts = new LinkedList();
        accepts.add("text/n3");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getConstructTest(headers, OK);
        postConstructTest(headers, OK);
    }

    @Test
    public void ConstructNTriplesTest() {
        List <String> accepts = new LinkedList();
        accepts.add("text/n-triples");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getConstructTest(headers, OK);
        postConstructTest(headers, OK);
    }

    @Test
    public void ConstructAnyMediaTypeTest() {
        List <String> accepts = new LinkedList();
        accepts.add("*/*");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getConstructTest(headers, OK);
        postConstructTest(headers, OK);
    }

    @Test
    public void ConstructNotAceptableTest() {
        List <String> accepts = new LinkedList();
        accepts.add("test/fail");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getConstructTest(headers, NOT_ACCEPTABLE);
        postConstructTest(headers, NOT_ACCEPTABLE);
    }

    @Test
    public void DescribeRdfXmlTest() {
        List <String> accepts = new LinkedList();
        accepts.add("application/rdf+xml");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getDescribeTest(headers, OK);
        postDescribeTest(headers, OK);
    }

    @Test
    public void DescribeRdfJsonTest() {
        List <String> accepts = new LinkedList();
        accepts.add("application/rdf+json");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getDescribeTest(headers, OK);
        postDescribeTest(headers, OK);
    }

    @Test
    public void DescribeTurtleTest1() {
        List <String> accepts = new LinkedList();
        accepts.add("text/turtle");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getDescribeTest(headers, OK);
        postDescribeTest(headers, OK);
    }

    @Test
    public void DescribeTurtleTest2() {
        List <String> accepts = new LinkedList();
        accepts.add("application/x-turtle");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getDescribeTest(headers, OK);
        postDescribeTest(headers, OK);
    }

    @Test
    public void DescribeN3Test1() {
        List <String> accepts = new LinkedList();
        accepts.add("text/rdf+n3");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getDescribeTest(headers, OK);
        postDescribeTest(headers, OK);
    }

    @Test
    public void DescribeN3Test2() {
        List <String> accepts = new LinkedList();
        accepts.add("text/n3");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getDescribeTest(headers, OK);
        postDescribeTest(headers, OK);
    }

    @Test
    public void DescribeNTriplesTest() {
        List <String> accepts = new LinkedList();
        accepts.add("text/n-triples");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getDescribeTest(headers, OK);
        postDescribeTest(headers, OK);
    }

    @Test
    public void DescribeNotAceptableTest() {
        List <String> accepts = new LinkedList();
        accepts.add("test/fail");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getDescribeTest(headers, NOT_ACCEPTABLE);
        postDescribeTest(headers, NOT_ACCEPTABLE);
    }

    @Test
    public void DescribeAnyMediaTypeTest() {
        List <String> accepts = new LinkedList();
        accepts.add("*/*");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getDescribeTest(headers, OK);
        postDescribeTest(headers, OK);
    }

    @Test
    public void AskXmlTest() {
        List <String> accepts = new LinkedList();
        accepts.add("application/xml");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getAskTest(headers, OK, true);
        getAskTest(headers, OK, false);
        postAskTest(headers, OK, true);
        postAskTest(headers, OK, false);
    }

    @Test
    public void AskJsonTest() {
        List <String> accepts = new LinkedList();
        accepts.add("application/json");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getAskTest(headers, OK, true);
        getAskTest(headers, OK, false);
        postAskTest(headers, OK, true);
        postAskTest(headers, OK, false);
    }

    @Test
    public void AskAnyMediaTypeTest() {
        List <String> accepts = new LinkedList();
        accepts.add("*/*");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getAskTest(headers, OK, true);
        getAskTest(headers, OK, false);
        postAskTest(headers, OK, true);
        postAskTest(headers, OK, false);
    }

    @Test
    public void getAskNotAceptableTest() {
        List <String> accepts = new LinkedList();
        accepts.add("test/fail");

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        getAskTest(headers, NOT_ACCEPTABLE, true);
        getAskTest(headers, NOT_ACCEPTABLE, false);
        postAskTest(headers, NOT_ACCEPTABLE, true);
        postAskTest(headers, NOT_ACCEPTABLE, false);
    }

    @Test
    public void obtainResourceOKTest() {
        Response returned;
        String path = "path";
        String type = "application/rdf+xml";
        Resource resource = new Resource();
        resource.setContent("resourceContent".getBytes());

        List <String> accepts = new LinkedList();
        accepts.add(type);

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        try {
            when(virtuosoResourceDAO.getResource(path, RestHelper.typeMap.get(type))).thenReturn(resource);
        } catch (DatasourceException ex) {
            fail(ex.getLocalizedMessage());
        }

        returned = toTest.obtainResource(headers, path);

        assertEquals(200, returned.getStatus());
        assertEquals(resource.getContent(), returned.getEntity());
    }

    @Test
    public void obtainResourceAnyMediaTypeTest() {
        Response returned;
        String path = "path";
        String type = "*/*";
        Resource resource = new Resource();
        resource.setContent("resourceContent".getBytes());

        List <String> accepts = new LinkedList();
        accepts.add(type);

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        try {
            when(virtuosoResourceDAO.getResource(path, RestHelper.typeMap.get(RestHelper.RdfDefaultType))).thenReturn(resource);
        } catch (DatasourceException ex) {
            fail(ex.getLocalizedMessage());
        }

        returned = toTest.obtainResource(headers, path);

        assertEquals(200, returned.getStatus());
        assertEquals(resource.getContent(), returned.getEntity());
    }

    @Test
    public void obtainResourceNotFoundTest() {
        Response returned;
        String path = "path";
        String type = "application/rdf+xml";

        List <String> accepts = new LinkedList();
        accepts.add(type);

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        try {
            when(virtuosoResourceDAO.getResource(path, RestHelper.typeMap.get(type))).thenReturn(null);
        } catch (DatasourceException ex) {
            fail(ex.getLocalizedMessage());
        }

        returned = toTest.obtainResource(headers, path);

        assertEquals(404, returned.getStatus());
    }

    @Test
    public void obtainResourceNotAcceptableTest() {
        Response returned;
        String path = "path";
        String type = "test/fail";

        List <String> accepts = new LinkedList();
        accepts.add(type);

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        try {
            when(virtuosoResourceDAO.getResource(path, RestHelper.typeMap.get(type))).thenReturn(null);
        } catch (DatasourceException ex) {
            fail(ex.getLocalizedMessage());
        }

        returned = toTest.obtainResource(headers, path);

        assertEquals(NOT_ACCEPTABLE, returned.getStatus());
    }

    @Test
    public void obtainResourceErrorTest() {
        Response returned;
        String path = "path";
        String type = "application/rdf+xml";

        List <String> accepts = new LinkedList();
        accepts.add(type);

        MultivaluedMap <String, String> acceptHeader = new MultivaluedMapImpl<>();
        acceptHeader.put("Accept", accepts);
        HttpHeaders headers = new ResteasyHttpHeaders(acceptHeader);

        try {
            when(virtuosoResourceDAO.getResource(path, RestHelper.typeMap.get(type))).thenThrow(DatasourceException.class);
        } catch (DatasourceException ex) {
            fail(ex.getLocalizedMessage());
        }

        returned = toTest.obtainResource(headers, path);

        assertEquals(500, returned.getStatus());
    }

}
