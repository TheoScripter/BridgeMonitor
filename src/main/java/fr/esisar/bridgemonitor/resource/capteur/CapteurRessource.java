package fr.esisar.bridgemonitor.resource.capteur;

import java.io.StringWriter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.NoCache;
import org.jboss.resteasy.reactive.RestResponse.StatusCode;

import fr.esisar.bridgemonitor.dto.plain.capteur.DTOPlainCapteur;
import fr.esisar.bridgemonitor.dto.plain.capteur.DTOPlainCapteurAnalogique;
import fr.esisar.bridgemonitor.dto.post.capteur.DTOPostCapteurAnalogique;
import fr.esisar.bridgemonitor.dto.post.capteur.DTOPostCapteurNumerique;
import fr.esisar.bridgemonitor.dto.releve.DTOReleve;
import fr.esisar.bridgemonitor.mapper.capteur.CapteurMapper;
import fr.esisar.bridgemonitor.mapper.releve.ReleveMapper;
import fr.esisar.bridgemonitor.model.capteur.Capteur;
import fr.esisar.bridgemonitor.model.capteur.CapteurAnalogique;
import fr.esisar.bridgemonitor.model.capteur.CapteurNumerique;
import fr.esisar.bridgemonitor.model.capteur.unite.UniteAnalogique;
import fr.esisar.bridgemonitor.model.capteur.unite.UniteNumerique;
import fr.esisar.bridgemonitor.model.pont.Pont;
import fr.esisar.bridgemonitor.model.releve.ReleveAnalogique;
import fr.esisar.bridgemonitor.model.releve.ReleveNumerique;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/capteurs")
public class CapteurRessource {

    @Inject
    CapteurMapper mapper;

    @Inject
    ReleveMapper releveMapper;

