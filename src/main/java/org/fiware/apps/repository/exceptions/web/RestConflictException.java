package org.fiware.apps.repository.exceptions.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.fiware.apps.repository.model.RepositoryException;

public class RestConflictException extends WebApplicationException {
	public RestConflictException(String message) {	
		super(Response.status(Response.Status.CONFLICT).type("application/xml").entity(new RepositoryException(Response.Status.CONFLICT,message)).build());
	}
}
