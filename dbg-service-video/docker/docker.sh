docker run -d -p 8600:8600 --name dbg-service-video \
--env PROFILES=dev \
--env DB_HOST=cdb-72nu8aw0.bj.tencentcdb.com \
--env DB_PORT=10037 \
--env DB_USER=device \
--env DB_PWD=dbg-mysql2019 \
--env JVM_OPTS='-server -Xmx256m -Xms128m' \
--env IS_DEBUG=true \
dbg-service-video:v1.0