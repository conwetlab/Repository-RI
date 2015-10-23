#! /bin/bash
export tomcatVersion=8.0.28

# Install tomcat 8
wget http://apache.rediris.es/tomcat/tomcat-8/v$tomcatVersion/bin/apache-tomcat-$tomcatVersion.zip
unzip apache-tomcat-$tomcatVersion.zip

mv apache-tomcat-$tomcatVersion apache-tomcat

export CATALINA_HOME=$INSPWD/apache-tomcat
echo "export CATALINA_HOME=\"$INSPWD/apache-tomcat\"" >> ~/.bashrc

chmod +x $INSPWD/apache-tomcat/bin/*.sh
