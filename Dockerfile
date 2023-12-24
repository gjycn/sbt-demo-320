FROM eclipse-temurin:21.0.1_12-jdk-jammy
WORKDIR /workspace
VOLUME /workspace/config
COPY /target/*.jar /workspace/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UseZGC", "-XX:+ZGenerational", "--enable-preview", "-jar", "/workspace/app.jar"]
CMD ["bash"]