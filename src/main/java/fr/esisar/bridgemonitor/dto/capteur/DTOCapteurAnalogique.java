package fr.esisar.bridgemonitor.dto.capteur;

import java.util.Set;

import fr.esisar.bridgemonitor.dto.plain.releve.DTOPlainReleveAnalogique;
import fr.esisar.bridgemonitor.dto.plain.capteur.unite.DTOPlainUniteAnalogique;

public class DTOCapteurAnalogique extends DTOCapteur{
    public Long periodicite;
    public DTOPlainUniteAnalogique uniteAnalogique;
    public Set<DTOPlainReleveAnalogique> releveAnalogiques;
}
