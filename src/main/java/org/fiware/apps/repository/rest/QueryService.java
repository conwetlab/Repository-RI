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

import com.hp.hpl.jena.query.QueryParseException;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.vocabulary.DCTerms;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.*;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import org.fiware.apps.repository.dao.VirtuosoDAOFactory;

import org.fiware.apps.repository.dao.impl.VirtuosoResourceDAO;
import org.fiware.apps.repository.exceptions.db.DatasourceException;
import org.fiware.apps.repository.model.RepositoryException;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.settings.RepositorySettings;

@Path("/services/"+RepositorySettings.QUERY_SERVICE_NAME)
public class QueryService {
    private VirtuosoResourceDAO virtuosoResourceDAO = VirtuosoDAOFactory.getVirtuosoResourceDAO();

    @Context
            UriInfo uriInfo;

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
            accepts = new LinkedList();
            accepts.add(MediaType.valueOf("application/rdf+xml"));
        }


        try {
            Resource resource;
            for (MediaType type : accepts) {
                String typeString = ("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) ? "application/rdf+xml" : type.getType()+"/"+type.getSubtype();

                if(RestHelper.isRDF(typeString)) {
                    resource = virtuosoResourceDAO.getResource(path, RestHelper.typeMap.get(typeString));
                    if (resource != null) {
                        return Response.status(Response.Status.OK).header("content-length", resource.getContent().length).entity(resource.getContent()).type(MediaType.valueOf(typeString)).build();
                    } else {
                        return Response.status(Response.Status.NOT_FOUND).type("application/xml").entity(new RepositoryException(Response.Status.NOT_FOUND,"MENSAJE")).build();
                    }
                }
            }
        } catch (DatasourceException ex) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).type("application/xml").entity(new RepositoryException(Status.INTERNAL_SERVER_ERROR, ex.getMessage())).build();
        }
        return Response.status(Status.NOT_ACCEPTABLE).build();
    }

    private Response executeAnyQuery(String query, List <MediaType> types) {
        if (query == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        // Check type sparql query and execute the method.
        if (query.toLowerCase().contains("select")) {
            for (MediaType type : types) {
                String typeString = ("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) ? "application/xml" : type.getType()+"/"+type.getSubtype();
                if (typeString.equalsIgnoreCase("application/xml") || typeString.equalsIgnoreCase("application/json")) {
                    try {
                        return Response.status(Status.OK).type(MediaType.valueOf(typeString))
                                .entity(virtuosoResourceDAO.executeQuerySelect(query)).build();
                    } catch (JenaException ex) {
                        return Response.status(Status.BAD_REQUEST).type(MediaType.valueOf(typeString)).entity(new RepositoryException(Status.BAD_REQUEST, ex.getMessage())).build();
                    }
                }
            }
            return Response.status(Status.NOT_ACCEPTABLE).build();
        }
        if (query.toLowerCase().contains("construct")) {
            for (MediaType type : types) {
                String typeString = ("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) ? "application/rdf+xml" : type.getType()+"/"+type.getSubtype();
                if (RestHelper.isRDF(typeString)) {
                    try {
                        return Response.status(Status.OK).type(MediaType.valueOf(typeString))
                                .entity(virtuosoResourceDAO.executeQueryConstruct(query, RestHelper.typeMap.get(typeString))).build();
                    } catch (JenaException ex) {
                        return Response.status(Status.BAD_REQUEST).type(MediaType.valueOf("application/json")).entity(new RepositoryException(Status.BAD_REQUEST, ex.getMessage())).build();
                    }
                }
            }
            return Response.status(Status.NOT_ACCEPTABLE).build();
        }
        if (query.toLowerCase().contains("describe")) {
            for (MediaType type : types) {
                String typeString = ("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) ? "application/rdf+xml" : type.getType()+"/"+type.getSubtype();
                if (RestHelper.isRDF(typeString)) {
                    try {
                        return Response.status(Status.OK).type(MediaType.valueOf(typeString))
                                .entity(virtuosoResourceDAO.executeQueryDescribe(query, RestHelper.typeMap.get(typeString))).build();
                    } catch (JenaException ex) {
                        return Response.status(Status.BAD_REQUEST).type(MediaType.valueOf("application/json")).entity(new RepositoryException(Status.BAD_REQUEST, ex.getMessage())).build();
                    }
                }
            }
            return Response.status(Status.NOT_ACCEPTABLE).build();
        }
        if (query.toLowerCase().contains("ask")) {
            for (MediaType type : types) {
                String typeString = ("*/*".equalsIgnoreCase(type.getType()+"/"+type.getSubtype())) ? "application/xml" : type.getType()+"/"+type.getSubtype();
                try {
                    if (typeString.equalsIgnoreCase("application/json")) {
                        return Response.status(Status.OK).type(MediaType.valueOf(typeString))
                                .entity(virtuosoResourceDAO.executeQueryAsk(query)).build();
                    } else if (typeString.equalsIgnoreCase("application/xml")) {
                        return Response.status(Status.OK).type(MediaType.valueOf(typeString))
                                .entity(Boolean.toString(virtuosoResourceDAO.executeQueryAsk(query))).build();
                    }
                } catch (JenaException ex) {
                    return Response.status(Status.BAD_REQUEST).type(MediaType.valueOf(typeString)).entity(new RepositoryException(Status.BAD_REQUEST, ex.getMessage())).build();
                }
            }
            return Response.status(Status.NOT_ACCEPTABLE).build();
        }
        return Response.status(Status.BAD_REQUEST).build();
    }


}
