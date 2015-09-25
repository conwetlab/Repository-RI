========================
User and Programer Guide
========================

------------
Introduction
------------

This document describes the basic concepts regarding the Repository GEri and explains the necessary steps to develop applications which makes use of the Repository backend functionality. The Repository API is based on REST principles and generally returns XML or JSON encoded responses. Since REST is independent of a concrete programming language, you just have to know how to make an HTTP request in the programming language of your choice.

Despite the Repository is a Generic Enabler which provides pure backend functionality to other applications (e.g. Generic Enablers or end user facing applications), it is possible to browse between the existing collections and resources using the basic web interface provided.

To give you a feeling of how the Repository works and how you can interact with the system let us take a look at some examples, realized with the command line tool cURL. 'cURL' is a command which can be used to perform any kind of HTTP operation - and therefore is also usable for the Repository. The library libcurl enables the integration in C programs as well. Other easy way to interact with the Repository is using any REST client like some browsers extensions provide to the user. We describe the API REST to show you what operations is able to do, and what responses are given by the Repository.

----------
User Guide
----------

The Repository allows the client to choose the appropriate data format for retrieving meta information about a resource or a collection. One of this data format is ``text/html`` which provides a user friendly interface to see the diferent collection and resources stored in the repository.

Browsing Collections
====================

It is possible to visualize the existing collections registered inside a given collection in the Repository by accessing the URL ``http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}``

.. image:: /images/collection.png
   :align: center

It is possible to visualize the existing resources contained in a given collection by accessing the URL ``http://[SERVER_HOST]/FiwareRepository/v2/collec/{collectionA}/{collectionB}/``

.. image:: /images/collectionCollection.png
   :align: center

Browsing Resources
==================

It is possible to visualize the meta information of a given resource by accessing the URL ``http://[SERVER_HOST]/FiwareRepository/v2/collec/{collectionA}/{collectionB}/{resource}``

.. image:: /images/resourcemeta.png
   :align: center

.. note::
   Depending on the configuration of the Repository RI instance, you may be redirected to the FIWARE Lab login page in order to be authenticated.

---------------
Programer Guide
---------------

The Repository offers pure backend functionality; in this way, the current section gives an overview of the operations that can be made with the API in order to integrate the functionality provided by the Repository with an existing solution.

REST API Operations
+++++++++++++++++++

This section contains an overview of the existing REST API. You can find detailed documentation of the API  in `Apiary <http://http://docs.fiwarerepository.apiary.io>`__ and in `GitHub Pages <http://conwetlab.github.io/Repository-RI/>`__.

Managing Collections
====================

Following you can find  the description of the operations that can be performed over collections:

* GET  http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection} Get a collection.
* POST  http://[SERVER_HOST]/FiwareRepository/v2/collec/  Create a collection.
* DELETE  http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/ Delete a resource

.. note::
   Every collection name must comply the regular expresion ``"[a-zA-Z0-9_-]+"``.


Getting collections
-------------------

* Request

    * Verb: GET
    * URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}
    * Content-type: application/json, application/xml, text/plain

* Responses

+-----------+-----------------------+--------------------------------------------------------------------------------+
| HTTP Code | Type                  | Description                                                                    |
+===========+=======================+================================================================================+
| 200       | OK                    | Your request has been completed properly.                                      |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 404       | Not Found             | The collection with the given path has not been found.                         |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 406       | Not Acceptable        | The collection can not be generated in the given format.                       |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 500       | Internal Server Error | There was an internal error in the system so your request cannot be completed. |
+-----------+-----------------------+--------------------------------------------------------------------------------+

Creating collections
--------------------

* Request 

    * Verb: POST
    * URI: http://[SERVER_HOST]FiwareRepository/v2/collec/[collection]
    * Content-Type: application/json, application/xml
    * Body: ::
    
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <collection xmlns:atom="http://www.w3.org/2005/Atom">
            <creator>CreatornameUpdate</creator>
            <name>TestCollection</name>
            <collections/>
            <resources/>
        </collection>
       
       
    * or ::

        {
            "type":"collection",
            "creator":"Creator",
            "creationDate":"",
            "modificationDate":"",
            "name":"TestCollection"
        }


* Responses

+-----------+------------------------+---------------------------------------------------------------------------------------------------------------------------------+
| HTTP Code | Type                   | Description                                                                                                                     |
+===========+========================+=================================================================================================================================+
| 201       | OK                     | The collection has been created. The `location` header will contain the final URL that users can use to access this collection. |
+-----------+------------------------+---------------------------------------------------------------------------------------------------------------------------------+
| 400       | Bad Request            | The collection is not well formed or collection name is not valid.                                                              |
+-----------+------------------------+---------------------------------------------------------------------------------------------------------------------------------+
| 409       | Conflict               | The collection with the path and the name given already exist.                                                                  |
+-----------+------------------------+---------------------------------------------------------------------------------------------------------------------------------+
| 415       | Unsopported Media Type | The request entity has a media type which a collection does not support.                                                        |
+-----------+------------------------+---------------------------------------------------------------------------------------------------------------------------------+
| 500       | Internal Server Error  | There was an internal error in the system so your request cannot be completed.                                                  |
+-----------+------------------------+---------------------------------------------------------------------------------------------------------------------------------+


