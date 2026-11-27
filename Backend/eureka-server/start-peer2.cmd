@echo off
rem Starts Eureka peer 2 on port 8762.
cd /d "%~dp0"
call mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=peer2
