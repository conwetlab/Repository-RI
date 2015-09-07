#! /bin/bash

if yum ; then
    sudo yum install -y autoconf automake libtool flex bison gperf gawk m4 make openssl openssl-devel
elif apt-get ; then
    sudo apt-get install -y autoconf automake libtool flex bison gperf gawk m4 make openSSL libssl-dev build-essential
fi
