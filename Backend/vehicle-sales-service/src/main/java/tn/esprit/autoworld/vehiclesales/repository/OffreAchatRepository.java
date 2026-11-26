package tn.esprit.autoworld.vehiclesales.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import tn.esprit.autoworld.vehiclesales.entity.OffreAchat;
import tn.esprit.autoworld.vehiclesales.entity.StatutOffre;

public interface OffreAchatRepository extends JpaRepository<OffreAchat, Long> {

	List<OffreAchat> findByAnnonceIdAndStatut(Long annonceId, StatutOffre statut);
}
