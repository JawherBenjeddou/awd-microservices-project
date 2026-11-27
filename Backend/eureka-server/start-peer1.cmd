@echo off
rem Starts Eureka peer 1 on port 8761.
cd /d "%~dp0"
call mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=peer1
