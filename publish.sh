#!/usr/bin/env bash
. ~/.bash_profile
./gradlew clean
./gradlew :viewtooltip:assembleDebug :viewtooltip:install
./gradlew :viewtooltip:bintrayUpload