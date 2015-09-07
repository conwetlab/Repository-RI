#! /bin/bash

if [ $REPO_OS == "centos6" ] || [ $REPO_OS == "centos7" ]; then

    wget --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3A%2F%2Fwww.oracle.com%2F; oraclelicense=accept-securebackup-cookie" "http://download.oracle.com/otn-pub/java/jdk/8u45-b14/jdk-8u45-linux-x64.tar.gz"

    tar xzf jdk-8u45-linux-x64.tar.gz

    sudo alternatives --install /usr/bin/java java $INSPWD/jdk1.8.0_45/bin/java 20000
    sudo alternatives --config java
    sudo alternatives --install /usr/bin/jar jar $INSPWD/jdk1.8.0_45/bin/jar 20000
    sudo alternatives --install /usr/bin/javac javac $INSPWD/jdk1.8.0_45/bin/javac 20000
    sudo alternatives --set jar $INSPWD/jdk1.8.0_45/bin/jar
    sudo alternatives --set javac $INSPWD/jdk1.8.0_45/bin/javac

elif [ $REPO_OS == "debian" ]; then

    sudo sh -c 'echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" >> /etc/apt/sources.list'
    sudo sh -c 'echo "deb-src http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" >> /etc/apt/sources.list'
    sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886
    sudo apt-get update
    sudo apt-get install -y oracle-java8-installer oracle-java8-set-default
    sudo update-java-alternatives -s java-8-oracle

    JAVA_HOME=/usr/lib/jvm/java-8-oracle
    export JAVA_HOME

    sudo echo "JAVA_HOME=\"/usr/lib/jvm/java-8-oracle\"" | sudo tee -a /etc/environment > /dev/null 

elif [ $REPO_OS == "ubuntu12.04" ] || [ $REPO_OS == "ubuntu14.04" ]; then

    sudo add-apt-repository ppa:webupd8team/java
    sudo apt-get update

    sudo apt-get install -y software-properties-common python-software-properties oracle-java8-installer oracle-java8-set-default
    sudo update-java-alternatives -s java-8-oracle

    JAVA_HOME=/usr/lib/jvm/java-8-oracle
    export JAVA_HOME

    sudo echo "JAVA_HOME=\"/usr/lib/jvm/java-8-oracle\"" | sudo tee -a /etc/environment > /dev/null
fi