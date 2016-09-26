#!/bin/bash

export JAVA_VERSION=1.8.0
export JAVA_HOME=/usr/lib/jvm/java-${JAVA_VERSION}

# install packages
yum install epel-release unzip wget git rpm-build system-config-services -y
yum install java-${JAVA_VERSION}-openjdk-devel nodejs npm rpm-build mysql mysql-server mysql-connector-java -y

sudo service mysqld start
