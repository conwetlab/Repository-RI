#! /bin/bash

if [ $REPO_OS == "centos6" ] || [ $REPO_OS == "centos7" ]; then

    # Install mongodb
    ARCH=$(uname -m)

    # Install MongoDB repository
    if [[ $ARCH == "x86_64" ]]; then
            echo "[mongodb]
    name=MongoDB Repository
    baseurl=http://downloads-distro.mongodb.org/repo/redhat/os/x86_64/
    gpgcheck=0
    enabled=1" > /etc/yum.repos.d/mongodb.repo
    else
            echo "[mongodb]
    name=MongoDB Repository
    baseurl=http://downloads-distro.mongodb.org/repo/redhat/os/i686/
    gpgcheck=0
    enabled=1" > /etc/yum.repos.d/mongodb.repo
    fi

    sudo yum -y install mongodb-org

elif [ $REPO_OS == "debian" ] || [ $REPO_OS == "ubuntu12.04" ] || [ $REPO_OS == "ubuntu14.04" ]; then

    sudo apt-get -y install mongodb

fi