#! /bin/bash

# Install python software properties in case the command add-apt-repository do not exists
sudo sh -c 'echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" >> /etc/apt/sources.list'
sudo sh -c 'echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" >> /etc/apt/sources.list'
sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886
sudo apt-get update
sudo apt-get install -y oracle-java8-installer
sudo apt-get install -y oracle-java8-set-default
sudo update-java-alternatives -s java-8-oracle

JAVA_HOME=/usr/lib/jvm/java-8-oracle
export JAVA_HOME

sudo echo "JAVA_HOME=\"/usr/lib/jvm/java-8-oracle\"" | sudo tee -a /etc/environment > /dev/null
