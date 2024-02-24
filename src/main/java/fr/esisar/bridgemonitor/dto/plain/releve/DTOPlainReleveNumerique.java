package fr.esisar.bridgemonitor.dto.plain.releve;

import jakarta.validation.constraints.NotNull;

public class DTOPlainReleveNumerique extends DTOPlainReleve{

    @NotNull
    public Boolean valeur;
}
