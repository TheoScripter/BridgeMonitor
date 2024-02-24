package fr.esisar.bridgemonitor.dto.post.releve;

import fr.esisar.bridgemonitor.dto.plain.releve.DTOPlainReleveAnalogique;
import jakarta.validation.constraints.NotNull;

public class DTOPostReleveAnalogique extends DTOPlainReleveAnalogique{
    
    @NotNull
    public Long capteurAnalogiqueId;

}
