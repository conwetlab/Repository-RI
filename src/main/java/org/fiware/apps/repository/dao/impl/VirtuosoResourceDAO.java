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

package org.fiware.apps.repository.dao.impl;

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;

import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.model.Resource;

import com.hp.hpl.jena.rdf.model.Model;
import java.util.Iterator;
import java.util.Objects;
import java.util.Properties;
import org.fiware.apps.repository.dao.VirtModelFactory;
import org.fiware.apps.repository.model.SelectQueryResponse;
import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import org.fiware.apps.repository.dao.VirtuosoQueryExecutionFactory;
import org.fiware.apps.repository.settings.DefaultProperties;

public class VirtuosoResourceDAO {

    private VirtGraph set;
    private VirtModelFactory modelFactory;
    private VirtuosoQueryExecutionFactory queryExecutionFactory;
    private Properties properties;

    public VirtuosoResourceDAO(Properties properties) {
        this.properties = properties;
        this.set = new VirtGraph(properties.getProperty(DefaultProperties.VIRTUOSO_HOST.getPropertyName()) + properties.getProperty(DefaultProperties.VIRTUOSO_PORT.getPropertyName()),
                properties.getProperty(DefaultProperties.VIRTUOSO_USER.getPropertyName()), properties.getProperty(DefaultProperties.VIRTUOSO_PASSWORD.getPropertyName()));
        this.set.setReadFromAllGraphs(true);
        this.modelFactory = new VirtModelFactory(set);
        this.queryExecutionFactory = new VirtuosoQueryExecutionFactory();
    }

    public VirtuosoResourceDAO(VirtModelFactory factory, VirtGraph graph, VirtuosoQueryExecutionFactory queryExecutionFactory, Properties properties) {
        this.properties = Objects.requireNonNull(properties);
        this.set = Objects.requireNonNull(graph);
        this.set.setReadFromAllGraphs(true);
        this.modelFactory = Objects.requireNonNull(factory);
        this.queryExecutionFactory = Objects.requireNonNull(queryExecutionFactory);
    }

    public void closeConnection() {
        this.set.close();
    }

    public Resource getResource(String graph, String type) throws DatasourceException {

        //Obtain the resource in the specified format.
        Resource res = new Resource();
        Model model = getModel(graph);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        if (model.isEmpty()) {
            return null;
        }
        try {
            model.begin();

            model.write(output, type, null);

            model.abort();
        } catch (Exception e) {
            model.abort();
            throw new DatasourceException(e.getMessage(), Resource.class);
        }

        model.close();

        res.setContent(output.toByteArray());
        return res;
    }

    public Boolean insertResource(String graph, String content, String type) throws DatasourceException {

        //Insert the nodes in a graph.
        Model model = getModel(graph);
        ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes());
        try
        {
            model.begin();

            model.read(new InputStreamReader(input), null, type);

            model.commit();
        } catch (Exception e) {
            model.abort();
            throw new DatasourceException(e.getMessage(), Resource.class);
        }
        model.close();
        return true;
    }

    public Boolean isResource(String graph) {

        //Check if the graph exist and have any triple.
        Model model = getModel(graph);
        return !model.isEmpty();
    }

    public Boolean updateResource(String graph, String content, String type)
            throws DatasourceException {
        //Remove the content of the resource in the triple store, and insert the new content.
        Model model = getModel(graph);
        ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes());
        try {
            model.begin();

            model.removeAll();
            model.read(new InputStreamReader(input), null, type);

            model.commit();
        } catch (Exception e) {
            model.abort();
            throw new DatasourceException(e.getMessage(), Resource.class);
        }
        model.close();
        return true;
    }

    public Boolean replaceResource(String oldGraph, String newGraph, String type)
            throws DatasourceException {
        //
        Model oldModel = getModel(oldGraph);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            oldModel.write(output, type, null);
        } catch (Exception e) {
            throw new DatasourceException(e.getMessage(), Resource.class);
        }


        Model newModel = getModel(newGraph);
        ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
        try {
            oldModel.begin();
            newModel.begin();

            newModel.removeAll();
            newModel.read(new InputStreamReader(input), null, type);
            oldModel.removeAll();

            oldModel.commit();
            newModel.commit();
        } catch (Exception e) {
            oldModel.abort();
            newModel.abort();
            throw new DatasourceException(e.getMessage(), Resource.class);
        }

        oldModel.close();
        newModel.close();
        return true;
    }

    public Boolean deleteResource(String graph) {

        //It is not necessary to check if "id" is a graph.
        Model model = getModel(graph);

        try {
        model.begin();

        model.removeAll();

        model.commit();
        } catch (Exception ex) {
            model.abort();
            return false;
        }

        model.close();
        return true;
    }

    public SelectQueryResponse executeQuerySelect(String query) throws QueryParseException {

        //Query sparql = QueryFactory.create(query);
        VirtuosoQueryExecution vqe = queryExecutionFactory.create(query, set);
        ResultSet resultSet = vqe.execSelect();
        SelectQueryResponse selectQueryResponse = new SelectQueryResponse();
        Boolean named = false;
        while (resultSet.hasNext()) {
            QuerySolution querySolution = resultSet.next();
            Iterator <String> varNames = querySolution.varNames();
            for (int i = 0; varNames.hasNext(); i++) {
                if (!named) {
                    String varName = varNames.next();
                    selectQueryResponse.addColumn(varName);
                    selectQueryResponse.addValue(i, querySolution.get(varName).toString());
                }
                else {
                    selectQueryResponse.addValue(i, querySolution.get(varNames.next()).toString());
                }
            }
            named = true;
        }
        vqe.close();
        return selectQueryResponse;
    }

    public String executeQueryConstruct(String query, String type) throws QueryParseException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        VirtuosoQueryExecution vqe = queryExecutionFactory.create(query, set);
        Model model = vqe.execConstruct();
        model.write(output, type, null);

        return output.toString();
    }

    public boolean executeQueryAsk(String query) throws QueryParseException {
        boolean response;

        VirtuosoQueryExecution vqe = queryExecutionFactory.create(query, set);
        response = vqe.execAsk();

        return response;
    }

    public String executeQueryDescribe (String query, String type) throws QueryParseException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        VirtuosoQueryExecution vqe = queryExecutionFactory.create(query, set);
        Model model = vqe.execDescribe();
        model.write(output, type, null);

        return output.toString();
    }

    private Model getModel(String graph) {
        return modelFactory.openDatabaseModel(graph, properties.getProperty(DefaultProperties.VIRTUOSO_HOST.getPropertyName()) + properties.getProperty(DefaultProperties.VIRTUOSO_PORT.getPropertyName()),
                properties.getProperty(DefaultProperties.VIRTUOSO_USER.getPropertyName()), properties.getProperty(DefaultProperties.VIRTUOSO_PASSWORD.getPropertyName()));
    }

}
