FROM gradle:8.5
LABEL authors="Francisco Lucas"
COPY . /tmp
RUN cd /tmp; gradle clean; gradle build; gradle jar --no-daemon
ENTRYPOINT ["java", "-jar", "/tmp/build/libs/FL-Finances-1.0.jar"]