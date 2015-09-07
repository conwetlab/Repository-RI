#! /bin/bash

INSPWD=$PWD
export INSPWD
echo "Installation dir:"$INSPWD

if [ -f "/etc/centos-release" ]; then
    CONTENT=$(cat /etc/centos-release)
    if [[ $CONTENT == *7.* ]]; then
        export REPO_OS=centos7
    elif [[ $CONTENT == *6.* ]]; then
        export REPO_OS=centos6
    fi
elif [ -f "/etc/issue" ]; then
    # This file can exist in Debian and centos
    CONTENT=$(cat /etc/issue)
    if [[ $CONTENT == *CentOS* ]]; then
        export REPO_OS=centos6
    elif [[ $CONTENT == *Ubuntu\ 14.04* ]]; then
        export REPO_OS=ubuntu14.04
    elif [[ $CONTENT == *Ubuntu\ 12.04* ]]; then
        export REPO_OS=ubuntu12.04
    elif [[ $CONTENT == *Debian* ]]; then
        export REPO_OS=debian
    fi
fi

if [ $REPO_OS != "" ]; then
    ./scripts/installFiwareRepository.sh
else
    echo "Unsoported Operative System Version."
    echo "Check the installation guide to manual installation."
fi