Removing collections
--------------------

* Request

    * Verb: DELETE
    * URI: http://[SERVER_HOST]FiwareRepository/v2/collec/{collection}

* Responses

+-----------+-----------------------+--------------------------------------------------------------------------------+
| HTTP Code | Type                  | Description                                                                    |
+===========+=======================+================================================================================+
| 204       | No Content            | The collection and all its content has been deleted.                           |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 404       | Not Found             | The collection with the given path has not been found.                         |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 500       | Internal Server Error | There was an internal error in the system so your request cannot be completed. |
+-----------+-----------------------+--------------------------------------------------------------------------------+


Managing Resources
==================

Following you can find  the description of the operations that can be performed over resources:

* GET  http://[SERVER_HOST]FiwareRepository/v2/collec/{collection}/{resource}.meta  Get metadata of a resource
* GET  http://[SERVER_HOST]FiwareRepository/v2/collec/{collection}/{resource}  Get content of a resource.
* POST http://[SERVER_HOST]FiwareRepository/v2/collec/ Create a resource with specified metadata.
* PUT  http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}.meta  Replace metadata of a resource.
* PUT  http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}  Replace content of a resource.
* DELETE  http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource} Delete a resource

.. note::
   Every resource name must comply the regular expresion ``"[a-zA-Z0-9._-]+"``.

Getting resources metadata
--------------------------

* Request

    * Verb: GET
    * URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}.meta
    * Accept: application/json, application/xml, text/plain

* Responses

+-----------+-----------------------+--------------------------------------------------------------------------------+
| HTTP Code | Type                  | Description                                                                    |
+===========+=======================+================================================================================+
| 200       | OK                    | Your request has been completed properly.                                      |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 404       | Not Found             | The resource with the given path has not been found.                           |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 406       | Not Acceptable        | The resource can not be generated in the given format.                         |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 500       | Internal Server Error | There was an internal error in the system so your request cannot be completed. |
+-----------+-----------------------+--------------------------------------------------------------------------------+


Getting resources
-----------------

* Request

    * Verb: GET
    * URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}
    * Accept: application/json, application/xml, application/rdf+xml, text/turtle, application/x-turtle, text/n3, text/rdf+n3, text/n-triples, text/plain

* Responses

+-----------+-----------------------+--------------------------------------------------------------------------------+
| HTTP Code | Type                  | Description                                                                    |
+===========+=======================+================================================================================+
| 200       | OK                    | Your request has been completed properly.                                      |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 204       | No Content            | Your request has been processed, but the resource has not got content.         |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 404       | Not Found             | The resource with the given path has not been found.                           |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 406       | Not Acceptable        | The resource content can not be generated in the given format.                 |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 500       | Internal Server Error | There was an internal error in the system so your request cannot be completed. |
+-----------+-----------------------+--------------------------------------------------------------------------------+

Creating resources
------------------

* Request

    * Verb: POST
    * URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}
    * Content-Type: application/json, application/xml
    * Body: ::
    
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <resource>
            <creator>Creator</creator>
            <creationDate></creationDate>
            <modificationDate></modificationDate>
            <name>TestResource</name>
            <contentUrl>http://testresourceurl.com/resource</contentUrl>
            <contentFileName>resourceFileName</contentFileName>
        </resource>
    
    
    * or ::
    
        {
            "type":"resource",
            "creator":"Creator",
            "creationDate":"",
            "modificationDate":"",
            "name":"TestResource",
            "contentUrl":"http://testresourceurl.com/resource",
            "contetnFileName":"resourceFileName"
        }

* Responses

+-----------+------------------------+-------------------------------------------------------------------------------------------------------------------------------+
| HTTP Code | Type                   | Description                                                                                                                   |
+===========+========================+===============================================================================================================================+
| 201       | Created                | The resource has been created. The `location` header will contain the final URL that users can use to access this collection. |
+-----------+------------------------+-------------------------------------------------------------------------------------------------------------------------------+
| 400       | Bad Request            | The resource is not well formed or collection name is not valid.                                                              |
+-----------+------------------------+-------------------------------------------------------------------------------------------------------------------------------+
| 409       | Conflict               | The resource with the path and the name given already exist.                                                                  |
+-----------+------------------------+-------------------------------------------------------------------------------------------------------------------------------+
| 415       | Unsopported Media Type | The request entity has a media type which a resource does not support.                                                        |
+-----------+------------------------+-------------------------------------------------------------------------------------------------------------------------------+
| 500       | Internal Server Error  | There was an internal error in the system so your request cannot be completed.                                                |
+-----------+------------------------+-------------------------------------------------------------------------------------------------------------------------------+


