package com.autoworld.repairmaintenance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RepairMaintenanceServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RepairMaintenanceServiceApplication.class, args);
	}

}
