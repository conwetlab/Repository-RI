#! /bin/bash

sudo yum update
sudo yum -y install unzip
    
set +e
# Install java 8
./scripts/installJavaCentos.sh
cd $INSPWD

# Install tomcat 8
./scripts/installTomcat8.sh

# PreInstall Virtuoso
sudo ./scripts/preVirtuosoCentos.sh

# Install virtuoso
./scripts/installVirtuoso.sh

set -e

# Install mongodb
sudo ./scripts/installMongoCentos.sh    

# Deploy the war file
if [ -f "./target/FiwareRepository.war" ]; then
    cp ./target/FiwareRepository.war $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository.war
else
    cp ./FiwareRepository.war $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository.war
fi

#Modify Repository OAuth2
./scripts/oAuthConfig.sh

#Start Virtuoso
cd $INSPWD/virtuoso7/var/lib/virtuoso/db/
$INSPWD/virtuoso7/bin/virtuoso-t -f &
cd $INSPWD

#Start Tomcat
cd $INSPWD/apache-tomcat-8.0.21/bin/
./shutdown.sh
./startup.sh
cd $INSPWD

#Create taks
sudo ./scripts/startupCentos.sh
