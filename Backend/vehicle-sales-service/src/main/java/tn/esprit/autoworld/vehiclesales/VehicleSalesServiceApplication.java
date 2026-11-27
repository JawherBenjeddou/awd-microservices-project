package tn.esprit.autoworld.vehiclesales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class VehicleSalesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleSalesServiceApplication.class, args);
	}

}
