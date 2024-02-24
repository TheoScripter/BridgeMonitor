package fr.esisar.bridgemonitor.releve;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import fr.esisar.bridgemonitor.dto.plain.releve.DTOPlainReleveAnalogique;
import fr.esisar.bridgemonitor.dto.plain.releve.DTOPlainReleveNumerique;
import fr.esisar.bridgemonitor.dto.post.releve.DTOPostReleveAnalogique;
import fr.esisar.bridgemonitor.dto.post.releve.DTOPostReleveNumerique;
import fr.esisar.bridgemonitor.dto.releve.DTOReleveAnalogique;
import fr.esisar.bridgemonitor.dto.releve.DTOReleveNumerique;
import fr.esisar.bridgemonitor.matchers.JsonKeyMatcher;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReleveRessourceTest {

    private static final int NB_RELEVES = 4;
    private static final int NB_RELEVESANALOGIQUES = 2;
    private static final int NB_RELEVESNUMERIQUES = 2;

    private static List<String> RELEVE_ANALOGIQUE_KEYS = Arrays.asList("id","dateTimeReleve","valeur","capteurAnalogique");
    private static List<String> RELEVE_NUMERIQUE_KEYS = Arrays.asList("id","dateTimeReleve","valeur","capteurNumerique");

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
    void shouldNotGetUnknownReleve(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", -1)
            .when()
            .get("/releves/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void shouldNotAddInvalidReleveAnalogique(){
        DTOPlainReleveAnalogique dtoReleve = new DTOPlainReleveAnalogique();
        dtoReleve.dateTimeReleve = LocalDateTime.now();
        dtoReleve.valeur = 2.5F;

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoReleve)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/releves/analogiques")
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldNotAddInvalidReleveNumerique(){
        DTOPlainReleveNumerique dtoReleve = new DTOPlainReleveNumerique();
        dtoReleve.dateTimeReleve = LocalDateTime.now();
        dtoReleve.valeur = false;

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoReleve)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/releves/numeriques")
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(1)
    void shouldGetInitialReleves(){
        List<LinkedHashMap<String, Object>> releves = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/releves")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".");
        assertEquals(NB_RELEVES, releves.size());

        for (LinkedHashMap<String, Object> json : releves ){
            List<String> keys = json.keySet().stream().collect(Collectors.toList());
            assertThat(keys, anyOf(JsonKeyMatcher.containsAllStrings(RELEVE_ANALOGIQUE_KEYS),JsonKeyMatcher.containsAllStrings(RELEVE_NUMERIQUE_KEYS)));
        }
    }

    private static final LocalDateTime DEFAULT_DATETIMERELEVE = LocalDateTime.now();
    private static final float DEFAULT_VALEURANALOGIQUE = 3.3F;
    private static final boolean DEFAULT_VALEURNUMERIQUE = true;
    private static final Long idCapteurAnalogique = 1L;
    private static final Long idCapteurNumerique = 80L;

    private static Long idReleveAnalogique;
    private static Long idReleveNumerique;

    @Test
    @Order(2)
    void shouldAddAReleveAnalogique(){
        DTOPostReleveAnalogique dtoReleve = new DTOPostReleveAnalogique();
        dtoReleve.dateTimeReleve = DEFAULT_DATETIMERELEVE;
        dtoReleve.valeur = DEFAULT_VALEURANALOGIQUE;
        dtoReleve.capteurAnalogiqueId = idCapteurAnalogique;

        DTOReleveAnalogique releve = given()
            .header("Authorization", "Bearer " + token)
            .body(dtoReleve)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/releves/analogiques")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .body()
            .as(DTOReleveAnalogique.class);

        assertNotNull(releve.id);
        idReleveAnalogique = releve.id;

        List<DTOReleveAnalogique> releves = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/releves/analogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOReleveAnalogique.class);
        assertEquals(NB_RELEVESANALOGIQUES + 1, releves.size());
    }

    @Test
    @Order(2)
    void shouldAddAReleveNumerique(){
        DTOPostReleveNumerique dtoReleve = new DTOPostReleveNumerique();
        dtoReleve.dateTimeReleve = DEFAULT_DATETIMERELEVE;
        dtoReleve.valeur = DEFAULT_VALEURNUMERIQUE;
        dtoReleve.capteurNumeriqueId = idCapteurNumerique;

        DTOReleveNumerique releve = given()
            .header("Authorization", "Bearer " + token)
            .body(dtoReleve)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/releves/numeriques")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .body()
            .as(DTOReleveNumerique.class);

        assertNotNull(releve.id);
        idReleveNumerique = releve.id;

        List<DTOReleveNumerique> releves = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/releves/numeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOReleveNumerique.class);
        assertEquals(NB_RELEVESNUMERIQUES + 1, releves.size());
    }

    @Test
    @Order(3)
    void shouldGetAReleveAnalogiqueById(){
        given() 
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idReleveAnalogique)
            .when()
            .get("/releves/{id}")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("dateTimeReleve", Is.is(DEFAULT_DATETIMERELEVE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))))
            .body("valeur", Is.is(DEFAULT_VALEURANALOGIQUE))
            .body("capteurAnalogique.id", Is.is(idCapteurAnalogique.intValue()));
    }

    @Test
    @Order(3)
    void shouldGetAReleveNumeriqueById(){
        given() 
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idReleveNumerique)
            .when()
            .get("/releves/{id}")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("dateTimeReleve", Is.is(DEFAULT_DATETIMERELEVE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))))
            .body("valeur", Is.is(DEFAULT_VALEURNUMERIQUE))
            .body("capteurNumerique.id", Is.is(idCapteurNumerique.intValue()));
    }

    private static final LocalDateTime UPDATED_DATETIMERELEVE = LocalDateTime.of(2023, 12, 8, 9, 22, 48, 555000000);
    private static final float UPDATED_VALEURANALOGIQUE = 20.35F;
    private static final boolean UPDATED_VALEURNUMERIQUE = false;
    private static final Long updatedIdCapteurAnalogique = 2L;
    private static final Long updatedIdCapteurNumerique = 81L;

    @Test
    @Order(4)
    void shouldUpdateAReleveAnalogique(){
        DTOPostReleveAnalogique dtoReleve = new DTOPostReleveAnalogique();
        dtoReleve.id = idReleveAnalogique;
        dtoReleve.dateTimeReleve = UPDATED_DATETIMERELEVE;
        dtoReleve.valeur = UPDATED_VALEURANALOGIQUE;
        dtoReleve.capteurAnalogiqueId = updatedIdCapteurAnalogique;

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoReleve)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .put("/releves/analogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("dateTimeReleve", Is.is(UPDATED_DATETIMERELEVE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))))
            .body("valeur", Is.is(UPDATED_VALEURANALOGIQUE))
            .body("capteurAnalogique.id", Is.is(updatedIdCapteurAnalogique.intValue()));

        List<DTOReleveAnalogique> releves = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/releves/analogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOReleveAnalogique.class);
        assertEquals(NB_RELEVESANALOGIQUES + 1, releves.size());            
    }

    @Test
    @Order(4)
    void shouldUpdateAReleveNumerique(){
        DTOPostReleveNumerique dtoReleve = new DTOPostReleveNumerique();
        dtoReleve.id = idReleveNumerique;
        dtoReleve.dateTimeReleve = UPDATED_DATETIMERELEVE;
        dtoReleve.valeur = UPDATED_VALEURNUMERIQUE;
        dtoReleve.capteurNumeriqueId = updatedIdCapteurNumerique;

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoReleve)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .put("/releves/numeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("dateTimeReleve", Is.is(UPDATED_DATETIMERELEVE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))))
            .body("valeur", Is.is(UPDATED_VALEURNUMERIQUE))
            .body("capteurNumerique.id", Is.is(updatedIdCapteurNumerique.intValue()));

        List<DTOReleveNumerique> releves = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/releves/numeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOReleveNumerique.class);
        assertEquals(NB_RELEVESNUMERIQUES + 1, releves.size());            
    }

    @Test
    @Order(5)
    void shouldRemoveAReleveAnalogique(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idReleveAnalogique)
            .when()
            .delete("/releves/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        List<DTOReleveAnalogique> releves = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/releves/analogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOReleveAnalogique.class);
        assertEquals(NB_RELEVESANALOGIQUES, releves.size());
    }

    @Test
    @Order(5)
    void shouldRemoveAReleveNumerique(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idReleveNumerique)
            .when()
            .delete("/releves/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        List<DTOReleveNumerique> releves = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/releves/numeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOReleveNumerique.class);
        assertEquals(NB_RELEVESNUMERIQUES, releves.size());
    }
}
