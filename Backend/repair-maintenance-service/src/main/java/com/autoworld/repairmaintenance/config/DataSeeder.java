package com.autoworld.repairmaintenance.config;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.autoworld.repairmaintenance.domain.GarageService;
import com.autoworld.repairmaintenance.domain.ServiceCategory;
import com.autoworld.repairmaintenance.repository.GarageServiceRepository;
import com.autoworld.repairmaintenance.repository.ServiceCategoryRepository;

@Component
public class DataSeeder implements CommandLineRunner {

	private final ServiceCategoryRepository categoryRepository;

	private final GarageServiceRepository garageServiceRepository;

	public DataSeeder(ServiceCategoryRepository categoryRepository,
			GarageServiceRepository garageServiceRepository) {
		this.categoryRepository = categoryRepository;
		this.garageServiceRepository = garageServiceRepository;
	}

	@Override
	@Transactional
	public void run(String... args) {
		if (categoryRepository.count() == 0) {
			categoryRepository.save(new ServiceCategory("Vidange et filtres",
					"Vidange moteur, remplacement des filtres a huile, air et habitacle"));
			categoryRepository.save(new ServiceCategory("Freinage",
					"Plaquettes, disques, liquide de frein et purge du circuit"));
			categoryRepository.save(new ServiceCategory("Climatisation",
					"Recharge de gaz, diagnostic et desinfection du circuit de climatisation"));
			categoryRepository.save(new ServiceCategory("Diagnostic electronique",
					"Lecture des codes defaut et diagnostic des calculateurs du vehicule"));
		}

		if (garageServiceRepository.count() == 0) {
			GarageService oilChange = new GarageService();
			oilChange.setGarageId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001"));
			oilChange.setName("Vidange complete + filtres");
			oilChange.setDescription("Vidange huile moteur 5W30 et remplacement des trois filtres");
			oilChange.setBasePrice(new BigDecimal("350.00"));
			oilChange.setCurrency("TND");
			oilChange.setEstimatedDurationMinutes(60);
			oilChange.setCategory(category("Vidange et filtres"));
			garageServiceRepository.save(oilChange);

			GarageService brakes = new GarageService();
			brakes.setGarageId(UUID.fromString("aaaaaaaa-0000-0000-0000-000000000002"));
			brakes.setName("Remplacement plaquettes de frein");
			brakes.setDescription("Remplacement des plaquettes avant et controle du niveau de liquide");
			brakes.setBasePrice(new BigDecimal("420.00"));
			brakes.setCurrency("TND");
			brakes.setEstimatedDurationMinutes(90);
			brakes.setCategory(category("Freinage"));
			garageServiceRepository.save(brakes);
		}
	}

	private ServiceCategory category(String name) {
		return categoryRepository.findAll().stream()
				.filter(category -> category.getName().equals(name))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Categorie de reference absente: " + name));
	}

}
