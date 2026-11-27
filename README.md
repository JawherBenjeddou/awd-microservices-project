# AutoWorld

**AutoWorld — Tout l'automobile, sur une seule plateforme**

AutoWorld is a microservices platform built with Spring Boot and Spring Cloud for the AWD module
at ESPRIT (2026-2027). It covers vehicle sales, rental, repair, spare parts and professional
profiles, plus a shared interaction service.

Repository name convention: `autoworld-<classe>-<groupe>`.

## Team

| Member           | Own microservice              | Module in MS6 (shared) |
|------------------|-------------------------------|------------------------|
| Jawher Benjeddou | MS1 — Vente de véhicules      | Messagerie             |
| Jihen BJAOUI     | MS2 — Location                | Paiements              |
| Azmi Ghabi       | MS3 — Réparation              | Avis                   |
| Aziz Barhoumi    | MS4 — Pièces                  | Notifications          |
| Nader Ayedi      | MS5 — Profils                 | Modération             |

## Services and ports

| Service              | Folder                                   | Technology           | Port | 2nd instance |
|----------------------|------------------------------------------|----------------------|------|--------------|
| Eureka peer 1        | `Backend/eureka-server`                  | Spring Cloud Netflix | 8761 | —            |
| Eureka peer 2        | `Backend/eureka-server`                  | Spring Cloud Netflix | 8762 | —            |
| MS1 Vente            | `Backend/vehicle-sales-service`          | Spring Boot          | 8081 | 8091         |
| MS2 Location         | `Backend/vehicle-rental-service`         | Spring Boot          | 8082 | 8092         |
| MS3 Réparation       | `Backend/repair-maintenance-service`     | Spring Boot          | 8083 | 8093         |
| MS4 Pièces           | `Backend/spare-parts-service`            | Spring Boot          | 8084 | 8094         |
| MS5 Profils          | `Backend/profiles-professionals-service` | Spring Boot          | 8085 | 8095         |
| MS6 Interactions     | `Backend/interaction-service`            | Node.js + Express    | 8086 | 8096         |
| API gateway          | `Backend/api-gateway`                    | Spring Cloud Gateway | —    | —            |
| Config server        | `Backend/config-server`                  | Spring Cloud Config  | —    | —            |

A second instance of a service always runs on its port + 10. The API gateway and config server
are not created yet.

## Repository layout

```
Backend/
  eureka-server/                    service registry (2 peers)
  vehicle-sales-service/            MS1
  vehicle-rental-service/           MS2
  repair-maintenance-service/       MS3
  spare-parts-service/              MS4
  profiles-professionals-service/   MS5
  interaction-service/              MS6, shared, Node.js
Frontend/                           Angular application
Documentation/                      diagrams and presentation
```

## Run locally

Prerequisites: Java 17, Node.js, and MySQL from XAMPP (user `root`, empty password). No change to
the hosts file is needed.

Startup order:

1. MySQL (XAMPP control panel)
2. Eureka peer 1 — `Backend/eureka-server/start-peer1.cmd` (or `./start-peer1.sh`)
3. Eureka peer 2 — `Backend/eureka-server/start-peer2.cmd` (or `./start-peer2.sh`)
4. The microservices, in any order

Details, client configuration and troubleshooting: [Backend/eureka-server/README.md](Backend/eureka-server/README.md).

## Git workflow

| Branch                       | Purpose                                       |
|------------------------------|-----------------------------------------------|
| `main`                       | stable, always runnable                       |
| `develop`                    | integration of finished features              |
| `feature/<service>-<name>`   | one branch per feature, created from `develop`|
| `chore/<topic>`              | setup and tooling work                        |

1. Update `develop`, then create your branch from it.
2. Commit small, with the service as prefix (`MS1: ...`, `Eureka: ...`).
3. Push and open a pull request to `develop`.
4. A teammate reviews the pull request before it is merged.
5. `develop` is merged into `main` when a workshop deliverable is stable.

Only change your own service folder. Changes to shared folders (`eureka-server`,
`interaction-service`, `api-gateway`, `config-server`) go through a pull request reviewed by the
team.

The stable branch of this repository is currently named `master`; it plays the role of `main`.

## Screenshots

Add the images to `Documentation/Screenshots/` and link them here.

### Eureka dashboards

- [ ] Dashboard of peer 1 — http://localhost:8761/ (all services listed, peer 2 under available-replicas)
- [ ] Dashboard of peer 2 — http://localhost:8762/ (all services listed, peer 1 under available-replicas)

### Two instances of the two most used services

- [ ] Most used service 1 — 2 instances on different ports, visible on both dashboards
- [ ] Most used service 2 — 2 instances on different ports, visible on both dashboards

### Actuator endpoints of each service

| Service              | health | info | metrics | loggers |
|----------------------|--------|------|---------|---------|
| MS1 Vente (8081)     | [ ]    | [ ]  | [ ]     | [ ]     |
| MS2 Location (8082)  | [ ]    | [ ]  | [ ]     | [ ]     |
| MS3 Réparation (8083)| [ ]    | [ ]  | [ ]     | [ ]     |
| MS4 Pièces (8084)    | [ ]    | [ ]  | [ ]     | [ ]     |
| MS5 Profils (8085)   | [ ]    | [ ]  | [ ]     | [ ]     |
| MS6 Interactions (8086) | [ ] | [ ]  | —       | —       |

Spring Boot services expose them under `/actuator/health`, `/actuator/info`, `/actuator/metrics`
and `/actuator/loggers`. MS6 is a Node.js service and exposes `/health` and `/info`.
