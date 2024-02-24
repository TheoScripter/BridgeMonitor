package fr.esisar.bridgemonitor.resource.pont;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.NoCache;

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainEtat;
import fr.esisar.bridgemonitor.mapper.pont.EtatMapper;
import fr.esisar.bridgemonitor.model.pont.Etat;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;


@Path("/etats")
public class EtatRessource {
	
	@Inject
	EtatMapper mapper;
	
	@GET
	@RolesAllowed({"admin", "operateur"})
    @NoCache
	@Operation(summary = "Retourne tous les etats existants")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainEtat.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Aucun Etat")
	public Response getAllEtats(){
		List<Etat> etats = Etat.listAll();
		List<DTOPlainEtat> dtoEtats = etats.stream()
				.map(etat -> mapper.fromEtatToPlainEtat(etat))
				.collect(Collectors.toList());
				return Response.ok(dtoEtats).build();
	}
	
	
	@GET
	@RolesAllowed({"admin", "operateur"})
    @NoCache
	@Path("/{id}")
	@Operation(summary = "Retourne un etat à partir de son id")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainEtat.class)))
	@APIResponse(responseCode = "204", description = "Aucune Etat avec cet identifiant")
	public Response getEtat(@PathParam("id") Long id) {
		Etat etat = Etat.findById(id);
		if (etat == null) {
			return Response.noContent().build();
		}
		return Response.ok(mapper.fromEtatToPlainEtat(etat)).build();
	}
	
	
	@POST
	@RolesAllowed({"admin"})
    @NoCache
	@Operation(summary = "Cree un etat à partir de son id")
	@Transactional
	public Response createEtat(@Valid DTOPlainEtat dtoEtat) {
		Etat etat = mapper.toEtatFromPlainEtat(dtoEtat);
		etat.id = null;
		etat.persist();
		return Response.status(Status.CREATED)
		.entity(mapper.fromEtatToPlainEtat(etat)).build();
	}
	
	@PUT
	@RolesAllowed({"admin"})
    @NoCache
	@Operation(summary = "Modifie un etat à partir de son id")
	@Transactional
	public Response updateEtat(@Valid DTOPlainEtat dtoEtat) {
		Etat etat = mapper.toEtatFromPlainEtat(dtoEtat);
		Etat entity = Etat.findById(etat.id);
		if (entity == null) {
		return Response.status(Status.NOT_FOUND).build();
		}
		entity.nom = etat.nom;
		return Response.ok(mapper.fromEtatToPlainEtat(entity)).build();
	}
	
	
	@DELETE
	@RolesAllowed({"admin"})
    @NoCache
	@Operation(summary = "Supprime un etat à partir de son id")
	@Path("/{id}")
	@Transactional
	public Response deleteEtat(@PathParam("id") Long id) {
		Etat entity = Etat.findById(id);
		if (entity == null) {
		return Response.status(Status.NOT_FOUND).build();
		}
		entity.delete();
		return Response.noContent().build();
	}
	
	

}
