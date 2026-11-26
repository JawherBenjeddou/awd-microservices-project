package tn.esprit.autoworld.vehiclesales.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.autoworld.vehiclesales.entity.AnnonceVente;
import tn.esprit.autoworld.vehiclesales.entity.Carburant;
import tn.esprit.autoworld.vehiclesales.entity.PhotoVehicule;
import tn.esprit.autoworld.vehiclesales.entity.StatutAnnonce;
import tn.esprit.autoworld.vehiclesales.entity.Vehicule;
import tn.esprit.autoworld.vehiclesales.repository.AnnonceVenteRepository;

/** Inserts 3 sample listings the first time the service starts on an empty database. */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

	private final AnnonceVenteRepository annonceRepository;

	@Override
	@Transactional
	public void run(String... args) {
		if (annonceRepository.count() > 0) {
			return;
		}

		AnnonceVente golf = annonce(1L, "Volkswagen Golf 7 TDI - premiere main",
				"Entretien complet chez le concessionnaire, carnet a jour.", "62000", true, StatutAnnonce.PUBLIEE,
				vehicule("Volkswagen", "Golf 7", 2018, 95000, Carburant.DIESEL));
		addPhoto(golf, "https://placehold.co/800x600?text=Golf+7+avant", 1, true);
		addPhoto(golf, "https://placehold.co/800x600?text=Golf+7+interieur", 2, false);

		AnnonceVente peugeot = annonce(2L, "Peugeot 208 Allure - etat neuf",
				"Vehicule sous garantie, jamais accidente.", "55000", false, StatutAnnonce.PUBLIEE,
				vehicule("Peugeot", "208", 2021, 42000, Carburant.ESSENCE));
		addPhoto(peugeot, "https://placehold.co/800x600?text=Peugeot+208", 1, true);

		AnnonceVente yaris = annonce(1L, "Toyota Yaris Hybride - faible consommation",
				"Annonce en cours de redaction.", "78000", true, StatutAnnonce.BROUILLON,
				vehicule("Toyota", "Yaris", 2022, 30000, Carburant.HYBRIDE));

		annonceRepository.saveAll(List.of(golf, peugeot, yaris));
		log.info("Seeded 3 sample listings");
	}

	private AnnonceVente annonce(Long vendeurId, String titre, String description, String prix, boolean negociable,
			StatutAnnonce statut, Vehicule vehicule) {
		return AnnonceVente.builder()
				.vendeurId(vendeurId)
				.titre(titre)
				.description(description)
				.prix(new BigDecimal(prix))
				.negociable(negociable)
				.statut(statut)
				.vehicule(vehicule)
				.build();
	}

	private Vehicule vehicule(String marque, String modele, int annee, int kilometrage, Carburant carburant) {
		return Vehicule.builder()
				.marque(marque)
				.modele(modele)
				.annee(annee)
				.kilometrage(kilometrage)
				.carburant(carburant)
				.build();
	}

	private void addPhoto(AnnonceVente annonce, String url, int ordre, boolean principale) {
		annonce.getPhotos().add(PhotoVehicule.builder()
				.url(url)
				.ordre(ordre)
				.principale(principale)
				.annonce(annonce)
				.build());
	}
}
