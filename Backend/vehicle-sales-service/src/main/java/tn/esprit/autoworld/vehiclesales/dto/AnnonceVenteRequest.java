package tn.esprit.autoworld.vehiclesales.dto;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/** Body of POST and PUT /api/listings. The status is not part of it: it only changes through the dedicated endpoints. */
public record AnnonceVenteRequest(
		@NotNull @Positive Long vendeurId,
		@NotBlank @Size(max = 150) String titre,
		@Size(max = 2000) String description,
		@NotNull @Positive BigDecimal prix,
		Boolean negociable,
		@NotNull @Valid VehiculeRequest vehicule) {
}
