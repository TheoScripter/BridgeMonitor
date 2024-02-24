package fr.esisar.bridgemonitor.dto.post.pont;

import java.util.Set;

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainPont;
import jakarta.validation.constraints.NotNull;

public class DTOPostPont extends DTOPlainPont{
    
    @NotNull
    public Long etatId;

    @NotNull
    public Long regionId;

    public Set<Long> capteursId;
}
