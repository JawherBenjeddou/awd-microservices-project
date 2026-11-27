# api-gateway — AutoWorld single entry point

## Role

The API gateway is the only address a client (browser, Angular app, Postman) needs to know.
It receives every request on port 8080, finds the route that matches the URL, asks Eureka for
the instances of the target microservice, picks one, and forwards the request to it.

| Item           | Value |
|----------------|-------|
| Port           | 8080 |
| Name in Eureka | `API-GATEWAY` |
| Stack          | Spring Boot 3.5.16, Spring Cloud 2025.0.0 (Spring Cloud Gateway 4.3, WebFlux), Java 17 |
| Package        | `tn.esprit.autoworld.gateway` |
| Swagger portal | http://localhost:8080/swagger-ui.html |

## Run

Start MySQL, the two Eureka servers and the microservices first (see the root README), then:

```bash
cd Backend/api-gateway
mvnw.cmd spring-boot:run        # Windows
./mvnw spring-boot:run          # macOS / Linux
```

In IntelliJ, use the shared run configurations **ApiGateway** and **ApiGatewayRandom**.

Check it:

```bash
curl http://localhost:8080/vehicle-sales/api/listings/hello
# Hello from vehicle-sales-service on port 8081   (then 8091, 8081, 8091, ...)
```

## Routes

A route is a predicate, optional filters and a destination URI.

| Part      | Value for MS1 | Meaning |
|-----------|---------------|---------|
| id        | `vehicle-sales` | Name of the route |
| predicate | `Path=/vehicle-sales/**` | The route is used when the URL starts with `/vehicle-sales/` |
| filter    | `StripPrefix=1` | Removes the first path segment before forwarding |
| uri       | `lb://vehicle-sales-service` | Service name in Eureka; `lb` means load-balanced |

| Client calls the gateway | MS1 receives |
|--------------------------|--------------|
| `GET http://localhost:8080/vehicle-sales/api/listings` | `GET /api/listings` |
| `GET http://localhost:8080/vehicle-sales/v3/api-docs`  | `GET /v3/api-docs` |

The prefix gives each microservice its own space on the gateway, so two services can both have
`/api/...` or `/v3/api-docs` paths without colliding.

## The four ways of the workshop

The workshop defines routes in two parts (static, dynamic) and two methods (Java class,
properties file). Each combination is a Spring profile, so every one can be run and shown.
All four expose the same URLs.

