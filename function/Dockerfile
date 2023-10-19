FROM openjdk:21-jdk AS builder
WORKDIR workspace
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} function-0.0.1-SNAPSHOT.jar
RUN java -Djarmode=layertools -jar function-0.0.1-SNAPSHOT.jar extract
FROM openjdk:21-jdk
RUN useradd spring
USER spring
WORKDIR workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]