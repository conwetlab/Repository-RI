package test.org.fiware.apps.repository.dao.impl;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import static org.mockito.Mockito.*;

import com.hp.hpl.jena.rdf.model.RDFNode;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import org.fiware.apps.repository.dao.VirtModelFactory;
import org.fiware.apps.repository.dao.VirtuosoQueryExecutionFactory;
import org.fiware.apps.repository.dao.impl.VirtuosoResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.settings.RepositorySettings;
import org.junit.Assert;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtModel;
import virtuoso.jena.driver.VirtuosoQueryExecution;

public class VirtuosoResourceDAOTest {
    
    private VirtGraph virtGraph;
    private VirtModel virtModel;
    private VirtModelFactory virtModelFactory;
    private VirtuosoQueryExecutionFactory virtuosoQueryExecutionFactory;
    private VirtuosoQueryExecution vqe;
    VirtuosoResourceDAO toTest;
    
    public VirtuosoResourceDAOTest() {
    }
    
    
    @Before
    public void setUp() {
        
        virtGraph = mock(VirtGraph.class);
        virtModel = mock(VirtModel.class);
        virtModelFactory= mock(VirtModelFactory.class);
        virtuosoQueryExecutionFactory = mock(VirtuosoQueryExecutionFactory.class);
        vqe = mock(VirtuosoQueryExecution.class);
        
        toTest = new VirtuosoResourceDAO(virtModelFactory, virtGraph, virtuosoQueryExecutionFactory);
    }
    
    @Test
    public void getResourceTest() {
        
        String graph = "graph";
        String type = "type";
        Resource result = null;
        
        when(virtModelFactory.openDatabaseModel(anyString(), eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD))).thenReturn(virtModel);
        
