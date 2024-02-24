package fr.esisar.bridgemonitor.capteur.unite;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import fr.esisar.bridgemonitor.dto.plain.capteur.unite.DTOPlainUniteNumerique;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UniteNumeriqueRessourceTest {
    private static final String DEFAULT_ETATHAUT = "Distance";
    private static final String DEFAULT_ETATBAS = "m";

    private static final String UPDATED_ETATHAUT = "Ditance(en millimetres)";
    private static final String UPDATED_ETATBAS = "mm";

    private static final int NB_UNITES = 1;

    private static Long idUniteNumerique;

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
    void shouldNotGetUnknownUniteNumerique(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", -1)
            .when()
            .get("/unitesnumeriques/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void shouldNotAddInvalidUniteNumerique(){
        DTOPlainUniteNumerique dtoPlainUniteNumerique = new DTOPlainUniteNumerique();
        dtoPlainUniteNumerique.etatHaut = " ";
        dtoPlainUniteNumerique.etatBas = " ";

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPlainUniteNumerique)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/unitesnumeriques")
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(1)
    void shouldGetInitialPonts(){
        List<DTOPlainUniteNumerique> unites = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/unitesnumeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPlainUniteNumerique.class);

        assertEquals(NB_UNITES, unites.size());
    }

    @Test
    @Order(2)
    void shouldAddAUniteNumerique(){
        DTOPlainUniteNumerique dtoPlainUniteNumerique = new DTOPlainUniteNumerique();
        dtoPlainUniteNumerique.etatHaut = DEFAULT_ETATHAUT;
        dtoPlainUniteNumerique.etatBas = DEFAULT_ETATBAS;

        DTOPlainUniteNumerique unite = given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPlainUniteNumerique)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/unitesnumeriques")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .body()
            .as(DTOPlainUniteNumerique.class);

        assertNotNull(unite.id);
        idUniteNumerique = unite.id;

        List<DTOPlainUniteNumerique> unites = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/unitesnumeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPlainUniteNumerique.class);
        
        assertEquals((NB_UNITES + 1), unites.size());
    }

    @Test
    @Order(3)
    void shouldGetAUniteNumeriqueById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idUniteNumerique)
            .when()
            .get("/unitesnumeriques/{id}")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("etatHaut", Is.is(DEFAULT_ETATHAUT))
            .body("etatBas", Is.is(DEFAULT_ETATBAS));

    }

    @Test
    @Order(4)
    void shouldUpdateAUniteNumerique(){
        DTOPlainUniteNumerique dtoPlainUniteNumerique = new DTOPlainUniteNumerique();

        dtoPlainUniteNumerique.id = idUniteNumerique;
        dtoPlainUniteNumerique.etatHaut = UPDATED_ETATHAUT;
        dtoPlainUniteNumerique.etatBas = UPDATED_ETATBAS;

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPlainUniteNumerique)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .put("/unitesnumeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("etatHaut", Is.is(UPDATED_ETATHAUT))
            .body("etatBas", Is.is(UPDATED_ETATBAS))
            .extract()
            .body()
            .as(DTOPlainUniteNumerique.class);

        List<DTOPlainUniteNumerique> dtoPlainUniteNumeriques = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/unitesnumeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPlainUniteNumerique.class);
        
        
        assertEquals((NB_UNITES + 1), dtoPlainUniteNumeriques.size());
    }

    @Test
    @Order(5)
    void shouldRemoveAUniteNumeriqueById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idUniteNumerique)
            .when()
            .delete("/unitesnumeriques/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());


        List<DTOPlainUniteNumerique> dtoPlainUniteNumeriques = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/unitesnumeriques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPlainUniteNumerique.class);
        
        assertEquals(NB_UNITES , dtoPlainUniteNumeriques.size());
    }
}
