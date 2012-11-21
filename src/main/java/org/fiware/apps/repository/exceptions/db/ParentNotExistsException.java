package org.fiware.apps.repository.exceptions.db;


public class ParentNotExistsException extends Exception {

	private static final long serialVersionUID = -3526457037580321983L;

	public ParentNotExistsException(String id){
		super("no Parent for id "+id);	
	}
}
