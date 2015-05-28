#! /bin/bash

# Configure Repository OAuth2
export X
echo "Do you want to activate OAuth2 authentication in the Repository? Y/N"
read X

while [ $X != "Y" ] && [ $X != "y" ] && [ $X != "N" ] && [ $X != "n" ]; do
	echo "Do you want to activate OAuth2 authentication in the Repository? Y/N"
	read X
done

if [  $X == "Y" ] || [ $X == "y" ]; then
	sudo sed -i "s/noSecurity/securityOAuth2/g" $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/web.xml
	
	echo "OAuth2 configuration is located in $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/classes/properties/repository.properties."

    echo "The default OAuth2 enpoint is http://account.lab.fiware.org"
    echo "Do you want to provide a different idm enpoint? Y/N"
    read O

    while [ $O != "Y" ] && [ $O != "y" ] && [ $O != "N" ] && [ $O != "n" ]; do
        echo "Do you want to provide a different idm enpoint? Y/N"
        read O
    done

    if [  $O == "Y" ] || [ $O == "y" ]; then
        echo "What is the identity manager endpoint?"
        read X
        sudo sed -i "s/https\:\/\/account\.lab\.fiware\.org/$X/g" $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/classes/properties/repository.properties
    fi

	echo "What is your FIWARE Client id?"
	read X
	sudo sed -i "s/FIWAREKEY/$X/g" $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/classes/properties/repository.properties
	
	echo "What is your FIWARE Client Secret?"
	read X
	sudo sed -i "s/FIWARESECRET/$X/g" $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/classes/properties/repository.properties
fi
