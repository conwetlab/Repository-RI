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

	unzip FiwareRepository.war -d temp 	

	sudo sed -i "s/noSecurity/securityOAuth2/g" $INSPWD/temp/WEB-INF/web.xml

	echo "OAuth2 configuration is located in $INSPWD/apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/classes/properties/repository.properties"

    echo "The default OAuth2 enpoint is http://account.lab.fiware.org"
    echo "Do you want to provide a different idm enpoint? Y/N"
    read O

    while [ $O != "Y" ] && [ $O != "y" ] && [ $O != "N" ] && [ $O != "n" ]; do
        echo "Do you want to provide a different idm enpoint? Y/N"
        read O
    done

    if [ $O == "Y" ] || [ $O == "y" ]; then
        echo "What is the identity manager endpoint?"
        read X
        sudo sed -i "/oauth2.server=/c\oauth2.server=$X" $INSPWD/temp/WEB-INF/classes/properties/repository.properties
    fi

    echo "What is your FIWARE Client id?"
	read X
	sudo sed -i "/oauth2.key=/c\oauth2.key=$X" $INSPWD/temp/WEB-INF/classes/properties/repository.properties

	echo "What is your FIWARE Client Secret?"
	read X
	sudo sed -i "/oauth2.secret=/c\oauth2.secret=$X" $INSPWD/temp/WEB-INF/classes/properties/repository.properties

	echo "What is your Callback URL?"
	read X
	sed -i "/oauth2.callbackURL=/c\oauth2.callbackURL=$X" $INSPWD/temp/WEB-INF/classes/properties/repository.properties

	cd temp
	zip -r FiwareRepositoryTemp.war *
	mv ./FiwareRepositoryTemp.war ../FiwareRepository.war
	cd ..
	rm -r temp
fi
