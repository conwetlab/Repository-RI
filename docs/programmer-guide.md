# Repository - Programer Guide

.fx: cover

@conwet

---
## Introduction

* This document describes the basic concepts regarding the Repository GEri and explains the necessary steps to develop applications which makes use of the Repository backend functionality. The Repository API is based on REST principles and generally returns XML or JSON encoded responses. Since REST is independent of a concrete programming language, you just have to know how to make an HTTP request in the programming language of your choice.

* To give you a feeling of how the Repository works and how you can interact with the system let us take a look at some examples, realized with the command line tool cURL and in Java. 'cURL' is a command which can be used to perform any kind of HTTP operation - and therefore is also usable for the Repository. The library libcurl enables the integration in C programs as well. Other easy way to interact with the Repository is using any REST client like some browsers extensions provide to the user. We describe the API REST to show you what operations is able to do, and what responses are given by the Repository.

---
## API REST Operations

.fx: section-title

---
## Managing Collections

Following you can find  the description of the operations that can be performed over collections:

* GET  http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection} Get a collection.
* POST  http://[SERVER_HOST]/FiwareRepository/v2/collec/  Create a collection.
* DELETE  http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/ Delete a resource

---
## Getting collections

* Request
<pre>
Verb: GET
URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}
Content-type: application/json, application/xml, text/plain
</pre>

* Responses

| Code | Description | Explanation |
| ---- | ----------- | ----------- |
| 200 | OK | The request was handled successfully and transmitted in response message. |
| 404 | NOT FOUND | The requested resource could not be found but may be available again in the future. |
| 406 | NOT ACCEPTABLE | The requested resource is only capable of generating content not acceptable according to the Accept headers sent in the request. |

---
## Creating collections

* Request
<pre>
Verb: POST
URI: http://[SERVER_HOST]FiwareRepository/v2/collec/
Content-Type: application/json, application/xml
Body: <pre>
	&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
	&lt;collection xmlns:atom="http://www.w3.org/2005/Atom"&gt;
	       &lt;creator&gt;CreatornameUpdate&lt;/creator&gt;
	       &lt;collections/&gt;
	       &lt;resources/&gt;
	&lt;/collection&gt;
	</pre>
</pre>

* Responses

| Code | Description | Explanation |
| ---- | ----------- | ----------- |
| 201 | CREATED | The request has been fulfilled and resulted in a new resource being created. |
| 409 | CONFLICT | Indicates that the request could not be processed because of conflict in the request, such as an edit conflict. |
| 415 | UNSUPPORTED MEDIA TYPE | The request entity has a media type which the server or resource does not support. |

---
## Removing collections

* Request
<pre>
Verb: DELETE
URI: http://[SERVER_HOST]FiwareRepository/v2/collec/{collection}
</pre>

* Responses

| Code | Description | Explanation |
| ---- | ----------- | ----------- |
| 204 | NO CONTENT | The server successfully processed the request, but is not returning any content. Usually used as a response to a successful delete request. |
| 404 | NOT FOUND | The requested resource could not be found but may be available again in the future. |

---
## Managing Resources

Following you can find  the description of the operations that can be performed over resources:

* GET  http://[SERVER_HOST]FiwareRepository/v2/collec/{collection}/{resource}.meta  Get metadata of a resource
* GET  http://[SERVER_HOST]FiwareRepository/v2/collec/{collection}/{resource}  Get content of a resource.
* POST http://[SERVER_HOST]FiwareRepository/v2/collec/ Create a resource with specified metadata.
* PUT  http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}.meta  Replace metadata of a resource.
* PUT  http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}  Replace content of a resource.
* DELETE  http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource} Delete a resource

---
## Getting resources metadata

* Request
<pre>
Verb: GET
URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}.meta
Accept: application/json, application/xml, text/plain
</pre>

* Responses

| Code | Description | Explanation |
| ---- | ----------- | ----------- |
| 200 | OK | The request was handled successfully and transmitted in response message. |
| 404 | NOT FOUND | The requested resource could not be found but may be available again in the future. |
| 406 | NOT ACCEPTABLE | The requested resource is only capable of generating content not acceptable according to the Accept headers sent in the request. |

---
## Getting resources

* Request
<pre>
Verb: GET
URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}
Accept: application/json, application/xml, application/rdf+xml, text/turtle, application/x-turtle, text/n3, text/rdf+n3, text/n-triples, text/plain
</pre>

* Responses

| Code | Description | Explanation |
| ---- | ----------- | ----------- |
| 200 | OK | The request was handled successfully and transmitted in response message. |
| 204 | NO CONTENT | The server successfully processed the request, but is not returning any content. |
| 404 | NOT FOUND | The requested resource could not be found but may be available again in the future. |

---
## Creating resources

* Request
<pre>
Verb: POST
URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/
Content-Type: application/json, application/xml
Body: <pre>
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
</pre>

