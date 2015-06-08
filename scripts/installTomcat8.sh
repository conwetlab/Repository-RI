#! /bin/bash

# Install tomcat 8
wget http://apache.rediris.es/tomcat/tomcat-8/v8.0.22/bin/apache-tomcat-8.0.22.zip
unzip apache-tomcat-8.0.22.zip

export CATALINA_HOME=$INSPWD/apache-tomcat-8.0.22
echo "export CATALINA_HOME=\"$INSPWD/apache-tomcat-8.0.22\"" >> ~/.bashrc

chmod +x $INSPWD/apache-tomcat-8.0.22/bin/*.sh
$INSPWD/apache-tomcat-8.0.22/bin/startup.sh
