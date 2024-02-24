package fr.esisar.bridgemonitor.mapper.pont;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainHistoriqueEtatPont;
import fr.esisar.bridgemonitor.dto.pont.DTOHistoriqueEtatPont;
import fr.esisar.bridgemonitor.model.pont.HistoriqueEtatPont;

@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HistoriqueEtatPontMapper {
	
	//DTOPlainHistoriqueEtatPont mapping
	DTOPlainHistoriqueEtatPont fromHistoriqueToDTOPlainHistoriqueEtatPont(HistoriqueEtatPont historique);
	@InheritInverseConfiguration (name = "fromHistoriqueToDTOPlainHistoriqueEtatPont")
	@Mapping(target = "pont", ignore = true)
	@Mapping(target = "etat", ignore = true)
	HistoriqueEtatPont toHistoriqueFromDTOPlainHistorique(DTOPlainHistoriqueEtatPont dtoHistorique);

	//DTOHistoriqueEtatPont mapping with only etat
	DTOHistoriqueEtatPont fromHistoriqueEtatPontToDTOHistoriqueEtatPontWithEtat(HistoriqueEtatPont etatPont);
	@InheritInverseConfiguration (name = "fromHistoriqueEtatPontToDTOHistoriqueEtatPontWithEtat")
	@Mapping(target = "pont", ignore = true)
	HistoriqueEtatPont toHistoriqueEtatPontFromDTOHistoriqueEtatPontWithEtat(DTOHistoriqueEtatPont dtoHistoriqueEtatPont);
	
	//DTOHistoriqueEtatPont mapping
	DTOHistoriqueEtatPont fromHistoriqueEtatPontToDTOHistoriqueEtatPont(HistoriqueEtatPont etatPont);
	@InheritInverseConfiguration (name = "fromHistoriqueEtatPontToDTOHistoriqueEtatPont")
	HistoriqueEtatPont toHistoriqueEtatPontFromDTOHistoriqueEtatPont(DTOHistoriqueEtatPont dtoHistoriqueEtatPont);
}
