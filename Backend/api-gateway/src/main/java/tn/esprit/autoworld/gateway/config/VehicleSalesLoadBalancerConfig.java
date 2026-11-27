package tn.esprit.autoworld.gateway.config;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.RandomLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * Load-balancing strategy of MS1 only: Random instead of the default Round Robin.
 *
 * This class is deliberately not annotated with @Configuration. With @Configuration it would be
 * found by component scanning and its bean would replace the load balancer of every service.
 * It is loaded only through @LoadBalancerClient in RandomLoadBalancingConfig, inside the
 * context that Spring Cloud LoadBalancer creates for vehicle-sales-service.
 */
public class VehicleSalesLoadBalancerConfig {

	@Bean
	public ReactorLoadBalancer<ServiceInstance> randomLoadBalancer(Environment environment,
			LoadBalancerClientFactory factory) {
		// Name of the service this load balancer is being built for: vehicle-sales-service.
		String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
		return new RandomLoadBalancer(factory.getLazyProvider(name, ServiceInstanceListSupplier.class), name);
	}

}
