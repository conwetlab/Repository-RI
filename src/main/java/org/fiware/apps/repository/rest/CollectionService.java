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
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletContext;

import javax.ws.rs.*;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBException;

import org.fiware.apps.repository.dao.CollectionDAO;
import org.fiware.apps.repository.dao.MongoDAOFactory;
import org.fiware.apps.repository.dao.ResourceDAO;
import org.fiware.apps.repository.dao.VirtuosoDAOFactory;
import org.fiware.apps.repository.dao.impl.VirtuosoResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.exceptions.db.SameIdException;
import org.fiware.apps.repository.model.AbstractResource;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;
import org.fiware.apps.repository.settings.RepositorySettings;

@Path("/"+RepositorySettings.COLLECTION_SERVICE_NAME)
public class CollectionService {

    private MongoDAOFactory mongoFactory;
    private CollectionDAO mongoCollectionDAO;
    private ResourceDAO mongoResourceDAO;
    private VirtuosoResourceDAO virtuosoResourceDAO;

    public CollectionService(@Context ServletContext servletContext) {
        RepositorySettings repositorySettings = new RepositorySettings(servletContext.getInitParameter("propertiesFile"));
        Properties repositoryProperties = repositorySettings.getProperties();
        this.mongoFactory = new MongoDAOFactory();
        this.mongoCollectionDAO = mongoFactory.getCollectionDAO(repositoryProperties);
        this.mongoResourceDAO = mongoFactory.getResourceDAO(repositoryProperties);
        this.virtuosoResourceDAO = new VirtuosoDAOFactory().getVirtuosoResourceDAO(repositoryProperties);
    }

    @GET
    @Path("/")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getResourceRoot(@Context HttpHeaders headers) {
        return RestHelper.sendError("Please specify a collection or a resource", Status.NOT_FOUND, headers.getAcceptableMediaTypes());
    }

    @GET
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")
    public Response getResource(@Context UriInfo uriInfo,
            @Context HttpHeaders headers,
            @PathParam("path") String path) {

        List accepts = headers.getAcceptableMediaTypes();
        if(accepts.isEmpty()) {
            accepts = RestHelper.addDefaultAcceptedTypes();
        }

        return getResourceOrCollection(path, accepts, uriInfo);
    }

