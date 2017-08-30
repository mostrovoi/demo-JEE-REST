#!/bin/bash

java -Djava.net.preferIPv4Stack=true -Djava.security.egd=file:/dev/./urandom -jar /tmp/app.war
