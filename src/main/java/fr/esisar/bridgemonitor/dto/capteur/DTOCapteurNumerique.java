package fr.esisar.bridgemonitor.dto.capteur;

import java.util.Set;

import fr.esisar.bridgemonitor.dto.plain.releve.DTOPlainReleveNumerique;
import fr.esisar.bridgemonitor.dto.plain.capteur.unite.DTOPlainUniteNumerique;

public class DTOCapteurNumerique extends DTOCapteur{
    public DTOPlainUniteNumerique uniteNumerique;
    public Set<DTOPlainReleveNumerique> releveNumeriques;

}
