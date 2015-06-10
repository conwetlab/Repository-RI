#! /bin/bash

# Install python software properties in case the command add-apt-repository do not exists
sudo apt-get install software-properties-common python-software-properties
sudo add-apt-repository ppa:webupd8team/java
sudo apt-get update
sudo apt-get install -y oracle-java8-installer
sudo apt-get install -y oracle-java8-set-default
sudo update-java-alternatives -s java-8-oracle

JAVA_HOME=/usr/lib/jvm/java-8-oracle
export JAVA_HOME

sudo echo "JAVA_HOME=\"/usr/lib/jvm/java-8-oracle\"" | sudo tee -a /etc/environment > /dev/null