        try {
            result = toTest.getResource(graph, type);
        } catch (DatasourceException ex) {
            fail("exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        verify(virtModelFactory).openDatabaseModel(eq(graph),
                eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD));
        verify(virtModel).write(any(ByteArrayOutputStream.class), eq(type), isNull(String.class));
        verify(virtModel).close();
        assertNotNull(result);
        assertNotNull(result.getContent());
    }
    
    @Test(expected = DatasourceException.class)
    public void getResourceExceptionTest() throws DatasourceException {
        String graph = "graph";
        String type = "type";
        
        when(virtModelFactory.openDatabaseModel(anyString(), eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD))).thenReturn(virtModel);
        when(virtModel.write(any(ByteArrayOutputStream.class), eq(type), isNull(String.class))).thenThrow(Exception.class);
        toTest.getResource(graph, type);
    }
    
    @Test
    public void insertResourceTest() {
        String graph = "graph";
        String content = "content";
        String type = "type";
        
        when(virtModelFactory.openDatabaseModel(anyString(), eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD))).thenReturn(virtModel);
        try {
            toTest.insertResource(graph, content, type);
        } catch (DatasourceException ex) {
            fail("exception not expected:\n" + ex.getLocalizedMessage());
        }
        verify(virtModelFactory).openDatabaseModel(eq(graph),
                eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD));
        verify(virtModel).read(any(InputStreamReader.class), isNull(String.class), eq(type));
        verify(virtModel).close();
    }
    
    @Test(expected = DatasourceException.class)
    public void insertResourceExceptionTest() throws DatasourceException {
        String graph = "graph";
        String content = "content";
        String type = "type";
        
        when(virtModelFactory.openDatabaseModel(anyString(), eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD))).thenReturn(virtModel);
        when(virtModel.read(any(InputStreamReader.class), isNull(String.class), eq(type))).thenThrow(Exception.class);
        toTest.insertResource(graph, content, type);
    }
    
    @Test
    public void isResourceTest() {
        String graph = "graph";
        
        when(virtModelFactory.openDatabaseModel(anyString(), eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD))).thenReturn(virtModel);
        when(virtModel.isEmpty()).thenReturn(false);
        assertTrue(toTest.isResource(graph));
    }
    
    @Test
    public void updateResourceTest() {
        String graph = "graph";
        String content = "content";
        String type = "type";
        
        when(virtModelFactory.openDatabaseModel(anyString(), eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD))).thenReturn(virtModel);
        try {
            toTest.updateResource(graph, content, type);
        } catch (DatasourceException ex) {
            fail("exception not expected:\n" + ex.getLocalizedMessage());
        }
        verify(virtModel).removeAll();
        verify(virtModel).read(any(InputStreamReader.class), isNull(String.class), eq(type));
        verify(virtModel).close();
    }
    
    @Test(expected = DatasourceException.class)
    public void updateResourceExceptionTest() throws DatasourceException {
        String graph = "graph";
        String content = "content";
        String type = "type";
        
        when(virtModelFactory.openDatabaseModel(anyString(), eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD))).thenReturn(virtModel);
        when(virtModel.read(any(InputStreamReader.class), isNull(String.class), eq(type))).thenThrow(Exception.class);
        toTest.updateResource(graph, content, type);
    }
    
    @Test
    public void replaceResourceTest() {
        String oldGraph = "graph";
        String newGraph = "graph";
        String type = "type";
        
        when(virtModelFactory.openDatabaseModel(anyString(), eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD))).thenReturn(virtModel);
        
        try {
            toTest.replaceResource(oldGraph, newGraph, type);
        } catch (DatasourceException ex) {
            fail("exception not expected:\n" + ex.getLocalizedMessage());
        }
        
        verify(virtModel).write(any(ByteArrayOutputStream.class), eq(type), isNull(String.class));
        verify(virtModel, times(2)).removeAll();
        verify(virtModel).read(any(InputStreamReader.class), isNull(String.class), eq(type));
        verify(virtModel, times(2)).close();
    }
    
    @Test(expected = DatasourceException.class)
    public void replaceResourceTestException1() throws DatasourceException {
        String oldGraph = "graph";
        String newGraph = "graph";
        String type = "type";
        
        when(virtModelFactory.openDatabaseModel(anyString(), eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD))).thenReturn(virtModel);
        when(virtModel.write(any(ByteArrayOutputStream.class), eq(type), isNull(String.class))).thenThrow(Exception.class);
        toTest.replaceResource(oldGraph, newGraph, type);
    }
    
    @Test(expected = DatasourceException.class)
    public void replaceResourceTestException2() throws DatasourceException {
        String oldGraph = "graph";
        String newGraph = "graph";
        String type = "type";
        
        when(virtModelFactory.openDatabaseModel(anyString(), eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD))).thenReturn(virtModel);
        when(virtModel.read(any(InputStreamReader.class), isNull(String.class), eq(type))).thenThrow(Exception.class);
        toTest.replaceResource(oldGraph, newGraph, type);
    }
    
    @Test
    public void deleteResourceTest() {
        String graph = "graph";
        boolean excepted;
        
        when(virtModelFactory.openDatabaseModel(anyString(), eq(RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT),
                eq(RepositorySettings.VIRTUOSO_USER), eq(RepositorySettings.VIRTUOSO_PASSWORD))).thenReturn(virtModel);
        
        excepted = toTest.deleteResource(graph);
        
        verify(virtModel).removeAll();
        verify(virtModel).close();
        assertTrue(excepted);
    }
    
    @Test
    public void executeQuerySelectTest() {
        String query = "query";
        ResultSet resultSet = mock(ResultSet.class);
        QuerySolution querySolution = mock(QuerySolution.class);
        RDFNode rDFNode = mock(RDFNode.class);
        List list = new LinkedList();
        
        list.add("value1");
        list.add("value2");
        when(virtuosoQueryExecutionFactory.create(eq(query), eq(virtGraph))).thenReturn(vqe);
        when(vqe.execSelect()).thenReturn(resultSet);
        when(resultSet.hasNext()).thenReturn(true, true, false);
        when(resultSet.next()).thenReturn(querySolution);
        when(querySolution.get(anyString())).thenReturn(rDFNode);
        when(querySolution.varNames()).thenReturn(list.iterator());
        when(rDFNode.toString()).thenReturn("string");
        
        toTest.executeQuerySelect(query);
        
    }
    
    @Test(expected = QueryParseException.class)
    public void executeQuerySelectExceptionTest() throws QueryParseException{
        String query = "query";
        ResultSet resultSet = mock(ResultSet.class);
        QuerySolution querySolution = mock(QuerySolution.class);
        RDFNode rDFNode = mock(RDFNode.class);
        List list = new LinkedList();
        
        list.add("value1");
        list.add("value2");
        when(virtuosoQueryExecutionFactory.create(eq(query), eq(virtGraph))).thenReturn(vqe);
        when(vqe.execSelect()).thenThrow(QueryParseException.class);
        when(resultSet.hasNext()).thenReturn(true, true, false);
        when(resultSet.next()).thenReturn(querySolution);
        when(querySolution.get(anyString())).thenReturn(rDFNode);
        when(querySolution.varNames()).thenReturn(list.iterator());
        when(rDFNode.toString()).thenReturn("string");
        
        toTest.executeQuerySelect(query);
    }
    
    @Test
    public void executeQueryConstructTest() {
        String query = "query";
        String type = "type";
        
        when(virtuosoQueryExecutionFactory.create(eq(query), eq(virtGraph))).thenReturn(vqe);
        when(vqe.execConstruct()).thenReturn(virtModel);
        
        toTest.executeQueryConstruct(query, type);

    }
    
    @Test(expected = QueryParseException.class)
    public void executeQueryConstructExceptionTest() throws QueryParseException{
        String query = "query";
        String type = "type";
        
        when(virtuosoQueryExecutionFactory.create(eq(query), eq(virtGraph))).thenReturn(vqe);
        when(vqe.execConstruct()).thenThrow(QueryParseException.class);
        
        toTest.executeQueryConstruct(query, type);
    }
    
    @Test
    public void executeQueryAskTest() {
        String query = "query";
        
        when(virtuosoQueryExecutionFactory.create(eq(query), eq(virtGraph))).thenReturn(vqe);
        when(vqe.execAsk()).thenReturn(true);
        
        toTest.executeQueryAsk(query);
    }
    
    @Test(expected = QueryParseException.class)
    public void executeQueryAskExceptionTest() throws QueryParseException{
        String query = "query";
        
        when(virtuosoQueryExecutionFactory.create(eq(query), eq(virtGraph))).thenReturn(vqe);
        when(vqe.execAsk()).thenThrow(QueryParseException.class);
        
        toTest.executeQueryAsk(query);
    }
    
    @Test
    public void executeQueryDescribeTest() {
        String query = "query";
        String type = "type";
        
        when(virtuosoQueryExecutionFactory.create(eq(query), eq(virtGraph))).thenReturn(vqe);
        when(vqe.execDescribe()).thenReturn(virtModel);
        
        toTest.executeQueryDescribe(query, type);
    }
    
    @Test(expected = QueryParseException.class)
    public void executeQueryDescribeExceptionTest() throws QueryParseException {
        String query = "query";
        String type = "type";
        
        when(virtuosoQueryExecutionFactory.create(eq(query), eq(virtGraph))).thenReturn(vqe);
        when(vqe.execDescribe()).thenThrow(QueryParseException.class);
        
        toTest.executeQueryDescribe(query, type);
    }
}
