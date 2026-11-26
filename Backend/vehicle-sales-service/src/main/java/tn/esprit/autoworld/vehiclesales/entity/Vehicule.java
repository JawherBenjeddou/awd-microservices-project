package tn.esprit.autoworld.vehiclesales.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 60)
	private String marque;

	@Column(nullable = false, length = 60)
	private String modele;

	@Column(nullable = false)
	private Integer annee;

	@Column(nullable = false)
	private Integer kilometrage;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Carburant carburant;
}
