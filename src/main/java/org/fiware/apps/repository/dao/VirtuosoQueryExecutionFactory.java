/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fiware.apps.repository.dao;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.util.FileManager;
import java.util.List;
import javax.ws.rs.core.Context;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;

public class VirtuosoQueryExecutionFactory {
    
    public VirtuosoQueryExecutionFactory() {
        
    }
    
    public VirtuosoQueryExecution create(Query query, VirtGraph graph)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.create(query, graph); }

    public VirtuosoQueryExecution create(String query, VirtGraph graph)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.create(query, graph); }

    public QueryExecution create(Query query, Dataset dataset)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.create(query, dataset); }

    public QueryExecution create(String queryStr, Dataset dataset)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.create(queryStr, dataset); }

    public QueryExecution create(Query query, FileManager fm)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.create(query, fm); }

    public QueryExecution create(String queryStr, FileManager fm)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.create(queryStr, fm); }

    public QueryExecution create(Query query, Model model)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.create(query, model); }

    public QueryExecution create(String queryStr, Model model)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.create(queryStr, model); }

    public QueryExecution create(Query query, QuerySolution initialBinding)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.create(query, initialBinding); }

    public QueryExecution create(String queryStr, QuerySolution initialBinding)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.create(queryStr, initialBinding); }

    public QueryExecution create(Query query, Dataset dataset, QuerySolution initialBinding)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.create(query, dataset, initialBinding); }

    public QueryExecution create(String queryStr, Dataset dataset, QuerySolution initialBinding)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.create(queryStr, dataset, initialBinding); }

    public QueryExecution sparqlService(String service, Query query)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.sparqlService(service, query); }

    public QueryExecution sparqlService(String service, Query query, String defaultGraph)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.sparqlService(service, query, service); }

    public QueryExecution sparqlService(String service, Query query, List defaultGraphURIs, List namedGraphURIs)
    { return virtuoso.jena.driver.VirtuosoQueryExecutionFactory.sparqlService(service, query, defaultGraphURIs, namedGraphURIs); }
    
}
