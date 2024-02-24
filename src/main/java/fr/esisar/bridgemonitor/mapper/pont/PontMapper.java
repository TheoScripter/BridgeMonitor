
package fr.esisar.bridgemonitor.mapper.pont;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainPont;
import fr.esisar.bridgemonitor.dto.pont.DTOPont;
import fr.esisar.bridgemonitor.dto.post.pont.DTOPostPont;
import fr.esisar.bridgemonitor.mapper.capteur.CapteurMapper;
import fr.esisar.bridgemonitor.model.pont.Pont;

@Mapper(componentModel = "jakarta", uses = {CapteurMapper.class, HistoriqueEtatPontMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface PontMapper {

	//Pont simple avec sa region
	DTOPlainPont fromPontToDTOPlainPont(Pont pont);
	@InheritInverseConfiguration (name = "fromPontToDTOPlainPont")
	@Mapping(target = "historiqueEtatPonts", ignore = true)
	@Mapping(target = "capteurs", ignore = true)
	@Mapping(target = "etat", ignore = true)
	@Mapping(target = "region", ignore = true)
	Pont toPontFromDTOPlainPont(DTOPlainPont dtoPont);
	
	DTOPont fromPontToDTOPont(Pont pont);

	//Mapping in the other way is not done because it's passed by id reference
	@Mapping(target = "historiqueEtatPonts", ignore = true)
	@Mapping(target = "capteurs", ignore = true)
	@Mapping(target = "etat", ignore = true)
	@Mapping(target = "region", ignore = true)
	Pont toPontFromDtoPostPont(DTOPostPont dtoPostPont);
}