Updating resources metadata
---------------------------

* Request

    * Verb: PUT
    * URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}.meta
    * Content-Type: application/json, application/xml
    * Body: ::
        <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        <resource id="collectionA/collectionB/resource">
            <creator>Creator</creator>
            <creationDate></creationDate>
            <modificationDate></modificationDate>
            <name>Test resource</name>
            <contentFileName>resourceFileName</contentFileName>
        </resource>
        
        
    * or ::
        
        {
           "type":"resource",
           "creator":"Creator",
           "creationDate":"",
           "modificationDate":"",
           "name":"TestResource",
           "contentUrl":"http://testresourceurl.com/resource",
           "contetnFileName":"resourceFileName"
        }


* Responses

+-----------+------------------------+--------------------------------------------------------------------------------+
| HTTP Code | Type                   | Description                                                                    |
+===========+========================+================================================================================+
| 200       | OK                     | Your request has been completed properly.                                      |
+-----------+------------------------+--------------------------------------------------------------------------------+
| 400       | Bad Request            | The request is not well formed or collection name is not valid.                |
+-----------+------------------------+--------------------------------------------------------------------------------+
| 403       | Forbidden              | The request was a valid request, but the server is refusing to respond to it.  |
+-----------+------------------------+--------------------------------------------------------------------------------+
| 409       | Conflict               | The resource with the path and the name given already exist.                   |
+-----------+------------------------+--------------------------------------------------------------------------------+
| 415       | Unsopported Media Type | The request entity has a media type which a resource does not support.         |
+-----------+------------------------+--------------------------------------------------------------------------------+
| 500       | Internal Server Error  | There was an internal error in the system so your request cannot be completed. |
+-----------+------------------------+--------------------------------------------------------------------------------+


Updating resources content
--------------------------

* Request 

    * Verb: PUT
    * URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}
    * Content-Type: application/json, application/xml, application/rdf+xml, text/turtle, application/x-turtle, text/n3, text/rdf+n3, text/n-triples, text/plain

* Responses

+-----------+------------------------+---------------------------------------------------------------------------------+
| HTTP Code | Type                   | Description                                                                     |
+===========+========================+=================================================================================+
| 200       | OK                     | Your request has been completed properly.                                       |
+-----------+------------------------+---------------------------------------------------------------------------------+
| 400       | Bad Request            | The resource content is not well formed.                                        |
+-----------+------------------------+---------------------------------------------------------------------------------+
| 404       | Not Found              | The resource with the given path has not been found.                            |
+-----------+------------------------+---------------------------------------------------------------------------------+
| 415       | Unsopported Media Type | The request entity has a media type which the resource content does not support.|
+-----------+------------------------+---------------------------------------------------------------------------------+
| 500       | Internal Server Error  | There was an internal error in the system so your request cannot be completed.  |
+-----------+------------------------+---------------------------------------------------------------------------------+

Deleting resources
------------------

* Request

    * Verb: PUT
    * URI: http://[SERVER_HOST]/FiwareRepository/v2/collec/{collection}/{resource}

* Responses

+-----------+-----------------------+--------------------------------------------------------------------------------+
| HTTP Code | Type                  | Description                                                                    |
+===========+=======================+================================================================================+
| 204       | No Content            | The resource and all its content has been deleted.                             |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 404       | Not Found             | The resource with the given path has not been found.                           |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 500       | Internal Server Error | There was an internal error in the system so your request cannot be completed. |
+-----------+-----------------------+--------------------------------------------------------------------------------+


Making SPARQL queries
=====================

Following you can find the quering operations that can be executed.

* GET  http://[SERVER_HOST]/FiwareRepository/v2/services/query/{contentUrl}
* GET  http://[SERVER_HOST]/FiwareRepository/v2/services/query?query=[Query] Execute a query in the triple store.
* POST http://[SERVER_HOST]/FiwareRepository/v2/services/query | Execute a long query in the triple store.


Getting a resource by Url Content
---------------------------------

* Request

    * Verb: GET
    * URI: http://[SERVER_HOST]/FiwareRepository/v2/services/query/{contentUrl}
    * Accept: application/json, application/xml, application/rdf+xml, text/turtle, application/x-turtle, text/n3, text/rdf+n3, text/n-triples, text/plain

* Responses

+-----------+-----------------------+--------------------------------------------------------------------------------+
| HTTP Code | Type                  | Description                                                                    |
+===========+=======================+================================================================================+
| 200       | OK                    | Your request has been completed properly.                                      |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 204       | No Content            | Your request has been processed, but the resource has not got content.         |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 404       | Not Found             | The resource with the given path has not been found.                           |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 406       | Not Acceptable        | The resource content can not be generated in the given format.                 |
+-----------+-----------------------+--------------------------------------------------------------------------------+
| 500       | Internal Server Error | There was an internal error in the system so your request cannot be completed. |
+-----------+-----------------------+--------------------------------------------------------------------------------+

