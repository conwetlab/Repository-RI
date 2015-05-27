# Repository Installation and Administration Guide

The purpose of this document is to describe how to install and administrate the software necessary to run the Repository on a server.
The Repository itself is a Java Web Application, packaged in a WAR file and relies on a MongoDB NoSQL database system.

## Background and Detail

This Installation and Administration Guide relates to the Repository GE which is part of the Applications and Services chapter.
Please find more information about this Generic Enabler in its Open Specification.

## System Requirements
This section covers the requirements needed to install and use the Repository.

### Hardware Requirements
The following table contains the minimum resource requirements for running the Repository: 

CPU: 1-2 cores with at least 2.4 GHZ</br>
Physical RAM: 1G-2GB</br>
Disk Space: 25GB The actual disk space depends on the amount of data being stored within the Repositories NoSQL database System.

### Operating System Support
The Repository has been tested against the following Operating Systems:

* Ubuntu 12.04, 14.04
* CentOS 6.3, 6.5, 7.0

### Software Requirements 
In order to have the Repository running, the following software is needed. However, these dependencies are not meant to be installed manually in this step, as they will be installed throughout the documentation:

* MongoDB 2.x - mandatory
* Java 1.8.x - mandatory
* Virtuoso 7.x - mandatory
* Application Server, Apache Tomcat 8.x - mandatory
* Repository Software - mandatory
* Mongo Shell - optional (JavaScript shell that allows you to execute commands on the internal data store of the Repository from the command line)

## Software Installation

### Installing the Repository using scripts

In order to facilitate the installation of the Repository, the script *install.sh* has been provided. This script install all needed dependencies, configures the repository and deploys it. To use this script execute the following command:

    $ ./install.sh
    
The install script will ask you want to install some dependencies which are needed by Virtuoso installation, and about configuration of OAtuh2 authentication.

### Manually installing the Repository

#### Ubuntu/Debian

All the mandatory dependencies can be easily installed on a debian based Linux distribution using diferent scripts:

<pre>
 # export INSPWD=$PWD
 # ./installTomcat8.sh
 # ./installJavaDebian.sh
 # ./installVirtuoso.sh
 # apt-get install mongodb
</pre>

The variable <code>INSPWD</code> contains the path where the repository (Virtuoso, and Tomcat) has been installed.

#### CentOS/RedHat

The different dependencies can be installed in CentOS/RedHat using *yum*.

<pre>
# export INSPWD=$PWD
# ./installTomcat8.sh
# wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u25-b17/jdk-8u25-linux-x64.rpm"
# sudo rpm -ivh jdk-8u25-linux-x64.rpm
# ./installVirtuoso.sh
# ./installMongoCentos.sh
</pre>

## Configuration

### Virtuoso 7 Configuration

The first step is to create and configurate the Virtuoso database to store RDF content. You may need to have root permissions to do that.

<pre>
 $ cd $INSPWD/virtuoso7/var/lib/virtuoso/db/
 $ $INSPWD/virtuoso7/bin/virtuoso-t -f &
 $ cd $INSPWD
</pre>

This permits yo to start the Virtuoso database. To make avanced configuration you can edit the file <code>$INSPWD/virtuoso7/var/lib/virtuoso/db/virtuoso.ini</code> by your own.

### MongoDB Configuration

The next step is to create the Repository internal database named e.g. "test". You may need to have root permissions to do that. 

<pre>
 $ mongo
 $ use test
</pre>

As default the Database saves its data in <code>/var/lib/mongodb</code>. Since all the Resources you upload to the tepository are stored there, the size of this folder can grow rapidly.
If you want to relocate that folder, you have to edit <code>/etc/mongodb.conf</code>

<pre>
# mongodb.conf

# Where to store the data.
dbpath=/var/lib/mongodb
</pre>

### Tomcat 8 Configuration

To continue, the next step is to start and to configurate Tomcat 8. You may need to have root permissions to do that.

<pre>
 $ cd $INSPWD/apache-tomcat-8.0.22/bin/
 $ ./shutdown.sh
 $ ./startup.sh
 $ cd
</pre>

To start Apache Tomcat 8 is necesary have some variables well configurated like <code>CATALINA_HOME, JAVA_HOME</code>. Maybe you will need configurate them if you make manual install. 

### Application Server Configuration

