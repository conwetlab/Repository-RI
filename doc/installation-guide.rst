=====================================
Installation and Administration Guide
=====================================

-------------------
System Requirements
-------------------

Hardware Requirements
=====================

The following table contains the minimum resource requirements for running the Repository: 

* CPU: 1-2 cores with at least 2.4 GHZ
* Physical RAM: 1G-2GB
* Disk Space: 25GB The actual disk space depends on the amount of data being stored within the Repositories NoSQL database System.

Operating System Support
========================
The Repository has been tested in the following Operating Systems:

* Ubuntu 12.04, 14.04
* CentOS 6.3, 6.5, 7.0

Software Requirements
===================== 
In order to have the Repository running, the following software is needed. However, these dependencies are not meant to be installed manually in this step, as they will be installed throughout the documentation:

* MongoDB 2.x - mandatory
* Java 1.8.x - mandatory
* Virtuoso 7.x - mandatory
* Application Server, Apache Tomcat 8.x - mandatory
* Repository Software - mandatory
* Mongo Shell - optional (JavaScript shell that allows you to execute commands on the internal data store of the Repository from the command line)

To install the required version of Virtuoso, it is possible to download a compiled version for the suported Operative Systems or it is possible to compile it from the source code. In this way, the installation of Virtuoso has some extra requirements, that will be also installed throughout this document.

* autoconf
* automake
* libtool
* flex
* bison
* gperf
* gawk
* m4
* make
* openssl
* openssl-devel

---------------------
Software Installation
---------------------

Getting the Repository software
===============================

The packaged version of the Repository software can be downloaded from:

