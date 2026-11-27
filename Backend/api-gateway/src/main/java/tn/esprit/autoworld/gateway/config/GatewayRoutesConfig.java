package tn.esprit.autoworld.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Method 1 of the workshop: routes declared in a configuration class.
 *
 * Each bean is the Java twin of a properties file (method 2) and is only created when its
 * profile is active, so the same route is never declared twice:
 * static-java = application-static.properties, dynamic-java = application-dynamic.properties.
 */
@Configuration
public class GatewayRoutesConfig {

	/** Part 2: the gateway points straight at one instance of MS1, without Eureka. */
	@Bean
	@Profile("static-java")
	public RouteLocator staticRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("vehicle-sales", r -> r.path("/vehicle-sales/**")
						.filters(f -> f.stripPrefix(1))
						.uri("http://localhost:8081"))
				.build();
	}

	/** Part 3: lb:// asks Eureka for the instances of MS1 and load-balances between them. */
	@Bean
	@Profile("dynamic-java")
	public RouteLocator dynamicRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("vehicle-sales", r -> r.path("/vehicle-sales/**")
						.filters(f -> f.stripPrefix(1))
						.uri("lb://vehicle-sales-service"))
				.build();
	}

}