It is possible to use the Apache Tomcat Application server as is, that is, without any further configuration. However, it is recommended to allow incoming connections to the Repository only through HTTPS. 
This can be achieved by using a front-end HTTPS server that will proxy all requests to Repository, or by configuring the Application Server in order to accept only HTTPS/SSL connection, please refer to [http://tomcat.apache.org/tomcat-8.0-doc/ssl-howto.html this link] for more information.
 
### Repository Configuration

Now you can deploy the Repository software to your Application Server. For that you have to copy the Repository WAR package into the "webapp" folder of Apache Tomcat. 
To install it on other Java Application Servers (e.g. JBoss), please refer to the specific application server guidelines.

The repository can use OAuth2 authentication with Fiware Lab accounts. When install script finish, it ask you if you want to activate OAuth2 authentication, and if you answer yes, they ask you Client ID and Client Secret (you have to register an aplication in your Fiware Lab acount).

If you want change the repository authentication system, you must edit repository.propierties and web.xml files located in <code></code> and <code></code>. The properties oauth2.key and oauth2.secret represent Fiware Client ID and Fiware Client Secret. If you want do not use authentication, you must replace in <code>web.xml</code> the file <code>securityOAuth2.xml</code> by <code>noSecurity.xml</code>.

## Sanity check procedures
The Sanity Check Procedures are those activities that a System Administrator has to perform to verify that an installation is ready to be tested. 
Therefore there is a preliminary set of tests to ensure that obvious or basic malfunctioning is fixed before proceeding to unit tests, integration tests and user validation.

### End to End testing

Although one End to End testing must be associated to the Integration Test, we can show here a quick testing to check that everything is up and running.
The first test step involves creating a new resource as well as the implicit creation of a collection. The second test step checks if meta information in different file formats can be obtained.

**Step 1 - Create the Resource**
Create a file named resource.txt with resource content like this.
<pre>
```xml
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<resource id="collectionA/collectionB/ResourceExample">
	<creator>Yo</creator>
	<creationDate></creationDate>
	<modificationDate></modificationDate>
	<name>Resource Example</name>
	<contentUrl>http://localhost:8080/FiwareRepository/v2/collec/collectionA/collectionB/ResourceExample</contentUrl>
	<contentFileName>http://whereistheresource.com/ResourceExample</contentFileName>
</resource>
```
</pre>

<pre>
curl -v -H "Content-Type: application/xml" -X POST --data "@resource.txt" http://[SERVER_URL]:8080/FiwareRepository/v2/collec/
</pre>

You should receive a HTTP/1.1 201 as status code

Create a file named resourceContent.txt with rbitrary content.

<pre>
curl -v -H "Content-Type: text/plain" -X PUT --data "@resourceContent.txt" http://localhost:8080/FiwareRepository/v2/collec/collectionA/collectionB/ResourceExample
</pre>

You should receive a HTTP/1.1 200 as status code

**Step 2 - Retrieve meta information**

Test HTML Response:

Open <code>
http://[SERVER_URL]:8080/FiwareRepository/v2/collec/collectionA/</code> in your web browser. You should receive meta information about the implicit created collection in HTML format.

Test Text Response:
<pre>
curl -v -H "Content-Type: text/plain" -X GET http://[SERVER_URL]:8080/FiwareRepository/v2/collectionA/collectionB/ResourceExample
</pre>


You should receive meta information about the implicit created collection in text format. 
You may use curl to also test the other supported content types (''application/json,application/rdf+xml,text/turtle,text/n3,text/html,text/plain,application/xml'')

### List of Running Processes

You can execute the following command to check whether the Tomcat web server and the MongoDB database are running:

<pre>
ps -ax | grep 'tomcat\|mongo\|virtuoso-t'
</pre>

The resulting output should show a message text similar to the following:

<pre>
$ ps -ax | grep 'tomcat\|mongo\|virtuoso-t'

  646 ?        Ssl    0:53 /usr/bin/mongod --config /etc/mongodb.conf
 1100 ?        Sl     0:12 /opt/bitnami/java/bin/java -Djava.util.logging.config.file=/opt/bitnami/apache-tomcat/conf/logging.properties 
            -XX:MaxPermSize=512m -Xms256m -Xmx512m -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager 
            -Djava.endorsed.dirs=/opt/bitnami/apache-tomcat/endorsed -classpath /opt/bitnami/apache-tomcat/bin/bootstrap.jar 
            -Dcatalina.base=/opt/bitnami/apache-tomcat -Dcatalina.home=/opt/bitnami/apache-tomcat 
            -Djava.io.tmpdir=/opt/bitnami/apache-tomcat/temp org.apache.catalina.startup.Bootstrap start
 3824 pts/3    S+     0:00 grep --color=auto tomcat\|mongo


</pre>

### Network interfaces Up & Open
To check the ports in use and listening, execute the command:

<pre>
$ sudo netstat -ltp
</pre>

The produced output must be somehow similar to the following:

<pre>
Active Internet connections (only servers)
Proto Recv-Q Send-Q Local Address           Foreign Address         State       PID/Program name
tcp        0      0 localhost:28017         *:*                     LISTEN      646/mongod
tcp        0      0 localhost:27017         *:*                     LISTEN      646/mongod
tcp        0      0 localhost:1111          *:*                     LISTEN      664/virtuoso-t   
tcp6       0      0 [::]:8009               [::]:*                  LISTEN      1100/java
tcp6       0      0 [::]:8080               [::]:*                  LISTEN      1100/java   
</pre>

## Diagnosis Procedures
The Diagnosis Procedures are the first steps that a System Administrator has to take in order to locate the source of an error in a GE implementation. Once the nature of the error is identified by these tests, the system admin very often hasto resort to more concrete and specific testing in order to pinpoint the exact point of error and a possible solution. Such specific testing is out of the scope of this section.
The following sections have to be filled in with the information or an “N/A” (“Not Applicable”) where needed.

### Resource availability
The resource load of the Repository strongly depends on the number of concurrent requests received as well as free main memory and disk space:
* mimimum available main memory: 256 MB
* mimimum available hard disk space: 10 GB

### Remote Service Access
N/A 

### Resource consumption
Resource consumption strongly depends on the load, especially on the number of concurrent requests.

* the main memory consumption of the Tomcat application server should be between 48MB and 1024MB. These numbers can vary significanatly if you use a different application server.

### I/O flows
The only expected I/O flow is of type HTTP or HTTPS, on ports defined in Apache Tomcat configuration files, inbound and outbound. Requests interactivity should be low.

