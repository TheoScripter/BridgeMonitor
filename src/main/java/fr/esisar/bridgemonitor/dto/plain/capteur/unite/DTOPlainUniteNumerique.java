package fr.esisar.bridgemonitor.dto.plain.capteur.unite;

import jakarta.validation.constraints.NotBlank;

public class DTOPlainUniteNumerique {
    public Long id;
    @NotBlank
    public String etatHaut;
    @NotBlank
    public String etatBas;
}
