package tn.esprit.autoworld.vehiclesales.dto;

import jakarta.validation.constraints.NotNull;
import tn.esprit.autoworld.vehiclesales.entity.StatutOffre;

/** Body of PUT /api/offers/{id}/decision. Only ACCEPTEE and REFUSEE are accepted. */
public record DecisionOffreRequest(@NotNull StatutOffre decision) {
}
