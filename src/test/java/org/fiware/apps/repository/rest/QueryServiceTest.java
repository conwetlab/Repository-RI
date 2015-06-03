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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import org.fiware.apps.repository.dao.*;
import org.fiware.apps.repository.dao.impl.VirtuosoResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.SelectQueryResponse;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(VirtuosoDAOFactory.class)
public class QueryServiceTest {

    @Mock private VirtuosoResourceDAO virtuosoResourceDAO;
    private QueryService toTest;
    private SelectQueryResponse querySelect = new SelectQueryResponse();
    private String queryConstruct = "Construct query";
    private String queryDescribe = "Describe query";
    private int OK = 200;
    private int UNSOPORTED_MEDIA_TYPE = 415;

    public QueryServiceTest() {
    }

    @Before
    public void setUp() {
        PowerMockito.mockStatic(VirtuosoDAOFactory.class);
        virtuosoResourceDAO = mock(VirtuosoResourceDAO.class);
        PowerMockito.when(VirtuosoDAOFactory.getVirtuosoResourceDAO()).thenReturn(virtuosoResourceDAO);
        when(virtuosoResourceDAO.executeQuerySelect(anyString())).thenReturn(querySelect);
        when(virtuosoResourceDAO.executeQueryConstruct(anyString(), anyString())).thenReturn(queryConstruct);
        when(virtuosoResourceDAO.executeQueryDescribe(anyString(), anyString())).thenReturn(queryDescribe);


        toTest = new QueryService();
    }

    public void getSelectTest(String accept, int response) {
        Response returned = toTest.executeQuery(accept, "select");

        assertEquals(response, returned.getStatus());
    }

    public void getConstructTest(String accept, int response) {
        Response returned = toTest.executeQuery(accept, "construct");

        assertEquals(response, returned.getStatus());
    }

    public void getDescribeTest(String accept, int response) {
        Response returned = toTest.executeQuery(accept, "describe");

        assertEquals(response, returned.getStatus());
    }

    public void getAskTest(String accept, int response, boolean askResponse) {
        when(virtuosoResourceDAO.executeQueryAsk(anyString())).thenReturn(askResponse);

        Response returned = toTest.executeQuery(accept, "ask");

        assertEquals(response, returned.getStatus());
    }

    public void postSelectTest(String accept, int response) {
        Response returned = toTest.executeLongQuery(accept, "select");

        assertEquals(response, returned.getStatus());
    }


    public void postConstructTest(String accept, int response) {
        Response returned = toTest.executeLongQuery(accept, "construct");

        assertEquals(response, returned.getStatus());
    }


    public void postDescribeTest(String accept, int response) {
        Response returned = toTest.executeLongQuery(accept, "describe");

        assertEquals(response, returned.getStatus());
    }

    public void postAskTest(String accept, int response, boolean askResponse) {
        when(virtuosoResourceDAO.executeQueryAsk(anyString())).thenReturn(askResponse);

        Response returned = toTest.executeLongQuery(accept, "ask");

        assertEquals(response, returned.getStatus());
    }

    @Test
    public void SelectXmlTest() {
        getSelectTest("application/xml", OK);
        postSelectTest("application/xml", OK);
    }

    @Test
    public void SelectJsonTest() {
        getSelectTest("application/json", OK);
        postSelectTest("application/json", OK);
    }

    @Test
    public void SelectUnsoportedMediaTypeTest() {
        getSelectTest("anything", UNSOPORTED_MEDIA_TYPE);
        postSelectTest("anything", UNSOPORTED_MEDIA_TYPE);
    }

    @Test
    public void ConstructRdfXmlTest() {
        getConstructTest("application/rdf+xml", OK);
        postConstructTest("application/rdf+xml", OK);
    }

    @Test
    public void ConstructRdfJsonTest() {
        getConstructTest("application/rdf+json", OK);
        postConstructTest("application/rdf+json", OK);
    }

    @Test
    public void postConstructTurtleTest1() {
        getConstructTest("text/turtle", OK);
        postConstructTest("text/turtle", OK);
    }

    @Test
    public void ConstructTurtleTest2() {
        getConstructTest("application/x-turtle", OK);
        postConstructTest("application/x-turtle", OK);
    }

    @Test
    public void ConstructN3Test1() {
        getConstructTest("text/rdf+n3", OK);
        postConstructTest("text/rdf+n3", OK);
    }

