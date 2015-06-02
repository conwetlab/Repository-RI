#!/bin/bash

echo "-------------------------------------------------"
echo "Some packages are needed for installing Virtuoso: autoconf, automake, libtoo, flex, bison, gperf, gawk, m4, make, openssl, openssl-devel"
echo "Do you want to install them? Y/N"
read X
while [ $X != "Y" ] && [ $X != "y" ] && [ $X != "N" ] && [ $X != "n" ]; do
	echo "Do you want to install them? Y/N"
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
