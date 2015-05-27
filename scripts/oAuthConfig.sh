#! /bin/bash

#Modify Repository OAuth2
export X
echo "Do you want to activate OAuth2 authentication in Repository? Y/N"
read X

while [ $X != "Y" ] && [ $X != "y" ] && [ $X != "N" ] && [ $X != "n" ]; do
	echo "Do you want to activate OAuth2 authentication in Repository? Y/N"
	read X
done
if [  $X == "Y" ] || [ $X == "y" ]; then
	sudo sed -i "s/noSecurity/securityOAuth2/g" $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/web.xml
	
	echo "OAuth2 configuration is located in $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/classes/properties/repository.properties."

	echo "What is your FIWAREClient Key?"
	read X
	sudo sed -i "s/FIWAREKEY/$X/g" $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/classes/properties/repository.properties
	
	echo "What is your FIWAREClient Secret?"
	read X
	sudo sed -i "s/FIWARESECRET/$X/g" $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/classes/properties/repository.properties
fi
