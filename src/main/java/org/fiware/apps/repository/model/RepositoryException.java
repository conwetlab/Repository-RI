package org.fiware.apps.repository.model;

import javax.ws.rs.core.Response.Status;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "exception")
public class RepositoryException {
	private int errorCode;
	private String description;
	private String reasonPhrase;

	public RepositoryException() {

	}

	public RepositoryException(Status status, String message) {
		this.description = message;
		this.errorCode = status.getStatusCode();
		this.reasonPhrase =	status.getReasonPhrase();

	}
	@XmlElement
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public String getReasonPhrase() {
		return reasonPhrase;
	}
	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}

}
