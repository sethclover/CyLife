
package CyLife;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ClubControllerTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testGetAllClubs() {
        Response response = RestAssured.given()
                .when()
                .get("/clubs");

        assertEquals(200, response.getStatusCode());
        assertEquals("application/json", response.getContentType());
    }

    @Test
    public void testCreateClub() {
        String clubJson = "{ \"clubName\": \"Chess Club\", \"description\": \"A club for chess enthusiasts\", \"clubEmail\": \"chess@club.com\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(clubJson)
                .when()
                .post("/clubs");

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"message\":\"Success\"}", response.getBody().asString());
    }

    @Test
    public void testUpdateClub() {
        String updateJson = "{ \"clubName\": \"Updated Club\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/clubs/1");

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void testDeleteClub() {
        Response response = RestAssured.given()
                .when()
                .delete("/clubs/1");

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"message\":\"Success\"}", response.getBody().asString());
    }
}
