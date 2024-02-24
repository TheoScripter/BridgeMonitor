package fr.esisar.bridgemonitor.dto.pont;

import java.util.Set;

import fr.esisar.bridgemonitor.dto.plain.capteur.DTOPlainCapteur;
import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainEtat;
import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainHistoriqueEtatPont;
import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainPont;
import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainRegion;
import jakarta.validation.constraints.NotNull;

public class DTOPont extends DTOPlainPont{

    @NotNull
    public DTOPlainRegion region;

    @NotNull
    public DTOPlainEtat etat;

    public Set<DTOPlainHistoriqueEtatPont> historiqueEtatPonts;

    public Set<DTOPlainCapteur> capteurs; 
  
}
