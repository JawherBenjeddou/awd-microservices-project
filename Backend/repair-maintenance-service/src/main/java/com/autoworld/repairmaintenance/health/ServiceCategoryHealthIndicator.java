package com.autoworld.repairmaintenance.health;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

import com.autoworld.repairmaintenance.repository.ServiceCategoryRepository;

@Component("serviceCatalog")
public class ServiceCategoryHealthIndicator implements HealthIndicator {

	private final ServiceCategoryRepository repository;

	public ServiceCategoryHealthIndicator(ServiceCategoryRepository repository) {
		this.repository = repository;
	}

	@Override
	public Health health() {
		try {
			long categories = repository.count();
			if (categories > 0) {
				return Health.up().withDetail("serviceCategories", categories).build();
			}
			return Health.down()
					.withDetail("serviceCategories", 0)
					.withDetail("reason", "Reference data service_category is empty")
					.build();
		}
		catch (RuntimeException ex) {
			// Toute panne d'acces a la base (DataAccessException, TransactionException...)
			// doit produire DOWN et non une erreur 500 du endpoint health.
			return Health.down(ex).build();
		}
	}

}
