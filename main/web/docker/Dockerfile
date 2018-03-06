#this image use openjdk 9
#FROM tomcat:9.0
# image using JDK 8
FROM davidcaste/alpine-tomcat:jre8tomcat8

#copy configuration to enable remote administration
COPY ./resources/manager.xml /opt/tomcat/conf/Catalina/localhost/
COPY ./resources/tomcat-users.xml /opt/tomcat/conf/

#copy globale properties for application (can be overwritten with docker module)
# if you modify a path here remember to modify the path in context smilan-web.xml and the volume in docker-compose file
#COPY ./resources/smilan-web.xml /opt/tomcat/conf/Catalina/localhost/
COPY ./resources/context.xml /opt/tomcat/conf/Catalina/
COPY ./resources/main.properties /opt/tomcat/conf/

#copy application
COPY ./www/*.war /opt/tomcat/webapps/

CMD /opt/tomcat/bin/catalina.sh run