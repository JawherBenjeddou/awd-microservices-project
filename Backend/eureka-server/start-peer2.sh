#!/usr/bin/env sh
# Starts Eureka peer 2 on port 8762.
cd "$(dirname "$0")" || exit 1
./mvnw spring-boot:run -Dspring-boot.run.profiles=peer2