* The FIWARE Files page (https://forge.fiware.org/frs/?group_id=7)
* The FIWARE catalgue (http://catalogue.fiware.org/enablers/downloads-10).

This package contains the war file of the Repository as well as the intallation scripts used in this document.

Alternatively, it is possible to install the Repository from the sources published in GitHub. To clone the repository, the git package is needed: ::

    # Ubuntu/Debian
    $ apt-get install git

    # CentOS
    $ yum -y install git

To download the source code usig git, execute the following command: ::

    $ git clone https://github.com/conwetlab/Repository-RI.git

Installing the Repository using scripts
=======================================

In order to facilitate the installation of the Repository, the script *install.sh* has been provided. This script installs all needed dependencies, configures the repository and deploys it. 

Note that the installation script installs dependencies such as Java 1.8 or Tomcat. If you are installing the Repository in a system that is already in used, you may want to have more control over what dependecies are installed. In this case have a look at section *Manually Installing the Repository*.

To use the installation script execute the following command: ::

    $ ./install.sh

The installation script also optionally resolves the extra dependencies that are needed for the installation of Virtuoso.

    Some packages are needed for installing Virtuoso: autoconf, automake, libtoo, flex, bison, gperf, gawk, m4, make, openssl, openssl-devel
    Do you want to install them? Y/N

Finally, the installation script allows to configure the OAuth2 user authentication. ::

    Do you want to activate OAuth2 authentication in the Repository? Y/N
    y
    
    The default OAuth2 enpoint is http://account.lab.fiware.org
    Do you want to provide a different idm enpoint? Y/N
    n
    
    What is your FIWARE Client id?
    [client id]
    
    What is your FIWARE Client Secret?
    [client secret]
    
    What is your Callback URL?
    http://[host]:[port]/FiwareRepository/v2/callback

Debian Manually installing the Repository
=========================================

All the mandatory dependencies can be easily installed on a debian based Linux distribution using diferent scripts: ::

    $ export INSPWD=$PWD
    $ export REPO_OS=debian
    $ ./scripts/installTools.sh
    $ ./scripts/installJava8.sh
    $ ./scripts/installTomcat8.sh
    $ ./scripts/installMongoDB.sh
    $ ./scripts/installVirtuoso7.sh

To install Virtuoso from the source code, it is possible to do it by using a Operative System version not supported: ::

    $ export REPO_OS=""
    $ ./scripts/installVirtuoso7.sh

The variable ``INSPWD`` contains the path where the repository (Virtuoso, and Tomcat) has been installed.

Ubuntu Manually installing the Repository
=========================================

All the mandatory dependencies can be easily installed on a debian based Linux distribution using diferent scripts, and replacing "XX.XX" by Ubuntu version. ::

    $ export INSPWD=$PWD
    $ export REPO_OS=ubuntuXX.XX
    $ ./scripts/installTools.sh
    $ ./scripts/installJava8.sh
    $ ./scripts/installTomcat8.sh
    $ ./scripts/installMongoDB.sh
    $ ./scripts/installVirtuoso7.sh

To install Virtuoso from the source code, it is possible to do it by using a Operative System version not supported: ::

    $ export REPO_OS=""
    $ ./scripts/installVirtuoso7.sh

The variable ``INSPWD`` contains the path where the repository (Virtuoso, and Tomcat) has been installed.

CentOS/RedHat Manually installing the Repository
================================================

Similarly, the different dependencies can be installed in CentOS/RedHat, and replacing "X" by Centos version. ::

    $ export INSPWD=$PWD
    $ export REPO_OS=centosX
    $ ./scripts/installTools.sh
    $ ./scripts/installJava8.sh
    $ ./scripts/installTomcat8.sh
    $ ./scripts/installMongoDB.sh
    $ ./scripts/installVirtuoso7.sh

To install Virtuoso from the source code, it is possible to do it by using a Operative System version not supported: ::

    $ export REPO_OS=""
    $ ./scripts/installVirtuoso7.sh

-------------
Configuration
-------------

This configuration section assumes that the enviroment variable INSPWD exists, this variable is created during the installation process. If it does not exists execute the following command from the directory where the repository have been installed: ::

    $ export INSPWD=$PWD

Please note that if you have used the script *install.sh* you can skip *Virtuoso 7 Configuration* and *Tomcat 8 Configuration* sections, since the specified actions are performed by the script. 
 
Virtuoso 7 Configuration
========================

The first step is to create and configure the Virtuoso database to store RDF content. You may need to have root rights to do that. ::

    $ cd $INSPWD/virtuoso7/var/lib/virtuoso/db/
    $ $INSPWD/virtuoso7/bin/virtuoso-t -f &
    $ cd $INSPWD

This allows you to start the Virtuoso database. To make avanced configuration you can edit the file ``$INSPWD/virtuoso7/var/lib/virtuoso/db/virtuoso.ini`` by your own.

MongoDB Configuration
=====================

By default the Database saves its data in ``/var/lib/mongodb``. Since all the Resources you upload to the Repository are stored there, the size of this folder can grow rapidly.
If you want to relocate that folder, you have to edit ``/etc/mongodb.conf`` ::

    # mongodb.conf

    # Where to store the data.
    dbpath=/var/lib/mongodb

Tomcat 8 Configuration
======================

To continue, the next step is to start and to configurate Tomcat 8. You may need to have root rights to do that. ::

    $ cd $INSPWD/apache-tomcat/bin/
    $ ./shutdown.sh
    $ ./startup.sh
    $ cd

To start Apache Tomcat 8 is necesary to have some variables well configurated like ``CATALINA_HOME, JAVA_HOME``. Maybe you will need configure them if you make a manual installation. 

It is possible to use the Apache Tomcat Application server as is, that is, without any further configuration. However, it is recommended to allow incoming connections to the Repository only through HTTPS. 
This can be achieved by using a front-end HTTPS server that will proxy all requests to Repository, or by configuring the Application Server in order to accept only HTTPS/SSL connection, please refer to http://tomcat.apache.org/tomcat-8.0-doc/ssl-howto.html for more information.


Repository Configuration
========================

If you have installed the Repository manually, you have to deploy the Repository software to your Application Server. For that you have to copy the Repository WAR package into the "webapp" folder of Apache Tomcat. To install it on other Java Application Servers (e.g. JBoss), please refer to the specific application server guidelines.

Also, you have to create a properties file located at ``/etc/default/Repository-RI.properties`` with the configuration of the repository. To create the properties file with basic configuration it is possible use the script ``repositorySettings.sh``.

The repository can use OAuth2 authentication with FIWARE Lab accounts. If you have used the automatic installation script you have been already asked to choose whether you want to use this authentication mechanism and to provide OAuth2 credentials in that case. 

Before enabling OAuth2 authentication in the Repository, it is needed to have registered it on the corresponding idM (KeyRock) instance. 

It is needed to provide:
* A name for the application
* A description
* The URL of the Repository
* The callback URL of the Repository: http://[host]:[port]/FiwareRepository/v2/callback?client_name=FIWAREClient

The OAuth2 authentication can be enabled and disabled modifiying the file ``web.xml`` located at ``WEB-INF/web.xml``.

To enable OAuth2 include ``securityOAuth2.xml`` ::

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>
            /WEB-INF/securityOAuth2.xml
        </param-value>
    </context-param>

To disable OAuth2 include ``noSecurity.xml`` ::
 
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>
            /WEB-INF/noSecurity.xml
        </param-value>
    </context-param>

You can modify OAuth2 credentials in the ``Repository-RI.properties`` file located at ``/etc/default/Repository-RI.properties`` ::

    oauth2.server=https://account.lab.fiware.org
    oauth2.key=[Client id]
    oauth2.secret=[Client secret]
    oauth2.callbackURL=http://[host]/FiwareRepository/v2/callback

.. note::
   If you have decided to use OAuth2 authentication you will need to modify ``oauth2.callbackURL`` property to include the host where the Repository is going to run. 

Finally, you can configure the MongoDB and Virtuoso instances the Repository is going to use in ``Repository-RI.properties``, which contains the following values by default. ::

    #MongoDb Database
    mongodb.host=127.0.0.1
    mongodb.db=test
    mongodb.port=27017

    #Virtuoso Database
    virtuoso.host=jdbc:virtuoso://localhost:
    virtuoso.port=1111
    virtuoso.user=dba
    virtuoso.password=dba

-----------------------
Sanity check procedures
-----------------------

The Sanity Check Procedures are those activities that a System Administrator has to perform to verify that an installation is ready to be tested. 
Therefore there is a preliminary set of tests to ensure that obvious or basic malfunctioning is fixed before proceeding to unit tests, integration tests and user validation.


End to End testing
==================

Although one End to End testing must be associated to the Integration Test, we can show here a quick testing to check that everything is up and running.
The first test step involves creating a new resource as well as the implicit creation of a collection. The second test step checks if meta information in different file formats can be obtained.

Step 1 - Create the Resource
----------------------------

Create a file named resource.xml with resource content like this. ::

    <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
    <resource>
	   <creator>Yo</creator>
	   <creationDate></creationDate>
	   <modificationDate></modificationDate>
	   <name>Resource Example</name>
	   <contentUrl>http://localhost:8080/FiwareRepository/v2/collec/collectionA/collectionB/ResourceExample</contentUrl>
	   <contentFileName>http://whereistheresource.com/ResourceExample</contentFileName>
    </resource>

Send the request: ::

    curl -v -H "Content-Type: application/xml" -X POST --data "@resource.xml" http://[SERVER_URL]:8080/FiwareRepository/v2/collec/

You should receive a HTTP/1.1 201 as status code

Create a file named resourceContent.txt with arbitrary content. ::

    curl -v -H "Content-Type: text/plain" -X PUT --data "@resourceContent.txt" http://localhost:8080/FiwareRepository/v2/collec/collectionA/collectionB/ResourceExample

You should receive a HTTP/1.1 200 as status code


Step 2 - Retrieve meta information
----------------------------------

Test HTML Response:

Open ``http://[SERVER_URL]:8080/FiwareRepository/v2/collec/collectionA/`` in your web browser. You should receive meta information about the implicit created collection in HTML format.

Test Text Response: ::

    curl -v -H "Content-Type: text/plain" -X GET http://[SERVER_URL]:8080/FiwareRepository/v2/collectionA/collectionB/ResourceExample


You should receive meta information about the implicit created collection in text format. 
You may use curl to also test the other supported content types (``application/json``, ``application/rdf+xml``, ``text/turtle``, ``text/n3``, ``text/html``, ``text/plain``, ``application/xml``)
