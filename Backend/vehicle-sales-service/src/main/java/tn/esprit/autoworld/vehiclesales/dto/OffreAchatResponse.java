package tn.esprit.autoworld.vehiclesales.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import tn.esprit.autoworld.vehiclesales.entity.StatutAnnonce;
import tn.esprit.autoworld.vehiclesales.entity.StatutOffre;

public record OffreAchatResponse(
		Long id,
		Long annonceId,
		StatutAnnonce statutAnnonce,
		Long acheteurId,
		BigDecimal montant,
		String message,
		StatutOffre statut,
		LocalDateTime dateOffre) {
}
