For development in Eclipse use " mvn eclipse:eclipse -Dwtpversion=2.0 "

Repository-RI
=============

New features V2.00:

- Content Header Negotiation
	Supported Types for reading Resource Meta Information or Collection Information:
	- application/json
	- application/rdf+xml
	- text/turtle
	- text/n3
	- text/html
	- application/x-ms-application"
	- text/plain
	- application/xml

___________________________________________________________


1) Install Mongodb and create a database "test".

2) Start Mongo DB:
service mongodb start

3) Start Virtuoso:
cd PATH_TO_VIRTUOSO/var/lib/virtuoso/db/
PATH_TO_VIRTUOSO/bin/virtuoso-t -f &

4)Deploy FiwareRepository.war to a Tomcat 8.x:
PATH_TO_TOMCAT/bin/startup.sh
cp FirewareRepository.war PATH_TO_TOMCAT/webapps/FirewareRepository.war

5) Create a Resource
[PUT] http://[SYSTEM:PORT]/FiwareRepository/v1/collectionA/collectionB/ResourceName

6) Read the Resource
[GET] http://[SYSTEM:PORT]/FiwareRepository/v1/collectionA/collectionB/ResourceName


#### Create Resource

Create Resource Request:
	URL:	[POST] http://[SYSTEM:PORT]/FiwareRepository/v2/collec/
	Header:	{Accept=*}
	Body:	content

Create Resource Response:
	Response Status:
	201
____________________________________________________________

#### Update Resource Meta Data

Create Resource Request:
	URL:	[PUT] http://[SYSTEM:PORT]/FiwareRepository/v2/collec/collectionA/collectionB/ResourceName.meta
	Header:	{Accept=[application/xml],[application/json]}
	Body:	content

Create Resource Response:
	Response Status:
	200
____________________________________________________________

#### Update Resource Content

Create Resource Request:
	URL:	[PUT] http://[SYSTEM:PORT]/FiwareRepository/v2/collec/collectionA/collectionB/ResourceName
	Header:	{Accept=[application/xml],[application/json]}
	Body:	content

Create Resource Response:
	Response Status:
	200
____________________________________________________________

#### Get Resource 

Get Resource Request:
	URL:	[GET] http://[SYSTEM:PORT]/FiwareRepository/v2/collec/collectionA/collectionB/ResourceName
	Header:	{Accept=*}

	
Get Resource Response:
	Response Status:
	200
____________________________________________________________

#### Get Resource Meta Data

Get Resource Meta Data Request:
	URL:	[GET] http://[SYSTEM:PORT]/FiwareRepository/v1/collectionA/collectionB/ResourceName.meta
	Header:	{Accept=[application/xml],[application/json]}
	
Get Resource Meta Data Response:
	Response Status:
	200
____________________________________________________________

#### Delete Resource

Delete Resource Request:
	URL:	[DELETE] http://[SYSTEM:PORT]/FiwareRepository/v1/collectionA/collectionB/ResourceName
	Header:	{Accept=*}
	
Delete Resource Response:
	Response Status:
	204
____________________________________________________________

#### Create Collection

Update Collection Request:
	URL:	[POST] http://[SYSTEM:PORT]/FiwareRepository/v2/collec/
	Header:	{Accept=[application/xml],[application/json]}
	Body:	<?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection xmlns:atom="http://www.w3.org/2005/Atom"><creator>CreatornameUpdate</creator><collections/><resources/></collection>

#### Update Collection Response:
	Response Status:
	201
____________________________________________________________

#### Get Collection

Get Collection Request:
	URL:	[GET] http://[SYSTEM:PORT]/FiwareRepository/v2/collec/collectionA/collectionB
	Header:	Accept=[application/xml],[application/json]}

Get Collection Response:
	Response Status:
	200
____________________________________________________________

#### Delete Collection

Delete CollectionRequest:
	URL:	[DELETE] http://[SYSTEM:PORT]/FiwareRepository/v2/collec/collectionA
	Header:	{Accept=*}
	
Delete Collection Response:
	Response Status:
	204
____________________________________________________________

#### Execute Short Query 

Delete CollectionRequest:
	URL:	[GET] http://[SYSTEM:PORT]/FiwareRepository/v2/services/query?query=[QUERY]
	Header:	{Accept=*}
	
Delete Collection Response:
	Response Status:
	200
____________________________________________________________

#### Execute Long Query  

Delete CollectionRequest:
	URL:	[POST] http://[SYSTEM:PORT]/FiwareRepository/v2/services/query?query=[QUERY]
	Header:	{Accept=*}
	Body:   [QUERY]
	
Delete Collection Response:
	Response Status:
	200
____________________________________________________________	

#### Get Resource by URL Content

Delete CollectionRequest:
	URL:	[POST] http://[SYSTEM:PORT]/FiwareRepository/v2/services/query/urlContent
	Header:	{Accept=*}
	Body:   [QUERY]
	
Delete Collection Response:
	Response Status:
	200
____________________________________________________________	