package fr.esisar.bridgemonitor.resource.pont;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import fr.esisar.bridgemonitor.dto.capteur.DTOCapteur;
import fr.esisar.bridgemonitor.dto.plain.capteur.DTOPlainCapteur;
import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainHistoriqueEtatPont;
import fr.esisar.bridgemonitor.dto.pont.DTOHistoriqueEtatPont;
import fr.esisar.bridgemonitor.dto.pont.DTOPont;
import fr.esisar.bridgemonitor.dto.post.pont.DTOPostPont;
import fr.esisar.bridgemonitor.mapper.capteur.CapteurMapper;
import fr.esisar.bridgemonitor.mapper.pont.EtatMapper;
import fr.esisar.bridgemonitor.mapper.pont.HistoriqueEtatPontMapper;
import fr.esisar.bridgemonitor.mapper.pont.PontMapper;
import fr.esisar.bridgemonitor.model.capteur.Capteur;
import fr.esisar.bridgemonitor.model.capteur.CapteurAnalogique;
import fr.esisar.bridgemonitor.model.capteur.CapteurNumerique;
import fr.esisar.bridgemonitor.model.pont.Etat;
import fr.esisar.bridgemonitor.model.pont.HistoriqueEtatPont;
import fr.esisar.bridgemonitor.model.pont.Pont;
import fr.esisar.bridgemonitor.model.pont.Region;
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

import jakarta.annotation.security.RolesAllowed;
import org.jboss.resteasy.reactive.NoCache;

@Path("/ponts")
public class PontRessource {
	@Inject
	PontMapper mapper;
	
	@Inject
	EtatMapper etatMapper;

	@Inject
	HistoriqueEtatPontMapper historiqueEtatPontMapper;

	@Inject
	CapteurMapper capteurMapper;
	
	@GET
	@RolesAllowed({"admin", "operateur"})
	@NoCache
	@Operation(summary = "Retourne tous les ponts")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPont.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Aucun pont")
	public Response getAllPonts() {
		List<Pont> ponts = Pont.listAll();
		List<DTOPont> dtoPont = ponts.stream()
				.map(pont -> mapper.fromPontToDTOPont(pont))
				.collect(Collectors.toList());
		return Response.ok(dtoPont).build();
	}

