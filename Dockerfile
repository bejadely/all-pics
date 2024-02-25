FROM amazoncorretto:17.0.9
WORKDIR app
ARG JAR_FILE=build/libs/bejadely-back-end-mission1.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul
ENTRYPOINT ["java", "-jar", "app.jar"]