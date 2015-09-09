#! /bin/bash

## Tools Install Script 

if [ $REPO_OS == "centos6" ] || [ $REPO_OS == "centos7" ]; then

    sudo yum update
    sudo yum install -y wget unzip zip maven

elif [ $REPO_OS == "ubuntu12.04" ] || [ $REPO_OS == "ubuntu14.04" ] || [ $REPO_OS == "debian" ]; then

    sudo apt-get update
    sudo apt-get install -y wget unzip zip maven
fi

