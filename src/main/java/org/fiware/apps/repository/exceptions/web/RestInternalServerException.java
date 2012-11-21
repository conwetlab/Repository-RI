package org.fiware.apps.repository.exceptions.web;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.fiware.apps.repository.model.RepositoryException;

public class RestInternalServerException extends WebApplicationException {
	public RestInternalServerException(String message) {	
		super(Response.status(Response.Status.INTERNAL_SERVER_ERROR).type("application/xml").entity(new RepositoryException(Response.Status.INTERNAL_SERVER_ERROR,message)).build());
	}


}
