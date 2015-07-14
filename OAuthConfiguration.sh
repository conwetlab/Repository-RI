#! /bin/bash

# Configure Repository OAuth2
export REPODIR
export X
echo "-------------------------------------------------"
echo "This script activate, deactivate or update OAuth2 authentication in the Repository."

if [ -f "./apache-tomcat-8.0.22/webapps/FiwareRepository/WEB-INF/web.xml" ]; then
    REPODIR=$PWD/apache-tomcat-8.0.22/webapps/FiwareRepository
elif [ -f "$CATALINA_HOME/webapps/FiwareRepository/WEB-INF/web.xml" ]; then
    REPODIR=$CATALINA_HOME/webapps/FiwareRepository
else

	echo "Where is located Repository-RI? Insert the path:"
	read REPODIR
	while [ ! -f "$REPODIR/WEB-INF/web.xml" ]; do
		echo "Repository-RI is not located in '$REPODIR'"
		echo "Where is located Repository-RI? Insert the path:"
		read REPODIR
	done
fi

echo "Do you want to activate(Y), update info(U), deactivate(N) or cancel(C)?"
read X


while [ $X != "Y" ] && [ $X != "y" ] && [ $X != "N" ] && [ $X != "n" ] && [ $X != "U" ] && [ $X != "u" ] && [ $X != "C" ] && [ $X != "c" ]; do
    echo "Do you want to activate(Y), update info(U), deactivate(N) or cancel(C)?"
    read X
done

if [  $X == "C" ] || [ $X == "c" ]; then
    echo "Configuration aborted"
    exit 0
fi

echo "Shutting down Tomcat Server"
$CATALINA_HOME/bin/shutdown.sh

WAR=$REPODIR.war
mv $WAR .
rm -rf $REPODIR
unzip FiwareRepository.war -d temp

if [  $X == "Y" ] || [ $X == "y" ] || [ $X == "U" ] || [ $X == "u" ]; then

    # Activate oauth2 and configure
    if [ $X != "U" ] || [ $X != "u" ]; then
        echo "OAuth2 configuration is located in /etc/default/Repository-RI.properties."
        sed -i "s/noSecurity/securityOAuth2/g" temp/WEB-INF/web.xml
    fi

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
        sudo sed -i "/oauth2.server=/c\oauth2.server=$X" /etc/default/Repository-RI.properties
    fi

    echo "What is your FIWARE Client id?"
    read X
    sed -i "/oauth2.key=/c\oauth2.key=$X" /etc/default/Repository-RI.properties

    echo "What is your FIWARE Client Secret?"
    read X
    sed -i "/oauth2.secret=/c\oauth2.secret=$X" /etc/default/Repository-RI.properties

    echo "What is your Callback URL?"
    read X
    sed -i "/oauth2.callbackURL=/c\oauth2.callbackURL=$X" /etc/default/Repository-RI.properties

elif [  $X == "N" ] || [ $X == "n" ]; then
    # Desactivate oauth2
    echo "OAuth2 configuration is located in /etc/default/Repository-RI.properties."
    sed -i "s/securityOAuth2/noSecurity/g" temp/WEB-INF/web.xml
fi

cd temp
zip -r FiwareRepositoryTemp.war *
mv ./FiwareRepositoryTemp.war ../FiwareRepository.war
cd ..
rm -rf temp

mv FiwareRepository.war $WAR

echo "Startting up Tomcat"
$CATALINA_HOME/bin/startup.sh


