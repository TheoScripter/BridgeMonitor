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

import fr.esisar.bridgemonitor.dto.plain.pont.DTOPlainRegion;
import fr.esisar.bridgemonitor.dto.pont.DTORegion;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegionRessourceTest {
    private static final String DEFAULT_NOM = "Nouvelle Région";
    private static final Long DEFAULT_CODE_REGION = 100L;

    private static final String UPDATED_NOM = "Updated Région";
    private static final Long UPDATED_CODE_REGION = 99L;

    private static final int NB_REGIONS = 13;

    private static Long idRegion;

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
    void shouldNotGetUnknownRegion(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", -1)
            .when()
            .get("/regions/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void shouldNotAddInvalidRegion(){
        DTOPlainRegion dtoPlainRegion = new DTOPlainRegion();
        dtoPlainRegion.nom = " ";

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPlainRegion)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/regions")
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    @Order(1)
    void shouldGetInitialRegions(){
        List<DTORegion> regions = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/regions")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTORegion.class);

        assertEquals(NB_REGIONS, regions.size());
    }

    @Test
    @Order(2)
    void shouldAddARegion(){
        DTOPlainRegion dtoPlainRegion = new DTOPlainRegion();
        dtoPlainRegion.nom = DEFAULT_NOM;
        dtoPlainRegion.codeRegion = DEFAULT_CODE_REGION;

        DTOPlainRegion etat = given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPlainRegion)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .post("/regions")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .body()
            .as(DTOPlainRegion.class);

        assertNotNull(etat.id);
        idRegion = etat.id;

        List<DTORegion> regions = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/regions")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTORegion.class);
        
        assertEquals((NB_REGIONS + 1), regions.size());
    }

    @Test
    @Order(3)
    void shouldGetARegionById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idRegion)
            .when()
            .get("/regions/{id}")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("nom", Is.is(DEFAULT_NOM))
            .body("codeRegion", Is.is(DEFAULT_CODE_REGION.intValue()));

    }

    @Test
    @Order(4)
    void shouldUpdateARegion(){
        DTOPlainRegion dtoPlainRegion = new DTOPlainRegion();

        dtoPlainRegion.id = idRegion;
        dtoPlainRegion.nom = UPDATED_NOM;
        dtoPlainRegion.codeRegion = UPDATED_CODE_REGION;

        given()
            .header("Authorization", "Bearer " + token)
            .body(dtoPlainRegion)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
            .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON)
            .when()
            .put("/regions")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .body("nom", Is.is(UPDATED_NOM))
            .body("codeRegion", Is.is(UPDATED_CODE_REGION.intValue()))
            .extract()
            .body()
            .as(DTOPlainRegion.class);

        List<DTORegion> dtoPlainRegions = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/regions")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTORegion.class);
        
        
        assertEquals((NB_REGIONS + 1), dtoPlainRegions.size());
    }

    @Test
    @Order(5)
    void shouldRemoveARegionById(){
        given()
            .header("Authorization", "Bearer " + token)
            .pathParam("id", idRegion)
            .when()
            .delete("/regions/{id}")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());


        List<DTORegion> dtoPlainRegions = given()
            .header("Authorization", "Bearer " + token)
            .when()
            .get("/regions")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(MediaType.APPLICATION_JSON)
            .extract()
            .body()
            .jsonPath()
            .getList(".", DTORegion.class);
        
        assertEquals(NB_REGIONS , dtoPlainRegions.size());
    }
}
