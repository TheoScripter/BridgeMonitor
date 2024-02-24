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

import fr.esisar.bridgemonitor.dto.plain.capteur.unite.DTOPlainUniteAnalogique;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UniteAnalogiqueRessourceTest {
    private static final String DEFAULT_NOM = "Distance";
    private static final String DEFAULT_SYMBOLE = "m";

    private static final String UPDATED_NOM = "Ditance(en millimetres)";
    private static final String UPDATED_SYMBOLE = "mm";

    private static final int NB_UNITES = 3;

    private static Long idUniteAnalogique;

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
    void shouldNotGetUnknownUniteAnalogique(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", -1)
            .when()
            .get("/unitesanalogiques/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void shouldNotAddInvalidUniteAnalogique(){
        DTOPlainUniteAnalogique dtoPlainUniteAnalogique = new DTOPlainUniteAnalogique();
        dtoPlainUniteAnalogique.nom = " ";

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPlainUniteAnalogique)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/unitesanalogiques")
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(1)
    void shouldGetInitialPonts(){
        List<DTOPlainUniteAnalogique> unites = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/unitesanalogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPlainUniteAnalogique.class);

        assertEquals(NB_UNITES, unites.size());
    }

    @Test
    @Order(2)
    void shouldAddAUniteAnalogique(){
        DTOPlainUniteAnalogique dtoPlainUniteAnalogique = new DTOPlainUniteAnalogique();
        dtoPlainUniteAnalogique.nom = DEFAULT_NOM;
        dtoPlainUniteAnalogique.symbole = DEFAULT_SYMBOLE;

        DTOPlainUniteAnalogique unite = given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPlainUniteAnalogique)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/unitesanalogiques")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .body()
            .as(DTOPlainUniteAnalogique.class);

        assertNotNull(unite.id);
        idUniteAnalogique = unite.id;

        List<DTOPlainUniteAnalogique> unites = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/unitesanalogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPlainUniteAnalogique.class);
        
        assertEquals((NB_UNITES + 1), unites.size());
    }

    @Test
    @Order(3)
    void shouldGetAUniteAnalogiqueById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idUniteAnalogique)
            .when()
            .get("/unitesanalogiques/{id}")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("nom", Is.is(DEFAULT_NOM))
            .body("symbole", Is.is(DEFAULT_SYMBOLE));

    }

    @Test
    @Order(4)
    void shouldUpdateAUniteAnalogique(){
        DTOPlainUniteAnalogique dtoPlainUniteAnalogique = new DTOPlainUniteAnalogique();

        dtoPlainUniteAnalogique.id = idUniteAnalogique;
        dtoPlainUniteAnalogique.nom = UPDATED_NOM;
        dtoPlainUniteAnalogique.symbole = UPDATED_SYMBOLE;

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPlainUniteAnalogique)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .put("/unitesanalogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("nom", Is.is(UPDATED_NOM))
            .body("symbole", Is.is(UPDATED_SYMBOLE))
            .extract()
            .body()
            .as(DTOPlainUniteAnalogique.class);

        List<DTOPlainUniteAnalogique> dtoPlainUniteAnalogiques = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/unitesanalogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPlainUniteAnalogique.class);
        
        
        assertEquals((NB_UNITES + 1), dtoPlainUniteAnalogiques.size());
    }

    @Test
    @Order(5)
    void shouldRemoveAUniteAnalogiqueById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idUniteAnalogique)
            .when()
            .delete("/unitesanalogiques/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());


        List<DTOPlainUniteAnalogique> dtoPlainUniteAnalogiques = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/unitesanalogiques")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPlainUniteAnalogique.class);
        
        assertEquals(NB_UNITES , dtoPlainUniteAnalogiques.size());
    }
}
