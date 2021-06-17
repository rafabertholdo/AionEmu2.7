FROM openjdk:8

RUN apt-get update && apt-get install -y \
  ant \
  maven \  
  && rm -rf /var/lib/apt/lists/*

COPY AL-Game /root/AL-Game
COPY AE-Commons /root/AE-Commons

WORKDIR /root/AL-Game

RUN ant

# Create final runtime image
FROM rafabertholdo/java:6

ARG AION_DB=localhost
ENV AION_DB $AION_DB

ARG AION_DB_USER=aion
ENV AION_DB_USER $AION_DB_USER

ARG AION_DB_PASSWORD=P@ssw0rd
ENV AION_DB_PASSWORD $AION_DB_PASSWORD

ARG AION_LS=localhost
ENV AION_LS $AION_LS

ARG AION_LS_PASSWORD=aion
ENV AION_LS_PASSWORD $AION_LS_PASSWORD

ARG AION_CS=localhost
ENV AION_CS $AION_CS

ARG AION_CS_PASSWORD=aion
ENV AION_CS_PASSWORD $AION_CS_PASSWORD

ARG AION_GSID=1
ENV AION_GSID $AION_GSID

ARG SERVER_NAME=Siel
ENV SERVER_NAME $SERVER_NAME

ARG SERVER_CC=1
ENV SERVER_CC $SERVER_CC

ARG HOST_NAME=127.0.0.1
ENV HOST_NAME $HOST_NAME

# Create our group & user.
RUN apt-get update && apt-get install -y \
  zip \
  && rm -rf /var/lib/apt/lists/*

# Copy our Darling build from previous stage
COPY --from=0 /root/AL-Game/build/AL-Game.zip /root

WORKDIR /root
RUN mkdir /root/AL-Game
RUN unzip AL-Game.zip -d /root/AL-Game

WORKDIR /root/AL-Game

EXPOSE 2106 9014

CMD [ "sh", "StartGS.sh"]