# eureka-server — AutoWorld service registry

## Role

Eureka is the phone book of the platform. Every microservice registers itself at startup
(name, IP, port) and sends a heartbeat every 30 s. Other services, and later the API gateway,
look a service up by name instead of hard-coding its address, so a second instance on another
port is found automatically. We run two Eureka servers ("peers") that copy their registry to
each other, so the platform keeps working if one of them stops.

| Peer  | Port | Spring profile | Dashboard              |
|-------|------|----------------|------------------------|
| peer1 | 8761 | `peer1`        | http://localhost:8761/ |
| peer2 | 8762 | `peer2`        | http://localhost:8762/ |

Stack: Spring Boot 3.5.16, Spring Cloud 2025.0.0, Java 17.

## Run both peers

Open two terminals in `Backend/eureka-server`.

| Peer  | Windows           | macOS / Linux      |
|-------|-------------------|--------------------|
| peer1 | `start-peer1.cmd` | `./start-peer1.sh` |
| peer2 | `start-peer2.cmd` | `./start-peer2.sh` |

Both scripts run `mvnw spring-boot:run -Dspring-boot.run.profiles=peerX`.

In IntelliJ, use the shared run configurations **EurekaPeer1** and **EurekaPeer2** (stored in
`.run/`, they appear in the run dropdown once the Maven project is imported). They are plain
"Application" configurations, so they work in both the Community and Ultimate editions.

Running the application with no profile starts peer1 (`spring.profiles.default=peer1`).

While only one peer is up, its log shows `Connection refused` errors: it is trying to reach the
other peer. They stop once the second peer has started.

## Startup order

1. MySQL (XAMPP)
2. Eureka peer1 (8761)
3. Eureka peer2 (8762)
4. The microservices

Wait about 30 s after starting peer2 before judging the dashboards: the peers and the clients
only refresh their view every 30 s.

## Why `localhost` on one peer and `127.0.0.1` on the other

Both peers run on the same machine, and Eureka identifies a peer by its hostname. If both were
called `localhost`, each server would treat the other peer's URL as its own address and drop it
from its replica list, so nothing would be replicated. We could not use made-up names such as
`peer1` / `peer2` either, because that needs a change to the hosts file on every machine.

Instead, peer1 calls itself `localhost` and peer2 calls itself `127.0.0.1`. Both names already
point to the local machine on every OS, yet they are different strings, so each peer sees the
other as a separate server and lists it under **available-replicas** on its dashboard.

| Peer  | `eureka.instance.hostname` | Replicates to                |
|-------|----------------------------|------------------------------|
| peer1 | `localhost`                | `http://127.0.0.1:8762/eureka/` |
| peer2 | `127.0.0.1`                | `http://localhost:8761/eureka/` |

## Client configuration

### Spring Boot service

Add the `spring-cloud-starter-netflix-eureka-client` dependency (Spring Cloud BOM `2025.0.0`),
then paste this into `application.properties`:

```properties
eureka.client.service-url.defaultZone=http://localhost:8761/eureka,http://localhost:8762/eureka
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.instance.prefer-ip-address=true
eureka.instance.instance-id=${spring.application.name}:${server.port}
```

A client registers with the first server that answers, and that server replicates the
registration to the other peer.

### Node.js service (`eureka-js-client`)

```js
const { Eureka } = require('eureka-js-client');

const PORT = Number(process.env.PORT) || 8086;
const APP = 'interaction-service';

const client = new Eureka({
  instance: {
    app: APP.toUpperCase(),
    instanceId: `${APP}:${PORT}`,
    hostName: 'localhost',
    ipAddr: '127.0.0.1',
    port: { $: PORT, '@enabled': true },
    vipAddress: APP,
    statusPageUrl: `http://localhost:${PORT}/info`,
    healthCheckUrl: `http://localhost:${PORT}/health`,
    dataCenterInfo: {
      '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
      name: 'MyOwn',
    },
  },
  eureka: {
    serviceUrls: {
      default: [
        'http://localhost:8761/eureka/apps/',
        'http://localhost:8762/eureka/apps/',
      ],
    },
  },
});

client.start();
process.on('SIGINT', () => client.stop(() => process.exit(0)));
```

### Python service (`py-eureka-client`)

```python
import py_eureka_client.eureka_client as eureka_client

PORT = 8087

eureka_client.init(
    eureka_server="http://localhost:8761/eureka,http://localhost:8762/eureka",
    app_name="my-python-service",
    instance_id=f"my-python-service:{PORT}",
    instance_host="localhost",
    instance_port=PORT,
)

# on shutdown
eureka_client.stop()
```

## Useful URLs

| URL                                   | What it shows                                   |
|---------------------------------------|-------------------------------------------------|
| http://localhost:8761/                | peer1 dashboard                                 |
| http://localhost:8762/                | peer2 dashboard                                 |
| http://localhost:8761/eureka/apps     | registry as XML (same path on 8762)             |
| http://localhost:8761/actuator/health | health of the server (same path on 8762)        |
| http://localhost:8761/actuator/info   | name and description (same path on 8762)        |

## Troubleshooting

| Symptom | Cause and fix |
|---------|---------------|
| `Port 8761 was already in use` (or 8762) | Another process holds the port, usually a peer left running. Windows: `netstat -ano \| findstr :8761` then `taskkill /PID <pid> /F`. macOS / Linux: `lsof -i :8761` then `kill <pid>`. |
| The other peer is listed under **unavailable-replicas** | The other peer is not running, or it started less than 30 s ago. Start it, wait 30 s and refresh. If it stays unavailable, check that the two `application-peerX.properties` files still use `localhost` on one side and `127.0.0.1` on the other. |
| A service is visible on only one peer | Normal just after startup: a client registers with one server and replication to the other takes up to about 30 s. Wait and refresh. If it never appears, check the `available-replicas` line on both dashboards. |
| A service disappears from the dashboard | The server stopped receiving its heartbeat. An instance that sends no heartbeat for 90 s is removed (the eviction task runs every 10 s). Check that the service is still running and can reach port 8761 or 8762. |
| A stopped service is still listed | Same 90 s rule if it was killed without a clean shutdown. A clean stop (Ctrl+C, IntelliJ stop button) removes it immediately. |

Self-preservation is switched off (`eureka.server.enable-self-preservation=false`) so that dead
instances are really removed during demos. The dashboard shows a red warning about it; this is
expected.
