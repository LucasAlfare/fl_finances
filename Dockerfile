FROM gradle:8.5
COPY . /tmp
RUN cd /tmp; gradle clean; gradle build --exclude-task test; gradle jar --no-daemon
ENTRYPOINT ["java", "-jar", "/tmp/build/libs/fl_finances-1.0.jar"]