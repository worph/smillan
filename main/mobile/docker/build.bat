#rm -R ./www
rmdir /s/q .\www\
xcopy /s .\..\ionic-src\www .\www\
#cp -R ../www ./
docker build -t smillan-mobile .
docker image save smillan-mobile:latest -o smillan-mobile-latest.docker_image