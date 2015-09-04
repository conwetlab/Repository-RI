#! /bin/bash

# Download compiled Virtuoso 7 for Ubuntu 14.04
wget http://repo.conwet.fi.upm.es/artifactory/libs-release-local/org/fiware/apps/repository/virtuoso/7.1.0/virtuoso-7.1.0_ubuntu-14_amd64.tar.xz

tar -Jxf virtuoso-7.1.0_ubuntu-14_amd64.tar.xz

sudo rm virtuoso-7.1.0_ubuntu-14_amd64.tar.xz