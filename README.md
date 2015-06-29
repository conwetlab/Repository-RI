#Repository-RI

The Repository GE  is a core enabler of the FIWARE Business Framework (together with the Store GE and the Marketplace GE). The Repository GE and it reference implementation provide a consistent uniform API to access USDL service descriptions and associated media files for applications of the business framework. A service provider can use the Repository GE to publish the description of various aspects of the service according to a unified description language.

Moreover, the Repository provides a uniform API to perform smart searches over the published descriptions. This API is oriented to service descriptions and media files serialized in RDF formats (e.g a Linked-USDL document) and allows the usage of SPARQL as query language.

## New features V4.3.3:

- Content Header Negotiation Supported Types for reading Resource Meta Information or Collection Information:
	- application/json
	- application/rdf+xml
	- text/turtle
	- text/n3
	- text/html
	- application/x-ms-application"
	- text/plain
	- application/xml
	
- Virtuoso as "triple store" to store RDF data.
 	
- Endpoint for executing SPARQL queries on resources content.

- Endpoint for retrieving resources by its content-url.

- Support for IdM OAtuh2 authentication.
	
## Prerequisites

- Tomcat 8
- MongoDB
- Virtuoso 7
- Java 8
- Maven

## Installation

This section give a brief explanation on how to install the Repository. If you need more accurate instructions, you can find them in the installation guide in the file <code>Repository-RI/docs/installation-guide.md</code>.

You can install Repository-RI using the installation script <code>install.sh</code> or by following these steps:

1. Install the prerequisites.

2. Choose a database for the Repository-RI in MongoDB. By default, Repository-RI uses the database <code>test</code>.

3. Create a user for the Repository-RI in Virtuoso. By default, Repository-RI uses user <code>dba</code> and password <code>dba</code>.

4. Update <code>src/main/resources/properties/repository.properties</code> to set the preferences of your databases (MongoDB and Virtuoso).

5. Configure the security:

    a. If you do not want to use FIWARE IdM OAtuh2 authentication to manage users, ensure that the file <code>noSecurity.xml</code> is imported in the web.xml file.
    
    b. If you want to use FIWARE IdM OAtuh2 authentication to manage users, ensure that the file <code>securityOAuth2.xml</code> is imported in the web.xml file and modify <code>src/main/resources/properties/repository.properties</code> to set your OAuth2 configuration.
    
6. Generate the WAR file for the source code, run <code>mvn install</code>.

7. Copy the generated WAR file into the webapps folder of your Tomcat instance.

## API Reference
Here you have a basic reference of all the status codes that you can get when you are dealing with Repository-RI API:

| HTTP Code | Type | Description |
| --------- | ---- | ----------- |
| 200 | OK | Your request has been properly completed. |
| 201 | Created | The request has been fulfilled and resulted in a new resource being created. |
| 204 | No Content | The server successfully processed the request, but is not returning any content. |
| 403 | Forbidden | The request was a valid request, but the server is refusing to respond to it. |
| 404 | Not Found | The requested resource could not be found but may be available again in the future.  |
| 406 | Not Acceptable | The requested resource is only capable of generating content not acceptable according to the Accept headers sent in the request. |
| 409 | Conflict | Indicates that the request could not be processed because of conflict in the request, such as an edit conflict in the case of multiple updates. |
| 500 | Internal Server Error | A generic error message, given when an unexpected condition was encountered and no more specific message is suitable. |

## Users Management API

### Resources API

#### Create a Resource

- **Path**: /FiwareRepository/v2/collec/collectionA
- **Method**: POST
- **Content-Type**: <code>application/json</code> or <code>application/xml</code>
- **Body**:
	<pre>
	&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
    &lt;resource id="collectionA/collectionB/resource"&gt;
	    &lt;creator&gt;Creator&lt;/creator&gt;
        &lt;creationDate&gt;&lt;/creationDate&gt;
        &lt;modificationDate&gt;&lt;/modificationDate&gt;
	    &lt;name&gt;Test resource&lt;/name&gt;
        &lt;contentUrl&gt;http://testresourceurl.com/resource&lt;/contentUrl&gt;
	    &lt;contentFileName&gt;resourceFileName&lt;/contentFileName&gt;
    &lt;/resource&gt;
	</pre>

- **Response**:
    - **Status**: 201


#### Update Resource Metadata

- **Path**: /FiwareRepository/v2/collec/collectionA/collectionB/ResourceName.meta
- **Method**: PUT
- **Content-Type**: <code>application/json</code> or <code>application/xml</code>
- **Body**:
    <pre>
    &lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
    &lt;resource id="collectionA/collectionB/resource"&gt;
	    &lt;creator&gt;Creator&lt;/creator&gt;
        &lt;creationDate&gt;&lt;/creationDate&gt;
        &lt;modificationDate&gt;&lt;/modificationDate&gt;
	    &lt;name&gt;Test resource&lt;/name&gt;
	    &lt;contentFileName&gt;resourceFileName&lt;/contentFileName&gt;
    &lt;/resource&gt;
    </pre>
    
- **Response**:
    - **Status**: 200


