package org.fiware.apps.repository.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.jboss.resteasy.links.RESTServiceDiscovery;

@XmlRootElement(name = "collection")
public class ResourceCollection extends AbstractResource{

    private final static String namePatern = "[a-zA-Z0-9_-]+";
    private List<ResourceCollection> collections= new ArrayList <ResourceCollection>();
    private List<Resource> resources=new ArrayList <Resource>();


    @XmlElementRef
    private RESTServiceDiscovery rest;


    @XmlElementWrapper
    public List<ResourceCollection> getCollections() {
        return collections;
    }
    public void setCollections(List<ResourceCollection> collections) {
        this.collections = collections;
    }

    @XmlElementWrapper
    public List<Resource> getResources() {
        return resources;
    }
    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    @Override
    public boolean checkName() {
        return this.getName().matches(this.namePatern);
    }
    public static boolean checkName(String name) {
        return name.matches(namePatern);
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
