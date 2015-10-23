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

package org.fiware.apps.repository.rest;

import com.hp.hpl.jena.shared.JenaException;
import java.util.List;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.ws.rs.*;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.fiware.apps.repository.dao.VirtuosoDAOFactory;

import org.fiware.apps.repository.dao.impl.VirtuosoResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.settings.RepositorySettings;

@Path("/services/"+RepositorySettings.QUERY_SERVICE_NAME)
public class QueryService {
    private VirtuosoResourceDAO virtuosoResourceDAO;

    public QueryService(@Context ServletContext servletContext) {
        RepositorySettings repositorySettings = new RepositorySettings(servletContext.getInitParameter("propertiesFile"));
        Properties repositoryProperties = repositorySettings.getProperties();

        this.virtuosoResourceDAO = new VirtuosoDAOFactory().getVirtuosoResourceDAO(repositoryProperties);
    }

    @GET
    public Response executeQuery(@Context HttpHeaders headers, @QueryParam("query") String query) {
        return executeAnyQuery(query, headers.getAcceptableMediaTypes());
    }

    @POST
    @Consumes("text/plain")
    public Response executeLongQuery(@Context HttpHeaders headers, String content) {
        return executeAnyQuery(content, headers.getAcceptableMediaTypes());
    }

    @GET
    @Path("/{path:[a-zA-Z0-9_\\:\\.\\-\\+\\/]*}")
    public Response obtainResource(@Context HttpHeaders headers, @PathParam("path") String path) {
        List <MediaType> accepts = headers.getAcceptableMediaTypes();
        if(accepts.isEmpty()) {
            accepts = RestHelper.addDefaultAcceptedTypes();
        }

        try {
            Resource resource;
            for (MediaType type : accepts) {
                String typeString = ("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) ? RestHelper.RdfDefaultType : type.getType()+"/"+type.getSubtype();

                if(RestHelper.isRDF(typeString)) {
                    resource = virtuosoResourceDAO.getResource(path, RestHelper.typeMap.get(typeString));
                    if (resource != null) {
                        if (resource.getContent() == null || resource.getContent().equals("".getBytes())) {
                            return Response.status(Status.NO_CONTENT).build();
                        } else {
                            return Response.status(Response.Status.OK).header("content-length", resource.getContent().length).entity(resource.getContent()).type(MediaType.valueOf(typeString)).build();
                        }
                    } else {
                        return RestHelper.sendError("Content Url not found.", Status.NOT_FOUND, accepts);
                    }
                }
            }
        } catch (DatasourceException ex) {
            return RestHelper.sendError(ex.getMessage(), Status.INTERNAL_SERVER_ERROR, accepts);
        }
        return RestHelper.sendError("Not acceptable.", Status.NOT_ACCEPTABLE, accepts);
    }

    private Response executeAnyQuery(String query, List <MediaType> accepts) {
        if (query == null) {
            return RestHelper.sendError("Query is empty.", Status.BAD_REQUEST, accepts);
        }

        // Check type sparql query and execute the method.
        if (query.toLowerCase().contains("select")) {
            for (MediaType type : accepts) {
                String typeString = ("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) ? RestHelper.ResourcesDefaultType : type.getType()+"/"+type.getSubtype();
                if (typeString.equalsIgnoreCase("application/xml") || typeString.equalsIgnoreCase("application/json")) {
                    try {
                        return Response.status(Status.OK).type(MediaType.valueOf(typeString))
                                .entity(virtuosoResourceDAO.executeQuerySelect(query)).build();
                    } catch (JenaException ex) {
                        return RestHelper.sendError(ex.getMessage(), Status.BAD_REQUEST, accepts);
                    }
                }
            }
            return RestHelper.sendError("Not acceptable.", Status.NOT_ACCEPTABLE, accepts);
        }
        if (query.toLowerCase().contains("construct")) {
            for (MediaType type : accepts) {
                String typeString = ("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) ? RestHelper.RdfDefaultType : type.getType()+"/"+type.getSubtype();
                if (RestHelper.isRDF(typeString)) {
                    try {
                        return Response.status(Status.OK).type(MediaType.valueOf(typeString))
                                .entity(virtuosoResourceDAO.executeQueryConstruct(query, RestHelper.typeMap.get(typeString))).build();
                    } catch (JenaException ex) {
                        return RestHelper.sendError(ex.getMessage(), Status.BAD_REQUEST, accepts);
                    }
                }
            }
            return RestHelper.sendError("Not acceptable.", Status.NOT_ACCEPTABLE, accepts);
        }
        if (query.toLowerCase().contains("describe")) {
            for (MediaType type : accepts) {
                String typeString = ("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) ? RestHelper.RdfDefaultType : type.getType()+"/"+type.getSubtype();
                if (RestHelper.isRDF(typeString)) {
                    try {
                        return Response.status(Status.OK).type(MediaType.valueOf(typeString))
                                .entity(virtuosoResourceDAO.executeQueryDescribe(query, RestHelper.typeMap.get(typeString))).build();
                    } catch (JenaException ex) {
                        return RestHelper.sendError(ex.getMessage(), Status.BAD_REQUEST, accepts);
                    }
                }
            }
            return RestHelper.sendError("Not acceptable.", Status.NOT_ACCEPTABLE, accepts);
        }
        if (query.toLowerCase().contains("ask")) {
            for (MediaType type : accepts) {
                String typeString = ("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) ? RestHelper.ResourcesDefaultType : type.getType()+"/"+type.getSubtype();
                try {
                    if (typeString.equalsIgnoreCase("application/json")) {
                        return Response.status(Status.OK).type(MediaType.valueOf(typeString))
                                .entity(virtuosoResourceDAO.executeQueryAsk(query)).build();
                    } else if (typeString.equalsIgnoreCase("application/xml")) {
                        return Response.status(Status.OK).type(MediaType.valueOf(typeString))
                                .entity(Boolean.toString(virtuosoResourceDAO.executeQueryAsk(query))).build();
                    }
                } catch (JenaException ex) {
                    return RestHelper.sendError(ex.getMessage(), Status.BAD_REQUEST, accepts);
                }
            }
            return RestHelper.sendError("Not acceptable.", Status.NOT_ACCEPTABLE, accepts);
        }
        return RestHelper.sendError("Query do not match any query form (SELECT, CONSTRUCT, DESCRIBE, ASK).", Status.BAD_REQUEST, accepts);
    }

}
