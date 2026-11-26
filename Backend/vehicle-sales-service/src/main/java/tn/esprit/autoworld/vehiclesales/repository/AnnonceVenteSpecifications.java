package tn.esprit.autoworld.vehiclesales.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import tn.esprit.autoworld.vehiclesales.entity.AnnonceVente;
import tn.esprit.autoworld.vehiclesales.entity.StatutAnnonce;

/** Builds the WHERE clause of GET /api/listings from the optional filters. */
public final class AnnonceVenteSpecifications {

	private AnnonceVenteSpecifications() {
	}

	public static Specification<AnnonceVente> withFilters(String marque, BigDecimal prixMin, BigDecimal prixMax,
			StatutAnnonce statut) {
		return (root, query, cb) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (marque != null && !marque.isBlank()) {
				predicates.add(cb.equal(cb.lower(root.get("vehicule").get("marque")), marque.trim().toLowerCase()));
			}
			if (prixMin != null) {
				predicates.add(cb.greaterThanOrEqualTo(root.get("prix"), prixMin));
			}
			if (prixMax != null) {
				predicates.add(cb.lessThanOrEqualTo(root.get("prix"), prixMax));
			}
			if (statut != null) {
				predicates.add(cb.equal(root.get("statut"), statut));
			}
			return cb.and(predicates.toArray(new Predicate[0]));
		};
	}
}
