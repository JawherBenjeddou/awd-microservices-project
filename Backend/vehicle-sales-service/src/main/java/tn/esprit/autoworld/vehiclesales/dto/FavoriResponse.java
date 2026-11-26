package tn.esprit.autoworld.vehiclesales.dto;

import java.time.LocalDateTime;

public record FavoriResponse(
		Long id,
		Long annonceId,
		Long userId,
		LocalDateTime dateAjout) {
}
