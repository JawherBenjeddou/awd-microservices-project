package tn.esprit.autoworld.vehiclesales.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import tn.esprit.autoworld.vehiclesales.entity.AnnonceVente;
import tn.esprit.autoworld.vehiclesales.entity.Vehicule;
import tn.esprit.autoworld.vehiclesales.exception.ResourceNotFoundException;
import tn.esprit.autoworld.vehiclesales.repository.AnnonceVenteRepository;
import tn.esprit.autoworld.vehiclesales.service.AnnonceVenteService;

@Service
@RequiredArgsConstructor
@Transactional
public class AnnonceVenteServiceImpl implements AnnonceVenteService {

	private final AnnonceVenteRepository annonceRepository;

	@Override
	public AnnonceVente create(AnnonceVente annonce) {
		// Ids come from the database, never from the request.
		annonce.setId(null);
		annonce.getVehicule().setId(null);
		// The vehicle is saved with the listing (cascade ALL).
		return annonceRepository.save(annonce);
	}

	@Override
	@Transactional(readOnly = true)
	public List<AnnonceVente> findAll() {
		return annonceRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public AnnonceVente findById(Long id) {
		return annonceRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Listing " + id + " not found"));
	}

	@Override
	public AnnonceVente update(Long id, AnnonceVente annonce) {
		AnnonceVente existing = findById(id);
		existing.setVendeurId(annonce.getVendeurId());
		existing.setTitre(annonce.getTitre());
		existing.setDescription(annonce.getDescription());
		existing.setPrix(annonce.getPrix());
		existing.setNegociable(annonce.isNegociable());
		if (annonce.getStatut() != null) {
			existing.setStatut(annonce.getStatut());
		}

		Vehicule vehicule = existing.getVehicule();
		vehicule.setMarque(annonce.getVehicule().getMarque());
		vehicule.setModele(annonce.getVehicule().getModele());
		vehicule.setAnnee(annonce.getVehicule().getAnnee());
		vehicule.setKilometrage(annonce.getVehicule().getKilometrage());
		vehicule.setCarburant(annonce.getVehicule().getCarburant());

		// "existing" is a managed entity: the changes are written when the transaction commits.
		return existing;
	}

	@Override
	public void delete(Long id) {
		annonceRepository.delete(findById(id));
	}
}
