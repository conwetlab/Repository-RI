/*
Modified BSD License  
====================

Copyright (c) 2012, SAP AG
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

package org.fiware.apps.repository.rest;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.fiware.apps.repository.dao.CollectionDAO;
import org.fiware.apps.repository.dao.DAOFactory;
import org.fiware.apps.repository.dao.ResourceDAO;
import org.fiware.apps.repository.dao.impl.VirtuosoResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.exceptions.web.RestConflictException;
import org.fiware.apps.repository.exceptions.web.RestInternalServerException;
import org.fiware.apps.repository.exceptions.web.RestNotFoundException;
import org.fiware.apps.repository.model.AbstractResource;
import org.fiware.apps.repository.model.FileUploadForm;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

@Path("/")
public class CollectionService {

	DAOFactory mongoFactory = DAOFactory.getDAOFactory(DAOFactory.MONGO);
	CollectionDAO mongoCollectionDAO = mongoFactory.getCollectionDAO();
	ResourceDAO mongoResourceDAO = mongoFactory.getResourceDAO();
	VirtuosoResourceDAO virtuosoResourceDAO = new VirtuosoResourceDAO();
	JAXBContext ctx;

	 @Context
	 UriInfo uriInfo;

	@GET	
	@Path("/")		
	@Produces({"application/xml", "application/json"})
	public Response getResourceRoot() {		
		throw new RestNotFoundException("Please specify a collection");

	}
	
	@GET
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")	
	public Response getResource(@HeaderParam("Accept") String accept, @PathParam("path") String path) {
			return getResource(path, false, accept);
	}
	
	@GET	
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}.meta")
	public Response getResourceMeta(@HeaderParam("Accept") String accept, @PathParam("path") String path) {		
		return getResource(path, true, accept);
	}

	private Response getResource(String path, boolean meta, String type) {		
		Resource resource = null;
		ResourceCollection resourceCollection = null;
		
		try {
			// Check, if the path is a resource or a collection or does not exists
			resource = mongoResourceDAO.getResource(path);
			if (resource == null)
			{
				resourceCollection = mongoCollectionDAO.getCollection(path);
				if (resourceCollection == null)
					throw new RestNotFoundException("Collection or resource not found");
				else
					return RestHelper.multiFormatResponse(resourceCollection, ResourceCollection.class, type, uriInfo);
			}
			
			if(!meta)
			{
				// Obtain the resource content from virtuoso triple store.
				if(!type.equalsIgnoreCase("application/rdf+xml") && RestHelper.isRDF(type))
					resource.setContent(virtuosoResourceDAO.getResource(resource.getContentFileName(), RestHelper.typeMap.get(type)).getContent());
				// Obtain the resource content mongo DB. 
				else
					resource.setContent(mongoResourceDAO.getResourceContent(path).getContent());
			}
			
			return Response.status(Response.Status.OK).header("content-length", resource.getContent().length).entity(resource.getContent()).type(type).build();
			
		} catch (DatasourceException e) {
			throw new RestInternalServerException(e.getMessage());
		} catch (JAXBException e) {
			throw new RestInternalServerException("Error parsing content");
		} 
	}

	@POST	
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")		
	@Consumes({"application/xml", "application/json"})
	public Response postResource(@PathParam("path") String path,  AbstractResource absRes) {	
		if(path.startsWith("/")){
			path = path.substring(1);
		}
		return insertResource(path, absRes);
	}
	
	private Response insertResource(String path, AbstractResource absRes) {
		Resource resource = (Resource) absRes;
		try {
			if(RestHelper.isRDF(resource.getContentMimeType())) {
				virtuosoResourceDAO.insertResource(path, resource.getContent().toString(), 
						RestHelper.typeMap.get(resource.getContentMimeType()));
			}
			mongoResourceDAO.insertResource(resource);
			return Response.status(Status.CREATED).contentLocation(new URI(path)).build();
		} catch (DatasourceException e) {
			e.printStackTrace();
			throw new RestInternalServerException(e.getMessage());
		} catch (SameIdException e) {
			e.printStackTrace();
			return Response.status(Status.CONFLICT).build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
			throw new RestInternalServerException(e.getMessage());
		}
	}
	
	
	private Response insertResource(String path, FileUploadForm form) {
		Resource resource;
		try {
			resource = new Resource();
			resource.setId(path);
			resource.setContentFileName(form.getFilename());
			resource.setContentMimeType(form.getMimeType());
			resource.setContent(form.getFileData());
			//resource.setContentUrl();
			//resource.setCreationDate();
			//resource.setCreator(creator);
			//resource.setModificationDate();
			//resource.setName("");
			if(RestHelper.isRDF(resource.getContentMimeType()))
			{
				virtuosoResourceDAO.insertResource(path, resource.getContent().toString(), 
						RestHelper.typeMap.get(resource.getContentMimeType()));
			}
			mongoResourceDAO.insertResource(resource);
			return Response.status(Status.CREATED).build();
		} catch (DatasourceException e) {
			throw new RestInternalServerException(e.getMessage());
		} catch (SameIdException e) {
			throw new RestConflictException(e.getMessage());
		}
	}
	
	private Response insertResourceContentRdfGeneric(String path, String content, String rdfType) {
		try {			
			Resource r = mongoResourceDAO.getResource(path);
			if(r==null){
				r= new Resource();
				r.setId(path);
				try {
					mongoResourceDAO.insertResource(r);
				} catch (SameIdException e) {
				}
			}
			r.setContent(content.getBytes());	
			r.setContentFileName("filename");
			r.setContentMimeType(rdfType);		
			try {
				mongoResourceDAO.updateResourceContent(r);
			} catch (DatasourceException e) {
				throw new RestConflictException(e.getMessage());
			}
			return Response.status(Status.CREATED).build();

		} catch (DatasourceException e) {
		
			throw new RestInternalServerException(e.getMessage());
		}
	}
		
	
	@PUT	
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")	
	public Response putResource(@HeaderParam("Accept") String accept,@HeaderParam("Content-Type") String contentType,  @PathParam("path") String path, String content/*, @MultipartForm FileUploadForm form*/) {	
	
		if(path.startsWith("/")){
			path = path.substring(1);
		}
	/*	if(accept.startsWith("multipart/form-data")){
			return insertResourceContent(path, form);
		}*/
		
		//return insertResourceContentRdfGeneric(path, content, contentType);
		return updateResource(path, content, contentType);
		
	}
	
	private Response updateResource(String path, String content, String type) {
		Resource resource;
		try {
			resource = mongoResourceDAO.getResource(path);
			resource.setContent(content.getBytes());
			resource.setContentMimeType(type);
			if(RestHelper.isRDF(resource.getContentMimeType())) {
				virtuosoResourceDAO.updateResource(path, resource.getContent().toString(), 
						RestHelper.typeMap.get(resource.getContentMimeType()));
			}
			mongoResourceDAO.insertResource(resource);
			
		} catch (DatasourceException e) {
			e.printStackTrace();
			throw new RestInternalServerException(e.getMessage());
		} catch (SameIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Status.OK).build();
	}
	
	private Response updateResource(String path, AbstractResource absRes) {
		Resource resource;
		ResourceCollection resourceCollection;
		try {
			if (absRes.getClass().equals(Resource.class))
			{
				resource = (Resource) absRes;
				mongoResourceDAO.updateResource(path, resource);
				if(RestHelper.isRDF(resource.getContentMimeType())) {
					virtuosoResourceDAO.updateResource(path, resource.getContent().toString(), 
							RestHelper.typeMap.get(resource.getContentMimeType()));
				}
			}
			else
			{
				resourceCollection = (ResourceCollection) absRes;
				mongoCollectionDAO.updateCollection(path, resourceCollection);
			}
		} catch (DatasourceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(Status.OK).build();
	}

	
	
/*
	@PUT
	@POST
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")	
	@Consumes("application/rdf+xml")
	public Response insertResourceContentRdfXml(@PathParam("path") String path, String content) {
		return insertResourceContentRdfGeneric(path, content, "application/rdf+xml");
	}

	@PUT
	@POST
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")	
	@Consumes("text/turtle")
	public Response insertResourceContentRdfTurtle(@PathParam("path") String path, String content) {
		return insertResourceContentRdfGeneric(path, content, "text/turtle");
	}


	@PUT
	@POST
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")	
	@Consumes("text/n3")
	public Response insertResourceContentRdf(@PathParam("path") String path, String content) {
		return insertResourceContentRdfGeneric(path, content, "text/n3");
	}

*/

	@DELETE
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")	
	public Response delete(@PathParam("path") String path) {
		return deleteResource(path);
	}
	
	private Response deleteResource(String path)
	{
		// Check if the path is a resource or a collection and remove it.
		try {
			if (mongoResourceDAO.isResource(path))
			{
				mongoResourceDAO.deleteResource(path);
				virtuosoResourceDAO.deleteResource(path);
			}
			else
			{
				mongoCollectionDAO.deleteCollection(path);
			}
		} catch (DatasourceException e) {
			e.printStackTrace();
			throw new RestInternalServerException(e.getMessage());
		}
		return Response.status(Status.ACCEPTED).build();
	}
}
