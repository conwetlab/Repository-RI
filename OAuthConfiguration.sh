#! /bin/bash

# Configure Repository OAuth2
export REPODIR
export X
echo "-------------------------------------------------"
echo "This script activate or desactivate OAuth2 authentication in the Repository."

if [ -f "./apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/classes/properties/repository.properties" ]; then
	REPODIR=$PWD/apache-tomcat-8.0.22/webapps/FiwareRepository
elif [ -f "$CATALINA_HOME/webapps/FiwareRepository/WEB-INF/classes/properties/repository.properties" ]; then
	
else
	echo "Where is located Repository-RI? Insert the path:"
	read REPODIR
	while [ ! -f "$REPODIR/WEB-INF/classes/properties/repository.properties" ]; do
		echo "Repository-RI is not located in '$REPODIR'"
		echo "Where is located Repository-RI? Insert the path:"
		read REPODIR
	done
fi

echo "Do you want to activate(Y), desactivate(N) or cancel(C)?"
read X

echo "Shutting down Tomcat Server"
$CATALINA_HOME/bin/shutdown.sh

while [ $X != "Y" ] && [ $X != "y" ] && [ $X != "N" ] && [ $X != "n" ] && [ $X != "C" ] && [ $X != "c" ]; do
	echo "Do you want to activate(Y), desactivate(N) or cancel(C)?"
	read X
done

if [  $X == "Y" ] || [ $X == "y" ]; then
	# Activate oauth2 and configurate
	echo "OAuth2 configuration is located in $REPODIR/WEB-INF/classes/properties/repository.properties."
	sed -i "s/noSecurity/securityOAuth2/g" $REPODIR/WEB-INF/web.xml

	echo "What is your FIWAREClient Key?"
	read X
	sed -i "/oauth2.key=/c\oauth2.key=$X" $REPODIR/WEB-INF/classes/properties/repository.properties
	
	echo "What is your FIWAREClient Secret?"
	read X
	sed -i "/oauth2.secret=/c\oauth2.secret=$X" $REPODIR/WEB-INF/classes/properties/repository.properties

	echo "What is your Callback URL?"
	read X
	sed -i "/oauth2.callbackURL=/c\oauth2.callbackURL=$X" $REPODIR/WEB-INF/classes/properties/repository.properties

elif [  $X == "N" ] || [ $X == "n" ]; then
	# Desactivate oauth2
	echo "OAuth2 configuration is located in $REPODIR/WEB-INF/classes/properties/repository.properties."
	sed -i "s/securityOAuth2/noSecurity/g" $REPODIR/WEB-INF/web.xml

elif [  $X == "C" ] || [ $X == "c" ]; then
	echo "Configuration aborted"
fi

"Startting up Tomcat"
$CATALINA_HOME/bin/startup.sh


