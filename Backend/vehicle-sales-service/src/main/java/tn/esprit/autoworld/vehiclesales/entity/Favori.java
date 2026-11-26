package tn.esprit.autoworld.vehiclesales.entity;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "favori", uniqueConstraints = @UniqueConstraint(name = "uk_favori_user_annonce", columnNames = {
		"user_id", "annonce_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favori {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** User profile id in MS5 (profiles). External reference: no foreign key. */
	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Column(nullable = false, updatable = false)
	private LocalDateTime dateAjout;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "annonce_id", nullable = false)
	@JsonIgnore
	private AnnonceVente annonce;

	@PrePersist
	void onCreate() {
		if (dateAjout == null) {
			dateAjout = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		}
	}
}
