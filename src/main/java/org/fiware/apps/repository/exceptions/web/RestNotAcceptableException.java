package org.fiware.apps.repository.exceptions.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.fiware.apps.repository.model.RepositoryException;

public class RestNotAcceptableException extends WebApplicationException {
	public RestNotAcceptableException(String message) {	
		super(Response.status(Response.Status.NOT_ACCEPTABLE).entity(new RepositoryException(Response.Status.NOT_ACCEPTABLE,message)).build());
	}
}
