#!/bin/bash

./gradlew deploy
[ $? -eq 0 ] && scp deploy/*.jar nix3.thauvin.us:/opt/lib/jtalk-ee/httpstatus
