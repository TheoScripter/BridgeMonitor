package fr.esisar.bridgemonitor.mapper.capteur.unite;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import fr.esisar.bridgemonitor.dto.capteur.unite.DTOUniteAnalogique;
import fr.esisar.bridgemonitor.model.capteur.unite.UniteAnalogique;

@Mapper(componentModel = "jakarta")
public interface UniteAnalogiqueMapper {
    DTOUniteAnalogique fromUniteAnalogiqueToDTOUniteAnalogique(UniteAnalogique uniteAnalogique);
    @InheritInverseConfiguration(name = "fromUniteAnalogiqueToDTOUniteAnalogique")
    UniteAnalogique toUniteAnalogiqueFromDTOUniteAnalogique(DTOUniteAnalogique dtoUniteAnalogique);
}
