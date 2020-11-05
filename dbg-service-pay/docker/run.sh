#!/bin/bash
if [ -z "$PROFILES" ]; then
  PROFILES="dev"
fi

echo java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Dfile.encoding=UTF-8 -Dspring.profiles.active=$PROFILES $JVM_OPTS /opt/dbg-service-pay-0.0.1-SNAPSHOT.jar
java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005 -Dfile.encoding=UTF-8 -Dspring.profiles.active=$PROFILES $JVM_OPTS /opt/dbg-service-pay-0.0.1-SNAPSHOT.jar

