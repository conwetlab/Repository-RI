package org.fiware.apps.repository.model;

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.resteasy.links.ParentResource;
import org.jboss.resteasy.links.RESTServiceDiscovery;

@XmlRootElement(name = "resource")
public class Resource extends AbstractResource{

    private final String namePatern = "[a-zA-Z0-9._-]+";
    private final String noMetaPatern = ".*.meta$";
    private String contentUrl = "";
    private String contentMimeType = "";
    private String contentFileName = "";
    byte[] content;

    @XmlElementRef
    private RESTServiceDiscovery rest;

    @XmlElement
    public String getContentUrl() {
        return contentUrl;
    }
    public void setContentUrl(String content) {
        this.contentUrl = content;
    }

    @XmlElement
    public String getContentMimeType() {
        return contentMimeType;
    }
    public void setContentMimeType(String contentMimeType) {
        this.contentMimeType = contentMimeType;
    }

    @XmlElement
    public String getContentFileName() {
        return contentFileName;
    }
    public void setContentFileName(String contentFileName) {
        this.contentFileName = contentFileName;
    }

    public byte[] getContent() {
        return content;
    }
    public void setContent(byte[] bs) {
        this.content = bs;
    }

    @Override
    public boolean checkName() {
        if (this.getName().matches(this.namePatern) && !this.getName().matches(this.noMetaPatern)) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean checkName(String name) {
        if (this.namePatern.matches(name) && !this.noMetaPatern.matches(name)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getCreator() {
        return this.creator;
    }

    @Override
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public Date getCreationDate() {
        return this.creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public Date getModificationDate() {
        return this.modificationDate;
    }

    @Override
    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }
}