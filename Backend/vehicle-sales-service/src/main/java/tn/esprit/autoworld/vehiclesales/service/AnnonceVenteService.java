package tn.esprit.autoworld.vehiclesales.service;

import java.util.List;

import tn.esprit.autoworld.vehiclesales.entity.AnnonceVente;

public interface AnnonceVenteService {

	/** Creates the listing together with its vehicle. */
	AnnonceVente create(AnnonceVente annonce);

	List<AnnonceVente> findAll();

	AnnonceVente findById(Long id);

	/** Updates the listing together with its vehicle. */
	AnnonceVente update(Long id, AnnonceVente annonce);

	void delete(Long id);
}
