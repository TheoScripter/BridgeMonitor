package fr.esisar.bridgemonitor.resource.releve;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.NoCache;

import fr.esisar.bridgemonitor.dto.post.releve.DTOPostReleveAnalogique;
import fr.esisar.bridgemonitor.dto.post.releve.DTOPostReleveNumerique;
import fr.esisar.bridgemonitor.dto.releve.DTOReleve;
import fr.esisar.bridgemonitor.dto.releve.DTOReleveAnalogique;
import fr.esisar.bridgemonitor.dto.releve.DTOReleveNumerique;
import fr.esisar.bridgemonitor.mapper.releve.ReleveMapper;
import fr.esisar.bridgemonitor.model.capteur.CapteurAnalogique;
import fr.esisar.bridgemonitor.model.capteur.CapteurNumerique;
import fr.esisar.bridgemonitor.model.releve.Releve;
import fr.esisar.bridgemonitor.model.releve.ReleveAnalogique;
import fr.esisar.bridgemonitor.model.releve.ReleveNumerique;
import io.quarkus.panache.common.Sort;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/releves")
public class ReleveResource {

    @Inject
    ReleveMapper mapper;

    @GET
    @RolesAllowed({"admin", "operateur"})
    @NoCache
    @Operation(summary = "Retourne tous les releves")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON))
	@APIResponse(responseCode = "204", description = "Aucun releve")
    public Response getAllReleves(){
        List<Releve> releves = Releve.listAll(Sort.by("id"));
        if (releves.isEmpty()) {
            return Response.noContent().build();
        }
        List<DTOReleve> dtoReleves = releves.stream().map(releve -> mapper.fromReleveToDtoReleve(releve)).collect(Collectors.toList());

        return Response.ok(dtoReleves).build();
    }

    @GET
    @RolesAllowed({"admin", "operateur"})
    @NoCache
    @Path("/analogiques")
    @Operation(summary = "Retourne tous les releves analogiques")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON,  schema = @Schema(implementation = DTOReleveAnalogique.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Aucun releve analogique")
    public Response getRelevesAnalogiques(){
        List<ReleveAnalogique> releves = ReleveAnalogique.listAll(Sort.by("id"));
        if (releves.isEmpty()) {
            return Response.noContent().build();
        }
        List<DTOReleveAnalogique> dtoReleves = releves.stream().map(releve -> mapper.fromReleveAnalogiqueToDtoReleveAnalogique(releve)).collect(Collectors.toList());

        return Response.ok(dtoReleves).build();
    }

    @GET
    @RolesAllowed({"admin", "operateur"})
    @NoCache
    @Path("/numeriques")
    @Operation(summary = "Retourne tous les releves numeriques")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON,  schema = @Schema(implementation = DTOReleveNumerique.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Aucun releve numeriques")
    public Response getRelevesNumeriques(){
        List<ReleveNumerique> releves = ReleveNumerique.listAll(Sort.by("id"));
        if (releves.isEmpty()) {
            return Response.noContent().build();
        }
        List<DTOReleveNumerique> dtoReleves = releves.stream().map(releve -> mapper.fromReleveNumeriqueToDtoReleveNumerique(releve)).collect(Collectors.toList());

        return Response.ok(dtoReleves).build();
    }

    @GET
    @RolesAllowed({"admin", "operateur"})
    @NoCache
    @Path("/{id}")
    @Operation(summary = "Retourne un releve a partir de son id")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOReleve.class)))
	@APIResponse(responseCode = "204", description = "Aucun releve avec cet id")
    public Response getReleve(@PathParam("id") Long id){
        Releve releve = Releve.findById(id);
        if(releve == null){
            return Response.noContent().build();
        }
        return Response.ok(mapper.fromReleveToDtoReleve(releve)).build();
    }
    
    @POST
    @Path("/analogiques")
    @Operation(summary = "Crée un releve analogique")
    @APIResponse(responseCode = "201", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostReleveAnalogique.class)))
	@APIResponse(responseCode = "400", description = "Aucun capteur analogique avec cet identifiant")
    @Transactional
    public Response createReleveAnalogique(@Valid DTOPostReleveAnalogique dtoPostReleve){
        ReleveAnalogique releve = mapper.toReleveAnalogiqueFromDtoPostReleveAnalogique(dtoPostReleve);
        releve.id = null;

        CapteurAnalogique capteur = CapteurAnalogique.findById(dtoPostReleve.capteurAnalogiqueId);
        if (capteur == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        releve.capteurAnalogique = capteur;
        releve.persist();
        return Response.status(Status.CREATED).entity(mapper.fromReleveAnalogiqueToDtoReleveAnalogique(releve)).build();
    }

    @POST
    @Path("/numeriques")
    @Operation(summary = "Crée un releve numerique")
    @APIResponse(responseCode = "201", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostReleveNumerique.class)))
	@APIResponse(responseCode = "400", description = "Aucun capteur numerique avec cet identifiant")
    @Transactional
    public Response createReleveNumerique(@Valid DTOPostReleveNumerique dtoPostReleve){
        ReleveNumerique releve = mapper.toReleveNumeriqueFromDtoPostReleveNumerique(dtoPostReleve);
        releve.id = null;

        CapteurNumerique capteur = CapteurNumerique.findById(dtoPostReleve.capteurNumeriqueId);
        if (capteur == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        releve.capteurNumerique = capteur;
        releve.persist();
        return Response.status(Status.CREATED).entity(mapper.fromReleveNumeriqueToDtoReleveNumerique(releve)).build();
    }

    @PUT
    @RolesAllowed({"admin", "operateur"})
    @NoCache
    @Path("/analogiques")
    @Operation(summary = "modifie un releve analogique")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostReleveAnalogique.class)))
	@APIResponse(responseCode = "400", description = "Aucun capteur analogique avec cet identifiant")
    @APIResponse(responseCode = "404", description = "Aucun releve analogique avec cet identifiant")
    @Transactional
    public Response updateReleveAnalogique(@Valid DTOPostReleveAnalogique dtoPostReleve){
        ReleveAnalogique entity = ReleveAnalogique.findById(dtoPostReleve.id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        CapteurAnalogique capteur = CapteurAnalogique.findById(dtoPostReleve.capteurAnalogiqueId);

        if (capteur == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        entity.dateTimeReleve = dtoPostReleve.dateTimeReleve;
        entity.valeur = dtoPostReleve.valeur;
        entity.capteurAnalogique = capteur;

        return Response.ok(mapper.fromReleveAnalogiqueToDtoReleveAnalogique(entity)).build();
    }
    

    @PUT
    @RolesAllowed({"admin", "operateur"})
    @NoCache
    @Path("/numeriques")
    @Operation(summary = "modifie un releve numerique")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostReleveNumerique.class)))
	@APIResponse(responseCode = "400", description = "Aucun capteur numerique avec cet identifiant")
    @APIResponse(responseCode = "404", description = "Aucun releve numerique avec cet identifiant")
    @Transactional
    public Response updateReleveNumerique(@Valid DTOPostReleveNumerique dtoPostReleve){
        ReleveNumerique entity = ReleveNumerique.findById(dtoPostReleve.id);
        if (entity == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        CapteurNumerique capteur = CapteurNumerique.findById(dtoPostReleve.capteurNumeriqueId);

        if (capteur == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        entity.dateTimeReleve = dtoPostReleve.dateTimeReleve;
        entity.valeur = dtoPostReleve.valeur;
        entity.capteurNumerique = capteur;

        return Response.ok(mapper.fromReleveNumeriqueToDtoReleveNumerique(entity)).build();
    }

    @DELETE
    @RolesAllowed({"admin", "operateur"})
    @NoCache
    @Path("/{id}")
    @Operation(summary = "supprime un releve")
    @APIResponse(responseCode = "204", description = "Releve supprimé")
    @APIResponse(responseCode = "404", description = "Aucun releve avec cet identifiant")
    @Transactional
    public Response deleteReleve(@PathParam("id") Long id){
        Releve releve = Releve.findById(id);
        if (releve == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        releve.delete();
        return Response.noContent().build();
    }

    

}
