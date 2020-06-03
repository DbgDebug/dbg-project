#!/bin/bash
# build the web docker
base_dir="$( cd "$(dirname "$0")" ; pwd -P )"
cd $base_dir

cd ../
mvn -pl dbg-service-blog -am -DskipTests package || exit $?

cd $base_dir
cp target/dbg-service-blog-1.0.0-SNAPSHOT.jar ./docker/
cd $base_dir/docker

dos2unix run.sh

docker build --tag=dbg-service-blog:v1.0 . || exit $?
#docker push ip:port/dbg-service-blog:v1.0 || exit $?