package tn.esprit.autoworld.vehiclesales.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record OffreAchatRequest(
		@NotNull @Positive Long acheteurId,
		@NotNull @Positive BigDecimal montant,
		@Size(max = 1000) String message) {
}
