/*
Modified BSD License
====================

Copyright (c) 2015, CoNWeTLab, Universidad Politecnica Madrid
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
* Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
* Neither the name of the SAP AG nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL SAP AG BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package test.org.fiware.apps.repository.rest;

import javax.ws.rs.core.Response;
import org.fiware.apps.repository.dao.*;
import org.fiware.apps.repository.dao.impl.VirtuosoResourceDAO;
import org.fiware.apps.repository.model.SelectQueryResponse;
import org.fiware.apps.repository.rest.QueryService;
import org.junit.*;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(VirtuosoDAOFactory.class)
public class QueryServiceTest {
    
    @Mock private VirtuosoResourceDAO virtuosoResourceDAO;
    private QueryService toTest;
    private SelectQueryResponse querySelect = new SelectQueryResponse();
    private String queryConstruct = "Construct query";
    private String queryDescribe = "Describe query";
    private String queryAsk = "true";
    
    
    public QueryServiceTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        PowerMockito.mockStatic(VirtuosoDAOFactory.class);
        virtuosoResourceDAO = mock(VirtuosoResourceDAO.class);
        PowerMockito.when(VirtuosoDAOFactory.getVirtuosoResourceDAO()).thenReturn(virtuosoResourceDAO);
        when(virtuosoResourceDAO.executeQuerySelect(anyString())).thenReturn(querySelect);
        when(virtuosoResourceDAO.executeQueryConstruct(anyString(), anyString())).thenReturn(queryConstruct);
        when(virtuosoResourceDAO.executeQueryDescribe(anyString(), anyString())).thenReturn(queryDescribe);
        when(virtuosoResourceDAO.executeQueryAsk(anyString())).thenReturn(true);
        
        toTest = new QueryService();
    }
    
    @After
    public void tearDown() {
        
    }
    
    @Test
    public void getSelect() {
        Response returned;
        String accept = "";
        String query = "select";
        
        returned = toTest.executeQuery(accept, query);
        
        assertEquals(200, returned.getStatus());
        assertEquals(querySelect, returned.getEntity());
    }
    
    @Test
    public void getConstruct() {
        Response returned;
        String accept = "";
        String query = "construct";
        
        returned = toTest.executeQuery(accept, query);
        
        assertEquals(200, returned.getStatus());
        assertEquals(queryConstruct, returned.getEntity());
    }
    
    @Test
    public void getDescribe() {
        Response returned;
        String accept = "";
        String query = "describe";
        
        returned = toTest.executeQuery(accept, query);
        
        assertEquals(200, returned.getStatus());
        assertEquals(queryDescribe, returned.getEntity());
    }
    
    @Test
    public void getAsk() {
        Response returned;
        String accept = "";
        String query = "ask";
        
        returned = toTest.executeQuery(accept, query);
        
        assertEquals(200, returned.getStatus());
        assertEquals(queryAsk, returned.getEntity());
    }

    @Test
    public void postSelect() {
        Response returned;
        String accept = "";
        String query = "select";
        
        returned = toTest.executeLongQuery(accept, query);
        
        assertEquals(200, returned.getStatus());
        assertEquals(querySelect, returned.getEntity());
    }
    
    @Test
    public void postConstruct() {
        Response returned;
        String accept = "";
        String query = "construct";
        
        returned = toTest.executeLongQuery(accept, query);
        
        assertEquals(200, returned.getStatus());
        assertEquals(queryConstruct, returned.getEntity());
    }
    
    @Test
    public void postDescribe() {
        Response returned;
        String accept = "";
        String query = "describe";
        
        returned = toTest.executeLongQuery(accept, query);
        
        assertEquals(200, returned.getStatus());
        assertEquals(queryDescribe, returned.getEntity());
    }
    
    @Test
    public void postAsk() {
        Response returned;
        String accept = "";
        String query = "ask";
        
        returned = toTest.executeLongQuery(accept, query);
        
        assertEquals(200, returned.getStatus());
        assertEquals(queryAsk, returned.getEntity());
    }
}
