FROM mcr.microsoft.com/java/jdk:8u222-zulu-ubuntu

WORKDIR /opt/
COPY dbg-service-upload-0.0.1-SNAPSHOT.jar /opt/
COPY run.sh /opt/run.sh
RUN chmod +x /opt/run.sh

ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8700
ENTRYPOINT ["/opt/run.sh"]