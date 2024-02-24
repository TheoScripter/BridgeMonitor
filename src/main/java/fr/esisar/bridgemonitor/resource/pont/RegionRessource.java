package fr.esisar.bridgemonitor.resource.pont;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.NoCache;

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainPont;
import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainRegion;
import fr.esisar.bridgemonitor.dto.pont.DTORegion;
import fr.esisar.bridgemonitor.mapper.pont.PontMapper;
import fr.esisar.bridgemonitor.mapper.pont.RegionMapper;
import fr.esisar.bridgemonitor.model.pont.Region;
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


@Path("/regions")
public class RegionRessource {
	
	@Inject
	RegionMapper mapper;
	
	@Inject
	PontMapper pontMapper;
		
	@GET
	@RolesAllowed({"admin", "operateur"})
    @NoCache
	@Operation(summary = "Retourne toutes les regions")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTORegion.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Aucune region")
	public Response getAllRegions() {
		List<Region> regions = Region.listAll();	
		if(regions.isEmpty()) {
			return Response.noContent().build();
		}
		List<DTORegion> dtoRegions = regions.stream()
			.map(region -> mapper.fromRegionToDTORegion(region))
			.collect(Collectors.toList());

		return Response.ok(dtoRegions).build();
	}
	
	
	
	@GET
	@RolesAllowed({"admin", "operateur"})
    @NoCache
	@Path("/{id}")
	@Operation(summary = "Retourne une region à partir de son id")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTORegion.class)))
	@APIResponse(responseCode = "204", description = "Aucune region")
	public Response getRegionById(@PathParam("id") Long id){
			Region region = Region.findById(id);

			if(region == null){
				return Response.noContent().build();
			}

			return Response.ok(mapper.fromRegionToDTORegion(region)).build();
	}

	@GET
	@RolesAllowed({"admin", "operateur"})
    @NoCache
	@Path("/{id}/ponts")
	@Operation(summary = "Retourne les ponts d'une région définie par son identifiant")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainPont.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Aucune region")
	@APIResponse(responseCode = "403", description = "Aucune region")
	public Response getPontsOfRegionById(@PathParam("id") Long id){
			Region region = Region.findById(id);
			if (region == null || region.ponts.isEmpty()) {
				return Response.noContent().build();
			}
			Set<DTOPlainPont> dtoPlainPonts = region.ponts.stream()
				.map(pont -> pontMapper.fromPontToDTOPont(pont))
				.collect(Collectors.toSet());

			return Response.ok(dtoPlainPonts).build();
	}
	
	@POST
	@RolesAllowed({"admin"})
    @NoCache
	@Operation(summary = "Cree une region à partir de son identifiant")
	@APIResponse(responseCode = "201", description = "Region crée", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainRegion.class)))
	@Transactional
	public Response createRegion(@Valid DTOPlainRegion dtoRegion) {
		Region region = mapper.toRegionFromDTOPlainRegion(dtoRegion);
		region.id = null;
		region.persist();
		return Response.status(Status.CREATED)
		.entity(mapper.fromRegionToDTOPlainRegion(region)).build();
	}
	
	@PUT
	@RolesAllowed({"admin"})
    @NoCache
	@Operation(summary = "Modifie une Region à partir de son id")
	@Transactional
	public Response updateRegion(@Valid DTOPlainRegion dtoRegion) {
		Region region = mapper.toRegionFromDTOPlainRegion(dtoRegion);
		Region entity = Region.findById(region.id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		entity.nom = region.nom;
		entity.codeRegion = region.codeRegion;
		return Response.ok(mapper.fromRegionToDTOPlainRegion(entity)).build();
	}
	
	@DELETE
	@RolesAllowed({"admin"})
    @NoCache
	@Operation(summary = "Supprime une Region à partir de son id")
	@Path("/{id}")
	@Transactional
	public Response deleteRegion(@PathParam("id") Long id) {
		Region entity = Region.findById(id);
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		entity.delete();
		return Response.noContent().build();
	}
	
}
