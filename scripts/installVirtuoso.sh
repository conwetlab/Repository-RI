#! /bin/bash

# Install virtuoso
wget https://github.com/openlink/virtuoso-opensource/archive/stable/7.zip

if [[ $? -ne "0" ]]; then
	echo "Virtuoso 7 donwload failed. Check internet connection."
	exit 1
fi
	
unzip 7.zip
cd virtuoso-opensource-stable-7

CFLAGS="-O2 -m64"
export CFLAGS

./autogen.sh
if [[ $? -ne "0" ]]; then
	echo "Check that all the needed packages are installed."
	exit 1
fi
./configure
if [[ $? -ne "0" ]]; then
	echo "Check that all the needed packages are installed."
	exit 1
fi

make install prefix=$INSPWD/virtuoso7
if [[ $? -ne "0" ]]; then
	echo "Compilation error, check that all the needed packages are installed."
	exit 1
fi
	
cd ..
sudo rm -r virtuoso-opensource-stable-7