#### Update Resource Content

- **Path**: /FiwareRepository/v2/collec/collectionA/collectionB/ResourceName
- **Method**: PUT
- **Content-Type**: <code>text/plain</code>, <code>application/json</code>, <code>application/xml</code>, <code>application/RDF+xml</code>, <code>text/turtle</code>, <code>text/n3</code>
- **Body**:
    <pre>
    &lt;rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:app="http://www.app.fake/app#"&gt;

    &lt;rdf:Description
   rdf:about="http://www.app.fake/app/App1"&gt;
      &lt;app:name&gt;App1&lt;/app:name&gt;
      &lt;app:country&gt;USA&lt;/app:country&gt;
      &lt;app:company&gt;Company1&lt;/app:company&gt;
      &lt;app:price&gt;0.99&lt;/app:price&gt;
      &lt;app:year&gt;2010&lt;/app:year&gt;
    &lt;/rdf:Description&gt;

    &lt;rdf:Description
    rdf:about="http://www.app.fake/app/App2"&gt;
      &lt;app:name&gt;App2&lt;/app:name&gt;
      &lt;app:country&gt;Spain&lt;/app:country&gt;
      &lt;app:company&gt;Company2&lt;/app:company&gt;
      &lt;app:price&gt;0.99&lt;/app:price&gt;
      &lt;app:year&gt;2010&lt;/app:year&gt;
    &lt;/rdf:Description&gt;

    &lt;/rdf:RDF&gt;
    </pre>

- **Response**:
    - **Status**: 200


#### Get Resource 

- **Path**: /FiwareRepository/v2/collec/collectionA/collectionB/ResourceName
- **Method**: GET
- **Accept**: <code>text/plain</code>, <code>text/html</code>, <code>application/json</code> or <code>application/xml</code>
	
- **Response**:
    - **Status**: 200
    - **Body**: [Resource Metadata]


#### Get Resource Metadata

- **Path**: /FiwareRepository/v2/collec/collectionA/collectionB/ResourceName.meta
- **Method**: GET
- **Accept**: <code>text/plain</code>, <code>text/html</code>, <code>application/json</code> or <code>application/xml</code>
	
- **Response**:
    - **Status**: 200
    - **Body**: [Resource Content]



#### Delete Resource

- **Path**: /FiwareRepository/v2/collec/collectionA/collectionB/ResourceName
- **Method**: DELETE
	
- **Response**:
    - **Status**: 204


### Collection API

#### Create Collection

- **Path**: /FiwareRepository/v2/collec/
- **Method**: POST
- **Content-Type**: <code>application/json</code> or <code>application/xml</code>
- **Body**:
	<pre>
	&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
	&lt;collection xmlns:atom="http://www.w3.org/2005/Atom"&gt;
	       &lt;creator&gt;CreatornameUpdate&lt;/creator&gt;
	       &lt;collections/&gt;
	       &lt;resources/&gt;
	&lt;/collection&gt;
	</pre>

- **Response**:
    - **Status**: 201


#### Get Collection

- **Path**: /FiwareRepository/v2/collec/collectionA
- **Method**: GET
- **Accept**: <code>text/plain</code>, <code>text/html</code>, <code>application/json</code> or <code>application/xml</code>

- **Response**:
    - **Status**: 200
    - **Body**: [Collection]


#### Delete Collection

- **Path**: /FiwareRepository/v2/collec/collectionA
- **Method**: DELETE
	
- **Response**:
    - **Status**: 204


### Query API

#### Execute Short Query 

- **Path**: /FiwareRepository/v2/services/query?query=SELECT+%3Fs+%3Fp+%3Fo+WHERE+%7B%3Fs+%3Fp+%3Fo+%7D+LIMIT+10
- **Method**: GET
- **Accept**: <code>text/plain</code>, <code>application/json</code>, <code>application/xml</code>, <code>application/RDF+xml</code>, <code>application/RDF+json</code>, <code>text/turtle</code>, <code>text/n3</code>
	
- **Response**: 
    - **Status**: 200
    - **Body**: [Query Response]


#### Execute Long Query  

- **Path**: /FiwareRepository/v2/services/query
- **Method**: POST
- **Accept**: <code>text/plain</code>, <code>application/json</code>, <code>application/xml</code>, <code>application/RDF+xml</code>, <code>application/RDF+json</code>, <code>text/turtle</code>, <code>text/n3</code>
- **Content-Type**: <code>text/plain</code>
- **Body**: <pre>SELECT ?s ?p ?o WHERE {?s ?p ?o }</pre>
	
- **Response**: 
    - **Status**: 200
    - **Body**: [Query Response]


#### Get Resource Content by URL Content

- **Path**: /FiwareRepository/v2/services/query/urlContent
- **Method**: GET
- **Accept**: <code>text/plain</code>, <code>application/json</code>, <code>application/xml</code>, <code>application/RDF+xml</code>, <code>application/RDF+json</code>, <code>text/turtle</code>, <code>text/n3</code>
	
- **Response**:
    - **Status**: 200
    - **Body**: [Resource Metadata]
	
