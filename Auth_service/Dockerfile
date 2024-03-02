# Build
FROM gradle:latest AS TEMP_BUILD
ENV DIR=/app
WORKDIR $DIR

COPY Auth/. $DIR
ADD . .
USER root
RUN gradle bootJar
RUN echo "Build Completed!"

# Run
FROM amazoncorretto:20
ENV DIR=/app
ENV JAR_NAME=PR8-0.0.1-SNAPSHOT.jar
ENV DB_PORT=5432
WORKDIR $DIR
COPY --from=TEMP_BUILD $DIR/build/libs/$JAR_NAME .

EXPOSE 80
ENTRYPOINT java -jar $JAR_NAME