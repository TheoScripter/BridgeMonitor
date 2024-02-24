package fr.esisar.bridgemonitor.mapper.pont;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainRegion;
import fr.esisar.bridgemonitor.dto.pont.DTORegion;
import fr.esisar.bridgemonitor.model.pont.Region;

@Mapper(componentModel = "jakarta", uses = {PontMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RegionMapper {
	
	//DTOPlainRegion mapping
	DTOPlainRegion fromRegionToDTOPlainRegion(Region region);
	@InheritInverseConfiguration(name= "fromRegionToDTOPlainRegion")
	@Mapping(target = "ponts", ignore = true)
	Region toRegionFromDTOPlainRegion(DTOPlainRegion dtoRegion);
	
	//DTORegion Mapping
	DTORegion fromRegionToDTORegion(Region region);
	@InheritInverseConfiguration(name = "fromRegionToDTORegion")
	Region toRegionfromDTORegion(DTORegion dtoRegion);
}