* Responses

| Code | Description | Explanation |
| ---- | ----------- | ----------- |
| 201 | CREATED | The request has been fulfilled and resulted in a new resource being created. |
| 409 | CONFLICT | Indicates that the request could not be processed because of conflict in the request, such as an edit conflict. |
| 415 | UNSUPPORTED MEDIA TYPE | The request entity has a media type which the server or resource does not support. |

---
## Updating resources metadata

* Request
<pre>
Verb: PUT
URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}.meta
Content-Type: application/json, application/xml
Body: <pre>
    &lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
    &lt;resource id="collectionA/collectionB/resource"&gt;
	    &lt;creator&gt;Creator&lt;/creator&gt;
        &lt;creationDate&gt;&lt;/creationDate&gt;
        &lt;modificationDate&gt;&lt;/modificationDate&gt;
	    &lt;name&gt;Test resource&lt;/name&gt;
	    &lt;contentFileName&gt;resourceFileName&lt;/contentFileName&gt;
    &lt;/resource&gt;
    </pre>
</pre>

* Responses

| Code | Description | Explanation |
| ---- | ----------- | ----------- |
| 200 | OK | The request was handled successfully and transmitted in response message. |
| 403 | FORBIDDEN | The request was a valid request, but the server is refusing to respond to it. |
| 415 | UNSUPPORTED MEDIA TYPE | The request entity has a media type which the server or resource does not support. |

---
## Updating resources content

* Request
<pre>
Verb: PUT
URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}
Content-Type: application/json, application/xml, application/rdf+xml, text/turtle, application/x-turtle, text/n3, text/rdf+n3, text/n-triples, text/plain
</pre>

* Responses

| Code | Description | Explanation |
| ---- | ----------- | ----------- |
| 200 | OK | The request was handled successfully and transmitted in response message. |
| 403 | FORBIDDEN | The request was a valid request, but the server is refusing to respond to it. |
| 404 | NOT FOUND | The requested resource could not be found but may be available again in the future. |

---
## Deleting resources

* Request
<pre>
Verb: PUT
URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}
</pre>

* Responses

| Code | Description | Explanation |
| ---- | ----------- | ----------- |
| 204 | NO CONTENT | The server successfully processed the request, but is not returning any content. Usually used as a response to a successful delete request. |
| 404 | NOT FOUND | The requested resource could not be found but may be available again in the future. |

---
## Making SPARQL queries

* Following you can find the quering operations that can be executed.

* GET  http://[SERVER_HOST]/FiwareRepository/v2/services/query/{contentUrl}
* GET  http://[SERVER_HOST]/FiwareRepository/v2/services/query?query=[Query] Execute a query in the triple store.
* POST http://[SERVER_HOST]/FiwareRepository/v2/services/query | Execute a long query in the triple store.

---
## Getting a resource by Url Content

* Request
<pre>
Verb: GET
URI: http://[SERVER_HOST]/FiwareRepository/v2/services/query/{contentUrl}
Accept: application/json, application/xml, application/rdf+xml, text/turtle, application/x-turtle, text/n3, text/rdf+n3, text/n-triples, text/plain
</pre>

* Responses

| Code | Description | Explanation |
| ---- | ----------- | ----------- |
| 200 | OK | The request was handled successfully and transmitted in response message. |
| 403 | FORBIDDEN | The request was a valid request, but the server is refusing to respond to it. |
| 404 | NOT FOUND | The requested resource could not be found but may be available again in the future. |

---
## Executing a short SPARQL query

* Request
<pre>
Verb: GET
URI: http://[SERVER_HOST]/FiwareRepository/v2/services/query?query=SELECT+%3Fs+%3Fp+%3Fo+WHERE+%7B%3Fs+%3Fp+%3Fo+%7D
Accept: application/json, application/xml, application/rdf+xml, text/turtle, application/x-turtle, text/n3, text/rdf+n3, text/n-triples, text/plain
</pre>

* Responses

| Code | Description | Explanation |
| ---- | ----------- | ----------- |
| 200 | OK | The request was handled successfully and transmitted in response message. |
| 406 | NOT ACCEPTABLE | The requested resource is only capable of generating content not acceptable according to the Accept headers sent in the request. |
| 500 | INTERNAL SERVER ERROR | A generic error message, given when an unexpected condition was encountered and no more specific message is suitable. |

---
## Executing a long SPARQL query

* Request
<pre>
Verb: GET
URI: http://[SERVER_HOST]/FiwareRepository/v2/services/query
Accept: application/json, application/xml, application/rdf+xml, text/turtle, application/x-turtle, text/n3, text/rdf+n3, text/n-triples, text/plain
Body: "<pre>SELECT ?s ?p ?o WHERE {?s ?p ?o }</pre>"
</pre>

* Responses

