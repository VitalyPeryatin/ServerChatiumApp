FROM openjdk:11
RUN mkdir /app
COPY . /app
ENTRYPOINT ["/app/build/install/ru.chatium.chatiumapp/bin/ru.chatium.chatiumapp"]