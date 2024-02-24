package fr.esisar.bridgemonitor.resource.capteur.unite;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.NoCache;

import fr.esisar.bridgemonitor.dto.capteur.unite.DTOUniteAnalogique;
import fr.esisar.bridgemonitor.mapper.capteur.unite.UniteAnalogiqueMapper;
import fr.esisar.bridgemonitor.model.capteur.unite.UniteAnalogique;
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

@Path("/unitesanalogiques")
public class UniteAnalogiqueRessource {
    @Inject
	UniteAnalogiqueMapper mapper;
	
	@GET
	@RolesAllowed({"admin", "operateur"})
    @NoCache
	@Operation(summary = "Retourne toutes les unités analogiques existantes")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOUniteAnalogique.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Aucune unité analogique")
	public Response getAllUnitesAnalogique(){
		List<UniteAnalogique> unitesAnalogiques = UniteAnalogique.listAll();
		List<DTOUniteAnalogique> dtoUnitesAnalogiques = unitesAnalogiques.stream()
				.map(etat -> mapper.fromUniteAnalogiqueToDTOUniteAnalogique(etat))
				.collect(Collectors.toList());
				return Response.ok(dtoUnitesAnalogiques).build();
	}
	
	@GET
	@RolesAllowed({"admin", "operateur"})
    @NoCache
	@Path("/{id}")
	@Operation(summary = "Retourne une unité analogique à partir de son id")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOUniteAnalogique.class)))
	@APIResponse(responseCode = "204", description = "Aucune unité analogique avec cet identifiant")
	public Response getUniteAnalogique(@PathParam("id") Long id) {
		UniteAnalogique uniteAnalogique = UniteAnalogique.findById(id);
		if (uniteAnalogique == null) {
		return Response.noContent().build();
		}
		return Response.ok(mapper.fromUniteAnalogiqueToDTOUniteAnalogique(uniteAnalogique)).build();
	}
	
	
	@POST
	@RolesAllowed({"admin"})
    @NoCache
	@Operation(summary = "Cree un unité analogique à partir de son id")
    @APIResponse(responseCode = "201", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOUniteAnalogique.class)))
	@Transactional
	public Response createUniteAnalogique(@Valid DTOUniteAnalogique dtoUniteAnalogique) {
		UniteAnalogique uniteAnalogique = mapper.toUniteAnalogiqueFromDTOUniteAnalogique(dtoUniteAnalogique);
		uniteAnalogique.id = null;
		uniteAnalogique.persist();
		return Response.status(Status.CREATED)
		.entity(mapper.fromUniteAnalogiqueToDTOUniteAnalogique(uniteAnalogique)).build();
	}
	
	@PUT
	@RolesAllowed({"admin"})
    @NoCache
	@Operation(summary = "Modifie une unité analogique à partir de son id")
    @APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOUniteAnalogique.class)))
    @APIResponse(responseCode = "404", description = "Aucune unité à partir de cet identifiant")
	@Transactional
	public Response updateUniteAnalogique(@Valid DTOUniteAnalogique dtoUniteAnalogique) {
		UniteAnalogique uniteAnalogique = mapper.toUniteAnalogiqueFromDTOUniteAnalogique(dtoUniteAnalogique);
		UniteAnalogique entity = UniteAnalogique.findById(dtoUniteAnalogique.id);
		if (entity == null) {
		    return Response.status(Status.NOT_FOUND).build();
		}
		entity.nom = uniteAnalogique.nom;
		entity.symbole = uniteAnalogique.symbole;
		return Response.ok(mapper.fromUniteAnalogiqueToDTOUniteAnalogique(entity)).build();
	}
	
	
	@DELETE
	@RolesAllowed({"admin"})
    @NoCache
    @Path("/{id}")
	@Transactional
	@Operation(summary = "Supprime une unité analogique à partir de son id")
    @APIResponse(responseCode = "204", description = "L'unité analogique a été supprimé")
	@APIResponse(responseCode = "404", description = "Aucune unité analogique avec cet identifiant")
	public Response deleteUniteAnalogique(@PathParam("id") Long id) {
		UniteAnalogique entity = UniteAnalogique.findById(id);

		if (entity == null) {
		    return Response.status(Status.NOT_FOUND).build();
		}

		entity.delete();

		return Response.noContent().build();
	}

}
