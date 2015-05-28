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

    @Test
    public void getSelectTest() {
        Response returned;
        String accept = "";
        String query = "select";

        returned = toTest.executeQuery(accept, query);

        assertEquals(200, returned.getStatus());
        assertEquals(querySelect, returned.getEntity());
    }

    @Test
    public void getConstructTest() {
        Response returned;
        String accept = "";
        String query = "construct";

        returned = toTest.executeQuery(accept, query);

        assertEquals(200, returned.getStatus());
        assertEquals(queryConstruct, returned.getEntity());
    }

    @Test
    public void getDescribeTest() {
        Response returned;
        String accept = "";
        String query = "describe";

        returned = toTest.executeQuery(accept, query);

        assertEquals(200, returned.getStatus());
        assertEquals(queryDescribe, returned.getEntity());
    }

    @Test
    public void getAskTest() {
        Response returned;
        String accept = "";
        String query = "ask";

        when(virtuosoResourceDAO.executeQueryAsk(anyString())).thenReturn(true);

        returned = toTest.executeQuery(accept, query);

        assertEquals(200, returned.getStatus());
        assertEquals("true", returned.getEntity());
    }

    @Test
    public void postSelectTest() {
        Response returned;
        String accept = "";
        String query = "select";

        returned = toTest.executeLongQuery(accept, query);

        assertEquals(200, returned.getStatus());
        assertEquals(querySelect, returned.getEntity());
    }

    @Test
    public void postConstructTest() {
        Response returned;
        String accept = "";
        String query = "construct";

        returned = toTest.executeLongQuery(accept, query);

        assertEquals(200, returned.getStatus());
        assertEquals(queryConstruct, returned.getEntity());
    }

    @Test
    public void postDescribeTest() {
        Response returned;
        String accept = "";
        String query = "describe";

        returned = toTest.executeLongQuery(accept, query);

        assertEquals(200, returned.getStatus());
        assertEquals(queryDescribe, returned.getEntity());
    }

    @Test
    public void postAskTest() {
        Response returned;
        String accept = "";
        String query = "ask";

        when(virtuosoResourceDAO.executeQueryAsk(anyString())).thenReturn(false);

        returned = toTest.executeLongQuery(accept, query);

        assertEquals(200, returned.getStatus());
        assertEquals("false", returned.getEntity());
    }

    @Test
    public void executeAnyQueryTest() {
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
    public void executeAnyQueryNullTest() {
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
    public void executeAnyQueryErrorTest() {
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
