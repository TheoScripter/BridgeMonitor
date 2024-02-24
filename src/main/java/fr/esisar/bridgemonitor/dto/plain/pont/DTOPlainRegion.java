package fr.esisar.bridgemonitor.dto.plain.pont;

import jakarta.validation.constraints.NotBlank;

public class DTOPlainRegion {
	public Long id;
	@NotBlank
	public String nom;
    public Long codeRegion;
}
