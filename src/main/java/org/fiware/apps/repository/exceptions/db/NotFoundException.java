package org.fiware.apps.repository.exceptions.db;


public class NotFoundException extends Exception {

	private static final long serialVersionUID = -3526457037580321983L;

	public NotFoundException(String id, Class entity){
		super("No Object with id "+id+" for "+entity.toString()+" found");
	}
}
