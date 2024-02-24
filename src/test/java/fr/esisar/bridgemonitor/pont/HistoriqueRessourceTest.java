package fr.esisar.bridgemonitor.pont;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainEtat;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HistoriqueRessourceTest {

    private static final int NB_HISTORIQUE = 2;

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
    void shouldNotGetUnknownHistorique(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", -1)
            .when()
            .get("/historique/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @Order(1)
    void shouldGetInitialHistoriques(){
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

        assertEquals(NB_HISTORIQUE, etats.size());
    }
}
