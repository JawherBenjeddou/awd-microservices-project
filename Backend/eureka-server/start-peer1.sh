#!/usr/bin/env sh
# Starts Eureka peer 1 on port 8761.
cd "$(dirname "$0")" || exit 1
./mvnw spring-boot:run -Dspring-boot.run.profiles=peer1
