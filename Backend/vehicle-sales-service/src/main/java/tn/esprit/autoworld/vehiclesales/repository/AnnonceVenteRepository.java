package tn.esprit.autoworld.vehiclesales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import tn.esprit.autoworld.vehiclesales.entity.AnnonceVente;

public interface AnnonceVenteRepository
		extends JpaRepository<AnnonceVente, Long>, JpaSpecificationExecutor<AnnonceVente> {
}