Executing a short SPARQL query
------------------------------

* Request

    * Verb: GET
    * URI: http://[SERVER_HOST]/FiwareRepository/v2/services/query?query=SELECT+%3Fs+%3Fp+%3Fo+WHERE+%7B%3Fs+%3Fp+%3Fo+%7D
    * Accept: application/json, application/xml, application/rdf+xml, text/turtle, application/x-turtle, text/n3, text/rdf+n3, text/n-triples, text/plain

* Responses

+-----------+-----------------------+--------------------------------------------------------------+
| HTTP Code | Type                  | Description                                                  |
+===========+=======================+==============================================================+
| 200       | OK                    | Your request has been completed properly.                    |
+-----------+-----------------------+--------------------------------------------------------------+
| 400       | Bad Request           | The query is not well formed.                                |
+-----------+-----------------------+--------------------------------------------------------------+
| 406       | Not Acceptable        | The query response can not be generated in the given format. |
+-----------+-----------------------+--------------------------------------------------------------+


Executing a long SPARQL query
-----------------------------

* Request

    * Verb: GET
    * URI: http://[SERVER_HOST]/FiwareRepository/v2/services/query
    * Accept: application/json, application/xml, application/rdf+xml, text/turtle, application/x-turtle, text/n3, text/rdf+n3, text/n-triples, text/plain
    * Content-Type: text/plain
    * Body: " ``SELECT ?s ?p ?o WHERE {?s ?p ?o }``"

* Responses

+-----------+------------------------+---------------------------------------------------------------------+
| HTTP Code | Type                   | Description                                                         |
+===========+========================+=====================================================================+
| 200       | OK                     | Your request has been completed properly.                           |
+-----------+------------------------+---------------------------------------------------------------------+
| 400       | Bad Request            | The query is not well formed.                                       |
+-----------+------------------------+---------------------------------------------------------------------+
| 406       | Not Acceptable         | The query response can not be generated in the given format.        |
+-----------+------------------------+---------------------------------------------------------------------+
| 415       | Unsopported Media Type | The request entity has a media type which a query does not support. |
+-----------+------------------------+---------------------------------------------------------------------+



Accessing the Repository with cURL
++++++++++++++++++++++++++++++++++

Creating a resource
===================
* Create a message body and save it to a file named ``resource.xml``. ::

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <resource>
        <creator>Creator</creator>
        <creationDate></creationDate>
        <modificationDate></modificationDate>
        <name>resource</name>
        <contentUrl>http://testresourceurl.com/resource</contentUrl>
        <contentFileName>resourceFileName</contentFileName>
    </resource>


* Send the request to the server. ::

    curl -v -H "Content-Type: application/xml" -X POST --data "@resource.xml" http://[SERVER_HOST]/FiwareRepository/v2/collec/collectionA/collectionB


* Response ::

    * Hostname was NOT found in DNS cache
    *   Trying 127.0.0.1...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > POST /FiwareRepository/v2/collec HTTP/1.1
    > User-Agent: curl/7.35.0
    > Host: localhost:8080
    > Accept: */*
    > Content-Type: application/xml
    > Content-Length: 349
    > 
    * upload completely sent off: 349 out of 349 bytes
    < HTTP/1.1 201 Created
    * Server Apache-Coyote/1.1 is not blacklisted
    < Server: Apache-Coyote/1.1
    < Content-Location: http://testresourceurl.com/resource
    < Content-Length: 0
    < Date: Thu, 28 May 2015 12:12:23 GMT
    < 
    * Connection #0 to host localhost left intact


Updating the content of a resource
==================================

* Create a message body and save it to a file named ``resourceContent.xml``. ::

    <rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:app="http://www.app.fake/app#">

    <rdf:Description
   rdf:about="http://www.app.fake/app/App1">
      <app:name>App1</app:name>
      <app:country>USA</app:country>
      <app:company>Company1</app:company>
      <app:price>0.99</app:price>
      <app:year>2010</app:year>
    </rdf:Description>

    <rdf:Description
    rdf:about="http://www.app.fake/app/App2">
      <app:name>App2</app:name>
      <app:country>Spain</app:country>
      <app:company>Company2</app:company>
      <app:price>0.99</app:price>
      <app:year>2010</app:year>
    </rdf:Description>

    </rdf:RDF>


* Send the request to the server. ::

    curl -v -H "Content-Type: application/rdf+xml" -X PUT --data-binary "@resourceContent.xml" http://localhost:8080/FiwareRepository/v2/collec/collectionA/collectionB/resource     


* Response: ::

    * Hostname was NOT found in DNS cache
    *   Trying 127.0.0.1...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > PUT /FiwareRepository/v2/collec/collectionA/collectionB/resource HTTP/1.1
    > User-Agent: curl/7.35.0
    > Host: localhost:8080
    > Accept: */*
    > Content-Type: application/rdf+xml
    > Content-Length: 645
    > 
    * upload completely sent off: 645 out of 645 bytes
    < HTTP/1.1 200 OK
    * Server Apache-Coyote/1.1 is not blacklisted
    < Server: Apache-Coyote/1.1
    < Content-Length: 0
    < Date: Thu, 28 May 2015 12:41:21 GMT
    < 
    * Connection #0 to host localhost left intact


