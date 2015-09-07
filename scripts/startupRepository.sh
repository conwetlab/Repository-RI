#! /bin/bash

if [ $REPO_OS == "centos6" ] || [ $REPO_OS == "centos7" ]; then

    sudo cp ./scripts/startup > /etc/repository-RI.conf
    
elif [ $REPO_OS == "ubuntu12.04" ] || [ $REPO_OS == "ubuntu14.04" ] || [ $REPO_OS == "debian" ]; then

    sudo cp ./scripts/startupInit > /etc/init/repository-RI.conf

fi