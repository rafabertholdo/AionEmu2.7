#!/bin/bash
AFF=$(hostname -i)
sed -i -e 's/AION_DB_USER/'$AION_DB_USER'/g' config/network/database.properties
sed -i -e 's/AION_DB_PASSWORD/'$AION_DB_PASSWORD'/g' config/network/database.properties
sed -i -e 's/AION_DB/'$AION_DB'/g' config/network/database.properties
sed -i -e 's/AION_LS_PASSWORD/'$AION_LS_PASSWORD'/g' config/network/network.properties
sed -i -e 's/AION_CS_PASSWORD/'$AION_CS_PASSWORD'/g' config/network/network.properties
sed -i -e 's/AION_LS/'$AION_LS'/g' config/network/network.properties
sed -i -e 's/AION_CS/'$AION_CS'/g' config/network/network.properties
sed -i -e 's/AION_GSID/'$AION_GSID'/g' config/network/network.properties
sed -i -e 's/SERVER_NAME/'$SERVER_NAME'/g' config/main/gameserver.properties
sed -i -e 's/SERVER_CC/'$SERVER_CC'/g' config/main/gameserver.properties
sed -i -e 's/HOST_NAME/'$AFF'/g' config/network/ipconfig.xml

java -Xms128m -Xmx4096m -ea -javaagent:./libs/al-commons-1.3.jar -cp ./libs/*:AL-Game.jar com.aionemu.gameserver.GameServer
