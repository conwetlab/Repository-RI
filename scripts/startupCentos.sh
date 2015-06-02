#! /bin/bash

echo "# Repository-RI - regular background program processing daemon
#

description	"regular background program processing daemon"

start on runlevel [2345]
stop on runlevel [!2345]

expect fork
respawn

script
	#Start Virtuoso
	cd $INSPWD/virtuoso7/var/lib/virtuoso/db/
	$INSPWD/virtuoso7/bin/virtuoso-t -f &
	cd

	#Start Tomcat
	cd $INSPWD/apache-tomcat-8.0.21/bin/
	./shutdown.sh
	./startup.sh
	cd
end script
" > /etc/repository-RI.conf

