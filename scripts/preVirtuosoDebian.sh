#!/bin/bash

echo "Some packages are needed for Virtuoso install."
echo "Do you want to install? Y/N"
read X
while [ $X != "Y" ] && [ $X != "y" ] && [ $X != "N" ] && [ $X != "n" ]; do
	echo "Do you want to install? Y/N"
	read X
done
if [  $X == "Y" ] || [ $X == "y" ]; then
	apt-get install -y autoconf
	apt-get install -y automake
	apt-get install -y libtool
	apt-get install -y flex
	apt-get install -y bison
	apt-get install -y gperf
	apt-get install -y gawk
	apt-get install -y m4
	apt-get install -y make
	apt-get install -y OpenSSL
	apt-get install -y libssl-dev
fi
