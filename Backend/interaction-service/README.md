# interaction-service — MS6 (shared)

Shared AutoWorld service for messaging, payments, reviews, notifications and moderation.
It is written in **Node.js + Express** on purpose: the workshop requires the shared service to
use a different technology from the Spring Boot services.

| Item          | Value                                   |
|---------------|-----------------------------------------|
| Port          | 8086 (second instance: 8096)            |
| Database      | MySQL `interaction_db` (created at startup if missing) |
| Name in Eureka| `INTERACTION-SERVICE`                   |
| Stack         | Node.js 18+, Express, Sequelize, eureka-js-client |

**Status: skeleton only.** There are no entities, models or business routes yet. Each member
adds their own module.

## Module ownership

| Module        | Folder                      | Owner            |
|---------------|-----------------------------|------------------|
| Messagerie    | `src/modules/messagerie`    | Jawher Benjeddou |
| Paiements     | `src/modules/paiements`     | Jihen BJAOUI     |
| Avis          | `src/modules/avis`          | Azmi Ghabi       |
| Notifications | `src/modules/notifications` | Aziz Barhoumi    |
| Modération    | `src/modules/moderation`    | Nader Ayedi      |

## Install

```bash
cd Backend/interaction-service
npm install
```

Then copy `.env.example` to `.env` (Windows: `copy .env.example .env`, macOS / Linux:
`cp .env.example .env`). The defaults match XAMPP (user `root`, empty password). `.env` is not
committed.

| Variable       | Default                         | Meaning                              |
|----------------|---------------------------------|--------------------------------------|
| `PORT`         | `8086`                          | HTTP port of this instance           |
| `DB_HOST`      | `localhost`                     | MySQL host                           |
| `DB_PORT`      | `3306`                          | MySQL port                           |
| `DB_NAME`      | `interaction_db`                | database, created if missing         |
| `DB_USER`      | `root`                          | MySQL user                           |
| `DB_PASSWORD`  | empty                           | MySQL password                       |
| `EUREKA_HOSTS` | `localhost:8761,localhost:8762` | Eureka servers, comma separated      |

## Run

Start MySQL and the two Eureka peers first (see [../eureka-server/README.md](../eureka-server/README.md)).

```bash
npm start      # node server.js
npm run dev    # nodemon, restarts on file change
```

The service appears as `INTERACTION-SERVICE` on http://localhost:8761/ and
http://localhost:8762/ within about 30 s. Stop it with Ctrl+C: it deregisters from Eureka before
exiting.

## Run a second instance (port 8096)

Open a second terminal in the same folder. A variable set in the terminal overrides `.env`.

| Shell              | Command                         |
|--------------------|---------------------------------|
| Windows cmd        | `set PORT=8096&& npm start`     |
| Windows PowerShell | `$env:PORT=8096; npm start`     |
| macOS / Linux      | `PORT=8096 npm start`           |

Both dashboards then show two instances: `interaction-service:8086` and
`interaction-service:8096`.

## Endpoints

| Method | Path                      | Response                                                       |
|--------|---------------------------|----------------------------------------------------------------|
| GET    | `/api/interactions/hello` | `{"message":"Hello from interaction-service on port 8086"}`    |
| GET    | `/health`                 | `200 {"status":"UP"}`, or `503 {"status":"DOWN"}` if the database is unreachable |
| GET    | `/info`                   | name, version and description from `package.json`              |

`/health` and `/info` play the role of `/actuator/health` and `/actuator/info`; they are the
health-check and status-page URLs sent to Eureka.

## Structure

```
server.js                  starts HTTP, registers with Eureka, handles shutdown
src/app.js                 Express app and the three routes above
src/config/env.js          reads .env
src/config/db.js           Sequelize connection, creates the database
src/eureka/eurekaClient.js Eureka registration
src/models|routes|controllers|services   shared code (empty for now)
src/modules/<module>/      one folder per member
```

## How the Eureka registration works

- `EUREKA_HOSTS` is turned into one URL per server (`http://<host>/eureka/apps/`).
- The client registers with the first server that answers and switches to the other one if it
  stops responding. The two servers replicate registrations to each other, so the service is
  visible on both dashboards.
- The instance id is `interaction-service:<port>`, so two instances never overwrite each other.
- The instance registers with the machine's IP address, like `prefer-ip-address=true` on the
  Spring Boot services. Set `INSTANCE_HOST` in `.env` to force another address.

## Troubleshooting

| Symptom | Fix |
|---------|-----|
| `/health` returns `DOWN` | MySQL is not running, or `DB_*` values are wrong. |
| `connect ECONNREFUSED ::1:3306` in the log | Node resolved `localhost` to IPv6. Set `DB_HOST=127.0.0.1` in `.env`. |
| `EADDRINUSE` | The port is already used; another instance is still running. |
| Not visible in Eureka | Check that the Eureka peers are running and that `EUREKA_HOSTS` is correct, then wait 30 s. |
| `npm install` reports vulnerabilities | Most come from `eureka-js-client`, which depends on the unmaintained `request` package. Avoid `npm audit fix --force`: it installs breaking major versions. |
