package fr.esisar.bridgemonitor.dto.plain.pont;

import jakarta.validation.constraints.NotBlank;

public class DTOPlainEtat {
	public Long id;
	@NotBlank
	public String nom;	
}
