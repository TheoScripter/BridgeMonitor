package fr.esisar.bridgemonitor.pont;

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

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainEtat;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EtatRessourceTest {
    private static final String DEFAULT_NOM = "En panne";

    private static final String UPDATED_NOM = "Bloqué";

    private static final int NB_ETATS = 2;

    private static Long idEtat;

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
    void shouldNotGetUnknownEtat(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", -1)
            .when()
            .get("/etats/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void shouldNotAddInvalidEtat(){
        DTOPlainEtat dtoPlainEtat = new DTOPlainEtat();
        dtoPlainEtat.nom = " ";

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPlainEtat)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/etats")
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(1)
    void shouldGetInitialPonts(){
        List<DTOPlainEtat> etats = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/etats")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPlainEtat.class);

        assertEquals(NB_ETATS, etats.size());
    }

    @Test
    @Order(2)
    void shouldAddAEtat(){
        DTOPlainEtat dtoPlainEtat = new DTOPlainEtat();
        dtoPlainEtat.nom = DEFAULT_NOM;

        DTOPlainEtat etat = given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPlainEtat)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/etats")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .body()
            .as(DTOPlainEtat.class);

        assertNotNull(etat.id);
        idEtat = etat.id;

        List<DTOPlainEtat> etats = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/etats")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPlainEtat.class);
        
        assertEquals((NB_ETATS + 1), etats.size());
    }

    @Test
    @Order(3)
    void shouldGetAEtatById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idEtat)
            .when()
            .get("/etats/{id}")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("nom", Is.is(DEFAULT_NOM));

    }

    @Test
    @Order(4)
    void shouldUpdateAEtat(){
        DTOPlainEtat dtoPlainEtat = new DTOPlainEtat();

        dtoPlainEtat.id = idEtat;
        dtoPlainEtat.nom = UPDATED_NOM;

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPlainEtat)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .put("/etats")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("nom", Is.is(UPDATED_NOM))
            .extract()
            .body()
            .as(DTOPlainEtat.class);

        List<DTOPlainEtat> dtoPlainEtats = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/etats")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPlainEtat.class);
        
        
        assertEquals((NB_ETATS + 1), dtoPlainEtats.size());
    }

    @Test
    @Order(5)
    void shouldRemoveAEtatById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idEtat)
            .when()
            .delete("/etats/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());


        List<DTOPlainEtat> dtoPlainEtats = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/etats")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTOPlainEtat.class);
        
        assertEquals(NB_ETATS , dtoPlainEtats.size());
    }
}