	@GET
	@RolesAllowed({"admin", "operateur"})
	@NoCache
	@Path("/{id}")
	@Operation(summary = "Retourne un pont par son identifiant")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPont.class)))
	@APIResponse(responseCode = "204", description = "Aucun pont")
	public Response getPontById(@PathParam("id") Long id) {
		Pont pont = Pont.findById(id);

		if(pont == null){
			return Response.noContent().build();
		}

		return Response.ok(mapper.fromPontToDTOPont(pont)).build();
	}

	@PUT
	@RolesAllowed({"admin"})
	@Operation(summary = "Modifie un Pont")
	@APIResponse(responseCode = "200", description = "Pont modifié", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostPont.class)))
	@APIResponse(responseCode = "400", description = "Les ids des attributs ne sont pas trouvables" ,content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostPont.class)))
	@APIResponse(responseCode = "404", description = "Pont non trouvé",  content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostPont.class)))
	@Transactional
	public Response updatePont(@Valid DTOPostPont dtoPostPont) {
		Pont pont = Pont.findById(dtoPostPont.id);

		if(pont == null){
			return Response.status(Status.NOT_FOUND).build();
		}

		pont.nom = dtoPostPont.nom;
    	pont.longueur = dtoPostPont.longueur;
    	pont.largeur = dtoPostPont.largeur;
    	pont.latitude = dtoPostPont.latitude;
    	pont.longitude = dtoPostPont.longitude;
 		pont.dateCreation = dtoPostPont.dateCreation;

		Etat etat = Etat.findById(dtoPostPont.etatId);
		Region region = Region.findById(dtoPostPont.regionId);
		Set<Capteur> capteurs = dtoPostPont.capteursId.stream()
			.map(id -> (Capteur) Capteur.findById(id))
			.collect(Collectors.toSet());

		if(etat == null || region == null || capteurs.contains(null)){
			return Response.status(Status.BAD_REQUEST).build();
		}

		//Ajout du nouveau changement d'état dans l'historique du pont
		if(etat.id != pont.etat.id){
			HistoriqueEtatPont historiqueEtatPont = new HistoriqueEtatPont();
			historiqueEtatPont.etat = etat;
			historiqueEtatPont.dateTimeChangement = LocalDateTime.now();
			historiqueEtatPont.pont = pont;

			historiqueEtatPont.persist();
		}

		pont.etat = etat;
		pont.region = region;

		for (Capteur capteur : capteurs) {
			capteur.pont = pont;
		}

		Pont.flush();

		return Response.ok(mapper.fromPontToDTOPont(pont)).build();
	}

		
	@DELETE
	@RolesAllowed({"admin"})
	@Operation(summary = "Supprime un Pont")
	@Path("/{id}")
	@Transactional
	public Response deletePont(@PathParam("id") Long id) {
		Pont pont = Pont.findById(id);
		if (pont == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		//Historique à supprimer + capteurs à détacher ?
		Set<HistoriqueEtatPont> historiques = pont.historiqueEtatPonts;
		Set<Capteur> capteurs = pont.capteurs;

		for(HistoriqueEtatPont historique : historiques){
			historique.delete();
		}

		for (Capteur capteur : capteurs) {
			capteur.pont = null;
		}

		pont.delete();
		return Response.noContent().build();
	}
	
	@GET
	@RolesAllowed({"admin", "operateur"})
	@NoCache
	@Path("/{id}/historique")
	@Operation(summary = "Retourne l'historique de changements d'état d'un pont")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOHistoriqueEtatPont.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "400", description = "Pont non trouvé")
	public Response getHistoriqueOfPont(@PathParam("id") Long id) {
		Pont pont = Pont.findById(id);

		if (pont == null) {
			return Response.status(Status.NOT_FOUND).build();
		}

		Set<HistoriqueEtatPont> historiqueEtatPont = pont.historiqueEtatPonts;
		
		Set<DTOHistoriqueEtatPont> dtoPlainHistoriqueEtatPonts = historiqueEtatPont.stream()
		 	.map(historique -> historiqueEtatPontMapper.fromHistoriqueEtatPontToDTOHistoriqueEtatPontWithEtat(historique))
		 	.collect(Collectors.toSet());

		return Response.ok(dtoPlainHistoriqueEtatPonts).build();
	}

	@GET
	@RolesAllowed({"admin", "operateur"})
	@NoCache
	@Path("/{id}/capteurs")
	@Operation(summary = "Retourne les capteurs d'un pont")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainCapteur.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Pas de capteurs pour ce pont" , content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainHistoriqueEtatPont.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "404", description = "Pont non trouvé" , content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainHistoriqueEtatPont.class, type = SchemaType.ARRAY)))
	public Response getCapteursOfPont(@PathParam("id") Long id) {
		Pont pont = Pont.findById(id);

		if(pont == null){
			return Response.status(Status.NOT_FOUND).build();
		}

		Set<Capteur> capteurs = pont.capteurs;

		if (capteurs.isEmpty()) {
			return Response.status(Status.NO_CONTENT).build();
		}

		
		Set<DTOCapteur> dtoCapteurs = capteurs.stream()
		 	.map(capteur -> capteurMapper.fromCapteurToDTOCapteur(capteur))
		 	.collect(Collectors.toSet());

		return Response.ok(dtoCapteurs).build();
	}
	@GET
	@RolesAllowed({"admin", "operateur"})
	@NoCache
	@Path("/{id}/capteurs/analogiques")
	@Operation(summary = "Retourne les capteurs d'un pont")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainCapteur.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Pas de capteurs pour ce pont" , content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainHistoriqueEtatPont.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "404", description = "Pont non trouvé" , content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainHistoriqueEtatPont.class, type = SchemaType.ARRAY)))
	public Response getCapteursAnalogiqueOfPont(@PathParam("id") Long id) {
		Pont pont = Pont.findById(id);

		if(pont == null){
			return Response.status(Status.NOT_FOUND).build();
		}

		Set<Capteur> capteurs = CapteurAnalogique.getByPontId(id);

		if(capteurs.isEmpty()){
			return Response.noContent().build();
		}

		Set<DTOPlainCapteur> dtoCapteurs = capteurs.stream()
			.map(capteur -> capteurMapper.fromCapteurToDTOPlainCapteur(capteur))
			.collect(Collectors.toSet());

		return Response.ok(dtoCapteurs).build();
	}
	@GET
	@Path("/{id}/capteurs/numeriques")
	@RolesAllowed({"admin", "operateur"})
	@Operation(summary = "Retourne les capteurs d'un pont")
	@APIResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainCapteur.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "204", description = "Pas de capteurs pour ce pont" , content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainHistoriqueEtatPont.class, type = SchemaType.ARRAY)))
	@APIResponse(responseCode = "404", description = "Pont non trouvé" , content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPlainHistoriqueEtatPont.class, type = SchemaType.ARRAY)))
	public Response getCapteursNumeriqueOfPont(@PathParam("id") Long id) {
		Pont pont = Pont.findById(id);

		if(pont == null){
			return Response.status(Status.NOT_FOUND).build();
		}

		Set<Capteur> capteurs = CapteurNumerique.getByPontId(id);

		if(capteurs.isEmpty()){
			return Response.noContent().build();
		}

		Set<DTOPlainCapteur> dtoCapteurs = capteurs.stream()
			.map(capteur -> capteurMapper.fromCapteurToDTOPlainCapteur(capteur))
			.collect(Collectors.toSet());

		return Response.ok(dtoCapteurs).build();
	}
	
	@POST
	@RolesAllowed({"admin"})
	@Operation(summary = "Crée un pont")
	@APIResponse(responseCode = "201", description = "Pont crée" ,content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostPont.class)))
	@APIResponse(responseCode = "400", description = "Les ids des attributs ne sont pas trouvables" ,content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostPont.class)))
	@Transactional
	public Response createPont(@Valid DTOPostPont dtoPostPont) {
		Pont pont = new Pont();
		pont.id = null;

		pont.nom = dtoPostPont.nom;
    	pont.longueur = dtoPostPont.longueur;
    	pont.largeur = dtoPostPont.largeur;
    	pont.latitude = dtoPostPont.latitude;
    	pont.longitude = dtoPostPont.longitude;
 		pont.dateCreation = dtoPostPont.dateCreation;

		Etat etat = Etat.findById(dtoPostPont.etatId);
		Region region = Region.findById(dtoPostPont.regionId);
		Set<Capteur> capteurs = dtoPostPont.capteursId.stream()
			.map(id -> (Capteur)Capteur.findById(id))
			.collect(Collectors.toSet());

		if(etat == null || region == null || capteurs.contains(null)){
			return Response.status(Status.BAD_REQUEST).build();
		}

		pont.etat = etat;
		pont.region = region;
		pont.persist();
		
		//Création du premier historique du nouveau état du pont

		HistoriqueEtatPont historiqueEtatPont = new HistoriqueEtatPont();

		historiqueEtatPont.etat = etat;
		historiqueEtatPont.dateTimeChangement = LocalDateTime.now();
		historiqueEtatPont.pont = pont;

		historiqueEtatPont.persist();

		//Assigner les capteurs au pont
		
		for (Capteur capteur : capteurs) {
			capteur.pont = pont;
		}
		
		Pont.flush();
		HistoriqueEtatPont.flush();
		Capteur.flush();

		return Response.status(Status.CREATED).entity(mapper.fromPontToDTOPont(pont)).build();
	}

	@PUT
	@RolesAllowed({"admin","operateur"})
	@Path("/{id}/capteurs")
	@Operation(summary = "Modifie les capteurs d'un Pont")
	@APIResponse(responseCode = "200", description = "Capteurs du pont modifié", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostPont.class)))
	@APIResponse(responseCode = "400", description = "Les ids des attributs ne sont pas trouvables" ,content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostPont.class)))
	@APIResponse(responseCode = "404", description = "Pont non trouvé",  content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DTOPostPont.class)))
	@Transactional
	public Response updateCapteursOfPont(@PathParam("id") Long id,@Valid Set<Long> capteursId) {
		Pont pont = Pont.findById(id);

		if(pont == null){
			return Response.status(Status.NOT_FOUND).build();
		}

		for (Capteur capteur : pont.capteurs) {
			capteur.pont = null;
		}

		Set<Capteur> capteurs = capteursId.stream()
			.map(capteurId -> (Capteur) Capteur.findById(capteurId))
			.collect(Collectors.toSet());

		if(capteurs.contains(null)){
			return Response.status(Status.BAD_REQUEST).build();
		}

		for (Capteur capteur : capteurs) {
			capteur.pont = pont;
		}

		Set<DTOPlainCapteur> dtoPlainCapteurs = capteurs.stream()
			.map(capteur -> capteurMapper.fromCapteurToDTOPlainCapteur(capteur))
			.collect(Collectors.toSet());

		return Response.ok(dtoPlainCapteurs).build();
	}

}
