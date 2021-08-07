FROM mcr.microsoft.com/java/jdk:11-zulu-ubuntu
WORKDIR /opt/
COPY dbg-service-video-1.0.0-SNAPSHOT.jar /opt/
COPY run.sh /opt/run.sh
RUN chmod +x /opt/run.sh

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8600
ENTRYPOINT ["/opt/run.sh"]