| Code | Description | Explanation |
| ---- | ----------- | ----------- |
| 200 | OK | The request was handled successfully and transmitted in response message. |
| 406 | NOT ACCEPTABLE | The requested resource is only capable of generating content not acceptable according to the Accept headers sent in the request. |
| 500 | INTERNAL SERVER ERROR | A generic error message, given when an unexpected condition was encountered and no more specific message is suitable. |

---
## Accessing the Repository with cURL

.fx: section-title

---
## Creating a resource

* Create a message body and save it to a file named resource.xml.

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

---
## Creating a resource
* Send the request to the server.

<pre>
curl -v -H "Content-Type: application/xml" -X POST --data "@resource.xml" http://[SERVER_HOST]/FiwareRepository/v2/collec                                                                        
</pre>

---
## Creating a resource
* Response

<pre>
* Hostname was NOT found in DNS cache
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
&gt; POST /FiwareRepository/v2/collec HTTP/1.1
&gt; User-Agent: curl/7.35.0
&gt; Host: localhost:8080
&gt; Accept: */*
&gt; Content-Type: application/xml
&gt; Content-Length: 349
&gt; 
* upload completely sent off: 349 out of 349 bytes
< HTTP/1.1 201 Created
* Server Apache-Coyote/1.1 is not blacklisted
&lt; Server: Apache-Coyote/1.1
&lt; Content-Location: http://testresourceurl.com/resource
&lt; Content-Length: 0
&lt; Date: Thu, 28 May 2015 12:12:23 GMT
&lt; 
* Connection #0 to host localhost left intact
</pre>

---
## Updating the content of a resource

* Create a message body and save it to a file named resourceContent.xml.

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
<pre>

---
## Updating the content of a resource
* Send the request to the server.

<pre>
curl -v -H "Content-Type: application/rdf+xml" -X PUT --data-binary "@resourceContent.xml" http://localhost:8080/FiwareRepository/v2/collec/collectionA/collectionB/resource     
</pre>

---
## Updating the content of a resource
* Response:

<pre>
* Hostname was NOT found in DNS cache
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
&gt; PUT /FiwareRepository/v2/collec/collectionA/collectionB/resource HTTP/1.1
&gt; User-Agent: curl/7.35.0
&gt; Host: localhost:8080
&gt; Accept: */*
&gt; Content-Type: application/rdf+xml
&gt; Content-Length: 645
&gt; 
* upload completely sent off: 645 out of 645 bytes
&lt; HTTP/1.1 200 OK
* Server Apache-Coyote/1.1 is not blacklisted
&lt; Server: Apache-Coyote/1.1
&lt; Content-Length: 0
&lt; Date: Thu, 28 May 2015 12:41:21 GMT
&lt; 
* Connection #0 to host localhost left intact
</pre>

---
## Getting a resource

* Send the request to the server.

<pre>
curl -v -H "Accept: application/rdf+xml" -X GET http://localhost:8080/FiwareRepository/v2/collec/collectionA/collectionB/resource     
</pre>

---
## Getting a resource
* Response:

<pre>
* Hostname was NOT found in DNS cache
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
&gt; GET /FiwareRepository/v2/collec/collectionA/collectionB/resource HTTP/1.1
&gt; User-Agent: curl/7.35.0
&gt; Host: localhost:8080
&gt; Accept: application/rdf+xml
&gt; 
&lt; HTTP/1.1 200 OK
* Server Apache-Coyote/1.1 is not blacklisted
&lt; Server: Apache-Coyote/1.1
&lt; Content-Type: application/rdf+xml
&lt; Content-Length: 645
&lt; Date: Thu, 28 May 2015 12:43:23 GMT
&lt; 
&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
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
* Connection #0 to host localhost left intact
</pre>

---
## Deleting a resource

* Send the request to the server.

<pre>
curl -v -X DELETE http://localhost:8080/FiwareRepository/v2/collec/collectionA/collectionB/resource     
</pre>

---
## Deleting a resource
* Response:

