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
package org.fiware.apps.repository.dao;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;
import java.util.List;
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
