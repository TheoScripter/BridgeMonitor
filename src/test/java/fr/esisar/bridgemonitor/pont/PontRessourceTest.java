package fr.esisar.bridgemonitor.pont;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import fr.esisar.bridgemonitor.dto.capteur.DTOCapteur;
import fr.esisar.bridgemonitor.dto.capteur.DTOCapteurAnalogique;
import fr.esisar.bridgemonitor.dto.capteur.DTOCapteurNumerique;
import fr.esisar.bridgemonitor.dto.pont.DTOHistoriqueEtatPont;
import fr.esisar.bridgemonitor.dto.pont.DTOPont;
import fr.esisar.bridgemonitor.dto.post.pont.DTOPostPont;
import fr.esisar.bridgemonitor.matchers.JsonKeyMatcher;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PontRessourceTest{
    private static final String DEFAULT_NOM = "pont Valence-Guillerand";
    private static final Float DEFAULT_LONGUEUR = 520.0F;
    private static final Float DEFAULT_LARGEUR = 20.0F;
    private static final String DEFAULT_LATITUDE = "44° 52′ 54″ N";
    private static final String DEFAULT_LONGITUDE = "4° 51′ 02″ E";
    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.of(1978, 1, 1);

    private static final Long DEFAULT_REGION_ID = 1L;

    private static final Long DEFAULT_ETAT_ID = 1L;

    private static final int NB_HISTORIQUE_ETAT_PONT = 1;
    private static final int NB_HISTORIQUE_ETAT_PONT_UPDATED = 2;

    private static final Long DEFAULT_CAPTEUR_1_ID = 1L;
    private static final Long DEFAULT_CAPTEUR_2_ID = 2L;
    private static final Long DEFAULT_CAPTEUR_3_ID = 3L;
    private static final Long DEFAULT_CAPTEUR_79_ID = 79L;
    private static final int NB_CAPTEURS = 4;
    private static final int NB_CAPTEURS_UPDATED = 1;
    private static final int NB_CAPTEURS_ANALOGIQUE = 3;
    private static final int NB_CAPTEURS_NUMERIQUE = 1;
    private static final Set<Long> DEFAULT_CAPTEURS = initCapteursSet(DEFAULT_CAPTEUR_1_ID, DEFAULT_CAPTEUR_2_ID, DEFAULT_CAPTEUR_3_ID, DEFAULT_CAPTEUR_79_ID);

    /* Updating variables of Pont fields */

    private static final String UPDATED_NOM = "pont Frédéric-Mistral";
    private static final Float UPDATED_LONGUEUR = 522.0F;
    private static final Float UPDATED_LARGEUR = 17.0F;
    private static final String UPDATED_LATITUDE = "44° 55′ 54″ N";
    private static final String UPDATED_LONGITUDE = "4° 53′ 02″ E";
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now();

    private static final Long UPDATED_REGION_ID = 2L;

    private static final Long UPDATED_ETAT_ID = 2L;


    private static final Long UPDATED_CAPTEUR_1_ID = 1L;
    private static final Set<Long> UPDATED_CAPTEURS = initCapteursSet(UPDATED_CAPTEUR_1_ID);

    private static final int NB_PONTS = 26;

    private static Long idPont;


    private static List<String> CAPTEUR_ANALOGIQUE_KEYS = Arrays.asList("id","nom","description","numeroSerie","pont", "periodicite","uniteAnalogique","releveAnalogiques");
    private static List<String> CAPTEUR_NUMERIQUE_KEYS = Arrays.asList("id","nom","description","numeroSerie","pont","uniteNumerique","releveNumeriques");


    private static String token;

    @ConfigProperty(name = "keycloak.url")
    private String keycloakUrl;


    @BeforeEach
    void authenticate(){
        token = given()
            .formParam("grant_type", "password")
            .formParam("client_secret", "PR2JufsdFwH58STeMCmHKlHpyMgMYWWk")
            .formParam("client_id", "bridgemonitor-backend")
            .formParam("username", "charif")
            .formParam("password", "admin")
            .when()
            .post(keycloakUrl+"/realms/bridgemonitor-realm/protocol/openid-connect/token")
            .then()
            .extract().jsonPath().getString("access_token");
    }

    private static Set<Long> initCapteursSet(Long... ids){
        Set<Long> capteurs = new HashSet<>();

        for (Long id : ids) {
            capteurs.add(id);
        }

        return capteurs;
    }


    @Test
    void shouldNotGetUnknownPont(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", -1)
            .when()
            .get("/ponts/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void shouldNotAddInvalidPont(){
        DTOPostPont dtoPostPont = new DTOPostPont();
        dtoPostPont.nom = " ";

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPostPont)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/ponts")
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(1)
    void shouldGetInitialPonts(){
        List<DTOPont> ponts = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/ponts")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPont.class);

        assertEquals(NB_PONTS, ponts.size());
    }

    @Test
    @Order(2)
    void shouldAddAPont(){
        DTOPostPont dtoPostPont = new DTOPostPont();
        dtoPostPont.nom = DEFAULT_NOM;
        dtoPostPont.largeur = DEFAULT_LARGEUR;
        dtoPostPont.longueur = DEFAULT_LONGUEUR;
        dtoPostPont.latitude = DEFAULT_LATITUDE;
        dtoPostPont.longitude = DEFAULT_LONGITUDE;
        dtoPostPont.dateCreation = DEFAULT_DATE_CREATION;

        dtoPostPont.regionId = DEFAULT_REGION_ID;
        dtoPostPont.etatId = DEFAULT_ETAT_ID;
        dtoPostPont.capteursId = DEFAULT_CAPTEURS;


        DTOPont pont = given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPostPont)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/ponts")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .body()
            .as(DTOPont.class);

        assertNotNull(pont.id);
        idPont = pont.id;

        List<DTOPont> dtoPonts = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/ponts")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPont.class);
        
        assertEquals((NB_PONTS + 1), dtoPonts.size());
    }

    @Test
    @Order(3)
    void shouldGetAPontById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idPont)
            .when()
            .get("/ponts/{id}")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("nom", Is.is(DEFAULT_NOM))
            .body("longueur", Is.is(DEFAULT_LONGUEUR))
            .body("largeur", Is.is(DEFAULT_LARGEUR))
            .body("latitude", Is.is(DEFAULT_LATITUDE))
            .body("longitude", Is.is(DEFAULT_LONGITUDE))
            .body("dateCreation", Is.is(DEFAULT_DATE_CREATION.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            .body("etat.id", Is.is(DEFAULT_ETAT_ID.intValue()))
            .body("region.id", Is.is(DEFAULT_REGION_ID.intValue()));

    }



    @Test
    @Order(4)
    void shouldGetHistoriqueEtatPontOfPont(){
        List<DTOHistoriqueEtatPont> dtoHistoriqueEtatPonts = given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idPont)
            .when()
            .get("/ponts/{id}/historique")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOHistoriqueEtatPont.class);

        DTOHistoriqueEtatPont dtoHistoriqueEtatPont = dtoHistoriqueEtatPonts.get(0);

        assertEquals(dtoHistoriqueEtatPonts.size(), NB_HISTORIQUE_ETAT_PONT);
        assertEquals(dtoHistoriqueEtatPont.pont.id, idPont);
        assertEquals(dtoHistoriqueEtatPont.etat.id, DEFAULT_ETAT_ID);
        assertNotNull(dtoHistoriqueEtatPont.dateTimeChangement);
        assertNotNull(dtoHistoriqueEtatPont.id);
    }

    @Test
    @Order(5)
    void shouldGetCapteursOfPont(){
        List<LinkedHashMap<String, Object>> capteurs = given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idPont)
            .when()
            .get("/ponts/{id}/capteurs")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".");

            for (LinkedHashMap<String, Object> json : capteurs ){
                List<String> keys = json.keySet().stream().collect(Collectors.toList());
                assertThat(keys, anyOf(JsonKeyMatcher.containsAllStrings(CAPTEUR_ANALOGIQUE_KEYS),JsonKeyMatcher.containsAllStrings(CAPTEUR_NUMERIQUE_KEYS)));
            }   

            assertEquals(capteurs.size(), NB_CAPTEURS);
    }

    @Test
    @Order(6)
    void shouldGetCapteursAnalogiqueOfPont(){
        List<DTOCapteurAnalogique> capteurs = given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idPont)
            .when()
            .get("/ponts/{id}/capteurs/analogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOCapteurAnalogique.class);

            assertEquals(capteurs.size(), NB_CAPTEURS_ANALOGIQUE);
    }

    @Test
    @Order(7)
    void shouldGetCapteursNumeriqueOfPont(){
        List<DTOCapteurNumerique> capteurs = given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idPont)
            .when()
            .get("/ponts/{id}/capteurs/numeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOCapteurNumerique.class);

            assertEquals(capteurs.size(), NB_CAPTEURS_NUMERIQUE);
    }

    @Test
    @Order(8)
    void shouldGetHistoriqueOfPont(){
        List<DTOHistoriqueEtatPont> historique = given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idPont)
            .when()
            .get("/ponts/{id}/historique")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOHistoriqueEtatPont.class);

            assertEquals(historique.size(), NB_HISTORIQUE_ETAT_PONT);
    }

    @Test
    @Order(9)
    void shouldUpdateAPont(){
        DTOPostPont dtoPostPont = new DTOPostPont();
        dtoPostPont.id = idPont;
        dtoPostPont.nom = UPDATED_NOM;
        dtoPostPont.largeur = UPDATED_LARGEUR;
        dtoPostPont.longueur = UPDATED_LONGUEUR;
        dtoPostPont.latitude = UPDATED_LATITUDE;
        dtoPostPont.longitude = UPDATED_LONGITUDE;
        dtoPostPont.dateCreation = UPDATED_DATE_CREATION;

        dtoPostPont.regionId = UPDATED_REGION_ID;
        dtoPostPont.etatId = UPDATED_ETAT_ID;
        dtoPostPont.capteursId = new HashSet<>();


        DTOPont pont = given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPostPont)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .put("/ponts")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("nom", Is.is(UPDATED_NOM))
            .body("longueur", Is.is(UPDATED_LONGUEUR))
            .body("largeur", Is.is(UPDATED_LARGEUR))
            .body("latitude", Is.is(UPDATED_LATITUDE))
            .body("longitude", Is.is(UPDATED_LONGITUDE))
            .body("dateCreation", Is.is(UPDATED_DATE_CREATION.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
            .body("etat.id", Is.is(UPDATED_ETAT_ID.intValue()))
            .body("region.id", Is.is(UPDATED_REGION_ID.intValue()))
            .extract()
            .body()
            .as(DTOPont.class);

        List<DTOPont> dtoPonts = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/ponts")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPont.class);
        
        
        assertEquals((NB_PONTS + 1), dtoPonts.size());
        assertEquals((NB_HISTORIQUE_ETAT_PONT_UPDATED), pont.historiqueEtatPonts.size());
    }

    @Test
    @Order(10)
    void shouldUpdateCapteursOfAPont(){

        List<DTOCapteur> capteurs = given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idPont)
            .body(UPDATED_CAPTEURS)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .put("/ponts/{id}/capteurs")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body(".", Is.isA(List.class))
            .extract()
            .jsonPath()
            .getList(".");

        assertEquals((NB_CAPTEURS_UPDATED), capteurs.size());
    }

    @Test
    @Order(11)
    void shouldRemoveAPontById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idPont)
            .when()
            .delete("/ponts/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());


        List<DTOPont> dtoPonts = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/ponts")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPont.class);

        
        assertEquals(NB_PONTS , dtoPonts.size());
    }
}
