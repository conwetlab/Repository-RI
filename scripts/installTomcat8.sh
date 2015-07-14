#! /bin/bash

# Install tomcat 8
wget http://apache.rediris.es/tomcat/tomcat-8/v8.0.24/bin/apache-tomcat-8.0.24.zip
unzip apache-tomcat-8.0.24.zip

mv apache-tomcat-8.0.24 apache-tomcat

export CATALINA_HOME=$INSPWD/apache-tomcat
echo "export CATALINA_HOME=\"$INSPWD/apache-tomcat\"" >> ~/.bashrc

chmod +x $INSPWD/apache-tomcat/bin/*.sh
$INSPWD/apache-tomcat/bin/startup.sh
