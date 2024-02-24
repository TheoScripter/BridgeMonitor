package fr.esisar.bridgemonitor.dto.plain.capteur.unite;

import jakarta.validation.constraints.NotBlank;

public class DTOPlainUniteAnalogique {
    public Long id;
    @NotBlank
    public String nom;
    public String symbole;
}
