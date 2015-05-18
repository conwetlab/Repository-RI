/*
Modified BSD License
====================

Copyright (c) 2012, SAP AG
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

package org.fiware.apps.repository.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.fiware.apps.repository.dao.CollectionDAO;
import org.fiware.apps.repository.dao.DAOFactory;
import org.fiware.apps.repository.dao.ResourceDAO;
import org.fiware.apps.repository.dao.VirtuosoDAOFactory;
import org.fiware.apps.repository.dao.impl.VirtuosoResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.AbstractResource;
import org.fiware.apps.repository.model.FileUploadForm;
import org.fiware.apps.repository.model.RepositoryException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;
import org.fiware.apps.repository.settings.RepositorySettings;

@Path("/"+RepositorySettings.COLLECTION_SERVICE_NAME)
public class CollectionService {
    
    private DAOFactory mongoFactory = DAOFactory.getDAOFactory(DAOFactory.MONGO);
    private CollectionDAO mongoCollectionDAO = mongoFactory.getCollectionDAO();
    private ResourceDAO mongoResourceDAO = mongoFactory.getResourceDAO();
    private VirtuosoResourceDAO virtuosoResourceDAO = VirtuosoDAOFactory.getVirtuosoResourceDAO();
    JAXBContext ctx;
    
    @GET
    @Path("/")
    @Produces({"application/xml", "application/json"})
    public Response getResourceRoot() {
        return Response.status(Response.Status.NOT_FOUND).type("application/xml").entity(new RepositoryException(Response.Status.NOT_FOUND,"Please specify a collection")).build();
        //throw new RestNotFoundException("Please specify a collection", new Exception());
    }
    
    @GET
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")
    public Response getResource(@Context UriInfo uriInfo, @HeaderParam("Accept") String accept, @PathParam("path") String path) {
        return getResource(path, false, accept, uriInfo);
    }
    
    @GET
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}.meta")
    public Response getResourceMeta(@Context UriInfo uriInfo, @HeaderParam("Accept") String accept, @PathParam("path") String path) {
        return getResource(path, true, accept, uriInfo);
    }
    
    private Response getResource(String path, boolean meta, String type, UriInfo uriInfo) {
        
        try {
            Resource resource = null;
            ResourceCollection resourceCollection = null;
            
            // Check, if the path is a resource or a collection or it does not exist.
            resource = mongoResourceDAO.getResource(path);
            if (resource == null)
            {
                resourceCollection = mongoCollectionDAO.getCollection(path);
                if (resourceCollection == null || meta)
                    return Response.status(Response.Status.NOT_FOUND).type("application/xml").entity(new RepositoryException(Response.Status.NOT_FOUND,"Collection or resource not found")).build();
                else
                    return RestHelper.multiFormatResponse(resourceCollection, ResourceCollection.class, type, uriInfo);
            }
            
            if(!meta)
            {
                // Obtain the resource content from virtuoso triple store.
                if(!type.equalsIgnoreCase(resource.getContentMimeType()) && RestHelper.isRDF(type)) {
                    resource.setContent(virtuosoResourceDAO.getResource(path, RestHelper.typeMap.get(type)).getContent());
                }
                // Obtain the resource content mongo DB.
                else {
                    resource.setContent(mongoResourceDAO.getResourceContent(path).getContent());
                }
                if (resource.getContent() == null)
                {
                    return Response.status(Response.Status.NO_CONTENT).build();
                }
                return Response.status(Response.Status.OK).header("content-length", resource.getContent().length).entity(resource.getContent()).type(type).build();
            }
            else
            {
                return RestHelper.multiFormatResponse(resource, Resource.class, type, uriInfo);
            }
        } catch (DatasourceException | JAXBException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type("application/xml").entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        }
    }
    
    @POST
    @Consumes({"application/xml", "application/json"})
    public Response postResource(@Context UriInfo uriInfo, AbstractResource absRes) {
        if(absRes.getClass().equals(Resource.class)) {
            return insertResource((Resource) absRes, uriInfo);
        }
        else {
            return insertCollection((ResourceCollection) absRes);
        }
    }
    
    private Response insertResource(Resource resource, UriInfo uriInfo) {
        // Create a new resource with the resource metadata given.
        // Some metadata can not be given by the user.
        resource.setContentMimeType("");
        resource.setCreationDate(new Date());
        resource.setContentUrl(uriInfo.getAbsolutePath().toString() + "/" + resource.getId());
        try {
            mongoResourceDAO.insertResource(resource);
            return Response.status(Status.CREATED).contentLocation(new URI(resource.getContentUrl())).build();
        } catch (DatasourceException | URISyntaxException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        } catch (SameIdException ex) {
            return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.CONFLICT, ex.getMessage())).build();
        }
    }
    
    private Response insertCollection(ResourceCollection resourceCollection) {
        try {
            mongoCollectionDAO.insertCollection(resourceCollection);
        } catch (DatasourceException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        } catch (SameIdException ex) {
            return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.CONFLICT, ex.getMessage())).build();
        }
        return Response.status(Status.CREATED).build();
    }
    
    @PUT
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")
    public Response putResource(@HeaderParam("Content-Type") String contentType, @PathParam("path") String path, String content/*, @MultipartForm FileUploadForm form*/) {
        return updateResource(path, content, contentType);
    }
    
    @PUT
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}.meta")
    public Response putResource(@HeaderParam("Content-Type") String contentType, @PathParam("path") String path, AbstractResource absRes) {
        if(absRes.getClass().equals(Resource.class)) {
            return updateResource(path, (Resource) absRes);
        }
        else {
            return Response.status(Status.FORBIDDEN).build();
        }
    }
    
    private Response updateResource(String path, String content, String type) {
        // Update a resource content inserting in virtuoso triple store if content is RDF.
        Resource resource;
        try {
            resource = mongoResourceDAO.getResource(path);
            if (resource == null)
            {
                resource = new Resource();
                resource.setId(path);
                mongoResourceDAO.insertResource(resource);
            }
            resource.setContent(content.getBytes());
            resource.setContentMimeType(type);
            if(RestHelper.isRDF(type)) {
                virtuosoResourceDAO.updateResource(path, content,
                        RestHelper.typeMap.get(resource.getContentMimeType()));
            }
            mongoResourceDAO.updateResourceContent(resource);
            
        } catch (DatasourceException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        } catch (SameIdException ex) {
            //this case is posible if concurrency exist.??
            return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.CONFLICT, ex.getMessage())).build();
        }
        return Response.status(Status.OK).build();
    }
    
    private Response updateResource(String path, Resource resource) {
        //Update a resource metadata
        try {
            Resource aux = mongoResourceDAO.getResourceContent(path);
            resource.setId(aux.getId());
            resource.setContentMimeType(aux.getContentMimeType());
            resource.setContent(aux.getContent());
            resource.setCreationDate(aux.getCreationDate());
            resource.setContentUrl(aux.getContentUrl());
            mongoResourceDAO.updateResource(path, resource);
        } catch (DatasourceException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        }
        return Response.status(Status.OK).build();
    }
    
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
        } catch (DatasourceException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        }
        return Response.status(Status.ACCEPTED).build();
    }
}
