/*
Modified BSD License
====================

Copyright (c) 2012, SAP AG
Copyright (c) 2015, CoNWeT Lab., Universidad Politecnica Madrid
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
* Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
* Neither the name of the copyright holders nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS BE LIABLE FOR ANY
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

import javax.ws.rs.*;
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
    public Response getResourceRoot(@HeaderParam("Accept") String accept) {
        return Response.status(Response.Status.NOT_FOUND).type(accept).entity(new RepositoryException(Response.Status.NOT_FOUND,"Please specify a collection")).build();
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
                    resource.setContent(virtuosoResourceDAO.getResource(resource.getContentUrl(), RestHelper.typeMap.get(type)).getContent());
                }
                // Obtain the resource content mongo DB.
                else {
                    resource.setContent(mongoResourceDAO.getResourceContent(path).getContent());
                }
                if (resource.getContent() == null)
                {
                    return Response.status(Response.Status.NO_CONTENT).build();
                }
                return Response.status(Response.Status.OK).header("content-length", resource.getContent().length).entity(resource.getContent()).build();
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
        return updateResourceContent(path, content, contentType);
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}.meta")
    public Response putResource(@HeaderParam("Content-Type") String contentType, @PathParam("path") String path, AbstractResource absRes) {
        if(absRes.getClass().equals(Resource.class)) {
            return updateResource(path, (Resource) absRes);
        }
        else {
            return Response.status(Status.FORBIDDEN).build();
        }
    }

    private Response updateResourceContent(String path, String content, String type) {
        // Update a resource content inserting in virtuoso triple store if content is RDF.
        Resource resource;
        try {
            resource = mongoResourceDAO.getResource(path);
            if (resource == null)
            {
                return Response.status(Response.Status.NOT_FOUND).type("application/xml").entity(new RepositoryException(Response.Status.NOT_FOUND,"Collection or resource not found")).build();
            }
            resource.setContent(content.getBytes());
            resource.setContentMimeType(type);
            if(RestHelper.isRDF(type)) {
                virtuosoResourceDAO.updateResource(resource.getContentUrl(), content,
                        RestHelper.typeMap.get(resource.getContentMimeType()));
            }
            else {
                virtuosoResourceDAO.deleteResource(resource.getContentUrl());
            }
            mongoResourceDAO.updateResourceContent(resource);

        } catch (DatasourceException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        }
        return Response.status(Status.OK).build();
    }

    private Response updateResource(String path, Resource resource) {
        //Update a resource metadata.
        try {
            Resource aux = mongoResourceDAO.getResourceContent(path);
            resource.setId(aux.getId());
            resource.setContent(aux.getContent());
            resource.setCreationDate(aux.getCreationDate());

            if (!resource.getContentMimeType().equals(aux.getContentMimeType())) {
                return Response.status(Status.FORBIDDEN).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Response.Status.FORBIDDEN,"Changing ContentMimeType is forbidden")).build();
            }
            if (!resource.getContentUrl().equals(aux.getContentUrl())) {
                return Response.status(Status.FORBIDDEN).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Response.Status.FORBIDDEN,"Changing UrlContent is forbidden")).build();
            }
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
            Resource resource = mongoResourceDAO.getResource(path);
            if (resource != null) {
                mongoResourceDAO.deleteResource(path);
                virtuosoResourceDAO.deleteResource(resource.getContentUrl());
            }
            else {
                if (mongoCollectionDAO.findCollection(path) != null) {
                    mongoCollectionDAO.deleteCollection(path);
                } else {
                    return Response.status(Response.Status.NOT_FOUND).type("application/xml").entity(new RepositoryException(Response.Status.NOT_FOUND,"Collection or resource not found")).build();
                }
            }
        } catch (DatasourceException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        }
        return Response.status(Status.NO_CONTENT).build();
    }
}
