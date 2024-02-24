package fr.esisar.bridgemonitor.resource.capteur.unite;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.NoCache;

import fr.esisar.bridgemonitor.dto.capteur.unite.DTOUniteNumerique;
import fr.esisar.bridgemonitor.mapper.capteur.unite.UniteNumeriqueMapper;
import fr.esisar.bridgemonitor.model.capteur.unite.UniteNumerique;
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

@Path("/unitesnumeriques")
public class UniteNumeriqueRessource {
    @Inject
	UniteNumeriqueMapper mapper;
	
	@GET
	@RolesAllowed({"admin", "operateur"})
    @NoCache
	@Operation(summary = "Retourne toutes les unités numeriques existantes")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOUniteNumerique.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Aucune unité numerique")
	public Response getAllUnitesNumerique(){
		List<UniteNumerique> unitesNumeriques = UniteNumerique.listAll();
		List<DTOUniteNumerique> dtoUnitesNumeriques = unitesNumeriques.stream()
				.map(etat -> mapper.fromUniteNumeriqueToDTOUniteNumerique(etat))
				.collect(Collectors.toList());
				return Response.ok(dtoUnitesNumeriques).build();
	}
	
	@GET
	@RolesAllowed({"admin", "operateur"})
    @NoCache
	@Path("/{id}")
	@Operation(summary = "Retourne une unité numerique à partir de son id")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOUniteNumerique.class)))
	@APIResponse(responseCode = "204", description = "Aucune unité numerique avec cet identifiant")
	public Response getUniteNumerique(@PathParam("id") Long id) {
		UniteNumerique uniteNumerique = UniteNumerique.findById(id);
		if (uniteNumerique == null) {
		return Response.noContent().build();
		}
		return Response.ok(mapper.fromUniteNumeriqueToDTOUniteNumerique(uniteNumerique)).build();
	}
	
	
	@POST
	@RolesAllowed({"admin"})
    @NoCache
	@Operation(summary = "Cree un unité numerique à partir de son id")
    @APIResponse(responseCode = "201", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOUniteNumerique.class)))
	@Transactional
	public Response createUniteNumerique(@Valid DTOUniteNumerique dtoUniteNumerique) {
		UniteNumerique uniteNumerique = mapper.toUniteNumeriqueToDTOUniteNumerique(dtoUniteNumerique);
		uniteNumerique.id = null;
		uniteNumerique.persist();
		return Response.status(Status.CREATED)
		.entity(mapper.fromUniteNumeriqueToDTOUniteNumerique(uniteNumerique)).build();
	}
	
	@PUT
	@RolesAllowed({"admin"})
    @NoCache
	@Operation(summary = "Modifie une unité numerique à partir de son id")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOUniteNumerique.class)))
    @APIResponse(responseCode = "404", description = "Aucune unité à partir de cet identifiant")
	@Transactional
	public Response updateUniteNumerique(@Valid DTOUniteNumerique dtoUniteNumerique) {
		UniteNumerique uniteNumerique = mapper.toUniteNumeriqueToDTOUniteNumerique(dtoUniteNumerique);
		UniteNumerique entity = UniteNumerique.findById(dtoUniteNumerique.id);
		if (entity == null) {
		    return Response.status(Status.NOT_FOUND).build();
		}
		entity.etatBas = uniteNumerique.etatBas;
        entity.etatHaut = uniteNumerique.etatHaut;
		return Response.ok(mapper.fromUniteNumeriqueToDTOUniteNumerique(entity)).build();
	}
	
	
	@DELETE
	@RolesAllowed({"admin"})
    @NoCache
    @Path("/{id}")
	@Transactional
	@Operation(summary = "Supprime une unité numerique à partir de son id")
    @APIResponse(responseCode = "204", description = "L'unité numerique a été supprimé")
	@APIResponse(responseCode = "404", description = "Aucune unité numerique avec cet identifiant")
	public Response deleteUniteNumerique(@PathParam("id") Long id) {
		UniteNumerique entity = UniteNumerique.findById(id);

		if (entity == null) {
		    return Response.status(Status.NOT_FOUND).build();
		}

		entity.delete();

		return Response.noContent().build();
	}

}

