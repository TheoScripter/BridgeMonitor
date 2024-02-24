package fr.esisar.bridgemonitor.mapper.capteur;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import fr.esisar.bridgemonitor.dto.capteur.DTOCapteur;
import fr.esisar.bridgemonitor.dto.capteur.DTOCapteurAnalogique;
import fr.esisar.bridgemonitor.dto.capteur.DTOCapteurNumerique;
import fr.esisar.bridgemonitor.dto.plain.capteur.DTOPlainCapteur;
import fr.esisar.bridgemonitor.dto.plain.capteur.DTOPlainCapteurAnalogique;
import fr.esisar.bridgemonitor.dto.plain.capteur.DTOPlainCapteurNumerique;
import fr.esisar.bridgemonitor.dto.post.capteur.DTOPostCapteurAnalogique;
import fr.esisar.bridgemonitor.dto.post.capteur.DTOPostCapteurNumerique;
import fr.esisar.bridgemonitor.model.capteur.Capteur;
import fr.esisar.bridgemonitor.model.capteur.CapteurAnalogique;
import fr.esisar.bridgemonitor.model.capteur.CapteurNumerique;

@Mapper(componentModel = "jakarta", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CapteurMapper {

    default DTOCapteur fromCapteurToDTOCapteur(Capteur capteur){
        if (capteur instanceof CapteurAnalogique) {
            return fromCapteurAnalogiqueToDTOCapteurAnalogique((CapteurAnalogique) capteur);
        }else if (capteur instanceof CapteurNumerique) {
            return fromCapteurNumeriqueToDTOCapteurNumerique((CapteurNumerique) capteur);
        }
        throw new IllegalArgumentException("Capteur de type unknown");
    }

    default DTOPlainCapteur fromCapteurToDTOPlainCapteur(Capteur capteur){
        if (capteur instanceof CapteurAnalogique) {
            return fromCapteurAnalogiqueToDtoPlainCapteurAnalogique((CapteurAnalogique) capteur);
        }else if (capteur instanceof CapteurNumerique) {
            return fromCapteurNumeriqueToDtoPlainCapteurNumerique((CapteurNumerique) capteur);
        }
        throw new IllegalArgumentException("Capteur de type unknown");
    }

    DTOPlainCapteurAnalogique fromCapteurAnalogiqueToDtoPlainCapteurAnalogique(CapteurAnalogique capteurAnalogique);
    @InheritInverseConfiguration(name = "fromCapteurAnalogiqueToDtoPlainCapteurAnalogique")
    @Mapping(target = "pont", ignore = true)
    @Mapping(target = "releveAnalogiques", ignore = true)
    @Mapping(target = "uniteAnalogique", ignore = true)
    @Mapping(target = "periodicite", ignore = true)
    CapteurAnalogique toCapteurAnalogiqueFromDtoPlainCapteurAnalogique(DTOPlainCapteurAnalogique dtoPlainCapteurAnalogique);

    DTOPlainCapteurNumerique fromCapteurNumeriqueToDtoPlainCapteurNumerique(CapteurNumerique capteurNumerique);
    @InheritInverseConfiguration(name = "fromCapteurNumeriqueToDtoPlainCapteurNumerique")
    @Mapping(target = "pont", ignore = true)
    @Mapping(target = "releveNumeriques", ignore = true)
    @Mapping(target = "uniteNumerique", ignore = true)
    CapteurNumerique toCapteurNumeriqueFromDtoPlainCapteurNumerique(DTOPlainCapteurNumerique dtoPlainCapteurNumerique);

    DTOCapteurNumerique fromCapteurNumeriqueToDTOCapteurNumerique(CapteurNumerique capteurNumerique);
    @InheritInverseConfiguration(name = "fromCapteurNumeriqueToDTOCapteurNumerique")
    CapteurNumerique toCapteurNumeriqueFromDtoCapteurNumerique(DTOCapteurNumerique dtoCapteurNumerique);

    DTOCapteurAnalogique fromCapteurAnalogiqueToDTOCapteurAnalogique(CapteurAnalogique capteurAnalogique);
    @InheritInverseConfiguration(name = "fromCapteurAnalogiqueToDTOCapteurAnalogique")
    CapteurAnalogique toCapteurAnalogiqueFromDtoCapteurAnalogique(DTOCapteurAnalogique capteurAnalogique);

    DTOPostCapteurNumerique fromCapteurNumeriqueToDtoPostCapteurNumerique(CapteurNumerique capteurNumerique);
    @InheritInverseConfiguration(name = "fromCapteurNumeriqueToDtoPostCapteurNumerique")
    CapteurNumerique toCapteurNumeriqueFromDtoPostCapteurNumerique(DTOPostCapteurNumerique dtoPostCapteurNumerique);

    DTOPostCapteurAnalogique fromCapteurAnalogiqueToDtoPostCapteurAnalogique(CapteurAnalogique capteurAnalogique);
    @InheritInverseConfiguration(name = "fromCapteurAnalogiqueToDtoPostCapteurAnalogique")
    CapteurAnalogique toCapteurAnalogiqueFromDtoPostCapteurAnalogique(DTOPostCapteurAnalogique dtoPostCapteurAnalogique);
}
