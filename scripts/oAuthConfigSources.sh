#! /bin/bash

# Configure Repository OAuth2
export X
echo "-------------------------------------------------"
echo "Do you want to activate OAuth2 authentication in the Repository? Y/N"
read X

while [ $X != "Y" ] && [ $X != "y" ] && [ $X != "N" ] && [ $X != "n" ]; do
	echo "Do you want to activate OAuth2 authentication in the Repository? Y/N"
	read X
done

if [  $X == "Y" ] || [ $X == "y" ]; then
	sudo sed -i "s/noSecurity/securityOAuth2/g" $INSPWD/src/main/webapp/WEB-INF/web.xml
	
	echo "OAuth2 configuration is located in $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/classes/properties/repository.properties"

	echo "What is your FIWAREClient Key?"
	read X
	sudo sed -i "/oauth2.key=/c\oauth2.key=$X" $INSPWD/src/main/resources/properties/repository.properties
	
	echo "What is your FIWAREClient Secret?"
	read X
	sudo sed -i "/oauth2.secret=/c\oauth2.secret=$X" $INSPWD/src/main/resources/properties/repository.properties

	echo "What is your Callback URL?"
	read X
	sed -i "/oauth2.callbackURL=/c\oauth2.callbackURL=$X" $INSPWD/src/main/resources/properties/repository.properties
fi
