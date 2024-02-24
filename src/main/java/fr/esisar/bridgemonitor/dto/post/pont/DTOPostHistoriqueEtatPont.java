package fr.esisar.bridgemonitor.dto.post.pont;

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainHistoriqueEtatPont;
import jakarta.validation.constraints.NotNull;

public class DTOPostHistoriqueEtatPont extends DTOPlainHistoriqueEtatPont{
    @NotNull
    public Long pontId;

    @NotNull
    public Long etatId;
}
