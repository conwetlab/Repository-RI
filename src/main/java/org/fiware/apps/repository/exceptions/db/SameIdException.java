package org.fiware.apps.repository.exceptions.db;


public class SameIdException extends Exception {

	private static final long serialVersionUID = -3526457037580321983L;

	public SameIdException(String id, Class entity){
		super("There is already a Component with the same ID "+ id +" . Entity "+entity.toString());	
	}
}
