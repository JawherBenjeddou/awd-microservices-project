package tn.esprit.autoworld.vehiclesales.service;

import java.math.BigDecimal;
import java.util.List;

import tn.esprit.autoworld.vehiclesales.dto.AnnonceVenteRequest;
import tn.esprit.autoworld.vehiclesales.dto.AnnonceVenteResponse;
import tn.esprit.autoworld.vehiclesales.dto.PhotoVehiculeRequest;
import tn.esprit.autoworld.vehiclesales.dto.PhotoVehiculeResponse;
import tn.esprit.autoworld.vehiclesales.entity.StatutAnnonce;

public interface AnnonceVenteService {

	/** Creates the listing and its vehicle in status BROUILLON. */
	AnnonceVenteResponse create(AnnonceVenteRequest request);

	/** Every filter is optional. Newest listings first. */
	List<AnnonceVenteResponse> search(String marque, BigDecimal prixMin, BigDecimal prixMax, StatutAnnonce statut);

	AnnonceVenteResponse getById(Long id);

	AnnonceVenteResponse update(Long id, AnnonceVenteRequest request);

	void delete(Long id);

	/** BROUILLON -> PUBLIEE. */
	AnnonceVenteResponse publish(Long id);

	/** RESERVEE -> VENDUE. */
	AnnonceVenteResponse markAsSold(Long id);

	PhotoVehiculeResponse addPhoto(Long id, PhotoVehiculeRequest request);

	/** Returns the listings in the order of the given ids. */
	List<AnnonceVenteResponse> compare(List<Long> ids);
}
