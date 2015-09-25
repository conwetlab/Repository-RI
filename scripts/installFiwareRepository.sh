#! /bin/bash

## Install necessary tools
echo "Installing necessary tools."
echo "-------------------------------------------------------------------------"
./scripts/installTools.sh

## Install Java 8 version
echo "Installing Java 8 (JDK)."
echo "-------------------------------------------------------------------------"
./scripts/installJava8.sh

## Install Tomcat 8
echo "Installing Apache Tomcat 8."
echo "-------------------------------------------------------------------------"
./scripts/installTomcat8.sh

## Install Virtuoso
echo "Installing Openlink Virtuoso 7."
echo "-------------------------------------------------------------------------"
./scripts/installVirtuoso7.sh

## Install MongoDB
echo "Installing MongoDB."
echo "-------------------------------------------------------------------------"
./scripts/installMongoDB.sh

sudo ./scripts/addRepositorySettings.sh

## Installing Fiware Repository-RI
if [ -d "$INSPWD/src" ]; then
	# Installation from source code
	mvn clean install -DskipTests
	cp ./target/FiwareRepository.war ./FiwareRepository.war
fi

./scripts/oAuthConfig.sh
cp ./FiwareRepository.war $INSPWD/apache-tomcat/webapps/FiwareRepository.war

## Starting Fiware Repository
cd $INSPWD/virtuoso7/var/lib/virtuoso/db/
$INSPWD/virtuoso7/bin/virtuoso-t -f &
cd $INSPWD

cd $INSPWD/apache-tomcat/bin/
./startup.sh
cd $INSPWD

## Creating the Respository-RI Task
./scripts/startupRepository.sh