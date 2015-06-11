#!/bin/bash

sudo yum install -y wget

wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u45-b14/jdk-8u45-linux-x64.tar.gz"

tar xzf jdk-8u45-linux-x64.tar.gz

sudo alternatives --install /usr/bin/java java $INSPWD/jdk1.8.0_45/bin/java 20000

sudo alternatives --config java

sudo alternatives --install /usr/bin/jar jar $INSPWD/jdk1.8.0_45/bin/jar 20000
sudo alternatives --install /usr/bin/javac javac $INSPWD/jdk1.8.0_45/bin/javac 20000
sudo alternatives --set jar $INSPWD/jdk1.8.0_45/bin/jar
sudo alternatives --set javac $INSPWD/jdk1.8.0_45/bin/javac
