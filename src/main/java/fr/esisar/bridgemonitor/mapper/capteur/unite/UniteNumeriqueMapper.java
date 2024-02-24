package fr.esisar.bridgemonitor.mapper.capteur.unite;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import fr.esisar.bridgemonitor.dto.capteur.unite.DTOUniteNumerique;
import fr.esisar.bridgemonitor.model.capteur.unite.UniteNumerique;

@Mapper(componentModel = "jakarta")
public interface UniteNumeriqueMapper {
    DTOUniteNumerique fromUniteNumeriqueToDTOUniteNumerique(UniteNumerique uniteNumerique);
    @InheritInverseConfiguration(name = "fromUniteNumeriqueToDTOUniteNumerique")
    UniteNumerique toUniteNumeriqueToDTOUniteNumerique(DTOUniteNumerique dtoUniteNumerique);
}
