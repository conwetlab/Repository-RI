#!/bin/bash

echo "Some packages are needed for Virtuoso install."
echo "Do you want to install? Y/N"
read X
while [ $X != "Y" ] && [ $X != "y" ] && [ $X != "N" ] && [ $X != "n" ]; do
	echo "Do you want to install? Y/N"
	read X
done
if [  $X == "Y" ] || [ $X == "y" ]; then
	yum -y install autoconf
	yum -y install automake
	yum -y install libtool
	yum -y install flex
	yum -y install bison
	yum -y install gperf
	yum -y install gawk
	yum -y install m4
	yum -y install make
	yum -y install openssl
	yum -y install openssl-devel
fi
