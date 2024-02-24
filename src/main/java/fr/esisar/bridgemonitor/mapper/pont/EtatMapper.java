package fr.esisar.bridgemonitor.mapper.pont;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainEtat;
import fr.esisar.bridgemonitor.model.pont.Etat;

@Mapper(componentModel = "jakarta")
public interface EtatMapper {

	DTOPlainEtat fromEtatToPlainEtat(Etat etat);
	@InheritInverseConfiguration(name= "fromEtatToPlainEtat")
	Etat toEtatFromPlainEtat(DTOPlainEtat dtoEtat);
	
}
