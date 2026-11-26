package tn.esprit.autoworld.vehiclesales.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FavoriRequest(@NotNull @Positive Long userId) {
}