| Workshop | Profile | Routes are in | Destination |
|----------|---------|---------------|-------------|
| Part 2, method 1 | `static-java`  | `GatewayRoutesConfig.staticRoutes()`  | `http://localhost:8081` |
| Part 2, method 2 | `static`       | `application-static.properties`       | `http://localhost:8081` |
| Part 3, method 1 | `dynamic-java` | `GatewayRoutesConfig.dynamicRoutes()` | `lb://vehicle-sales-service` |
| Part 3, method 2 | `dynamic` (default) | `application-dynamic.properties` | `lb://vehicle-sales-service` |
| Part 3, question 3 | `random` | `dynamic` routes + `RandomLoadBalancingConfig` | `lb://vehicle-sales-service` |

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=static     # one profile at a time
```

| | Static | Dynamic |
|-|--------|---------|
| Needs Eureka | No | Yes |
| Second instance on 8091 | Never called | Called in turn with 8081 |
| Service changes port or IP | Route is broken until the file is edited | Nothing to change |

### Names that differ from the workshop handout

The handout was written for Spring Boot 3.3. This project uses Spring Cloud 2025.0.0, where two
names were changed. The old ones are deprecated.

| Handout | This project |
|---------|--------------|
| `spring-cloud-starter-gateway` | `spring-cloud-starter-gateway-server-webflux` |
| `spring.cloud.gateway.routes[0]...` | `spring.cloud.gateway.server.webflux.routes[0]...` |

## Load balancing

`application.properties` sets these two loggers to `DEBUG`, as asked by the workshop:

```properties
logging.level.org.springframework.cloud.gateway=DEBUG
logging.level.reactor.netty.http.client=DEBUG
```

For each request the console then shows the matched route and, on the line that contains
`Handler is being applied`, the instance that was chosen.

### Question 2.b — default algorithm: Round Robin

With two instances of MS1 (8081 and 8091) and no load-balancer configuration, 10 requests to
`/vehicle-sales/api/listings/hello` were answered by:

```
8081 8091 8081 8091 8081 8091 8081 8091 8081 8091
```

Gateway console (shortened):

```
RoutePredicateHandlerMapping : Route matched: vehicle-sales
HttpClientConnect : [.. R:/172.14.2.78:8081] Handler is being applied: {uri=http://172.14.2.78:8081/api/listings/hello, method=GET}
RoutePredicateHandlerMapping : Route matched: vehicle-sales
HttpClientConnect : [.. R:/172.14.2.78:8091] Handler is being applied: {uri=http://172.14.2.78:8091/api/listings/hello, method=GET}
RoutePredicateHandlerMapping : Route matched: vehicle-sales
HttpClientConnect : [.. R:/172.14.2.78:8081] Handler is being applied: {uri=http://172.14.2.78:8081/api/listings/hello, method=GET}
```

The instances are used strictly one after the other, in a fixed order: this is **Round Robin**
(`RoundRobinLoadBalancer`, the default of Spring Cloud LoadBalancer).

### Question 3 — other algorithms, and Random for MS1 only

| Algorithm | How an instance is chosen | Where it exists |
|-----------|---------------------------|-----------------|
| Round Robin | Each instance in turn | Spring Cloud LoadBalancer (default), Ribbon |
| Random | An instance drawn at random for every request | Spring Cloud LoadBalancer, Ribbon |
| Weighted | Instances with a higher weight receive more requests | Spring Cloud LoadBalancer (`weighted` instance supplier) |
| Zone preference | Instances of the caller's zone first | Spring Cloud LoadBalancer (`zone-preference` instance supplier) |
| Weighted Response Time | Faster instances receive more requests | Netflix Ribbon (older, replaced by Spring Cloud LoadBalancer) |
| Zone Avoidance | Skips zones that are overloaded or failing | Netflix Ribbon |

Random is applied to `vehicle-sales-service` and to no other service by two classes:

- `VehicleSalesLoadBalancerConfig` creates a `RandomLoadBalancer` bean. It has no
  `@Configuration` annotation on purpose: with it, component scanning would find the bean and
  apply Random to every service.
- `RandomLoadBalancingConfig` carries
  `@LoadBalancerClient(name = "vehicle-sales-service", configuration = VehicleSalesLoadBalancerConfig.class)`,
  which loads that bean only for the service named `vehicle-sales-service`. Every other service
  keeps Round Robin.

It is active with the `random` profile, so the default behaviour can still be shown without it:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=random
```

12 requests in this mode were answered by:

```
8081 8091 8091 8081 8081 8081 8091 8091 8081 8081 8081 8091
```

The same instance is chosen several times in a row and there is no fixed order: the choice is
random.

## Swagger portal

http://localhost:8080/swagger-ui.html shows the documentation of every microservice in one
page, with a **Select a definition** list at the top right.

- Each microservice has `springdoc-openapi-starter-webmvc-ui` and publishes its OpenAPI document
  at `/v3/api-docs`.
- The gateway has `springdoc-openapi-starter-webflux-ui` (the gateway is a WebFlux application)
  and lists the documents to show, each one read through its own route:

```properties
springdoc.swagger-ui.urls[0].name=MS1 - Vente de vehicules
springdoc.swagger-ui.urls[0].url=/vehicle-sales/v3/api-docs
```

**Try it out** sends the request through the gateway, not straight to the microservice. Two
settings make this work:

| Where | Setting | Effect |
|-------|---------|--------|
| Gateway | `spring.cloud.gateway.server.webflux.trusted-proxies` | The gateway adds `X-Forwarded-Host`, `-Port` and `-Prefix` to forwarded requests. Since Spring Cloud Gateway 4.3 it does so only for client addresses matching this pattern (here: this machine). |
| Microservice | `server.forward-headers-strategy=framework` | The service reads these headers, so its OpenAPI document gives `http://localhost:8080/vehicle-sales` as server URL instead of its own address. |

## Postman

`postman_collection.json` is the collection **Atelier API Gateway**. Import it in Postman
(*Import → File*). Every request goes through `{{gatewayUrl}}` = `http://localhost:8080` and has
a test on the status code. One folder per microservice.

## Add a microservice

1. In the microservice: register it in Eureka, add `springdoc-openapi-starter-webmvc-ui` and
   `server.forward-headers-strategy=framework`.
2. In `application-dynamic.properties`, add a route with the next index:

```properties
spring.cloud.gateway.server.webflux.routes[1].id=vehicle-rental
spring.cloud.gateway.server.webflux.routes[1].uri=lb://vehicle-rental-service
spring.cloud.gateway.server.webflux.routes[1].predicates[0]=Path=/vehicle-rental/**
spring.cloud.gateway.server.webflux.routes[1].filters[0]=StripPrefix=1
```

3. In `application.properties`, add its documentation to the portal:

```properties
springdoc.swagger-ui.urls[1].name=MS2 - Location
springdoc.swagger-ui.urls[1].url=/vehicle-rental/v3/api-docs
```

4. Add a folder with at least one request to `postman_collection.json`.

## Troubleshooting

| Symptom | Fix |
|---------|-----|
| `503 Service Unavailable` | No instance of the service is known yet. Check the Eureka dashboard, wait about 30 s after starting the service, then retry. |
| `404 Not Found` from the gateway | No route matches the URL. Check the prefix (`/vehicle-sales/...`). |
| Always the same port in dynamic mode | Only one instance is registered. Start the second one on 8091. |
| Swagger **Try it out** fails with a network error | The microservice is missing `server.forward-headers-strategy=framework`, or the portal was opened with an address other than `localhost`. |
| `Unable to load io.netty.resolver.dns.macos...` at the first request | Harmless message on macOS only. The gateway works. |
