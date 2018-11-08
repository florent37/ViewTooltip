#!/usr/bin/env bash
. ~/.bash_profile
./gradlew clean
./gradlew :viewtooltip:assembleDebug
./gradlew :viewtooltip:install
./gradlew :viewtooltip:bintrayUpload