docker build -t rafabertholdo/aion-ls:2.7 .
docker run --rm --name aion-ls -e AION_DB=192.168.0.14 -e AION_DB_USER=root -e AION_DB_PASSWORD=aion --publish 2106:2106 --publish 9014:9014 rafabertholdo/aion-ls:2.7
