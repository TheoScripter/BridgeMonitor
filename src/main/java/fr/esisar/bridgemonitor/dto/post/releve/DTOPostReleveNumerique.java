package fr.esisar.bridgemonitor.dto.post.releve;

import fr.esisar.bridgemonitor.dto.plain.releve.DTOPlainReleveNumerique;
import jakarta.validation.constraints.NotNull;

public class DTOPostReleveNumerique extends DTOPlainReleveNumerique{
    
    @NotNull
    public Long capteurNumeriqueId;

}
