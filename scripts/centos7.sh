#! /bin/bash

sudo yum update
sudo yum -y install unzip
   
unzip Repository-RI-3.2.2.zip
    
set +e
# Install java 8
wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u25-b17/jdk-8u25-linux-x64.rpm"
sudo rpm -ivh jdk-8u25-linux-x64.rpm

# Install tomcat 8
./installTomcat8.sh

# PreInstall Virtuoso
sudo ./preVirtuosoCentos.sh

# Install virtuoso
./installVirtuoso.sh

set -e

# Install mongodb
sudo ./installMongoCentos.sh
    
# Deploy the war file
cp ./Repository-RI-3.2.2/FiwareRepository.war $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository.war
cd $INSPWD

#Modify Repository OAuth2
./oAuthConfig.sh

#Start Virtuoso
cd $INSPWD/virtuoso7/var/lib/virtuoso/db/
$INSPWD/virtuoso7/bin/virtuoso-t -f &
cd

#Start Tomcat
cd $INSPWD/apache-tomcat-8.0.21/bin/
./shutdown.sh
./startup.sh
cd

#Create taks
sudo ./startup.sh