Getting a resource content
==========================

* Send the request to the server. ::

    curl -v -H "Accept: application/rdf+xml" -X GET http://localhost:8080/FiwareRepository/v2/collec/collectionA/collectionB/resource     


* Response: ::

    * Hostname was NOT found in DNS cache
    *   Trying 127.0.0.1...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > GET /FiwareRepository/v2/collec/collectionA/collectionB/resource HTTP/1.1
    > User-Agent: curl/7.35.0
    > Host: localhost:8080
    > Accept: application/rdf+xml
    > 
    < HTTP/1.1 200 OK
    * Server Apache-Coyote/1.1 is not blacklisted
    < Server: Apache-Coyote/1.1
    < Content-Type: application/rdf+xml
    < Content-Length: 645
    < Date: Thu, 28 May 2015 12:43:23 GMT
    < 
    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:app="http://www.app.fake/app#">

    <rdf:Description
    rdf:about="http://www.app.fake/app/App1">
      <app:name>App1</app:name>
      <app:country>USA</app:country>
      <app:company>Company1</app:company>
      <app:price>0.99</app:price>
      <app:year>2010</app:year>
    </rdf:Description>

    <rdf:Description
    rdf:about="http://www.app.fake/app/App2">
      <app:name>App2</app:name>
      <app:country>Spain</app:country>
      <app:company>Company2</app:company>
      <app:price>0.99</app:price>
      <app:year>2010</app:year>
    </rdf:Description>

    </rdf:RDF>
    * Connection #0 to host localhost left intact


Deleting a resource
===================

* Send the request to the server. ::

    curl -v -X DELETE http://localhost:8080/FiwareRepository/v2/collec/collectionA/collectionB/resource     


