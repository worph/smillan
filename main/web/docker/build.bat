rmdir /s/q .\www\
xcopy /s .\..\target\smilan-web-1.0-SNAPSHOT.war .\www\
cd www
ren *.war smilan-web.war
cd ..
docker build -t smillan-war .
docker image save smillan-war:latest -o smillan-war-latest.docker_image