    @Test
    public void ConstructN3Test2() {
        getConstructTest("text/n3", OK);
        postConstructTest("text/n3", OK);
    }

    @Test
    public void ConstructNTriplesTest() {
        getConstructTest("text/n-triples", OK);
        postConstructTest("text/n-triples", OK);
    }

    @Test
    public void ConstructUnsoportedMediaTypeTest() {
        getConstructTest("anything", UNSOPORTED_MEDIA_TYPE);
        postConstructTest("anything", UNSOPORTED_MEDIA_TYPE);
    }

    @Test
    public void DescribeRdfXmlTest() {
        getDescribeTest("application/rdf+xml", OK);
        postDescribeTest("application/rdf+xml", OK);
    }

    @Test
    public void DescribeRdfJsonTest() {
        getDescribeTest("application/rdf+json", OK);
        postDescribeTest("application/rdf+json", OK);
    }

    @Test
    public void DescribeTurtleTest1() {
        getDescribeTest("text/turtle", OK);
        postDescribeTest("text/turtle", OK);
    }

    @Test
    public void DescribeTurtleTest2() {
        getDescribeTest("application/x-turtle", OK);
        postDescribeTest("application/x-turtle", OK);
    }

    @Test
    public void DescribeN3Test1() {
        getDescribeTest("text/rdf+n3", OK);
        postDescribeTest("text/rdf+n3", OK);
    }

    @Test
    public void DescribeN3Test2() {
        getDescribeTest("text/n3", OK);
        postDescribeTest("text/n3", OK);
    }

    @Test
    public void DescribeNTriplesTest() {
        getDescribeTest("text/n-triples", OK);
        postDescribeTest("text/n-triples", OK);
    }

    @Test
    public void DescribeUnsoportedMediaTypeTest() {
        getDescribeTest("anything", UNSOPORTED_MEDIA_TYPE);
        postDescribeTest("anything", UNSOPORTED_MEDIA_TYPE);
    }

    @Test
    public void getAskXmlTest() {
        getAskTest("application/xml", OK, true);
        getAskTest("application/xml", OK, false);
        postAskTest("application/xml", OK, true);
        postAskTest("application/xml", OK, false);
    }

    @Test
    public void getAskJsonTest() {
        getAskTest("application/xml", OK, true);
        getAskTest("application/xml", OK, false);
        postAskTest("application/xml", OK, true);
        postAskTest("application/xml", OK, false);
    }

    @Test
    public void getAskUnsoportedMediaTypeTest() {
        getAskTest("anything", UNSOPORTED_MEDIA_TYPE, true);
        getAskTest("anything", UNSOPORTED_MEDIA_TYPE, false);
        postAskTest("anything", UNSOPORTED_MEDIA_TYPE, true);
        postAskTest("anything", UNSOPORTED_MEDIA_TYPE, false);
    }

    @Test
    public void obtainResourceOKTest() {
        Response returned;
        String path = "path";
        String type = "application/rdf+xml";
        Resource resource = new Resource();
        resource.setContent("resourceContent".getBytes());

        try {
            when(virtuosoResourceDAO.getResource(path, RestHelper.typeMap.get(type))).thenReturn(resource);
        } catch (DatasourceException ex) {
            fail(ex.getLocalizedMessage());
        }

        returned = toTest.obtainResource(type, path);

        assertEquals(200, returned.getStatus());
        assertEquals(resource.getContent(), returned.getEntity());
    }

    @Test
    public void obtainResourceNotFoundTest() {
        Response returned;
        String path = "path";
        String type = "application/rdf+xml";

        try {
            when(virtuosoResourceDAO.getResource(path, RestHelper.typeMap.get(type))).thenReturn(null);
        } catch (DatasourceException ex) {
            fail(ex.getLocalizedMessage());
        }

        returned = toTest.obtainResource(type, path);

        assertEquals(404, returned.getStatus());
    }

    @Test
    public void obtainResourceErrorTest() {
        Response returned;
        String path = "path";
        String type = "application/rdf+xml";

        try {
            when(virtuosoResourceDAO.getResource(path, RestHelper.typeMap.get(type))).thenThrow(DatasourceException.class);
        } catch (DatasourceException ex) {
            fail(ex.getLocalizedMessage());
        }

        returned = toTest.obtainResource(type, path);

        assertEquals(500, returned.getStatus());
    }

}