* Response: ::

    * Hostname was NOT found in DNS cache
    *   Trying 127.0.0.1...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > DELETE /FiwareRepository/v2/collec/collectionA/collectionB/resource HTTP/1.1
    > User-Agent: curl/7.35.0
    > Host: localhost:8080
    > Accept: */*
    > 
    < HTTP/1.1 204 No Content
    * Server Apache-Coyote/1.1 is not blacklisted
    < Server: Apache-Coyote/1.1
    < Content-Length: 0
    < Date: Thu, 28 May 2015 12:46:33 GMT
    < 
    * Connection #0 to host localhost left intact  


Executing a Query
=================

* Send the request to the server. ::

    curl -v -H "Accept: application/json" -H "Content-Type: text/plain" -X POST --data "SELECT ?s ?p ?o WHERE {?s ?p ?o} LIMIT 10" http://localhost:8080/FiwareRepository/v2/services/query    


* Response: ::

    * Hostname was NOT found in DNS cache
    *   Trying 127.0.0.1...
    * Connected to localhost (127.0.0.1) port 8080 (#0)
    > POST /FiwareRepository/v2/services/query HTTP/1.1
    > User-Agent: curl/7.35.0
    > Host: localhost:8080
    > Accept: application/json
    > Content-Type: text/plain
    > Content-Length: 41
    > 
    * upload completely sent off: 41 out of 41 bytes
    < HTTP/1.1 200 OK
    * Server Apache-Coyote/1.1 is not blacklisted
    < Server: Apache-Coyote/1.1
    < Content-Type: application/json
    < Transfer-Encoding: chunked
    < Date: Thu, 28 May 2015 13:33:32 GMT
    < 
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


Managing different data formats
+++++++++++++++++++++++++++++++

HTTP content negotiation allows the client to choose the appropriate data format for retrieving meta information about a resource or a collection. Besides XML and JSON the Repository also supports human readable output formats using HTML rendering ('text/html' accept header) including hyperlinked representation and formatted text.

Text Representation
===================

* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}
* Accept Header: text/plain
* Result: ::

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


JSON Representation
===================

* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}
* Accept Header: application/json
* Result: ::

    {
      "resources": [
        {
          "type":"resource",
          "name": "testResource1",
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
          "type":"resource",
          "name": "testResource2",
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
          "type":"resource",
          "name": "testResource3",
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
          "type":"resource",
          "name": "testResource4",
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
          "type":"collection",
          "id": "testCollection\/collectionA",
          "name": "collectionA",
          "creationDate": 1363859273552,
          "creator": "",
          "modificationDate": null
        },
        {
          "resources": [

          ],
          "collections": [

          ],
          "type":"collection"
          "id": "testCollection\/collectionB",
          "name": "collectionB",
          "creationDate": 1363859273566,
          "creator": "",
          "modificationDate": null
        },
        {
          "resources": [

          ],
          "collections": [

          ],
          "type":"collection"
          "id": "testCollection\/",
          "name": "testCollection",
          "creationDate": 1363859273575,
          "creator": "",
          "modificationDate": null
        }
      ],
      "type":"collection"
      "id": "testCollection",
      "creationDate": 1363859199837,
      "creator": "",
      "modificationDate": null
    }


XML Representation
==================

* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}
* Accept Header: application/json
* Result: ::

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <collection id="testCollection" xmlns:atom="http://www.w3.org/2005/Atom">
        <creationDate>2013-03-21T10:46:39.837+01:00</creationDate>
        <creator/>
        <collections>
            <collections id="testCollection/collectionA">
                <creationDate>2013-03-21T10:47:53.552+01:00</creationDate>
                <name>collectionA</name>
                <creator/>
                <collections/>
                <resources/>
            </collections>
            <collections id="testCollection/collectionB">
                <creationDate>2013-03-21T10:47:53.566+01:00</creationDate>
                <name>collectionB</name>
                <creator/>
                <collections/>
                <resources/>
            </collections>
            <collections id="testCollection/">
                <creationDate>2013-03-21T10:47:53.575+01:00</creationDate>
                <name>testCollection</name>
                <creator/>
                <collections/>
                <resources/>
            </collections>
        </collections>
        <resources>
            <resources id="testCollection/testResource1">
                <creationDate>2013-03-21T10:46:39.839+01:00</creationDate>
                <creator/>
                <modificationDate>2013-03-21T10:46:39.839+01:00</modificationDate>
                <contentFileName>filename</contentFileName>
                <contentMimeType>application/rdf+xml</contentMimeType>
                <contentUrl/>
                <name>testResource1</name>
            </resources>
            <resources id="testCollection/testResource2">
                <creationDate>2013-03-21T10:47:53.515+01:00</creationDate>
                <creator/>
                <modificationDate>2013-03-21T10:47:53.515+01:00</modificationDate>
                <contentFileName>filename</contentFileName>
                <contentMimeType>plain/text</contentMimeType>
                <contentUrl/>
                <name>testResource2</name>
            </resources>
            <resources id="testCollection/testResource3">
                <creationDate>2013-03-21T10:47:53.535+01:00</creationDate>
                <creator/>
                <modificationDate>2013-03-21T10:47:53.535+01:00</modificationDate>
                <contentFileName>filename</contentFileName>
                <contentMimeType>text/turtle</contentMimeType>
                <contentUrl/>
                <name>testResource3</name>
            </resources>
            <resources id="testCollection/testResource4">
                <creationDate>2013-03-21T10:47:53.545+01:00</creationDate>
                <creator/>
                <modificationDate>2013-03-21T10:47:53.545+01:00</modificationDate>
                <contentFileName>filename</contentFileName>
                <contentMimeType>application/rdf+xml</contentMimeType>
                <contentUrl/>
                <name>testResource4</name>
            </resources>
        </resources>
    </collection>


Retrieving Resouce RDF content in different formats
+++++++++++++++++++++++++++++++++++++++++++++++++++

When you insert some resource content in any of the accepted RDF formats, it is also possible to retrieve that content in any of the allowed RDF formats. This section explains how to retireve resources in the different supported formats.

XML+RDF Representation
======================

* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}/{resource}
* Accept Header: application/rdf+xml
* Result: ::

    <rdf:RDF
    xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
    xmlns:app="http://www.app.fake/app#">

    <rdf:Description
    rdf:about="http://www.app.fake/app/App1">
        <app:name>App1</app:name>
        <app:country>USA</app:country>
        <app:company>Company1</app:company>
        <app:price>0.99</app:price>
        <app:year>2010</app:year>
        </rdf:Description>

    <rdf:Description
    rdf:about="http://www.app.fake/app/App2">
        <app:name>App2</app:name>
        <app:country>Spain</app:country>
        <app:company>Company2</app:company> 
        <app:price>0.99</app:price>
        <app:year>2010</app:year>
    </rdf:Description>
    </rdf:RDF>


Turtle Representation
=====================

* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}/{resource}
* Accept Header: "text/turtle"
* Result: ::

    @prefix rdfdf: <http://www.openlinksw.com/virtrdf-data-formats#> .
    @prefix owl:   <http://www.w3.org/2002/07/owl#> .
    @prefix fn:    <http://www.w3.org/2005/xpath-functions/#> .
    @prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .
    @prefix yago:  <http://dbpedia.org/class/yago/> .
    @prefix skos:  <http://www.w3.org/2004/02/skos/core#> .
    @prefix ogc:   <http://www.opengis.net/> .
    @prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .
    @prefix ogcgsf: <http://www.opengis.net/def/function/geosparql/> .
    @prefix protseq: <http://purl.org/science/protein/bysequence/> .
    @prefix xslwd: <http://www.w3.org/TR/WD-xsl> .
    @prefix sql:   <sql:> .
    @prefix geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#> .
    @prefix sc:    <http://purl.org/science/owl/sciencecommons/> .
    @prefix sd:    <http://www.w3.org/ns/sparql-service-description#> .
    @prefix ogcsf: <http://www.opengis.net/ont/sf#> .
    @prefix nci:   <http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#> .
    @prefix virtrdf: <http://www.openlinksw.com/schemas/virtrdf#> .
    @prefix xml:   <http://www.w3.org/XML/1998/namespace> .
    @prefix ogcgml: <http://www.opengis.net/ont/gml#> .
    @prefix ogcgsr: <http://www.opengis.net/def/rule/geosparql/> .
    @prefix rdfa:  <http://www.w3.org/ns/rdfa#> .
    @prefix mf:    <http://www.w3.org/2001/sw/DataAccess/tests/test-manifest#> .
    @prefix scovo: <http://purl.org/NET/scovo#> .
    @prefix virtcxml: <http://www.openlinksw.com/schemas/virtcxml#> .
    @prefix dbpprop: <http://dbpedia.org/property/> .
    @prefix foaf:  <http://xmlns.com/foaf/0.1/> .
    @prefix mesh:  <http://purl.org/commons/record/mesh/> .
    @prefix sioc:  <http://rdfs.org/sioc/ns#> .
    @prefix xsl10: <http://www.w3.org/XSL/Transform/1.0> .
    @prefix product: <http://www.buy.com/rss/module/productV2/> .
    @prefix void:  <http://rdfs.org/ns/void#> .
    @prefix dawgt: <http://www.w3.org/2001/sw/DataAccess/tests/test-dawg#> .
    @prefix go:    <http://purl.org/obo/owl/GO#> .
    @prefix dbpedia: <http://dbpedia.org/resource/> .
    @prefix vcard: <http://www.w3.org/2001/vcard-rdf/3.0#> .
    @prefix xsl1999: <http://www.w3.org/1999/XSL/Transform> .
    @prefix xf:    <http://www.w3.org/2004/07/xpath-functions> .
    @prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
    @prefix vcard2006: <http://www.w3.org/2006/vcard/ns#> .
    @prefix ldp:   <http://www.w3.org/ns/ldp#> .
    @prefix math:  <http://www.w3.org/2000/10/swap/math#> .
    @prefix ogcgs: <http://www.opengis.net/ont/geosparql#> .
    @prefix obo:   <http://www.geneontology.org/formats/oboInOwl#> .
    @prefix bif:   <bif:> .
    @prefix dc:    <http://purl.org/dc/elements/1.1/> .

    <http://www.app.fake/app/App1>
            <http://www.app.fake/app#company>
                    "Company1" ;
            <http://www.app.fake/app#country>
                    "USA" ;
            <http://www.app.fake/app#name>  "App1" ;
            <http://www.app.fake/app#price>
                    "0.99" ;
            <http://www.app.fake/app#year>  "2010" .

    <http://www.app.fake/app/App2>
            <http://www.app.fake/app#company>
                    "Company2" ;
            <http://www.app.fake/app#country>
                    "Spain" ;
            <http://www.app.fake/app#name>  "App2" ;
            <http://www.app.fake/app#price>
                    "0.99" ;
            <http://www.app.fake/app#year>  "2010" .


N3 Representation
=================

* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}/{resource}
* Accept Header: "text/n3"
* Result: ::

    @prefix rdfdf: <http://www.openlinksw.com/virtrdf-data-formats#> .
    @prefix owl:   <http://www.w3.org/2002/07/owl#> .
    @prefix fn:    <http://www.w3.org/2005/xpath-functions/#> .
    @prefix xsd:   <http://www.w3.org/2001/XMLSchema#> .
    @prefix yago:  <http://dbpedia.org/class/yago/> .
    @prefix skos:  <http://www.w3.org/2004/02/skos/core#> .
    @prefix ogc:   <http://www.opengis.net/> .
    @prefix rdfs:  <http://www.w3.org/2000/01/rdf-schema#> .
    @prefix ogcgsf: <http://www.opengis.net/def/function/geosparql/> .
    @prefix protseq: <http://purl.org/science/protein/bysequence/> .
    @prefix xslwd: <http://www.w3.org/TR/WD-xsl> .
    @prefix sql:   <sql:> .
    @prefix geo:   <http://www.w3.org/2003/01/geo/wgs84_pos#> .
    @prefix sc:    <http://purl.org/science/owl/sciencecommons/> .
    @prefix sd:    <http://www.w3.org/ns/sparql-service-description#> .
    @prefix ogcsf: <http://www.opengis.net/ont/sf#> .
    @prefix nci:   <http://ncicb.nci.nih.gov/xml/owl/EVS/Thesaurus.owl#> .
    @prefix virtrdf: <http://www.openlinksw.com/schemas/virtrdf#> .
    @prefix xml:   <http://www.w3.org/XML/1998/namespace> .
    @prefix ogcgml: <http://www.opengis.net/ont/gml#> .
    @prefix ogcgsr: <http://www.opengis.net/def/rule/geosparql/> .
    @prefix rdfa:  <http://www.w3.org/ns/rdfa#> .
    @prefix mf:    <http://www.w3.org/2001/sw/DataAccess/tests/test-manifest#> .
    @prefix scovo: <http://purl.org/NET/scovo#> .
    @prefix virtcxml: <http://www.openlinksw.com/schemas/virtcxml#> .
    @prefix dbpprop: <http://dbpedia.org/property/> .
    @prefix foaf:  <http://xmlns.com/foaf/0.1/> .
    @prefix mesh:  <http://purl.org/commons/record/mesh/> .
    @prefix sioc:  <http://rdfs.org/sioc/ns#> .
    @prefix xsl10: <http://www.w3.org/XSL/Transform/1.0> .
    @prefix product: <http://www.buy.com/rss/module/productV2/> .
    @prefix void:  <http://rdfs.org/ns/void#> .
    @prefix dawgt: <http://www.w3.org/2001/sw/DataAccess/tests/test-dawg#> .
    @prefix go:    <http://purl.org/obo/owl/GO#> .
    @prefix dbpedia: <http://dbpedia.org/resource/> .
    @prefix vcard: <http://www.w3.org/2001/vcard-rdf/3.0#> .
    @prefix xsl1999: <http://www.w3.org/1999/XSL/Transform> .
    @prefix xf:    <http://www.w3.org/2004/07/xpath-functions> .
    @prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
    @prefix vcard2006: <http://www.w3.org/2006/vcard/ns#> .
    @prefix ldp:   <http://www.w3.org/ns/ldp#> .
    @prefix math:  <http://www.w3.org/2000/10/swap/math#> .
    @prefix ogcgs: <http://www.opengis.net/ont/geosparql#> .
    @prefix obo:   <http://www.geneontology.org/formats/oboInOwl#> .
    @prefix bif:   <bif:> .
    @prefix dc:    <http://purl.org/dc/elements/1.1/> .

    <http://www.app.fake/app/App1>
            <http://www.app.fake/app#company>
                    "Company1" ;
            <http://www.app.fake/app#country>
                    "USA" ;
            <http://www.app.fake/app#name>  "App1" ;
            <http://www.app.fake/app#price>
                    "0.99" ;
            <http://www.app.fake/app#year>  "2010" .

    <http://www.app.fake/app/App2>
            <http://www.app.fake/app#company>
                    "Company2" ;
            <http://www.app.fake/app#country>
                    "Spain" ;
            <http://www.app.fake/app#name>  "App2" ;
            <http://www.app.fake/app#price>
                    "0.99" ;
            <http://www.app.fake/app#year>  "2010" .


N-Triples Representation
========================

* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}/{resource}
* Accept Header: "text/n-triples"
* Result: ::

    <http://www.app.fake/app/App1> <http://www.app.fake/app#company> "Company1" .
    <http://www.app.fake/app/App2> <http://www.app.fake/app#company> "Company2" .
    <http://www.app.fake/app/App1> <http://www.app.fake/app#country> "USA" .
    <http://www.app.fake/app/App2> <http://www.app.fake/app#country> "Spain" .
    <http://www.app.fake/app/App1> <http://www.app.fake/app#name> "App1" .
    <http://www.app.fake/app/App2> <http://www.app.fake/app#name> "App2" .
    <http://www.app.fake/app/App1> <http://www.app.fake/app#price> "0.99" .
    <http://www.app.fake/app/App2> <http://www.app.fake/app#price> "0.99" .
    <http://www.app.fake/app/App1> <http://www.app.fake/app#year> "2010" .
    <http://www.app.fake/app/App2> <http://www.app.fake/app#year> "2010" .


Retrieving Query response in different formats
++++++++++++++++++++++++++++++++++++++++++++++

When you execute a SPARQL query in the Repository, it genrates different responses depending on the type of query. If you execute a SELECT query, it returns a json object composed by n columns being n number of variables. If you execute a CONSTRUCT or a DESCRIBE query, you can specify the RDF format as described in the previous section.

Executing SELECT query
======================

* Request URL: http://[REPOSITORY_URL]/v2/collec/{collection}/{resource}
* Accept Header: "application/json"
* Body: SELECT ?s ?p ?o WHERE {?s ?p ?o}
* Result: ::

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

