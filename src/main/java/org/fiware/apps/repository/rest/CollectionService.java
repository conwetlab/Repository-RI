package org.fiware.apps.repository.rest;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.io.Writer;

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
	CollectionDAO collectionDAO = mongoFactory.getCollectionDAO();
	ResourceDAO resourceDAO = mongoFactory.getResourceDAO();
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

	/*

	@GET	
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}.rdf")	
	@Produces("application/rdf+xml")
	public Response getResourceRdfXml(@PathParam("path") String path) {		
		return getResourceRdf(path, "application/rdf+xml", "RDF/XML");

	}

	@GET	
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}.turtle")	
	@Produces("text/turtle")
	public Response getResourceRdfTurtle(@PathParam("path") String path) {		
		return getResourceRdf(path, "text/turtle", "TURTLE");

	}

	@GET	
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}.n3")	
	@Produces("text/n3")
	public Response getResourceRdfN3(@PathParam("path") String path) {		
		return getResourceRdf(path, "text/n3", "N3");

	}
	 */

	private Response getResourceRdf(String path, String rdfType, String jenaFormat){
		Resource r = null;
		try {			
			r = resourceDAO.getResourceContent(path);
			if(r.getContent()==null){
				 throw new RestNotFoundException("Representation not found");					
			}
			
			ByteArrayInputStream in = new ByteArrayInputStream(r.getContent());
						
			Model model = ModelFactory.createDefaultModel();
			model.read(in, null);			
			Writer writer = new StringWriter();
			in.close();
			
			model.write(writer, jenaFormat);			
			 
			String responseString = writer.toString();
			return Response.status(Response.Status.OK).header("content-length", responseString.length()).entity(responseString).type(rdfType).build();  

		} catch (Exception e) {	
			return Response.status(Response.Status.NO_CONTENT).build();  
		}		
	}




	private Response getResource(String path, boolean meta, String type) {		
		

		try {
			if (!resourceDAO.isResource(path)){		
				// Collection Content			
				ResourceCollection c;	
				c = collectionDAO.getCollection(path);
				if(c==null){					
					throw new RestNotFoundException("Collection not found");
				}
				return RestHelper.multiFormatResponse(c, ResourceCollection.class, type, uriInfo);			
			}else{
				// Resource Content
				Resource r = null;
				try {

					if(meta){
						r = resourceDAO.getResource(path);
					}else{
						r = resourceDAO.getResourceContent(path);
					}			

					if(r.getContent()==null){
						return RestHelper.multiFormatResponse(r, Resource.class, type, uriInfo);						
					}

					String responseString = new String(r.getContent());					
					if(r.getContentMimeType().equals("application/octet-stream")){
						String disHeader = "Attachment;  Filename=\""+r.getContentFileName()+"\"";			
						return Response.status(Response.Status.OK).header("Content-Disposition", disHeader).entity(r.getContent()).build();  
					}else{
						return Response.status(Response.Status.OK).header("content-length", responseString.length()).entity(r.getContent()).type(r.getContentMimeType()).build();  
					}
				} catch (Exception e) {	
					return Response.status(Response.Status.NO_CONTENT).build();  
				}		

			}
		} catch (DatasourceException e) {
			e.printStackTrace();
			throw new RestInternalServerException(e.getMessage()); 

		} catch (JAXBException e) {
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
		
		return insertResourceContentRdfGeneric(path, content, contentType);
		
	}

	


	@POST	
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")		
	@Consumes({"application/xml", "application/json"})
	public Response postResource(@PathParam("path") String path,  AbstractResource absRes) {	
		if(path.startsWith("/")){
			path = path.substring(1);
		}

		try {
			if(absRes.getClass().equals(Resource.class)){
				Resource res = (Resource) absRes;				
				resourceDAO.updateResource(path, res);
			}else{
				ResourceCollection col = (ResourceCollection) absRes;				
				collectionDAO.updateCollection(path, col);
			}
		} catch (DatasourceException e) {
			e.printStackTrace();
			throw new RestInternalServerException(e.getMessage());
		} 
		return Response.status(201).build();
	}


	
	private Response insertResourceContent(String path, FileUploadForm form) {
		try {			

			Resource r = resourceDAO.getResource(path);
			if(r==null){
				r= new Resource();
				r.setId(path);
				try {
					// create if not exist. TODO: set creator Name
					resourceDAO.insertResource(r);
				} catch (SameIdException e) {
					//should never happen
				}
			}

			r.setContent(form.getFileData());	
			r.setContentFileName(form.getFilename());
			r.setContentMimeType(form.getMimeType());		
			try {
				resourceDAO.updateResourceContent(r);
			} catch (DatasourceException e) {
				throw new RestConflictException(e.getMessage());
			}
			return Response.status(Status.OK).build();

		} catch (DatasourceException e) {
			throw new RestInternalServerException(e.getMessage());
		}

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


	private Response insertResourceContentRdfGeneric(String path, String content, String rdfType) {
		try {			
			Resource r = resourceDAO.getResource(path);
			if(r==null){
				r= new Resource();
				r.setId(path);
				try {
					resourceDAO.insertResource(r);
				} catch (SameIdException e) {
				}
			}
			r.setContent(content.getBytes());	
			r.setContentFileName("filename");
			r.setContentMimeType(rdfType);		
			try {
				resourceDAO.updateResourceContent(r);
			} catch (DatasourceException e) {
				throw new RestConflictException(e.getMessage());
			}
			return Response.status(Status.CREATED).build();

		} catch (DatasourceException e) {
		
			throw new RestInternalServerException(e.getMessage());
		}
	}



	@DELETE
	@Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")	
	public Response delete(@PathParam("path") String path) {
		try {
			if (resourceDAO.isResource(path)){
				resourceDAO.deleteResource(path);
			}else{
				collectionDAO.deleteCollection(path);
			}
		} catch (DatasourceException e) {
			throw new RestInternalServerException(e.getMessage());
		}
		return null;
	}


}
