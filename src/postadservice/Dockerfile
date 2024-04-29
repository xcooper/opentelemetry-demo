# Copyright The OpenTelemetry Authors
# SPDX-License-Identifier: Apache-2.0


FROM eclipse-temurin:21-jdk as builder

WORKDIR /usr/src/app/

COPY ./src/postadservice/gradlew* ./src/postadservice/settings.gradle* ./src/postadservice/build.gradle ./
COPY ./src/postadservice/gradle ./gradle
RUN ./gradlew

COPY ./src/postadservice/ ./
RUN ./gradlew build

# -----------------------------------------------------------------------------

FROM eclipse-temurin:21-jre

ARG version=2.3.0
WORKDIR /usr/src/app/

COPY --from=builder /usr/src/app/ ./
ADD --chmod=644 https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v$version/opentelemetry-javaagent.jar /usr/src/app/opentelemetry-javaagent.jar
ENV JAVA_TOOL_OPTIONS=-javaagent:/usr/src/app/opentelemetry-javaagent.jar

ENTRYPOINT [ "java", "-jar", "./build/libs/postadservice-0.0.1-SNAPSHOT.jar" ]