    @GET
    @RolesAllowed({"admin", "operateur"})
    @NoCache
    @Operation(summary = "Retourne tous les capteurs")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainCapteur.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Aucun capteur")
    public Response getAllCapteurs(){
        List<Capteur> capteurs = Capteur.listAll();

        if(capteurs.size() == 0){
            return Response.status(StatusCode.NO_CONTENT).build();
        }

        List<DTOPlainCapteur> dtoPlainCapteurs = capteurs.stream()
            .map(capteur -> mapper.fromCapteurToDTOCapteur(capteur))
            .collect(Collectors.toList());

        return Response.ok(dtoPlainCapteurs).build();
    }

        
    @GET
    @RolesAllowed({"admin", "operateur"})
    @NoCache
	@Path("/{id}")
	@Operation(summary = "Retourne un capteur à partir de son id")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainCapteur.class)))
	@APIResponse(responseCode = "204", description = "Aucun capteur avec cet identifiant")
	public Response getCapteurById(@PathParam("id") Long id) {
		Capteur capteur = Capteur.findById(id);

        if(capteur == null){
            return Response.status(StatusCode.NO_CONTENT).build();
        }

        return Response.ok(mapper.fromCapteurToDTOCapteur(capteur)).build();
	}


    @GET
    @RolesAllowed({"admin", "operateur"})
    @NoCache
	@Path("/{id}/releves")
	@Operation(summary = "Retourne les relevés à partir de l'id du capteurs")
    @Produces({MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN})
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainCapteur.class)))
	@APIResponse(responseCode = "204", description = "Aucun capteur avec cet identifiant")
	public Response getRelevesOfCapteurById(@PathParam("id") Long id, @DefaultValue("application/json") @HeaderParam("Content-Type") String contentType) {
		Capteur capteurFind = Capteur.findById(id);

        StringWriter writer = new StringWriter();
        
        writer.write("id; dateTime; valeur\n");

        Set<DTOReleve> dtoReleves;

        if(capteurFind == null){
            return Response.status(StatusCode.NO_CONTENT).build();
        }

        String fileName = "releves_"+ capteurFind.nom + ".csv";
        String headerName = "attachment; filename=" + fileName;

        
        if(capteurFind instanceof CapteurAnalogique){
            CapteurAnalogique capteur = CapteurAnalogique.findById(id);
            Set<ReleveAnalogique> releves = capteur.releveAnalogiques;
            if(contentType.equals("text/csv")){
                for(ReleveAnalogique releve : releves){
                    writer.write(releve.id + ";" + releve.dateTimeReleve + ";" + releve.valeur + "\n");
                }
                return Response.ok(writer).header("Content-Disposition", headerName).header("Content-Type", "text/csv").build();
            }
            dtoReleves = releves.stream()
                .map(releve -> releveMapper.fromReleveAnalogiqueToDtoReleveAnalogique(releve))
                .collect(Collectors.toSet());

        }else{
            CapteurNumerique capteur = CapteurNumerique.findById(id);
            Set<ReleveNumerique> releves = capteur.releveNumeriques;
            if(contentType.equals("text/csv")){
                for(ReleveNumerique releve : releves){
                    writer.write(releve.id + ";" + releve.dateTimeReleve + ";" + releve.valeur + "\n");
                }
                return Response.ok(writer).header("Content-Disposition", headerName).header("Content-Type", "text/csv").build();

            }
            dtoReleves = releves.stream()
                .map(releve -> releveMapper.fromReleveNumeriqueToDtoReleveNumerique(releve))
                .collect(Collectors.toSet());

        }

        return Response.ok(dtoReleves).build();
	}

    @GET
    @RolesAllowed({"admin", "operateur"})
    @NoCache
    @Path("/analogiques")
    @Operation(summary = "Retourne tous les capteurs analogiques")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainCapteurAnalogique.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Aucun capteur")
    public Response getAllCapteursAnalogiques(){
        List<Capteur> capteurs = CapteurAnalogique.listAll();

        if(capteurs.size() == 0){
            return Response.status(StatusCode.NO_CONTENT).build();
        }

        List<DTOPlainCapteur> dtoPlainCapteurs = capteurs.stream()
            .map(capteur -> mapper.fromCapteurToDTOCapteur(capteur))
            .collect(Collectors.toList());

        return Response.ok(dtoPlainCapteurs).build();
    }

    @GET
    @RolesAllowed({"admin", "operateur"})
    @NoCache
    @Path("/numeriques")
    @Operation(summary = "Retourne tous les capteurs numeriques")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainCapteur.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Aucun capteur")
    public Response getAllCapteursNumeriques(){
        List<Capteur> capteurs = CapteurNumerique.listAll();

        if(capteurs.size() == 0){
            return Response.status(StatusCode.NO_CONTENT).build();
        }

        List<DTOPlainCapteur> dtoPlainCapteurs = capteurs.stream()
            .map(capteur -> mapper.fromCapteurToDTOCapteur(capteur))
            .collect(Collectors.toList());

        return Response.ok(dtoPlainCapteurs).build();
    }

    @POST
    @RolesAllowed({"admin"})
    @NoCache
    @Path("/analogiques")
    @Transactional
    @Operation(summary = "Crée un capteur analogique")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostCapteurAnalogique.class)))
	@APIResponse(responseCode = "204", description = "Aucun capteur avec cet identifiant")
    public Response createCapteurAnalogique(@Valid DTOPostCapteurAnalogique dtoPostCapteurAnalogique){
        // Map DTO to entity
        CapteurAnalogique capteurAnalogique = mapper.toCapteurAnalogiqueFromDtoPostCapteurAnalogique(dtoPostCapteurAnalogique);
            
        // Set the ID to null to ensure a new entity is created
        capteurAnalogique.id = null;

        // Fetch associated entities (Pont and UniteAnalog)
        Pont pont = Pont.findById(dtoPostCapteurAnalogique.pontId);
        UniteAnalogique uniteAnalogique = UniteAnalogique.findById(dtoPostCapteurAnalogique.uniteAnalogiqueId);

        // Check if UniteAnalog exists
        if (uniteAnalogique == null) {
            return Response.status(Status.BAD_REQUEST).entity("UniteAnalogique not found").build();
        }

        // Associate the fetched entities with the CapteurAnalogique
        capteurAnalogique.pont = pont;
        capteurAnalogique.uniteAnalogique = uniteAnalogique;

        // Persist the CapteurAnalogique entity
        capteurAnalogique.persist();

        // Return the created entity as a response
        return Response.status(Status.CREATED).entity(mapper.fromCapteurAnalogiqueToDTOCapteurAnalogique(capteurAnalogique)).build();
   }

    @POST
    @RolesAllowed({"admin"})
    @NoCache
    @Path("/numeriques")
    @Transactional
    @Operation(summary = "Crée un capteur numérique")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostCapteurAnalogique.class)))
	@APIResponse(responseCode = "204", description = "Aucun capteur avec cet identifiant")
    public Response createCapteurNumerique(@Valid DTOPostCapteurNumerique dtoPostCapteurNumerique){
        // Map DTO to entity
        CapteurNumerique capteurNumerique = mapper.toCapteurNumeriqueFromDtoPostCapteurNumerique(dtoPostCapteurNumerique);
            
        // Set the ID to null to ensure a new entity is created
        capteurNumerique.id = null;

        // Fetch associated entities (Pont and UniteAnalog)
        Pont pont = Pont.findById(dtoPostCapteurNumerique.pontId);
        UniteNumerique uniteNumerique = UniteNumerique.findById(dtoPostCapteurNumerique.uniteNumeriqueId);

        // Check if UniteNumerique exists
        if (uniteNumerique == null) {
            return Response.status(Status.BAD_REQUEST).entity("UniteNumerique not found").build();
        }

        // Associate the fetched entities with the CapteurAnalogique
        capteurNumerique.pont = pont;
        capteurNumerique.uniteNumerique = uniteNumerique;

        // Persist the CapteurAnalogique entity
        capteurNumerique.persist();

        // Return the created entity as a response
        return Response.status(Status.CREATED).entity(mapper.fromCapteurNumeriqueToDTOCapteurNumerique(capteurNumerique)).build();
    }

    @PUT
    @RolesAllowed({"admin"})
    @NoCache
    @Path("/analogiques")
    @Transactional
    @Operation(summary = "Modifie un capteur analogique")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostCapteurAnalogique.class)))
	@APIResponse(responseCode = "204", description = "Aucun capteur avec cet identifiant")
    public Response updateCapteurAnalogique(@Valid DTOPostCapteurAnalogique dtoPostCapteurAnalogique){
        
        CapteurAnalogique capteurAnalogique = CapteurAnalogique.findById(dtoPostCapteurAnalogique.id);

        if(capteurAnalogique == null){
            return Response.noContent().build();
        }

        capteurAnalogique.nom = dtoPostCapteurAnalogique.nom;
        capteurAnalogique.description = dtoPostCapteurAnalogique.description;
        capteurAnalogique.numeroSerie = dtoPostCapteurAnalogique.numeroSerie;
        capteurAnalogique.periodicite = dtoPostCapteurAnalogique.periodicite;


        // Fetch associated entities (Pont and UniteAnalog)
        Pont pont = Pont.findById(dtoPostCapteurAnalogique.pontId);
        UniteAnalogique uniteAnalogique = UniteAnalogique.findById(dtoPostCapteurAnalogique.uniteAnalogiqueId);

        // Check if UniteAnalog exists
        if (uniteAnalogique == null) {
            return Response.status(Status.BAD_REQUEST).entity("UniteAnalogique not found").build();
        }

        // Associate the fetched entities with the CapteurAnalogique
        capteurAnalogique.pont = pont;
        capteurAnalogique.uniteAnalogique = uniteAnalogique;

        // Return the created entity as a response
        return Response.status(Status.OK).entity(mapper.fromCapteurAnalogiqueToDTOCapteurAnalogique(capteurAnalogique)).build();
    }

    @PUT
    @RolesAllowed({"admin"})
    @NoCache
    @Path("/numeriques")
    @Transactional
    @Operation(summary = "Modifie un capteur numerique")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostCapteurAnalogique.class)))
	@APIResponse(responseCode = "204", description = "Aucun capteur avec cet identifiant")
    public Response updateCapteurNumerique(@Valid DTOPostCapteurNumerique dtoPostCapteurNumerique){
        // Map DTO to entity
        CapteurNumerique capteurNumerique = CapteurNumerique.findById(dtoPostCapteurNumerique.id);

        if(capteurNumerique == null){
            return Response.noContent().build();
        }
          
        capteurNumerique.nom = dtoPostCapteurNumerique.nom;
        capteurNumerique.description = dtoPostCapteurNumerique.description;
        capteurNumerique.numeroSerie = dtoPostCapteurNumerique.numeroSerie;

        // Fetch associated entities (Pont and UniteAnalog)
        Pont pont = Pont.findById(dtoPostCapteurNumerique.pontId);
        UniteNumerique uniteNumerique = UniteNumerique.findById(dtoPostCapteurNumerique.uniteNumeriqueId);

        // Check if UniteNumerique exists
        if (uniteNumerique == null) {
            return Response.status(Status.BAD_REQUEST).entity("UniteNumerique not found").build();
        }

        // Associate the fetched entities with the CapteurAnalogique
        capteurNumerique.pont = pont;
        capteurNumerique.uniteNumerique = uniteNumerique;



        // Return the created entity as a response
        return Response.status(Status.OK).entity(mapper.fromCapteurNumeriqueToDTOCapteurNumerique(capteurNumerique)).build();
    }

    @DELETE
    @RolesAllowed({"admin"})
    @NoCache
	@Path("/{id}")
    @Transactional
	@Operation(summary = "Supprime un capteur à partir de son id")
	@APIResponse(responseCode = "204", description = "Le capteur a été supprimé")
	@APIResponse(responseCode = "404", description = "Aucun capteur avec cet identifiant")
	public Response deleteCapteurById(@PathParam("id") Long id) {
		Capteur capteur = Capteur.findById(id);

        if(capteur == null){
            Response.status(StatusCode.NOT_FOUND).build();
        }

        capteur.delete();

        return Response.noContent().build();
	}


}
