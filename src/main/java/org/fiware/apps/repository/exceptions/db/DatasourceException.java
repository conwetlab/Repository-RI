package org.fiware.apps.repository.exceptions.db;


public class DatasourceException extends Exception {

	private static final long serialVersionUID = -3526457037580321983L;

	public DatasourceException(String message, Class entity){
		super("Parser Exception for "+entity.toString()+" "+message);
	}
}
