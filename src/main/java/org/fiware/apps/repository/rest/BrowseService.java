package org.fiware.apps.repository.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.fiware.apps.repository.dao.CollectionDAO;
import org.fiware.apps.repository.dao.DAOFactory;
import org.fiware.apps.repository.dao.ResourceDAO;
import org.fiware.apps.repository.exceptions.web.RestConflictException;
import org.fiware.apps.repository.exceptions.web.RestNotAcceptableException;
import org.fiware.apps.repository.exceptions.web.RestNotFoundException;
import org.fiware.apps.repository.model.FileUploadForm;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;
import org.fiware.apps.repository.model.ResourceFilter;
import org.fiware.apps.repository.model.User;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;


@Path("/repo")
public class BrowseService {

	private User user = new User("testUser");

	DAOFactory mongoFactory = DAOFactory.getDAOFactory(DAOFactory.MONGO);
	ResourceDAO resourceDAO = mongoFactory.getResourceDAO();
	CollectionDAO collectionDAO = mongoFactory.getCollectionDAO();

	
	
	/*
	@GET
	@Path("/{collection}/{resource}")	
	@Produces({"application/xml", "application/json"})
	public Resource getResourcebyName(@PathParam("collection") String collection, @PathParam("resource") String resourceName) {
		try{
			Resource r = resourceDAO.findResourceByName(collection, resourceName);
		
			return r;
		}catch (Exception e){
			return null;
		}
	}

	@PUT
	@Path("/{collection}/{resource}")		
	@Consumes({"application/xml", "application/json"})
	@Produces({"application/xml", "application/json"})
	public Resource createResource(@PathParam("collection") String collection, @PathParam("resource") String resourceName, Resource resource) {
	
		return null;
	}


	@POST
	@Path("/{collection}/{resource}")	
	@Consumes({"application/xml", "application/json"})
	public Response updateResource(@PathParam("collection") String collection, @PathParam("resource") String resourceName, Resource resource) {		
		try {
			Boolean success = resourceDAO.updateResource(collection, resource);
			if (success){
				return Response.status(Status.OK).build();
			}
		} catch (Exception e) {
			throw new RestNotAcceptableException("Error: Update not possibe "+ e.getMessage());
		}	

		throw new RestNotFoundException("Error: There is no resource with the specified ID");
	}

	@DELETE
	@Path("/{collection}/{resource}")		
	public Response deleteResourceByName(@PathParam("collection") String collection, @PathParam("resource") String resourceName) {
		Boolean success = resourceDAO.deleteResourceByName(collection, resourceName);	
		if (success){
			return Response.status(Status.OK).build();
		}
		throw new RestConflictException("Deletion not possible");

	}


	@PUT
	@POST
	@Path("/{collection}/{resource}/content")	
	@Consumes("multipart/form-data")
	public Response insertResourceContent(@PathParam("collection") String collection, @PathParam("resource") String resourceName, @MultipartForm FileUploadForm form) {
		Resource r = getResourcebyName(collection, resourceName);
		r.setContent(form.getFileData());	
		r.setContentFileName(form.getFilename());
		r.setContentMimeType(form.getMimeType());		
		resourceDAO.updateResourceContent(r);
		return Response.status(Status.OK).build();
	}


	@GET
	@Path("/{collection}/{resource}/content")	
	public Response getResourceContent(@PathParam("collection") String collection, @PathParam("resource") String resourceName) {

		Resource r = resourceDAO.getResourceContentByName(collection, resourceName);
		String responseString = new String(r.getContent());
		if(r.getContentMimeType().equals("application/octet-stream")){
			String disHeader = "Attachment;  Filename=\""+r.getContentFileName()+"\"";			
			return Response.status(Response.Status.OK).header("Content-Disposition", disHeader).entity(r.getContent()).build();  
		}else{
			return Response.status(Response.Status.OK).header("content-length", responseString.length()).entity(r.getContent()).type(r.getContentMimeType()).build();  
		}
	}



	@GET
	@Path("/")	
	@Produces({"application/xml", "application/json"})
	public List<ResourceCollection> getCollection() {
		return collectionDAO.getCollections();	
		
	}

	@GET
	@Path("/{collection}")	
	@Produces({"application/xml", "application/json"})
	public List<Resource> getResources(@PathParam("collection") String collection, @QueryParam("limit") int limit, @QueryParam("offset") int offset, @QueryParam("filter") String filter ) {
		
		return resourceDAO.getResources(collection, new ResourceFilter(offset, limit, filter));	
		
		return null;
	}
	
	

*/


}