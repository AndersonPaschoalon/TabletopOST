FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /build

COPY pom.xml .
COPY src ./src
RUN mvn -B clean package -DskipTests
RUN mvn -B dependency:copy-dependencies -DoutputDirectory=target/dependency


FROM eclipse-temurin:21-jre-jammy

RUN apt-get update && apt-get install -y --no-install-recommends \
        ffmpeg \
        pulseaudio-utils \
        libxext6 \
        libxrender1 \
        libxtst6 \
        libxi6 \
        fontconfig \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY --from=build /build/target/tabletop-rpg-soundtrack-*.jar ./app.jar

COPY --from=build /build/target/dependency ./lib


VOLUME ["/app/local_storage", "/app/cache"]


ENV SDL_AUDIODRIVER=pulse

ENTRYPOINT ["java", "-cp", "app.jar:lib/*", "br.org.tabletoprpg.soundtrack.Main"]

