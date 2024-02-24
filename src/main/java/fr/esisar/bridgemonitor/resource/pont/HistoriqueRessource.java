package fr.esisar.bridgemonitor.resource.pont;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.resteasy.reactive.NoCache;

import fr.esisar.bridgemonitor.dto.pont.DTOHistoriqueEtatPont;
import fr.esisar.bridgemonitor.mapper.pont.HistoriqueEtatPontMapper;
import fr.esisar.bridgemonitor.model.pont.HistoriqueEtatPont;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/historique")
public class HistoriqueRessource {
	
	@Inject
	HistoriqueEtatPontMapper mapper;
	
	@GET
	@RolesAllowed({"admin", "operateur"})
    @NoCache
	@Operation(summary = "Retourne tous les changements d'état de ponts")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOHistoriqueEtatPont.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Aucune historique")
	public Response getAllHistoriqueEtatPont(){
		List<HistoriqueEtatPont> historiqueEtatPonts = HistoriqueEtatPont.listAll();

		if(historiqueEtatPonts.isEmpty()){
			return Response.noContent().build();
		}

		List<DTOHistoriqueEtatPont> dtoHistoriqueEtatPonts = historiqueEtatPonts.stream()
			.map(historique -> mapper.fromHistoriqueEtatPontToDTOHistoriqueEtatPont(historique))
			.collect(Collectors.toList());

		return Response.ok(dtoHistoriqueEtatPonts).build();

	}
	
	
	
	@GET
	@RolesAllowed({"admin", "operateur"})
    @NoCache
	@Path("/{id}")
	@Operation(summary = "Retourne un etat à partir de son identifiant")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOHistoriqueEtatPont.class)))
	@APIResponse(responseCode = "204", description = "Aucune historique avec cet identifiant")
	public Response getPont(@PathParam("id") Long id) {
		
		HistoriqueEtatPont historiqueEtatPont = HistoriqueEtatPont.findById(id);

		if(historiqueEtatPont == null){
			return Response.noContent().build();
		}

		DTOHistoriqueEtatPont dtoHistoriqueEtatPont = mapper.fromHistoriqueEtatPontToDTOHistoriqueEtatPont(historiqueEtatPont);

		return Response.ok(dtoHistoriqueEtatPont).build();
	}
	
	

}

