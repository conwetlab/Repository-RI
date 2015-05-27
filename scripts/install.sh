#! /bin/bash

INSPWD=$PWD
export INSPWD
echo "Installation dir:"$INSPWD

if [ -f "/etc/centos-release" ]; then
	CONTENT=$(cat /etc/centos-release)
	if [[ $CONTENT == *7.* ]]; then
		./centos7.sh
	elif [[ $CONTENT == *6.* ]]; then
		./centos6.sh
	fi
elif [ -f "/etc/issue" ]; then
	# This file can exist in Debian and centos
	CONTENT=$(cat /etc/issue)
	if [[ $CONTENT == *CentOS* ]]; then
		./centos6.sh
	elif [[ $CONTENT == *Ubuntu* || $CONTENT == *Debian* ]]; then
		./debian.sh
	fi
fi
