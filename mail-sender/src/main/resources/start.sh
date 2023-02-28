#!/bin/bash

cd /root/mail-sender/target/ || exit 1

echo "start socat"
nohup socat TCP4-LISTEN:22,reuseaddr,fork TCP4:$HOST_IP:$HOST_PORT > /root/socat.log 2>&1 &

echo "copy java file"
cp /root/mail-sender/target/mail-sender-*.jar /root/mail-sender.jar
echo "clear log file $LOG_FILE"
cat /dev/null > "$LOG_FILE"
echo "start java"
nohup java -jar /root/mail-sender.jar > /root/mail-sender.log 2>&1 &

cat /root/mail-sender.log

echo "init natapp config file"
cp /root/mail-sender/target/classes/config.ini.template /root/config.ini
echo "authtoken=$NATAPP_AUTH_TOKEN" >> /root/config.ini

cd /root
echo "chmod +x natapp"
chmod a+x ./natapp
echo "start natapp"
/root/natapp -config=/root/config.ini