/*
Modified BSD License
====================

Copyright (c) 2015, CoNWeTLab, Universidad Politecnica Madrid
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:
* Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
* Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
* Neither the name of UPM nor the
names of its contributors may be used to endorse or promote products
derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL UPM BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.fiware.apps.repository.it;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.fiware.apps.repository.model.Resource;
import org.fiware.apps.repository.model.ResourceCollection;
import org.fiware.apps.repository.settings.RepositorySettings;

public class IntegrationTestHelper {

    private HttpClient client;
    private final String collectionServiceUrl = RepositorySettings.REPOSITORY_BASE_URL + "v2/" + RepositorySettings.COLLECTION_SERVICE_NAME + "/";
    private final String queryServiceUrl = RepositorySettings.REPOSITORY_BASE_URL + "v2/" + "/services/"+RepositorySettings.QUERY_SERVICE_NAME;


    public IntegrationTestHelper() {
        this.client = new DefaultHttpClient();
    }

    public HttpResponse getResourceMeta(String resourceId, List <Header> headers) throws IOException {
        String finalURL = collectionServiceUrl + resourceId + ".meta";
        HttpGet request = new HttpGet(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse getResourceContent(String resourceId, List <Header> headers) throws IOException {
        String finalURL = collectionServiceUrl + resourceId;
        HttpGet request = new HttpGet(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse postResourceMeta(String collectionsId, String resource, List <Header> headers) throws IOException {
        String finalURL = collectionServiceUrl + collectionsId;
        HttpPost request = new HttpPost(finalURL);
        System.err.println("finalUrl: "+finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        //Add Entity
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream stream = new ByteArrayInputStream(resource.getBytes());

        entity.setContent(stream);
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse putResourceMeta(String resourceId, String resource, List <Header> headers) throws IOException {
        String finalURL = collectionServiceUrl + resourceId + ".meta";
        HttpPut request = new HttpPut(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        //Add Entity
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream stream = new ByteArrayInputStream(resource.getBytes());

        entity.setContent(stream);
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse putResourceContent(String resourceId, String resourceContent, List <Header> headers) throws IOException {
        String finalURL = collectionServiceUrl + resourceId;
        HttpPut request = new HttpPut(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        //Add Entity
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream stream = new ByteArrayInputStream(resourceContent.getBytes());

        entity.setContent(stream);
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse deleteResource(String resourceId, List <Header> headers) throws IOException {
        String finalURL = collectionServiceUrl + resourceId;
        HttpDelete request = new HttpDelete(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse getCollection(String collectionId, List <Header> headers) throws IOException {
        String finalURL = collectionServiceUrl + collectionId;
        HttpGet request = new HttpGet(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse postCollection(String collectionId, String collection, List <Header> headers) throws IOException {
        String finalURL = collectionServiceUrl + collectionId;
        HttpPost request = new HttpPost(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        //Add Entity
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream stream = new ByteArrayInputStream(collection.getBytes());

        entity.setContent(stream);
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse deleteCollection(String collectionId, List <Header> headers) throws IOException {
        String finalURL = collectionServiceUrl + collectionId;
        HttpDelete request = new HttpDelete(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse getQuery(String query, List <Header> headers) throws IOException {
        String finalURL = collectionServiceUrl + "?query="+query;
        HttpGet request = new HttpGet(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse postQuery(String query, List <Header> headers) throws IOException {
        String finalURL = collectionServiceUrl;
        HttpPost request = new HttpPost(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream stream = new ByteArrayInputStream(query.getBytes());

        entity.setContent(stream);
        request.setEntity(entity);

        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    public HttpResponse getResourceByUrlContent(String contentUrl, List <Header> headers) throws IOException {
        String finalURL = collectionServiceUrl + "/" + contentUrl;
        HttpGet request = new HttpGet(finalURL);

        //Add Headers
        for (Header header : headers) {
            request.setHeader(header);
        }

        HttpResponse response = client.execute(request);
        request.releaseConnection();
        return response;
    }

    public static String resourceToJson(Resource resource) {
        String resourceString = "{\n";

        if (resource.getContentFileName() != null) {
            resourceString = resourceString.concat("\"contentFileName\" : \""+resource.getContentFileName()+"\",\n");
        }
        if (resource.getContentMimeType() != null) {
            resourceString = resourceString.concat("\"contentMimeType\" : \""+resource.getContentMimeType()+"\",\n");
        }
        if (resource.getContentUrl() != null) {
            resourceString = resourceString.concat("\"contentUrl\" : \""+resource.getContentUrl()+"\",\n");
        }
        if (resource.getCreationDate() != null) {
            resourceString = resourceString.concat("\"creationDate\" : \""+resource.getCreationDate().toString()+"\",\n");
        }
        if (resource.getCreator() != null) {
            resourceString = resourceString.concat("\"creator\" : \""+resource.getCreator()+"\",\n");
        }
        if (resource.getId() != null) {
            resourceString = resourceString.concat("\"id\" : \""+resource.getId()+"\",\n");
        }
        if (resource.getModificationDate() != null) {
            resourceString = resourceString.concat("\"modificationDate\" : \""+resource.getModificationDate().toString()+"\",\n");
        }
        if (resource.getName() != null) {
            resourceString = resourceString.concat("\"name\" : \""+resource.getName()+"\",\n");
        }
        resourceString = resourceString.concat("\"type\" : \"resource\"\n");

        return resourceString.concat("\n}");
    }

    public static String resourceToXML(Resource resource) {
        String resourceString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n";

        if (resource.getId() != null) {
            resourceString = resourceString.concat("<resource id=\""+resource.getId()+"\">\n");
        } else {
            resourceString =resourceString.concat("<resource>\n");
        }
        if (resource.getContentFileName() != null) {
            resourceString = resourceString.concat("<contentFileName>"+resource.getContentFileName()+"</contentFileName>\n");
        }
        if (resource.getContentMimeType() != null) {
            resourceString = resourceString.concat("<contentMimeType>"+resource.getContentMimeType()+"</contentMimeType>\n");
        }
        if (resource.getContentUrl() != null) {
            resourceString = resourceString.concat("<contentUrl>"+resource.getContentUrl()+"</contentUrl>\n");
        }
        if (resource.getCreationDate() != null) {
            resourceString = resourceString.concat("<creationDate>"+resource.getCreationDate().toString()+"</creationDate>\n");
        }
        if (resource.getCreator() != null) {
            resourceString = resourceString.concat("<creator>"+resource.getCreator()+"</creator>\n");
        }
        if (resource.getModificationDate() != null) {
            resourceString = resourceString.concat("<modificationDate>"+resource.getModificationDate().toString()+"</modificationDate>\n");
        }
        if (resource.getName() != null) {
            resourceString = resourceString.concat("<name>"+resource.getName()+"</name>\n");
        }

        return resourceString.concat("</resource>");
    }

    public static String collectionToJson(ResourceCollection collection) {
        String collectionString = "{\n";
        if (collection.getCreationDate() != null) {
            collectionString = collectionString.concat("\"creationDate\" : \""+collection.getCreationDate().toString()+"\",\n");
        }
        if (collection.getCreator() != null) {
            collectionString = collectionString.concat("\"creator\" : \""+collection.getCreator()+"\",\n");
        }
        if (collection.getId() != null) {
            collectionString = collectionString.concat("\"id\" : \""+collection.getId()+"\",\n");
        }
        if (collection.getModificationDate() != null) {
            collectionString = collectionString.concat("\"modificationDate\" : \""+collection.getModificationDate().toString()+"\",\n");
        }
        if (collection.getName() != null) {
            collectionString = collectionString.concat("\"name\" : \""+collection.getName()+"\",\n");
        }
        collectionString = collectionString.concat("\"type\" : \"collection\"\n");
        return collectionString.concat("\n}");
    }

    public static String collectionToXml(ResourceCollection collection) {
        String collectionString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n";
        if (collection.getId() != null) {
            collectionString = collectionString.concat("<collection id=\""+collection.getId()+"\">");
        } else {
            collectionString = collectionString.concat("<collection>");
        }
        if (collection.getCreationDate() != null) {
            collectionString = collectionString.concat("<creationDate>"+collection.getCreationDate().toString()+"</creationDate>\n");
        }
        if (collection.getCreator() != null) {
            collectionString = collectionString.concat("<creator>"+collection.getCreator()+"</creator>\n");
        }
        if (collection.getModificationDate() != null) {
            collectionString = collectionString.concat("<modificationDate>"+collection.getModificationDate().toString()+"</modificationDate>\n");
        }
        if (collection.getName() != null) {
            collectionString = collectionString.concat("<name>"+collection.getName()+"</name>\n");
        }
        return collectionString.concat("</collection>");
    }

    public static Resource generateResource(String content, String fileName, String mimeType, String contentUrl,
            Date creationDate, String creator, String id, Date modificationDate, String name) {
        Resource resource = new Resource();
        if (content == null) {
            resource.setContent(null);
        } else {
            resource.setContent(content.getBytes());
        }
        resource.setContentFileName(fileName);
        resource.setContentMimeType(mimeType);
        resource.setContentUrl(contentUrl);
        resource.setCreationDate(creationDate);
        resource.setCreator(creator);
        resource.setId(id);
        resource.setModificationDate(modificationDate);
        resource.setName(name);
        return resource;
    }

    public static ResourceCollection generateResourceCollection(String id, String name, String creator, Date creationDate, Date modificationDate) {
        ResourceCollection resourceCollection = new ResourceCollection();
        resourceCollection.setCreationDate(creationDate);
        resourceCollection.setCreator(creator);
        resourceCollection.setId(id);
        resourceCollection.setModificationDate(modificationDate);
        resourceCollection.setName(name);

        return resourceCollection;
    }
}
