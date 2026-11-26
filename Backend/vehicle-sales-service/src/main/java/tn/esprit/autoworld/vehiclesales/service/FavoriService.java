package tn.esprit.autoworld.vehiclesales.service;

import tn.esprit.autoworld.vehiclesales.dto.FavoriResponse;

public interface FavoriService {

	FavoriResponse add(Long annonceId, Long userId);

	void remove(Long annonceId, Long userId);
}