    @GET
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}.meta")
    public Response getResourceMeta(@Context UriInfo uriInfo,
            @Context HttpHeaders headers,
            @PathParam("path") String path) {

        List accepts = headers.getAcceptableMediaTypes();
        if(accepts.isEmpty()) {
            accepts = RestHelper.addDefaultAcceptedTypes();
        }

        return getResourceMeta(path, accepts, uriInfo);
    }

    private Response getResourceOrCollection(String path, List <MediaType> accepteds, UriInfo uriInfo) {
        Resource resource = null;
        Resource resourceContent = null;

        // Check if path is a resource or a collection.
        try {
            resource = mongoResourceDAO.getResource(path);
        } catch (DatasourceException ex) {
            return RestHelper.sendError(ex.getMessage(), Status.INTERNAL_SERVER_ERROR, accepteds);
        }

        if (resource == null) {
            return getCollection(path, accepteds, uriInfo);
        }

        try {
            for (MediaType accepted : accepteds) {
                String typeString = accepted.getType()+"/"+accepted.getSubtype();

                if ("*/*".equalsIgnoreCase(typeString)) {
                    if (RestHelper.isRDF(resource.getContentMimeType())) {
                        typeString = RestHelper.RdfDefaultType;
                    } else {
                        typeString = resource.getContentMimeType();
                    }
                }

                //Check is posible to obtain the content from Mongo or from Virtuoso and return it.
                if (typeString.equalsIgnoreCase(resource.getContentMimeType())) {
                    resourceContent = mongoResourceDAO.getResourceContent(path);
                    if (resourceContent.getContent() != null && resourceContent.getContent() != "".getBytes()) {
                        return Response.status(Response.Status.OK).header("content-length", resourceContent.getContent().length).type(typeString).entity(resourceContent.getContent()).build();
                    } else {
                        return RestHelper.sendError("No content", Status.NO_CONTENT, accepteds);
                    }
                } else if (RestHelper.isRDF(typeString)) {
                    resourceContent = virtuosoResourceDAO.getResource(resource.getContentUrl(), RestHelper.typeMap.get(typeString));
                    if (resourceContent != null) {
                        return Response.status(Response.Status.OK).header("content-length", resourceContent.getContent().length).type(typeString).entity(resourceContent.getContent()).build();
                    } else {
                        return RestHelper.sendError("No content", Status.NO_CONTENT, accepteds);
                    }
                }
            }

        } catch (DatasourceException ex) {
            return RestHelper.sendError(ex.getMessage(), Status.INTERNAL_SERVER_ERROR, accepteds);
        }

        return RestHelper.sendError("Not acceptable.", Status.NOT_ACCEPTABLE, accepteds);
    }

    private Response getResourceMeta(String path, List <MediaType> accepteds, UriInfo uriInfo) {
        Resource resource = null;

        try {
            // Get the metadata of a resource
            resource = mongoResourceDAO.getResource(path);

            if (resource == null) {
                return RestHelper.sendError("Resource not found.", Status.NOT_FOUND, accepteds);
            }

            for(MediaType accepted : accepteds) {
                String typeString = ("*/*".equalsIgnoreCase(accepted.getType()+"/"+accepted.getSubtype())) ? RestHelper.ResourcesDefaultType : accepted.getType()+"/"+accepted.getSubtype();

                if (RestHelper.isResourceOrCollectionType(typeString)) {
                    return RestHelper.multiFormatResponse(resource, Resource.class, typeString, uriInfo);
                }
            }
        } catch (JAXBException | DatasourceException ex) {
            return RestHelper.sendError(ex.getMessage(), Status.INTERNAL_SERVER_ERROR, accepteds);
        }

        return RestHelper.sendError("Not acceptable.", Status.NOT_ACCEPTABLE, accepteds);
    }

    private Response getCollection(String path, List <MediaType> accepteds, UriInfo uriInfo) {
        ResourceCollection collection = null;

        try {
            //Get a collection
            collection = mongoCollectionDAO.getCollection(path);
            if (collection == null) {
                return RestHelper.sendError("Resource or collection not found.", Status.NOT_FOUND, accepteds);
            }

            for(MediaType accepted : accepteds) {
                String typeString = ("*/*".equalsIgnoreCase(accepted.getType()+"/"+accepted.getSubtype())) ? RestHelper.ResourcesDefaultType : accepted.getType()+"/"+accepted.getSubtype();

                if (RestHelper.isResourceOrCollectionType(typeString)) {
                    return RestHelper.multiFormatResponse(collection, ResourceCollection.class, typeString, uriInfo);
                }
            }
        } catch (JAXBException | DatasourceException ex) {
            return RestHelper.sendError(ex.getMessage(), Status.INTERNAL_SERVER_ERROR, accepteds);
        }

        return RestHelper.sendError("Not acceptable.", Status.NOT_ACCEPTABLE, accepteds);
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")
    public Response postResource(@Context HttpHeaders headers,
            @PathParam("path") String path,
            AbstractResource absResource) {

        List accepts = headers.getAcceptableMediaTypes();
        if(accepts.isEmpty()) {
            accepts = RestHelper.addDefaultAcceptedTypes();
        }

        if(absResource instanceof Resource) {
            return insertResource((Resource) absResource, path, accepts);
        }
        if (absResource instanceof ResourceCollection) {
            return insertCollection((ResourceCollection) absResource, path, accepts);
        }

        return RestHelper.sendError("The content is neither a resource nor collection.", Status.INTERNAL_SERVER_ERROR, accepts);
    }

    @POST
    @Consumes({"application/xml", "application/json"})
    @Path("/")
    public Response postCollection(@Context HttpHeaders headers,
            ResourceCollection resourceCollection) {

        List accepts = headers.getAcceptableMediaTypes();
        if(accepts.isEmpty()) {
            accepts = RestHelper.addDefaultAcceptedTypes();
        }

        return insertCollection(resourceCollection, "", accepts);
    }

    private Response insertResource(Resource resource,
            String path,
            List <MediaType> accepteds) {

        try {
            if (!resource.checkName()) {
                return RestHelper.sendError("Field name do not comply the pattern.", Status.BAD_REQUEST, accepteds);
            }

            if(mongoResourceDAO.isResourceByContentUrl(resource.getContentUrl())) {
                return RestHelper.sendError("Resource with field contentUrl '"+resource.getContentUrl()+"' already exists.", Status.CONFLICT, accepteds);
            }

            //Clean the resource, some metadata can not be given by the user
            resource.setId(path+"/"+resource.getName());
            resource.setContentMimeType("");
            resource.setCreationDate(new Date());

            if (path == null || path.equalsIgnoreCase("")) {
                return RestHelper.sendError("Resource must be created in a collection.", Status.BAD_REQUEST, accepteds);
            }
            if ((!checkPath(path) && !path.equalsIgnoreCase("")) ||
                    mongoCollectionDAO.getCollection(resource.getId()) != null ||
                    mongoResourceDAO.getResource(resource.getId()) != null) {
                return RestHelper.sendError("Path is a resource.", Status.CONFLICT, accepteds);
            }

            mongoResourceDAO.insertResource(resource);

            return Response.status(Status.CREATED).type(MediaType.APPLICATION_JSON).contentLocation(new URI("collec/" + resource.getId())).build();

        } catch (DatasourceException | URISyntaxException ex) {
            return RestHelper.sendError(ex.getMessage(), Status.INTERNAL_SERVER_ERROR, accepteds);
        } catch (SameIdException ex) {
            return RestHelper.sendError("Resource already exists.", Status.CONFLICT, accepteds);
        }
    }

    private Response insertCollection(ResourceCollection resourceCollection,
            String path,
            List <MediaType> accepteds) {

        try {
            if(!resourceCollection.checkName()) {
                return RestHelper.sendError("Field name do not comply the pattern.", Status.BAD_REQUEST, accepteds);
            }

            if (!path.equalsIgnoreCase("")) {
                resourceCollection.setId(path+"/"+resourceCollection.getName());
            } else {
                resourceCollection.setId(resourceCollection.getName());
            }

            if (!checkPath(path) ||
                    mongoCollectionDAO.getCollection(resourceCollection.getId()) != null ||
                    mongoResourceDAO.getResource(resourceCollection.getId()) != null) {
                return RestHelper.sendError("Path is a resource.", Status.CONFLICT, accepteds);
            }

            mongoCollectionDAO.insertCollection(resourceCollection);
            return Response.status(Status.CREATED).contentLocation(new URI("collec/" + resourceCollection.getId())).build();
        } catch (DatasourceException | URISyntaxException ex) {
            return RestHelper.sendError(ex.getMessage(), Status.INTERNAL_SERVER_ERROR, accepteds);
        } catch (SameIdException ex) {
            return RestHelper.sendError("Collection already exists.", Status.CONFLICT, accepteds);
        }
    }

    private boolean checkPath(String path) throws DatasourceException {
        if (!mongoResourceDAO.isResource(path)) {
            if (path.lastIndexOf("/") == -1) {
                return true;
            } else {
                if (!ResourceCollection.checkName(path.substring(path.lastIndexOf("/")+1, path.length()))) {
                    return false;
                } else {
                    return checkPath(path.substring(0, path.lastIndexOf("/")));
                }
            }
        } else {
            return false;
        }
    }

    @PUT
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")
    public Response putResourceContent(@Context HttpHeaders headers,
            @PathParam("path") String path,
            @HeaderParam("Content-Type") String contentType,
            String content) {

        List accepts = headers.getAcceptableMediaTypes();
        if(accepts.isEmpty()) {
            accepts = RestHelper.addDefaultAcceptedTypes();
        }

        if (contentType == null || contentType.equalsIgnoreCase("")) {
            return RestHelper.sendError("Especify the content type.", Status.UNSUPPORTED_MEDIA_TYPE, accepts);
        } else {
            return updateResourceContent(path, content, contentType, accepts);
        }
    }

    @PUT
    @Consumes({"application/xml", "application/json"})
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}.meta")
    public Response putResource(@Context HttpHeaders headers,
            @PathParam("path") String path,
            Resource resource) {

        List accepts = headers.getAcceptableMediaTypes();
        if(accepts.isEmpty()) {
            accepts = RestHelper.addDefaultAcceptedTypes();
        }

        return updateResourceMeta(path, (Resource) resource, accepts);
    }

    private Response updateResourceContent(String path,
            String content,
            String contentType,
            List <MediaType> accepteds) {

        Resource resource;
        try {
            resource = mongoResourceDAO.getResourceContent(path);
            if (resource == null)
            {
                return RestHelper.sendError("Resource not found.", Status.NOT_FOUND, accepteds);
            }
            resource.setContent(content.getBytes());
            resource.setContentMimeType(contentType);
            if(RestHelper.isRDF(contentType)) {
                virtuosoResourceDAO.updateResource(resource.getContentUrl(), content,
                        RestHelper.typeMap.get(resource.getContentMimeType()));
            }
            else {
                virtuosoResourceDAO.deleteResource(resource.getContentUrl());
            }
            mongoResourceDAO.updateResourceContent(resource);

        } catch (DatasourceException ex) {
            return RestHelper.sendError(ex.getMessage(), Status.INTERNAL_SERVER_ERROR, accepteds);
        }
        return Response.status(Status.OK).build();
    }

    private Response updateResourceMeta(String path, Resource resource, List <MediaType> accepteds) {
        //Update a resource metadata.
        if(!resource.checkName()) {
            return RestHelper.sendError("Field name do not comply the pattern.", Status.BAD_REQUEST, accepteds);
        }
        try {
            Resource aux = mongoResourceDAO.getResource(path);
            if (aux == null)
            {
                resource.setId(path);
                resource.setName(path.substring(path.lastIndexOf("/")+1, path.length()));
                return insertResource(resource ,path.substring(0, path.lastIndexOf("/")), accepteds);
            }

            resource.setId(path.substring(0, path.lastIndexOf("/"))+"/"+resource.getName());
            resource.setCreationDate(aux.getCreationDate());

            if (!resource.getName().equalsIgnoreCase(aux.getName()) &&
                    (mongoResourceDAO.isResource(resource.getId()) || (mongoCollectionDAO.getCollection(resource.getId()) != null))) {
                return RestHelper.sendError("Resource or collection already exist.", Status.CONFLICT, accepteds);
            }
            if (!resource.getContentMimeType().equalsIgnoreCase("") && !resource.getContentMimeType().equals(aux.getContentMimeType())) {
                return RestHelper.sendError("Is forbidden change the Content Mime Type.", Status.FORBIDDEN, accepteds);
            }
            if (!resource.getContentUrl().equalsIgnoreCase("") && !resource.getContentUrl().equals(aux.getContentUrl())) {
                return RestHelper.sendError("Is forbidden change the Content Url.", Status.FORBIDDEN, accepteds);
            }

            resource.setContentUrl(aux.getContentUrl());
            resource.setContentMimeType(aux.getContentMimeType());

            mongoResourceDAO.updateResource(path, resource);
        } catch (DatasourceException ex) {
            return RestHelper.sendError(ex.getMessage(), Status.INTERNAL_SERVER_ERROR, accepteds);
        }
        return Response.status(Status.OK).build();
    }

    @DELETE
    @Path("/{path:[a-zA-Z0-9_\\.\\-\\+\\/]*}")
    public Response delete(@Context HttpHeaders headers,
            @PathParam("path") String path) {

        List accepts = headers.getAcceptableMediaTypes();
        if(accepts.isEmpty()) {
            accepts = RestHelper.addDefaultAcceptedTypes();
        }

        return deleteResource(path, accepts);
    }

    private Response deleteResource(String path, List <MediaType> accepts) {

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
                    return RestHelper.sendError("Resource or Collection not found.", Status.NOT_FOUND, accepts);
                }
            }
        } catch (DatasourceException ex) {
            return RestHelper.sendError(ex.getMessage(), Status.INTERNAL_SERVER_ERROR, accepts);
        }
        return Response.noContent().build();
    }
}
