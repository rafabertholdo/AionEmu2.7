docker build -t rafabertholdo/aion-gs:2.7 .
docker run -it --rm --name aion-gs rafabertholdo/aion-gs:2.7 bash
docker exec -it aion-gs bash

docker run --rm --name aion-gs --publish 7777:7777 --publish 10241:10241 -e AION_DB=192.168.0.14 -e AION_DB_USER=root -e AION_DB_PASSWORD=aion -e HOST_NAME=192.168.0.14 -e AION_LS=192.168.0.14 rafabertholdo/aion-gs:2.7