package tn.esprit.autoworld.gateway.config;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Part 3, question 3: applies the Random strategy to vehicle-sales-service and to no other
 * service. Active with the "random" profile only, so the default strategy can still be observed
 * without it (question 2.b).
 */
@Configuration
@Profile("random")
@LoadBalancerClient(name = "vehicle-sales-service", configuration = VehicleSalesLoadBalancerConfig.class)
public class RandomLoadBalancingConfig {

}
