package fr.esisar.bridgemonitor.capteur;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
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
import fr.esisar.bridgemonitor.dto.post.capteur.DTOPostCapteurAnalogique;
import fr.esisar.bridgemonitor.dto.post.capteur.DTOPostCapteurNumerique;
import fr.esisar.bridgemonitor.dto.releve.DTOReleveAnalogique;
import fr.esisar.bridgemonitor.matchers.JsonKeyMatcher;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;


@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CapteurRessourceTest {

    private static final String DEFAULT_NOM_ANALOGIQUE = "capteur Valence-Guillerand";
    private static final String DEFAULT_DESCRIPTION_ANALOGIQUE = "Capteur de température";
    private static final String DEFAULT_NUMERO_SERIE_ANALOGIQUE = "12345678";
    private static final Long DEFAULT_PONT_ID_ANALOGIQUE = 1L;

    private static final Long DEFAULT_PERIODICITIE = 25L;
    private static final Long DEFAULT_UNITE_ANALOGIQUE_ID = 1L;

    private static final String DEFAULT_NOM_NUMERIQUE = "capteur Valence-Guillerand";
    private static final String DEFAULT_DESCRIPTION_NUMERIQUE = "Capteur de présence";
    private static final String DEFAULT_NUMERO_SERIE_NUMERIQUE = "1ABCE45";
    private static final Long DEFAULT_PONT_ID_NUMERIQUE = 1L;

    private static final Long DEFAULT_UNITE_NUMERIQUE_ID = 1L;


    /* Updating variables of Capteur fields */

    private static final String UPDATED_NOM_ANALOGIQUE = "capteur Valence";
    private static final String UPDATED_DESCRIPTION_ANALOGIQUE = "Capteur de pression moderne";
    private static final String UPDATED_NUMERO_SERIE_ANALOGIQUE = "123456789";
    private static final Long UPDATED_PONT_ID_ANALOGIQUE = 2L;

    private static final Long UPDATED_PERIODICITIE = 30L;
    private static final Long UPDATED_UNITE_ANALOGIQUE_ID = 1L;


    private static final String UPDATED_NOM_NUMERIQUE = "capteur Guillerand";
    private static final String UPDATED_DESCRIPTION_NUMERIQUE = "Capteur de présence moderne";
    private static final String UPDATED_NUMERO_SERIE_NUMERIQUE = "1ABCE456";
    private static final Long UPDATED_PONT_ID_NUMERIQUE = 2L;

    private static final Long UPDATED_UNITE_NUMERIQUE_ID = 1L;

    private static final int NB_CAPTEURS_ANALOGIQUE = 78;
    private static final int NB_CAPTEURS_NUMERIQUE = 26;
    private static final int NB_CAPTEURS = 104;


    private static Long idCapteurAnalogiqueWithReleves = 1L;

    private static Long idCapteurAnalogique;
    private static Long idCapteurNumerique;

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

    @Test
    void shouldNotGetUnknownCapteur(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", -1)
            .when()
            .get("/capteurs/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void shouldNotAddInvalidCapteurAnalogique(){
        DTOPostCapteurAnalogique dtoPostCapteurAnalogique = new DTOPostCapteurAnalogique();
        dtoPostCapteurAnalogique.nom = "";
        dtoPostCapteurAnalogique.description = "";
        dtoPostCapteurAnalogique.numeroSerie = "";
        dtoPostCapteurAnalogique.pontId = 0L;
        dtoPostCapteurAnalogique.periodicite = 0L;
        dtoPostCapteurAnalogique.uniteAnalogiqueId = 0L;

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPostCapteurAnalogique)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/capteurs/analogiques")
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldNotAddInvalidCapteurNumerique(){
        DTOPostCapteurNumerique dtoPostCapteurNumerique = new DTOPostCapteurNumerique();
        dtoPostCapteurNumerique.nom = "";
        dtoPostCapteurNumerique.description = "";
        dtoPostCapteurNumerique.numeroSerie = "";
        dtoPostCapteurNumerique.pontId = 0L;
        dtoPostCapteurNumerique.uniteNumeriqueId = 0L;

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPostCapteurNumerique)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/capteurs/numeriques")
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(1)
    void shouldGetInitialCapteurs(){
        List<LinkedHashMap<String, Object>> capteurs = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs")
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

        assertEquals(NB_CAPTEURS, capteurs.size());
    }

    @Test
    @Order(1)
    void shouldGetInitialCapteursAnalogique(){
        List<LinkedHashMap<String, Object>> capteurs = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs/analogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".");
        
        for (LinkedHashMap<String, Object> json : capteurs ){
            List<String> keys = json.keySet().stream().collect(Collectors.toList());
            assertThat(keys, JsonKeyMatcher.containsAllStrings(CAPTEUR_ANALOGIQUE_KEYS));
        }
    
        assertEquals(NB_CAPTEURS_ANALOGIQUE, capteurs.size());
    }

    @Test
    @Order(1)
    void shouldGetInitialCapteursNumerique(){
        List<LinkedHashMap<String, Object>> capteurs = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs/numeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".");
        
        for (LinkedHashMap<String, Object> json : capteurs ){
            Set<String> keys = json.keySet();
            assertThat(keys.stream().collect(Collectors.toList()), JsonKeyMatcher.containsAllStrings(CAPTEUR_NUMERIQUE_KEYS));
        }
    
        assertEquals(NB_CAPTEURS_NUMERIQUE, capteurs.size());
    }

    @Test
    @Order(2)
    void shouldAddACapteurAnalogique(){
        DTOPostCapteurAnalogique dtoPostCapteurAnalogique = new DTOPostCapteurAnalogique();
        dtoPostCapteurAnalogique.nom = DEFAULT_NOM_ANALOGIQUE;
        dtoPostCapteurAnalogique.description = DEFAULT_DESCRIPTION_ANALOGIQUE;
        dtoPostCapteurAnalogique.numeroSerie = DEFAULT_NUMERO_SERIE_ANALOGIQUE;
        dtoPostCapteurAnalogique.pontId = DEFAULT_PONT_ID_ANALOGIQUE;
        dtoPostCapteurAnalogique.periodicite = DEFAULT_PERIODICITIE;
        dtoPostCapteurAnalogique.uniteAnalogiqueId = DEFAULT_UNITE_ANALOGIQUE_ID;


        DTOCapteur capteur = given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPostCapteurAnalogique)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/capteurs/analogiques")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .body()
            .as(DTOCapteurAnalogique.class);

        assertNotNull(capteur.id);
        idCapteurAnalogique = capteur.id;

        List<DTOCapteur> dtoCapteurs = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".");
        
        List<DTOCapteurAnalogique> dtoCapteursAnalogique = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs/analogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOCapteurAnalogique.class);

        assertEquals((NB_CAPTEURS + 1), dtoCapteurs.size());
        assertEquals((NB_CAPTEURS_ANALOGIQUE + 1), dtoCapteursAnalogique.size());
    }

    @Test
    @Order(3)
    void shouldAddACapteurNumerique(){
        DTOPostCapteurNumerique dtoPostCapteurNumerique = new DTOPostCapteurNumerique();
        dtoPostCapteurNumerique.nom = DEFAULT_NOM_NUMERIQUE;
        dtoPostCapteurNumerique.description = DEFAULT_DESCRIPTION_NUMERIQUE;
        dtoPostCapteurNumerique.numeroSerie = DEFAULT_NUMERO_SERIE_NUMERIQUE;
        dtoPostCapteurNumerique.pontId = DEFAULT_PONT_ID_NUMERIQUE;
        dtoPostCapteurNumerique.uniteNumeriqueId = DEFAULT_UNITE_NUMERIQUE_ID;


        DTOCapteur capteur = given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPostCapteurNumerique)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/capteurs/numeriques")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .body()
            .as(DTOCapteurNumerique.class);

        assertNotNull(capteur.id);
        idCapteurNumerique = capteur.id;

        List<DTOCapteur> dtoCapteurs = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".");

        List<DTOCapteurNumerique> dtoCapteursNumeriques = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs/numeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".");
        
        assertEquals((NB_CAPTEURS + 2), dtoCapteurs.size());
        assertEquals((NB_CAPTEURS_NUMERIQUE + 1), dtoCapteursNumeriques.size());
    }

    @Test
    @Order(4)
    void shouldGetACapteurAnalogiqueById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idCapteurAnalogique)
            .when()
            .get("/capteurs/{id}")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("nom", Is.is(DEFAULT_NOM_ANALOGIQUE))
            .body("description", Is.is(DEFAULT_DESCRIPTION_ANALOGIQUE))
            .body("numeroSerie", Is.is(DEFAULT_NUMERO_SERIE_ANALOGIQUE))
            .body("periodicite", Is.is(DEFAULT_PERIODICITIE.intValue()))
            .body("uniteAnalogique.id", Is.is(DEFAULT_UNITE_ANALOGIQUE_ID.intValue()));    
    }

    @Test
    @Order(5)
    void shouldGetACapteurNumeriqueById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idCapteurNumerique)
            .when()
            .get("/capteurs/{id}")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("nom", Is.is(DEFAULT_NOM_NUMERIQUE))
            .body("description", Is.is(DEFAULT_DESCRIPTION_NUMERIQUE))
            .body("numeroSerie", Is.is(DEFAULT_NUMERO_SERIE_NUMERIQUE))
            .body("uniteNumerique.id", Is.is(DEFAULT_UNITE_NUMERIQUE_ID.intValue()));
    }

    @Test
    @Order(6)
    void shouldGetRelevesOfCapteur(){
        List<DTOReleveAnalogique> dtoRelevesAnalogiques = given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idCapteurAnalogiqueWithReleves)
            .when()
            .get("/capteurs/{id}/releves")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOReleveAnalogique.class);

            
        DTOReleveAnalogique dtoReleveAnalogique = dtoRelevesAnalogiques.get(0);

        assertEquals(2, dtoRelevesAnalogiques.size());
        assertNotNull(dtoReleveAnalogique.valeur);
        assertEquals(idCapteurAnalogiqueWithReleves, dtoReleveAnalogique.capteurAnalogique.id);
    }

    @Test
    @Order(7)
    void shouldUpdateACapteurAnalogique(){
        DTOPostCapteurAnalogique dtoPostCapteurAnalogique = new DTOPostCapteurAnalogique();
        dtoPostCapteurAnalogique.id = idCapteurAnalogique;
        dtoPostCapteurAnalogique.nom = UPDATED_NOM_ANALOGIQUE;
        dtoPostCapteurAnalogique.description = UPDATED_DESCRIPTION_ANALOGIQUE;
        dtoPostCapteurAnalogique.numeroSerie = UPDATED_NUMERO_SERIE_ANALOGIQUE;
        dtoPostCapteurAnalogique.pontId = UPDATED_PONT_ID_ANALOGIQUE;
        dtoPostCapteurAnalogique.periodicite = UPDATED_PERIODICITIE;
        dtoPostCapteurAnalogique.uniteAnalogiqueId = UPDATED_UNITE_ANALOGIQUE_ID;


        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPostCapteurAnalogique)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .put("/capteurs/analogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("nom", Is.is(UPDATED_NOM_ANALOGIQUE))
            .body("description", Is.is(UPDATED_DESCRIPTION_ANALOGIQUE))
            .body("numeroSerie", Is.is(UPDATED_NUMERO_SERIE_ANALOGIQUE))
            .body("periodicite", Is.is(UPDATED_PERIODICITIE.intValue()))
            .body("uniteAnalogique.id", Is.is(UPDATED_UNITE_ANALOGIQUE_ID.intValue()))
            .extract()
            .body()
            .as(DTOCapteurAnalogique.class);

        List<DTOCapteur> dtoCapteurs = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".");
        
        List<DTOCapteurAnalogique> dtoCapteursAnalogique = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs/analogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOCapteurAnalogique.class);

        assertEquals((NB_CAPTEURS + 2), dtoCapteurs.size());
        assertEquals((NB_CAPTEURS_ANALOGIQUE + 1), dtoCapteursAnalogique.size());
    }

    @Test
    @Order(8)
    void shouldUpdateACapteurNumerique(){
        DTOPostCapteurNumerique dtoPostCapteurNumerique = new DTOPostCapteurNumerique();
        dtoPostCapteurNumerique.id = idCapteurNumerique;
        dtoPostCapteurNumerique.nom = UPDATED_NOM_NUMERIQUE;
        dtoPostCapteurNumerique.description = UPDATED_DESCRIPTION_NUMERIQUE;
        dtoPostCapteurNumerique.numeroSerie = UPDATED_NUMERO_SERIE_NUMERIQUE;
        dtoPostCapteurNumerique.pontId = UPDATED_PONT_ID_NUMERIQUE;
        dtoPostCapteurNumerique.uniteNumeriqueId = UPDATED_UNITE_NUMERIQUE_ID;


        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPostCapteurNumerique)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .put("/capteurs/numeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("nom", Is.is(UPDATED_NOM_NUMERIQUE))
            .body("description", Is.is(UPDATED_DESCRIPTION_NUMERIQUE))
            .body("numeroSerie", Is.is(UPDATED_NUMERO_SERIE_NUMERIQUE))
            .body("uniteNumerique.id", Is.is(UPDATED_UNITE_NUMERIQUE_ID.intValue()))
            .extract()
            .body()
            .as(DTOCapteurNumerique.class);

        List<DTOCapteur> dtoCapteurs = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".");
        
        List<DTOCapteurAnalogique> dtoCapteursAnalogique = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs/analogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOCapteurAnalogique.class);

        assertEquals((NB_CAPTEURS + 2), dtoCapteurs.size());
        assertEquals((NB_CAPTEURS_ANALOGIQUE + 1), dtoCapteursAnalogique.size());
    }


    @Test
    @Order(9)
    void shouldRemoveACapteurAnalogiqueById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idCapteurAnalogique)
            .when()
            .delete("/capteurs/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());


        List<DTOCapteur> dtoCapteurs = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".");
        
        assertEquals(NB_CAPTEURS + 1 , dtoCapteurs.size());
    }

    @Test
    @Order(9)
    void shouldRemoveACapteurNumeriqueById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idCapteurNumerique)
            .when()
            .delete("/capteurs/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());


        List<DTOCapteur> dtoCapteurs = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/capteurs")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".");
        
        assertEquals(NB_CAPTEURS , dtoCapteurs.size());
    }
}
