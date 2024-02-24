package fr.esisar.bridgemonitor.dto.pont;

import java.util.Set;

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainPont;
import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainRegion;

public class DTORegion  extends DTOPlainRegion{
    public Set<DTOPlainPont> ponts; 
}
