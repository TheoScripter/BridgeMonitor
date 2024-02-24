package fr.esisar.bridgemonitor.dto.releve;

import fr.esisar.bridgemonitor.dto.plain.capteur.DTOPlainCapteurAnalogique;
import jakarta.validation.constraints.NotNull;

public class DTOReleveAnalogique extends DTOReleve{

    @NotNull
    public Float valeur;

    @NotNull
    public DTOPlainCapteurAnalogique capteurAnalogique;
    
}
