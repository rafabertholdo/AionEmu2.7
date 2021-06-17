#!/bin/bash
sed -i -e 's/AION_DB_USER/'$AION_DB_USER'/g' config/network/database.properties
sed -i -e 's/AION_DB_PASSWORD/'$AION_DB_PASSWORD'/g' config/network/database.properties
sed -i -e 's/AION_DB/'$AION_DB'/g' config/network/database.properties

err=1
until [ $err == 0 ];
do
	java -Xms8m -Xmx32m -ea -Xbootclasspath/p:./libs/jsr166.jar -cp ./libs/*:AL-Login.jar com.aionlightning.loginserver.LoginServer
	err=$?
	sleep 10
done
