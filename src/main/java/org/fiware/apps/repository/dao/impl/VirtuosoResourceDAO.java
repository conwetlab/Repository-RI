/*
Modified BSD License  
====================

Copyright (c) 2015, CoNWeTLab, UPM
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

package org.fiware.apps.repository.dao.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.fiware.apps.repository.dao.ResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceFilter;
import org.fiware.apps.repository.settings.RepositorySettings;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtModel;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

public class VirtuosoResourceDAO {
	
	public VirtuosoResourceDAO() {

	}
	
	public Resource getResource(String graph, String type) throws DatasourceException {
		
		//Obtain the resource in the specified format.
		Resource res = new Resource();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Model model = VirtModel.openDatabaseModel(graph, RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT,
				  RepositorySettings.VIRTUOSO_DB, RepositorySettings.VIRTUOSO_DB);
		
		try {
			model.write(output, type, null);
		} catch (Exception e) {
			throw new DatasourceException(e.getMessage(), Resource.class);
		}
		model.close();
		
		res.setContent(output.toByteArray());
		return res;
	}
	
	public Boolean insertResource(String graph, String content, String type) throws DatasourceException,
			SameIdException {
		
		//Insert the nodes in a graph.
		Model model = VirtModel.openDatabaseModel(graph, RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT,
				RepositorySettings.VIRTUOSO_DB, RepositorySettings.VIRTUOSO_DB);
		ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes());
		try
		{
			model.read(new InputStreamReader(input), null, type);
		} catch (Exception e) {
			throw new DatasourceException(e.getMessage(), Resource.class);
		}
		model.close();
		return true;
	}

	public Boolean isResource(String graph) {
		
		//Check if the graph exist and have any triple.
		Model model = VirtModel.openDatabaseModel(graph, RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT,
				RepositorySettings.VIRTUOSO_DB, RepositorySettings.VIRTUOSO_DB);
		return model.isEmpty();
	}

	public Boolean updateResource(String graph, Resource r)
			throws DatasourceException {
		// TODO: ES NECESARIO??
		return null;
	}

	public Boolean deleteResource(String graph) {
		
		//It is not necessary to check if "id" is a graph.
		Model model = VirtModel.openDatabaseModel(graph, RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT,
				RepositorySettings.VIRTUOSO_DB, RepositorySettings.VIRTUOSO_DB);
		model.removeAll();
		model.close();
		return true;
	}

}
