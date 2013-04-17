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
PATH_TO_MONGO\bin\mongod.exe --dbpath PATH_TO_MONGO\data\db

2)Deploy FiwareRepository.war to a Tomcat 6.x:
tomcat\bin\startup.bat

3) Create a Resource
[PUT] http://localhost:8080/FiwareRepository/v1/collectionA/collectionB/ResourceName

4) Read the Resource
[GET] http://localhost:8080/FiwareRepository/v1/collectionA/collectionB/ResourceName



Create Resource

Create Resource Request:
	URL:	[PUT] http://localhost:8080/FiwareRepository/v1/collectionA/collectionB/ResourceName
	Header:	{Accept=*}
	Body:	[Content]

Create Resource Response:
	Response Status:
	201

____________________________________________________________

Update Resource Meta Data

Create Resource Request:
	URL:	[POST] http://localhost:8080/FiwareRepository/v1/collectionA/collectionB/ResourceName
	Header:	{Accept=[application/xml],[application/json]}
	Body:	content

Create Resource Response:
	Response Status:
	201

____________________________________________________________


Get Resource 

Get Resource Request:
	URL:	[GET] http://localhost:8080/FiwareRepository/v1/collectionA/collectionB/ResourceName
	Header:	{Accept=*}

	
Get Resource Response:
	Response Status:
	200

____________________________________________________________


Get Resource Meta Data

Get Resource Meta Data Request:
	URL:	[GET] http://localhost:8080/FiwareRepository/v1/collectionA/collectionB/ResourceName.meta
	Header:	{Accept=[application/xml],[application/json]}
	
Get Resource Meta Data Response:
	Response Status:
	200
____________________________________________________________


Delete Resource

Delete Resource Request:
	URL:	[DELETE] http://localhost:8080/FiwareRepository/v1/collectionA/collectionB/ResourceName
	Header:	{Accept=*}
	
Delete Resource Response:
	Response Status:
	204

____________________________________________________________


Update Collection

Update Collection Request:
	URL:	[POST] http://localhost:8080/FiwareRepository/v1/collectionY
	Header:	{Accept=[application/xml],[application/json]}
	Body:	<?xml version="1.0" encoding="UTF-8" standalone="yes"?><collection xmlns:atom="http://www.w3.org/2005/Atom"><creator>CreatornameUpdate</creator><collections/><resources/></collection>

Update Collection Response:
	Response Status:
	201
____________________________________________________________


Get Collection

Get Collection Request:
	URL:	GET] http://localhost:8080/FiwareRepository/v1/collectionA/collectionB/
	Header:	Accept=[application/xml],[application/json]}

Get Collection Response:
	Response Status:
	200
____________________________________________________________


Delete Collection

Delete CollectionRequest:
	URL:	[DELETE] http://localhost:8080/FiwareRepository/v1/collectionA
	Header:	{Accept=*}
	
Delete Collection Response:
	Response Status:
	204
____________________________________________________________



