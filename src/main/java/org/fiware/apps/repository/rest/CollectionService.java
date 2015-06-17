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
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
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
    public Response getResourceRoot(@Context HttpHeaders headers) {
        List <MediaType> accepts = headers.getAcceptableMediaTypes();
        if(accepts.isEmpty()) {
            accepts = new LinkedList();
            accepts.add(MediaType.APPLICATION_XML_TYPE);
        }
        return Response.status(Response.Status.NOT_FOUND).type(accepts.get(0)).entity(new RepositoryException(Response.Status.NOT_FOUND,"Please specify a collection")).build();
    }

    @GET
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")
    public Response getResource(@Context UriInfo uriInfo, @Context HttpHeaders headers, @PathParam("path") String path) {
        return getResource(path, headers.getAcceptableMediaTypes(), uriInfo);
    }

    @GET
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}.meta")
    public Response getResourceMeta(@Context UriInfo uriInfo, @Context HttpHeaders headers, @PathParam("path") String path) {
        List <MediaType> accepts = headers.getAcceptableMediaTypes();
        //If Accept header is empty, add default media type.
        if(accepts.isEmpty()) {
            accepts = new LinkedList();
            accepts.add(MediaType.valueOf("application/xml"));
        }

        for (MediaType type : accepts) {
            if(RestHelper.isResourceOrCollectionType(type.getType()+"/"+type.getSubtype())) {
                return getResourceMeta(path, type.getType()+"/"+type.getSubtype(), uriInfo);
            }
            if("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) {
                return getResourceMeta(path, "application/xml", uriInfo);
            }
        }

        return Response.status(Status.NOT_ACCEPTABLE).build();
    }

    private Response getResource(String path, List <MediaType> types, UriInfo uriInfo) {

        //Check if the path is a resource or a resourceCollection
        Resource resource = null;
        try {
            resource = mongoResourceDAO.getResource(path);
        } catch (DatasourceException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type("application/xml").entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        }
        if (resource == null) {
            //If Accept header is empty, add default media type.
            if(types.isEmpty()) {
                types = new LinkedList();
                types.add(MediaType.valueOf("application/xml"));
            }
            //Ask the resourceCollection in the first compatible type
            for (MediaType type : types) {
                String typeString = ("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) ? "application/xml" : type.getType()+"/"+type.getSubtype();

                if(RestHelper.isResourceOrCollectionType(typeString)) {
                    return getCollection(path, typeString, uriInfo);
                }
            }
            return Response.status(Status.NOT_ACCEPTABLE).build();
        }

        //If Accept header is empty, add default media type.
        if(types.isEmpty()) {
            types = new LinkedList();
            types.add(MediaType.valueOf("application/rdf+xml"));
        }

        try {
            //Get the resource in the first compatible type
            for (MediaType type : types) {

                Resource resourceContent;
                String typeString = ("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) ? "application/rdf+xml" : type.getType()+"/"+type.getSubtype();

                //Check is posible to obtain the content from Mongo or from Virtuoso and return it.
                if (typeString.equalsIgnoreCase(resource.getContentMimeType())) {
                    resourceContent = mongoResourceDAO.getResourceContent(path);
                    if (resourceContent.getContent() != "".getBytes()) {
                        return Response.status(Response.Status.OK).header("content-length", resourceContent.getContent().length).type(typeString).entity(resourceContent.getContent()).build();
                    } else {
                        return Response.status(Response.Status.NO_CONTENT).build();
                    }
                } else if (RestHelper.isRDF(typeString)) {
                    resourceContent = virtuosoResourceDAO.getResource(resource.getContentUrl(), RestHelper.typeMap.get(typeString));
                    if (resourceContent != null) {
                        return Response.status(Response.Status.OK).header("content-length", resourceContent.getContent().length).type(typeString).entity(resourceContent.getContent()).build();
                    } else {
                        return Response.status(Response.Status.NO_CONTENT).build();
                    }
                }
            }

            //If there are not any compatible type, returns NOT_ACCEPTABLE.
            return Response.status(Status.NOT_ACCEPTABLE).build();

        } catch (DatasourceException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type("application/xml").entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        }

    }

    private Response getResourceMeta(String path, String type, UriInfo uriInfo) {
        try {
            Resource resource = mongoResourceDAO.getResource(path);
            if (resource != null) {
                return RestHelper.multiFormatResponse(resource, Resource.class, type, uriInfo);
            } else {
                return Response.status(Response.Status.NOT_FOUND).type("application/xml").entity(new RepositoryException(Response.Status.NOT_FOUND,"Resource not found")).build();
            }
        } catch (DatasourceException | JAXBException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type("application/xml").entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        }
    }

    private Response getCollection(String path, String type, UriInfo uriInfo) {
        try {
            ResourceCollection resourceCollection = mongoCollectionDAO.getCollection(path);
            if (resourceCollection != null) {
                return RestHelper.multiFormatResponse(resourceCollection, ResourceCollection.class, type, uriInfo);
            } else {
                return Response.status(Response.Status.NOT_FOUND).type("application/xml").entity(new RepositoryException(Response.Status.NOT_FOUND,"Resource or Collection not found")).build();
            }
        } catch (DatasourceException | JAXBException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type("application/xml").entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        }
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")
    public Response postResource(@PathParam("path") String path, AbstractResource absResource) {
        if(absResource instanceof Resource) {
            return insertResource((Resource) absResource, path);
        }
        if (absResource instanceof ResourceCollection) {
            return insertCollection((ResourceCollection) absResource, path);
        }
        return Response.status(Status.INTERNAL_SERVER_ERROR).type("application/xml").entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, absResource.getClass().toString())).build();
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    @Path("/")
    public Response postCollection(ResourceCollection resourceCollection) {
        return insertCollection(resourceCollection, "");
    }

    private Response insertResource(Resource resource, String path) {
        try {
            // Create a new resource with the resource metadata given.
            // Some metadata can not be given by the user.
            resource.setId(path+"/"+resource.getName());
            resource.setContentMimeType("");
            resource.setCreationDate(new Date());

            if (path == null || path.equalsIgnoreCase("")) {
                return Response.status(Status.BAD_REQUEST).build();
            }
            if ((!checkPath(path) && !path.equalsIgnoreCase("")) ||
                    mongoCollectionDAO.getCollection(resource.getId()) != null ||
                    mongoResourceDAO.getResource(resource.getId()) != null) {
                return Response.status(Status.CONFLICT).build();
            }

            mongoResourceDAO.insertResource(resource);
            return Response.status(Status.CREATED).contentLocation(new URI(resource.getId())).build();
        } catch (DatasourceException | URISyntaxException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        } catch (SameIdException ex) {
            return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.CONFLICT, ex.getMessage())).build();
        }
    }

    private Response insertCollection(ResourceCollection resourceCollection, String path) {
        try {

            if (!path.equalsIgnoreCase("")) {
                resourceCollection.setId(path+"/"+resourceCollection.getName());
            } else {
                resourceCollection.setId(resourceCollection.getName());
            }

            if (!checkPath(path) ||
                    mongoCollectionDAO.getCollection(resourceCollection.getId()) != null ||
                    mongoResourceDAO.getResource(resourceCollection.getId()) != null) {
                return Response.status(Status.CONFLICT).build();
            }

            mongoCollectionDAO.insertCollection(resourceCollection);
            return Response.status(Status.CREATED).contentLocation(new URI(resourceCollection.getId())).build();
        } catch (DatasourceException | URISyntaxException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        } catch (SameIdException ex) {
            return Response.status(Status.CONFLICT).type(MediaType.APPLICATION_XML).entity(new RepositoryException(Status.CONFLICT, ex.getMessage())).build();
        }

    }

    private boolean checkPath(String path) throws DatasourceException {
        if (!mongoResourceDAO.isResource(path)) {
            if (path.lastIndexOf("/") == -1) {
                return true;
            } else {
                return checkPath(path.substring(0, path.lastIndexOf("/")));
            }
        } else {
            return false;
        }
    }

    @PUT
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")
    public Response putResource(@HeaderParam("Content-Type") String contentType, @PathParam("path") String path, String content/*, @MultipartForm FileUploadForm form*/) {
        if (contentType == null || contentType.equalsIgnoreCase("")) {
            return Response.status(Status.BAD_REQUEST).build();
        } else {
            return updateResourceContent(path, content, contentType);
        }
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}.meta")
    public Response putResource(@HeaderParam("Content-Type") String contentType, @PathParam("path") String path, Resource resource) {
        return updateResource(path, (Resource) resource);
    }

    private Response updateResourceContent(String path, String content, String type) {
        // Update a resource content inserting in virtuoso triple store if content is RDF.
        Resource resource;
        try {
            resource = mongoResourceDAO.getResourceContent(path);
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
            Resource aux = mongoResourceDAO.getResource(path);
            if (aux == null)
            {
                resource.setId(path);
                resource.setName(path.substring(path.lastIndexOf("/")+1, path.length()));
                return postResource(path.substring(0, path.lastIndexOf("/")), resource);
            }

            resource.setId(path.substring(0, path.lastIndexOf("/"))+"/"+resource.getName());
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
