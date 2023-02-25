#!/bin/bash

cd /root || exit 1

cp ./config.ini.template ./config.ini

echo "authtoken=$NATAPP_AUTH_TOKEN" >> ./config.ini

echo "start socat"
nohup socat TCP4-LISTEN:22,reuseaddr,fork TCP4:$HOST_IP:22 > ./socat.log 2>&1 &

echo "start java"
cat /dev/null > "$LOG_FILE"
nohup java -jar /root/mail-sender.jar > ./mail-sender.log 2>&1 &

cat ./mail-sender.log

echo "start natapp"
chmod a+x ./natapp
./natapp -config=./config.ini