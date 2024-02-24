package fr.esisar.bridgemonitor.mapper.releve;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import fr.esisar.bridgemonitor.dto.plain.releve.DTOPlainReleve;
import fr.esisar.bridgemonitor.dto.plain.releve.DTOPlainReleveAnalogique;
import fr.esisar.bridgemonitor.dto.plain.releve.DTOPlainReleveNumerique;
import fr.esisar.bridgemonitor.dto.post.releve.DTOPostReleveAnalogique;
import fr.esisar.bridgemonitor.dto.post.releve.DTOPostReleveNumerique;
import fr.esisar.bridgemonitor.dto.releve.DTOReleve;
import fr.esisar.bridgemonitor.dto.releve.DTOReleveAnalogique;
import fr.esisar.bridgemonitor.dto.releve.DTOReleveNumerique;
import fr.esisar.bridgemonitor.model.releve.Releve;
import fr.esisar.bridgemonitor.model.releve.ReleveAnalogique;
import fr.esisar.bridgemonitor.model.releve.ReleveNumerique;

@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReleveMapper {

    //DTO Plain mapping

    default DTOPlainReleve fromReleveToPlainReleve(Releve releve){
        if (releve instanceof ReleveAnalogique){
            return fromReleveAnalogiqueToDtoPlainReleveAnalogique((ReleveAnalogique) releve);
        }
        if (releve instanceof ReleveNumerique){
            return fromReleveNumeriqueToDtoPlainReleveNumerique((ReleveNumerique) releve);
        }
        throw new IllegalArgumentException("Releve de type unknown");
    }

    DTOPlainReleveNumerique fromReleveNumeriqueToDtoPlainReleveNumerique(ReleveNumerique releveNumerique);

    @InheritInverseConfiguration(name = "fromReleveNumeriqueToDtoPlainReleveNumerique")
    @Mapping(target = "capteurNumerique", ignore = true)
    ReleveNumerique toReleveNumeriqueFromDtoPlainReleveNumerique(DTOPlainReleveNumerique dtoPlainReleveNumerique);

    DTOPlainReleveAnalogique fromReleveAnalogiqueToDtoPlainReleveAnalogique(ReleveAnalogique releveAnalogique);

    @InheritInverseConfiguration(name = "fromReleveAnalogiqueToDtoPlainReleveAnalogique")
    @Mapping(target = "capteurAnalogique", ignore = true)
    ReleveAnalogique toReleveAnalogiqueFromDtoPlainReleveAnalogique(DTOPlainReleveAnalogique dtoPlainReleveAnalogique);

    //DTO mapping

    default DTOReleve fromReleveToDtoReleve(Releve releve){
        if (releve instanceof ReleveAnalogique){
            return fromReleveAnalogiqueToDtoReleveAnalogique((ReleveAnalogique) releve);
        }
        if (releve instanceof ReleveNumerique){
            return fromReleveNumeriqueToDtoReleveNumerique((ReleveNumerique) releve);
        }
        throw new IllegalArgumentException("Releve de type unknown");
    }

    DTOReleveAnalogique fromReleveAnalogiqueToDtoReleveAnalogique(ReleveAnalogique releveAnalogique);

    @InheritInverseConfiguration(name = "fromReleveAnalogiqueToDtoReleveAnalogique")
    ReleveAnalogique toReleveAnalogiqueFromDtoReleveAnalogique(DTOReleveAnalogique dtoReleveAnalogique);

    DTOReleveNumerique fromReleveNumeriqueToDtoReleveNumerique(ReleveNumerique releveNumerique);

    @InheritInverseConfiguration(name = "fromReleveNumeriqueToDtoReleveNumerique")
    ReleveNumerique toReleveNumeriqueFromDtoReleveNumerique(DTOReleveNumerique dtoReleveNumerique);

    //DTO Post mapping

    DTOPostReleveAnalogique fromReleveAnalogiqueToDtoPostReleveAnalogique(ReleveAnalogique releveAnalogique);

    @InheritInverseConfiguration(name = "fromReleveAnalogiqueToDtoPostReleveAnalogique")
    ReleveAnalogique toReleveAnalogiqueFromDtoPostReleveAnalogique(DTOPostReleveAnalogique dtoPostReleveAnalogique);

    DTOPostReleveNumerique fromReleveNumeriqueToDtoPostReleveNumerique(ReleveNumerique releveNumerique);

    @InheritInverseConfiguration(name = "fromReleveNumeriqueToDtoPostReleveNumerique")
    ReleveNumerique toReleveNumeriqueFromDtoPostReleveNumerique(DTOPostReleveNumerique dtoPostReleveNumerique);


    
}
