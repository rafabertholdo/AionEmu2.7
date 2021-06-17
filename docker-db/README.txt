docker network create -d ipvlan --subnet=192.168.210.0/24 --gateway=192.168.210.254 -o ipvlan_mode=l2 ipvlan210
docker network create -d macvlan --subnet=192.168.0.0/24 --gateway=192.168.0.1 -o macvlan_mode=bridge -o parent=eth0 macvlan

docker network create -d macvlan --subnet=172.22.128.0/20 --gateway=172.22.128.1 -o macvlan_mode=bridge -o parent=eth0 macvlan

172.22.128.1

docker network create aion-network

#docker run -e MYSQL_ROOT_PASSWORD=aion --name aion-db --publish 3306:3306 aion-db
docker run --rm --name linux --network pub_net linux
docker run -e MYSQL_ROOT_PASSWORD=admin --name aion-db --publish 3306:3306 aion-db


docker build -t rafabertholdo/aion-db:2.7 .
docker run -e MYSQL_ROOT_PASSWORD=aion --rm --name aion-db --publish 3306:3306 -v /d/games/aion/servers/AionEmu2.7/docker-db/data:/var/lib/mysql rafabertholdo/aion-db:2.7