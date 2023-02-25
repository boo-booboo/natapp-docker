#!/bin/bash

PWD=$(pwd)
cd "$PWD"/mail-sender || exit 1
mvn package

cp ./target/mail-sender-*.jar ../dockerfile/scripts/mail-sender.jar


