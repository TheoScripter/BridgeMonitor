package fr.esisar.bridgemonitor.dto.plain.releve;

import jakarta.validation.constraints.NotNull;

public class DTOPlainReleveAnalogique extends DTOPlainReleve{

    @NotNull
    public Float valeur;
}
