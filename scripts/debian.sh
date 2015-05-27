#! /bin/bash

sudo apt-get update
sudo apt-get install unzip

unzip Repository-RI-3.2.2.zip

set +e
# Install java 8
./installJavaDebian.sh
   
# Install tomcat 8
./installTomcat8.sh

# Install mongodb
sudo apt-get install -y mongodb

# PreInstallVirtuoso
sudo ./preVirtuosoDebian.sh

# Install virtuoso
./installVirtuoso.sh

# Deploy the war file
cp ./Repository-RI-3.2.2/FiwareRepository.war $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository.war
cd $INSPWD

#Modify Repository OAuth2
./oAuthConfig.sh

#Start Virtuoso
cd $INSPWD/virtuoso7/var/lib/virtuoso/db/
$INSPWD/virtuoso7/bin/virtuoso-t -f &
cd $INSPWD

#Start Tomcat
cd $INSPWD/apache-tomcat-8.0.22/bin/
./shutdown.sh
./startup.sh
cd

#Create taks
sudo ./startup.sh
