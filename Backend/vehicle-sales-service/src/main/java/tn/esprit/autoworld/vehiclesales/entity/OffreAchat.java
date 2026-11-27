package tn.esprit.autoworld.vehiclesales.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "offre_achat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OffreAchat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** Buyer profile id in MS5 (profiles). External reference: no foreign key. */
	@Column(nullable = false)
	private Long acheteurId;

	@Column(nullable = false, precision = 12, scale = 2)
	private BigDecimal montant;

	@Column(length = 1000)
	private String message;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private StatutOffre statut;

	@Column(nullable = false, updatable = false)
	private LocalDateTime dateOffre;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "annonce_id", nullable = false)
	@JsonIgnore
	private AnnonceVente annonce;

	@PrePersist
	void onCreate() {
		if (dateOffre == null) {
			dateOffre = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		}
		if (statut == null) {
			statut = StatutOffre.EN_ATTENTE;
		}
	}
}
