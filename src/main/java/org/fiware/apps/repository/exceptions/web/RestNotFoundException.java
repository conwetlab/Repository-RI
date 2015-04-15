package org.fiware.apps.repository.exceptions.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.fiware.apps.repository.model.RepositoryException;

public class RestNotFoundException extends WebApplicationException {
	public RestNotFoundException(String message, Exception exception) {
                super(exception, Response.status(Response.Status.NOT_FOUND).type("application/xml").entity(new RepositoryException(Response.Status.NOT_FOUND,message)).build());
	}


}
