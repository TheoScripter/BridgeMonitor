package fr.esisar.bridgemonitor.dto.releve;

import fr.esisar.bridgemonitor.dto.plain.capteur.DTOPlainCapteurNumerique;
import jakarta.validation.constraints.NotNull;

public class DTOReleveNumerique extends DTOReleve{

    @NotNull
    public Boolean valeur;

    @NotNull
    public DTOPlainCapteurNumerique capteurNumerique;
    
}
