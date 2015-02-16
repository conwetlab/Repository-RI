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

import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.settings.RepositorySettings;

import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.rdf.model.Model;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtModel;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;

public class VirtuosoResourceDAO {
	
	private static VirtGraph set;
	
	public VirtuosoResourceDAO() {
		if (set == null)
			set = new VirtGraph (RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT, RepositorySettings.VIRTUOSO_DB, 
				RepositorySettings.VIRTUOSO_DB);
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

	public Boolean updateResource(String graph, String content, String type)
			throws DatasourceException {
		//Remove the content of the resource in the triple store, and insert the new content.
		Model model = VirtModel.openDatabaseModel(graph, RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT,
				RepositorySettings.VIRTUOSO_DB, RepositorySettings.VIRTUOSO_DB);
		ByteArrayInputStream input = new ByteArrayInputStream(content.getBytes());
		try {
			model.removeAll();
			model.read(new InputStreamReader(input), null, type);
		} catch (Exception e) {
			throw new DatasourceException(e.getMessage(), Resource.class);
		}
		model.close();
		return true;
	}

	public Boolean deleteResource(String graph) {
		
		//It is not necessary to check if "id" is a graph.
		Model model = VirtModel.openDatabaseModel(graph, RepositorySettings.VIRTUOSO_HOST + RepositorySettings.VIRTUOSO_PORT,
				RepositorySettings.VIRTUOSO_DB, RepositorySettings.VIRTUOSO_DB);
		model.removeAll();
		model.close();
		return true;
	}
	
	public String executeQuery(String query, String type)
	{
		//Execute the query and return result in the format given.
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		Model model = VirtuosoQueryExecutionFactory.create(QueryFactory.create(query), set).execSelect().getResourceModel();
		try {
			model.write(output, type, null);
		} catch (Exception e) {
			
		}
		return output.toString();
	}

}
