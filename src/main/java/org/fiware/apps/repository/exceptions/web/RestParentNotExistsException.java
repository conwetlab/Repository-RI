package org.fiware.apps.repository.exceptions.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.fiware.apps.repository.model.RepositoryException;

public class RestParentNotExistsException extends WebApplicationException {
	public RestParentNotExistsException(String message) {	
		super(Response.status(Response.Status.BAD_REQUEST).type("application/xml").entity(new RepositoryException(Response.Status.BAD_REQUEST,message)).build());
	}


}
