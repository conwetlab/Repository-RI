#! /bin/bash

sudo apt-get update
sudo apt-get -y install unzip
sudo apt-get install zip
sudo apt-get -y install maven

set +e
# Install java 8
./scripts/installJavaDebian.sh
   
# Install tomcat 8
./scripts/installTomcat8.sh

# Install mongodb
sudo apt-get install -y mongodb

# Install virtuoso
./scripts/installVirtuosoUbuntu12.04.sh

# Add the file settings
sudo ./scripts/repositorySettings.sh    

# Install the repository from source code or build
if [ -d "$INSPWD/src" ]; then
	# Installation from source code
	./scripts/oAuthConfigSources.sh
	mvn clean install
	cp ./target/FiwareRepository.war $INSPWD/apache-tomcat/webapps/FiwareRepository.war
else
	# Installation from build
	./scripts/oAuthConfig.sh
	cp ./FiwareRepository.war $INSPWD/apache-tomcat/webapps/FiwareRepository.war
fi

#Start Virtuoso
cd $INSPWD/virtuoso7/var/lib/virtuoso/db/
$INSPWD/virtuoso7/bin/virtuoso-t -f &
cd $INSPWD

#Start Tomcat
cd $INSPWD/apache-tomcat/bin/
./startup.sh
cd $INSPWD

#Create startup tasks
sudo ./scripts/startupDebian.sh