<pre>
* Hostname was NOT found in DNS cache
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
&gt; DELETE /FiwareRepository/v2/collec/collectionA/collectionB/resource HTTP/1.1
&gt; User-Agent: curl/7.35.0
&gt; Host: localhost:8080
&gt; Accept: */*
&gt; 
&lt; HTTP/1.1 204 No Content
* Server Apache-Coyote/1.1 is not blacklisted
&lt; Server: Apache-Coyote/1.1
&lt; Content-Length: 0
&lt; Date: Thu, 28 May 2015 12:46:33 GMT
&lt; 
* Connection #0 to host localhost left intact
</pre>  

---
## Executing a Query

* Send the request to the server.

<pre>
curl -v -H "Accept: application/json" -H "Content-Type: text/plain" -X POST --data "SELECT ?s ?p ?o WHERE {?s ?p ?o} LIMIT 10" http://localhost:8080/FiwareRepository/v2/services/query    
</pre>

---
## Executing a Query
* Response:

<pre>
* Hostname was NOT found in DNS cache
*   Trying 127.0.0.1...
* Connected to localhost (127.0.0.1) port 8080 (#0)
&gt; POST /FiwareRepository/v2/services/query HTTP/1.1
&gt; User-Agent: curl/7.35.0
&gt; Host: localhost:8080
&gt; Accept: application/json
&gt; Content-Type: text/plain
&gt; Content-Length: 41
&gt; 
* upload completely sent off: 41 out of 41 bytes
&lt; HTTP/1.1 200 OK
* Server Apache-Coyote/1.1 is not blacklisted
&lt; Server: Apache-Coyote/1.1
&lt; Content-Type: application/json
&lt; Transfer-Encoding: chunked
&lt; Date: Thu, 28 May 2015 13:33:32 GMT
&lt; 
{
vars: 3
columns: [3]
0:  {
values: [10]
0:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
1:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
2:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
3:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
4:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
5:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
6:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
7:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
8:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
9:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
-
name: "p"
}-
1:  {
values: [10]
0:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
1:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
2:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
3:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
4:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
5:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
6:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
7:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
8:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
9:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
-
name: "o"
}-
2:  {
values: [10]
0:  "http://www.openlinksw.com/virtrdf-data-formats#default-iid"
1:  "http://www.openlinksw.com/virtrdf-data-formats#default-iid-nullable"
2:  "http://www.openlinksw.com/virtrdf-data-formats#default-iid-nonblank"
3:  "http://www.openlinksw.com/virtrdf-data-formats#default-iid-nonblank-nullable"
4:  "http://www.openlinksw.com/virtrdf-data-formats#default"
5:  "http://www.openlinksw.com/virtrdf-data-formats#default-nullable"
6:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar"
7:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar-nullable"
8:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar-dt"
9:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar-dt-nullable"
-
name: "s"
}-
-
}
</pre>

---
## Managing different data formats

.fx: section-title

---
## Managing different data formats
* HTTP content negotiation allows the client to choose the appropriate data format for retrieving meta information about a resource or a collection. Besides XML and JSON the Repository also supports human readable output formats using HTML rendering ('text/html' accept header) including hyperlinked representation and formatted text.

---
## Text Representation

* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}
* Accept Header: text/plain
* Result:

<pre>
Collection: testCollection
Creation Date: Thu Mar 21 10:46:39 CET 2013

Collections:
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
++  Collection Id                  +   Creation Date                     ++
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
++  testCollection/collectionA     +   Thu Mar 21 10:47:53 CET 2013      ++
++  testCollection/collectionB     +   Thu Mar 21 10:47:53 CET 2013      ++
++  testCollection/                +   Thu Mar 21 10:47:53 CET 2013      ++
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


Resources:
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
++  Resource Id                    +   Creation Date                  +   Modification Date              +   Filename             +   Mime Type              ++
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
++  testCollection/testResource1   +   Thu Mar 21 10:46:39 CET 2013   +   Thu Mar 21 10:46:39 CET 2013   +   filename             +   application/rdf+xml    ++
++  testCollection/testResource2   +   Thu Mar 21 10:47:53 CET 2013   +   Thu Mar 21 10:47:53 CET 2013   +   filename             +   plain/text             ++
++  testCollection/testResource3   +   Thu Mar 21 10:47:53 CET 2013   +   Thu Mar 21 10:47:53 CET 2013   +   filename             +   text/turtle            ++
++  testCollection/testResource4   +   Thu Mar 21 10:47:53 CET 2013   +   Thu Mar 21 10:47:53 CET 2013   +   filename             +   application/rdf+xml    ++
+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
</pre>

---
## JSON Representation

* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}
* Accept Header: application/json
* Result:

<pre>
{
  "resources": [
    {
      "name": "",
      "content": null,
      "collection": null,
      "contentMimeType": "application\/rdf+xml",
      "contentFileName": "filename",
      "contentUrl": "",
      "id": "testCollection\/testResource1",
      "creationDate": 1363859199839,
      "creator": "",
      "modificationDate": 1363859199839
    },
    {
      "name": "",
      "content": null,
      "collection": null,
      "contentMimeType": "plain\/text",
      "contentFileName": "filename",
      "contentUrl": "",
      "id": "testCollection\/testResource2",
      "creationDate": 1363859273515,
      "creator": "",
      "modificationDate": 1363859273515
    },
    {
      "name": "",
      "content": null,
      "collection": null,
      "contentMimeType": "text\/turtle",
      "contentFileName": "filename",
      "contentUrl": "",
      "id": "testCollection\/testResource3",
      "creationDate": 1363859273535,
      "creator": "",
      "modificationDate": 1363859273535
    },
    {
      "name": "",
      "content": null,
      "collection": null,
      "contentMimeType": "application\/rdf+xml",
      "contentFileName": "filename",
      "contentUrl": "",
      "id": "testCollection\/testResource4",
      "creationDate": 1363859273545,
      "creator": "",
      "modificationDate": 1363859273545
    }
  ],
  "collections": [
    {
      "resources": [
        
      ],
      "collections": [
        
      ],
      "id": "testCollection\/collectionA",
      "creationDate": 1363859273552,
      "creator": "",
      "modificationDate": null
    },
    {
      "resources": [
        
      ],
      "collections": [
        
      ],
      "id": "testCollection\/collectionB",
      "creationDate": 1363859273566,
      "creator": "",
      "modificationDate": null
    },
    {
      "resources": [
        
      ],
      "collections": [
        
      ],
      "id": "testCollection\/",
      "creationDate": 1363859273575,
      "creator": "",
      "modificationDate": null
    }
  ],
  "id": "testCollection",
  "creationDate": 1363859199837,
  "creator": "",
  "modificationDate": null
}
</pre>

---
## XML Representation

* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}
* Accept Header: application/json
* Result:

<pre>
&lt;?xml version="1.0" encoding="UTF-8" standalone="yes"?&gt;
&lt;collection id="testCollection" xmlns:atom="http://www.w3.org/2005/Atom"&gt;
    &lt;creationDate&gt;2013-03-21T10:46:39.837+01:00&lt;/creationDate&gt;
    &lt;creator/&gt;
    &lt;collections&gt;
        &lt;collections id="testCollection/collectionA"&gt;
            &lt;creationDate&gt;2013-03-21T10:47:53.552+01:00&lt;/creationDate&gt;
            &lt;creator/&gt;
            &lt;collections/&gt;
            &lt;resources/&gt;
        &lt;/collections&gt;
        &lt;collections id="testCollection/collectionB"&gt;
            &lt;creationDate&gt;2013-03-21T10:47:53.566+01:00&lt;/creationDate&gt;
            &lt;creator/&gt;
            &lt;collections/&gt;
            &lt;resources/&gt;
        &lt;/collections&gt;
        &lt;collections id="testCollection/"&gt;
            &lt;creationDate&gt;2013-03-21T10:47:53.575+01:00&lt;/creationDate&gt;
            &lt;creator/&gt;
            &lt;collections/&gt;
            &lt;resources/&gt;
        &lt;/collections&gt;
    &lt;/collections&gt;
    &lt;resources&gt;
        &lt;resources id="testCollection/testResource1"&gt;
            &lt;creationDate&gt;2013-03-21T10:46:39.839+01:00&lt;/creationDate&gt;
            &lt;creator/&gt;
            &lt;modificationDate&gt;2013-03-21T10:46:39.839+01:00&lt;/modificationDate&gt;
            &lt;contentFileName&gt;filename&lt;/contentFileName&gt;
            &lt;contentMimeType&gt;application/rdf+xml&lt;/contentMimeType&gt;
            &lt;contentUrl/&gt;
            &lt;name/&gt;
        &lt;/resources&gt;
        &lt;resources id="testCollection/testResource2"&gt;
            &lt;creationDate&gt;2013-03-21T10:47:53.515+01:00&lt;/creationDate&gt;
            &lt;creator/&gt;
            &lt;modificationDate&gt;2013-03-21T10:47:53.515+01:00&lt;/modificationDate&gt;
            &lt;contentFileName&gt;filename&lt;/contentFileName&gt;
            &lt;contentMimeType&gt;plain/text&lt;/contentMimeType&gt;
            &lt;contentUrl/&gt;
            &lt;name/&gt;
        &lt;/resources&gt;
        &lt;resources id="testCollection/testResource3"&gt;
            &lt;creationDate&gt;2013-03-21T10:47:53.535+01:00&lt;/creationDate&gt;
            &lt;creator/&gt;
            &lt;modificationDate&gt;2013-03-21T10:47:53.535+01:00&lt;/modificationDate&gt;
            &lt;contentFileName&gt;filename&lt;/contentFileName&gt;
            &lt;contentMimeType&gt;text/turtle&lt;/contentMimeType&gt;
            &lt;contentUrl/&gt;
            &lt;name/&gt;
        &lt;/resources&gt;
        &lt;resources id="testCollection/testResource4"&gt;
            &lt;creationDate&gt;2013-03-21T10:47:53.545+01:00&lt;/creationDate&gt;
            &lt;creator/&gt;
            &lt;modificationDate&gt;2013-03-21T10:47:53.545+01:00&lt;/modificationDate&gt;
            &lt;contentFileName&gt;filename&lt;/contentFileName&gt;
            &lt;contentMimeType&gt;application/rdf+xml&lt;/contentMimeType&gt;
            &lt;contentUrl/&gt;
            &lt;name/&gt;
        &lt;/resources&gt;
    &lt;/resources&gt;
&lt;/collection&gt;
</pre>

---
## Retrieving Resouce content in different formats

.fx:section-title

---
## Retrieving Resouce content in different formats

* When you insert some resource content in any of the accepted RDF formats, it is possible to retrieve that content in any of the allowed RDF formats, as explined in the following slides.

---
## XML+RDF Representation
* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}/{resource}
* Accept Header: application/rdf+xml
* Result:

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

---
## Turtle Representation
* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}/{resource}
* Accept Header: "text/turtle"
* Result:

<pre>
@prefix rdfdf: &lt;http://www.openlinksw.com/virtrdf-data-formats#&gt; .
@prefix owl:   &lt;http://www.w3.org/2002/07/owl#&gt; .
@prefix fn:    &lt;http://www.w3.org/2005/xpath-functions/#&gt; .
@prefix xsd:   &lt;http://www.w3.org/2001/XMLSchema#&gt; .
@prefix yago:  &lt;http://dbpedia.org/class/yago/&gt; .
@prefix skos:  &lt;http://www.w3.org/2004/02/skos/core#&gt; .
@prefix ogc:   &lt;http://www.opengis.net/&gt; .
@prefix rdfs:  &lt;http://www.w3.org/2000/01/rdf-schema#&gt; .
@prefix ogcgsf: &lt;http://www.opengis.net/def/function/geosparql/&gt; .
@prefix protseq: &lt;http://purl.org/science/protein/bysequence/&gt; .
@prefix xslwd: &lt;http://www.w3.org/TR/WD-xsl&gt; .
@prefix sql:   &lt;sql:&gt; .
@prefix geo:   &lt;http://www.w3.org/2003/01/geo/wgs84_pos#&gt; .
@prefix sc:    &lt;http://purl.org/science/owl/sciencecommons/&gt; .
@prefix sd:    &lt;http://www.w3.org/ns/sparql-service-description#&gt; .
@prefix ogcsf: &lt;http://www.opengis.net/ont/sf#&gt; .
@prefix nci:   &lt;http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#&gt; .
@prefix virtrdf: &lt;http://www.openlinksw.com/schemas/virtrdf#&gt; .
@prefix xml:   &lt;http://www.w3.org/XML/1998/namespace&gt; .
@prefix ogcgml: &lt;http://www.opengis.net/ont/gml#&gt; .
@prefix ogcgsr: &lt;http://www.opengis.net/def/rule/geosparql/&gt; .
@prefix rdfa:  &lt;http://www.w3.org/ns/rdfa#&gt; .
@prefix mf:    &lt;http://www.w3.org/2001/sw/DataAccess/tests/test-manifest#&gt; .
@prefix scovo: &lt;http://purl.org/NET/scovo#&gt; .
@prefix virtcxml: &lt;http://www.openlinksw.com/schemas/virtcxml#&gt; .
@prefix dbpprop: &lt;http://dbpedia.org/property/&gt; .
@prefix foaf:  &lt;http://xmlns.com/foaf/0.1/&gt; .
@prefix mesh:  &lt;http://purl.org/commons/record/mesh/&gt; .
@prefix sioc:  &lt;http://rdfs.org/sioc/ns#&gt; .
@prefix xsl10: &lt;http://www.w3.org/XSL/Transform/1.0&gt; .
@prefix product: &lt;http://www.buy.com/rss/module/productV2/&gt; .
@prefix void:  &lt;http://rdfs.org/ns/void#&gt; .
@prefix dawgt: &lt;http://www.w3.org/2001/sw/DataAccess/tests/test-dawg#&gt; .
@prefix go:    &lt;http://purl.org/obo/owl/GO#&gt; .
@prefix dbpedia: &lt;http://dbpedia.org/resource/&gt; .
@prefix vcard: &lt;http://www.w3.org/2001/vcard-rdf/3.0#&gt; .
@prefix xsl1999: &lt;http://www.w3.org/1999/XSL/Transform&gt; .
@prefix xf:    &lt;http://www.w3.org/2004/07/xpath-functions&gt; .
@prefix rdf:   &lt;http://www.w3.org/1999/02/22-rdf-syntax-ns#&gt; .
@prefix vcard2006: &lt;http://www.w3.org/2006/vcard/ns#&gt; .
@prefix ldp:   &lt;http://www.w3.org/ns/ldp#&gt; .
@prefix math:  &lt;http://www.w3.org/2000/10/swap/math#&gt; .
@prefix ogcgs: &lt;http://www.opengis.net/ont/geosparql#&gt; .
@prefix obo:   &lt;http://www.geneontology.org/formats/oboInOwl#&gt; .
@prefix bif:   &lt;bif:&gt; .
@prefix dc:    &lt;http://purl.org/dc/elements/1.1/&gt; .

&lt;http://www.app.fake/app/App1&gt;
        &lt;http://www.app.fake/app#company&gt;
                "Company1" ;
        &lt;http://www.app.fake/app#country&gt;
                "USA" ;
        &lt;http://www.app.fake/app#name&gt;  "App1" ;
        &lt;http://www.app.fake/app#price&gt;
                "0.99" ;
        &lt;http://www.app.fake/app#year&gt;  "2010" .

&lt;http://www.app.fake/app/App2&gt;
        &lt;http://www.app.fake/app#company&gt;
                "Company2" ;
        &lt;http://www.app.fake/app#country&gt;
                "Spain" ;
        &lt;http://www.app.fake/app#name&gt;  "App2" ;
        &lt;http://www.app.fake/app#price&gt;
                "0.99" ;
        &lt;http://www.app.fake/app#year&gt;  "2010" .
</pre>

---
## N3 Representation
* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}/{resource}
* Accept Header: "text/n3"
* Result:

<pre>
@prefix rdfdf: &lt;http://www.openlinksw.com/virtrdf-data-formats#&gt; .
@prefix owl:   &lt;http://www.w3.org/2002/07/owl#&gt; .
@prefix fn:    &lt;http://www.w3.org/2005/xpath-functions/#&gt; .
@prefix xsd:   &lt;http://www.w3.org/2001/XMLSchema#&gt; .
@prefix yago:  &lt;http://dbpedia.org/class/yago/&gt; .
@prefix skos:  &lt;http://www.w3.org/2004/02/skos/core#&gt; .
@prefix ogc:   &lt;http://www.opengis.net/&gt; .
@prefix rdfs:  &lt;http://www.w3.org/2000/01/rdf-schema#&gt; .
@prefix ogcgsf: &lt;http://www.opengis.net/def/function/geosparql/&gt; .
@prefix protseq: &lt;http://purl.org/science/protein/bysequence/&gt; .
@prefix xslwd: &lt;http://www.w3.org/TR/WD-xsl&gt; .
@prefix sql:   &lt;sql:&gt; .
@prefix geo:   &lt;http://www.w3.org/2003/01/geo/wgs84_pos#&gt; .
@prefix sc:    &lt;http://purl.org/science/owl/sciencecommons/&gt; .
@prefix sd:    &lt;http://www.w3.org/ns/sparql-service-description#&gt; .
@prefix ogcsf: &lt;http://www.opengis.net/ont/sf#&gt; .
@prefix nci:   &lt;http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#&gt; .
@prefix virtrdf: &lt;http://www.openlinksw.com/schemas/virtrdf#&gt; .
@prefix xml:   &lt;http://www.w3.org/XML/1998/namespace&gt; .
@prefix ogcgml: &lt;http://www.opengis.net/ont/gml#&gt; .
@prefix ogcgsr: &lt;http://www.opengis.net/def/rule/geosparql/&gt; .
@prefix rdfa:  &lt;http://www.w3.org/ns/rdfa#&gt; .
@prefix mf:    &lt;http://www.w3.org/2001/sw/DataAccess/tests/test-manifest#&gt; .
@prefix scovo: &lt;http://purl.org/NET/scovo#&gt; .
@prefix virtcxml: &lt;http://www.openlinksw.com/schemas/virtcxml#&gt; .
@prefix dbpprop: &lt;http://dbpedia.org/property/&gt; .
@prefix foaf:  &lt;http://xmlns.com/foaf/0.1/&gt; .
@prefix mesh:  &lt;http://purl.org/commons/record/mesh/&gt; .
@prefix sioc:  &lt;http://rdfs.org/sioc/ns#&gt; .
@prefix xsl10: &lt;http://www.w3.org/XSL/Transform/1.0&gt; .
@prefix product: &lt;http://www.buy.com/rss/module/productV2/&gt; .
@prefix void:  &lt;http://rdfs.org/ns/void#&gt; .
@prefix dawgt: &lt;http://www.w3.org/2001/sw/DataAccess/tests/test-dawg#&gt; .
@prefix go:    &lt;http://purl.org/obo/owl/GO#&gt; .
@prefix dbpedia: &lt;http://dbpedia.org/resource/&gt; .
@prefix vcard: &lt;http://www.w3.org/2001/vcard-rdf/3.0#&gt; .
@prefix xsl1999: &lt;http://www.w3.org/1999/XSL/Transform&gt; .
@prefix xf:    &lt;http://www.w3.org/2004/07/xpath-functions&gt; .
@prefix rdf:   &lt;http://www.w3.org/1999/02/22-rdf-syntax-ns#&gt; .
@prefix vcard2006: &lt;http://www.w3.org/2006/vcard/ns#&gt; .
@prefix ldp:   &lt;http://www.w3.org/ns/ldp#&gt; .
@prefix math:  &lt;http://www.w3.org/2000/10/swap/math#&gt; .
@prefix ogcgs: &lt;http://www.opengis.net/ont/geosparql#&gt; .
@prefix obo:   &lt;http://www.geneontology.org/formats/oboInOwl#&gt; .
@prefix bif:   &lt;bif:&gt; .
@prefix dc:    &lt;http://purl.org/dc/elements/1.1/&gt; .

&lt;http://www.app.fake/app/App1&gt;
        &lt;http://www.app.fake/app#company&gt;
                "Company1" ;
        &lt;http://www.app.fake/app#country&gt;
                "USA" ;
        &lt;http://www.app.fake/app#name&gt;  "App1" ;
        &lt;http://www.app.fake/app#price&gt;
                "0.99" ;
        &lt;http://www.app.fake/app#year&gt;  "2010" .

&lt;http://www.app.fake/app/App2&gt;
        &lt;http://www.app.fake/app#company&gt;
                "Company2" ;
        &lt;http://www.app.fake/app#country&gt;
                "Spain" ;
        &lt;http://www.app.fake/app#name&gt;  "App2" ;
        &lt;http://www.app.fake/app#price&gt;
                "0.99" ;
        &lt;http://www.app.fake/app#year&gt;  "2010" .
</pre>

---
## N-Triples Representation
* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}/{resource}
* Accept Header: "text/n-triples"
* Result:

<pre>
    &lt;http://www.app.fake/app/App1&gt; &lt;http://www.app.fake/app#company&gt; "Company1" .
    &lt;http://www.app.fake/app/App2&gt; &lt;http://www.app.fake/app#company&gt; "Company2" .
    &lt;http://www.app.fake/app/App1&gt; &lt;http://www.app.fake/app#country&gt; "USA" .
    &lt;http://www.app.fake/app/App2&gt; &lt;http://www.app.fake/app#country&gt; "Spain" .
    &lt;http://www.app.fake/app/App1&gt; &lt;http://www.app.fake/app#name&gt; "App1" .
    &lt;http://www.app.fake/app/App2&gt; &lt;http://www.app.fake/app#name&gt; "App2" .
    &lt;http://www.app.fake/app/App1&gt; &lt;http://www.app.fake/app#price&gt; "0.99" .
    &lt;http://www.app.fake/app/App2&gt; &lt;http://www.app.fake/app#price&gt; "0.99" .
    &lt;http://www.app.fake/app/App1&gt; &lt;http://www.app.fake/app#year&gt; "2010" .
    &lt;http://www.app.fake/app/App2&gt; &lt;http://www.app.fake/app#year&gt; "2010" .
</pre>

---
## Retrieving Query response in different formats

.fx: section-title

---
## Retrieving Query response in different formats
* When you execute a SPARQL query in the Repository, it genrates different responses depending on the type of query. If you execute a SELECT query, it returns a json object composed by n columns being n number of variables. If you execute a CONSTRUCT or a DESCRIBE query, you can specifythe RDF format as described in the previous slides.

---
## Executing SELECT query
* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}/{resource}
* Accept Header: "application/json"
* Body: SELECT ?s ?p ?o WHERE {?s ?p ?o}
* Result:

<pre>
{
vars: 3
columns: [3]
0:  {
values: [15]
0:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
1:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
2:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
3:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
4:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
5:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
6:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
7:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
8:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
9:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
10:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
11:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
12:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
13:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
14:  "http://www.w3.org/1999/02/22-rdf-syntax-ns#type"
-
name: "p"
}-
1:  {
values: [15]
0:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
1:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
2:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
3:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
4:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
5:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
6:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
7:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
8:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
9:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
10:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
11:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
12:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
13:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
14:  "http://www.openlinksw.com/schemas/virtrdf#QuadMapFormat"
name: "o"
}-
2:  {
values: [15]
0:  "http://www.openlinksw.com/virtrdf-data-formats#default-iid"
1:  "http://www.openlinksw.com/virtrdf-data-formats#default-iid-nullable"
2:  "http://www.openlinksw.com/virtrdf-data-formats#default-iid-nonblank"
3:  "http://www.openlinksw.com/virtrdf-data-formats#default-iid-nonblank-nullable"
4:  "http://www.openlinksw.com/virtrdf-data-formats#default"
5:  "http://www.openlinksw.com/virtrdf-data-formats#default-nullable"
6:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar"
7:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar-nullable"
8:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar-dt"
9:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar-dt-nullable"
10:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar-lang"
11:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar-lang-nullable"
12:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar-fixedlang-x-any"
13:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar-fixedlang-x-any-nullable"
14:  "http://www.openlinksw.com/virtrdf-data-formats#sql-varchar-uri"
-
name: "s"
}-
-
}
</pre>

---

.fx: back-cover

Thanks!

FIWARE                                FIWARE Lab
OPEN APIs FOR OPEN MINDS              Spark your imagination

         www.fiware.org               FIWARE Ops
twitter: @Fiware                      Easing your operations

.fx: back-cover

Thanks!

FIWARE                                FIWARE Lab
OPEN APIs FOR OPEN MINDS              Spark your imagination

         www.fiware.org               FIWARE Ops
twitter: @Fiware                      Easing your operations
