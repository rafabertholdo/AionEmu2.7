FROM openjdk:8

RUN apt-get update && apt-get install -y \
  ant \
  maven \  
  && rm -rf /var/lib/apt/lists/*

COPY AL-Login /root/loginserver
WORKDIR /root/loginserver

RUN ant

# Create final runtime image
FROM rafabertholdo/java:6

ARG AION_DB=localhost
ENV AION_DB $AION_DB

ARG AION_DB_USER=aion
ENV AION_DB_USER $AION_DB_USER

ARG AION_DB_PASSWORD=P@ssw0rd
ENV AION_DB_PASSWORD $AION_DB_PASSWORD

# Create our group & user.
RUN apt-get update && apt-get install -y \
  zip \
  && rm -rf /var/lib/apt/lists/*

# Copy our Darling build from previous stage
COPY --from=0 /root/loginserver/build/AL-Login.zip /root

WORKDIR /root
RUN mkdir /root/AL-Login
RUN unzip AL-Login.zip -d /root/AL-Login

WORKDIR /root/AL-Login

EXPOSE 2106 9014

CMD [ "sh", "StartLS.sh"]