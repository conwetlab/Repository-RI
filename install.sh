#! /bin/bash

INSPWD=$PWD
export INSPWD
echo "Installation dir:"$INSPWD

if [ -f "/etc/centos-release" ]; then
	CONTENT=$(cat /etc/centos-release)
	if [[ $CONTENT == *7.* ]]; then
		./scripts/centos7.sh
	elif [[ $CONTENT == *6.* ]]; then
		./scripts/centos6.sh
	fi
elif [ -f "/etc/issue" ]; then
	# This file can exist in Debian and centos
	CONTENT=$(cat /etc/issue)
	if [[ $CONTENT == *CentOS* ]]; then
		./scripts/centos6.sh
	elif [[ $CONTENT == *Ubuntu* || $CONTENT == *Debian* ]]; then
		./scripts/debian.sh
	fi
fi
