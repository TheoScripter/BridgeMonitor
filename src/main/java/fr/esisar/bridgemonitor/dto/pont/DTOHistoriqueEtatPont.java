package fr.esisar.bridgemonitor.dto.pont;

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainEtat;
import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainHistoriqueEtatPont;
import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainPont;

public class DTOHistoriqueEtatPont extends DTOPlainHistoriqueEtatPont{
    public DTOPlainEtat etat;
    public DTOPlainPont pont;

}
