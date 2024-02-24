package fr.esisar.bridgemonitor.dto.capteur;

import fr.esisar.bridgemonitor.dto.plain.capteur.DTOPlainCapteur;
import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainPont;

public abstract class DTOCapteur extends DTOPlainCapteur{
    public DTOPlainPont pont;
}
