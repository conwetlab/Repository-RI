package org.fiware.apps.repository.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSeeAlso;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;

@XmlSeeAlso({Resource.class, ResourceCollection.class})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ResourceCollection.class, name = "collection"),
    @JsonSubTypes.Type(value = Resource.class, name = "resource"),
})
public abstract class AbstractResource {

    protected String id;
    protected String name="";
    protected Date creationDate;
    protected Date modificationDate;
    protected String creator="";

    @XmlID
    @XmlAttribute
    public abstract String getId();
    public abstract void setId(String id);

    @XmlElement
    public abstract String getName();
    public abstract void setName(String name);

    @XmlElement
    public abstract String getCreator();
    public abstract void setCreator(String creator);

    @XmlElement
    public abstract Date getCreationDate();
    public abstract void setCreationDate(Date creationDate);

    @XmlElement
    public abstract Date getModificationDate();
    public abstract void setModificationDate(Date modificationDate);

    public abstract boolean checkName();
    public abstract boolean checkName(String name);

}
