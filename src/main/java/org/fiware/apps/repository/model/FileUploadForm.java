package org.fiware.apps.repository.model;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import javax.ws.rs.FormParam;

//import org.jboss.resteasy.annotations.providers.multipart.PartType;

public class FileUploadForm {
	private byte[] filedata;
	private String mimeType;
	private String filename;


	public FileUploadForm() {}

	public byte[] getFileData() {
		return filedata;
	}

	@FormParam("filedata")
	public void setFileData(final byte[] filedata) {

		this.filedata = filedata;
	}

	@FormParam("mimeType")
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getMimeType() {
		return mimeType;
	}

	@FormParam("filename")
	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
	}


}