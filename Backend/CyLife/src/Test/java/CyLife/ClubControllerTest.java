package CyLife;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import CyLife.Clubs.ClubDTO;
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
    public void testGetClubById() {
        Response response = RestAssured.given()
                .when()
                .get("/clubs/24");

        if (response.getStatusCode() == 200) {
            assertEquals("application/json", response.getContentType());
        } else {
            assertEquals(404, response.getStatusCode()); // Not found case
        }
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
                .put("/clubs/24");

        if (response.getStatusCode() == 200) {
            assertEquals("application/json", response.getContentType());
        } else {
            assertEquals(404, response.getStatusCode()); // Not found case
        }
    }

    @Test
    public void testDeleteClub() {
        Response response = RestAssured.given()
                .when()
                .delete("/clubs/1");

        if (response.getStatusCode() == 200) {
            assertEquals("{\"message\":\"Success\"}", response.getBody().asString());
        } else {
            assertEquals(404, response.getStatusCode());
        }
    }

    @Test
    public void testGetAllClubRequests() {
        Response response = RestAssured.given()
                .when()
                .get("/club-requests");

        assertEquals(200, response.getStatusCode());
        assertEquals("application/json", response.getContentType());
    }

    @Test
    public void testCreateClubRequest() {
        String clubRequestJson = "{ \"studentId\": 123, \"clubName\": \"Art Club\", \"description\": \"A club for artists\", \"clubEmail\": \"art@club.com\", \"status\": \"PENDING\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(clubRequestJson)
                .when()
                .post("/club-requests");

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"message\":\"Success\"}", response.getBody().asString());
    }

    @Test
    public void testDeleteClubRequest() {
        Response response = RestAssured.given()
                .when()
                .delete("/club-requests/11");

        if (response.getStatusCode() == 200) {
            assertEquals("{ \"message\": \"Club request not found.\" }", response.getBody().asString());
        } else {
            assertEquals(404, response.getStatusCode()); // Not found case
        }
    }

    @Test
    public void testUpdateClubRequestStatus() {
        Response response = RestAssured.given()
                .param("status", "APPROVED")
                .when()
                .put("/club-requests/27/status");

        if (response.getStatusCode() == 200) {
            assertEquals("application/json", response.getContentType());
        } else {
            assertEquals(404, response.getStatusCode());
        }
    }

    @Test
    public void testCreateClubWithInvalidInput() {
        String invalidClubJson = "{ \"description\": \"No name provided\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(invalidClubJson)
                .when()
                .post("/clubs");

        assertEquals(400, response.getStatusCode()); // Assuming you return 400 for invalid input
    }

    @Test
    public void testUpdateClubWithInvalidId() {
        String updateJson = "{ \"clubName\": \"Nonexistent Club\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/clubs/99999"); // Use an ID you know doesn't exist

        assertEquals(404, response.getStatusCode()); // Expecting 404 Not Found
        assertEquals("{\"message\": \"Club not found.\"}", response.getBody().asString());
    }

    @Test
    public void testBoundaryConditionsForCreateClub() {
        String largeDescription = "A".repeat(5001); // Exceeds limit
        String clubJson = "{ \"clubName\": \"Boundary Club\", \"description\": \"" + largeDescription + "\", \"clubEmail\": \"boundary@club.com\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(clubJson)
                .when()
                .post("/clubs");

        assertEquals(400, response.getStatusCode()); // Expecting Bad Request
    }

    @Test
    public void testUpdateClubRequestWithInvalidStatus() {
        Response response = RestAssured.given()
                .param("status", "INVALID_STATUS")
                .when()
                .put("/club-requests/34/status"); // Use a valid or mock ID

        assertEquals(400, response.getStatusCode());
        assertEquals("{\"message\": \"Invalid status update.\"}", response.getBody().asString());
    }

    @Test
    public void testDeleteNonexistentClub() {
        Response response = RestAssured.given()
                .when()
                .delete("/clubs/99999"); // Use an ID you know doesn't exist

        assertEquals(404, response.getStatusCode()); // Expecting 404 Not Found
    }

    @Test
    public void testGetClubByInvalidId() {
        Response response = RestAssured.given()
                .when()
                .get("/clubs/abc"); // Invalid ID (non-integer)

        assertEquals(400, response.getStatusCode()); // Expecting 400 Bad Request due to invalid ID format
        assertEquals("application/json", response.getContentType());
    }

    @Test
    public void testUpdateClubWithEmptyPayload() {
        String emptyUpdateJson = "{}"; // No fields provided in the update payload

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(emptyUpdateJson)
                .when()
                .put("/clubs/25"); // Use a valid existing ID for this test

        if (response.getStatusCode() == 200) {
            assertEquals("application/json", response.getContentType());
            // Ensure no fields were changed
            // You might need to assert specific unchanged values if accessible
        } else {
            assertEquals(404, response.getStatusCode()); // Not found case
        }
    }

    @Test
    public void testCreateClubMissingRequiredField() {
        String invalidClubJson = "{ \"description\": \"A club without a name\", \"clubEmail\": \"noname@club.com\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(invalidClubJson)
                .when()
                .post("/clubs");

        assertEquals(400, response.getStatusCode()); // Expecting Bad Request
        assertEquals("{ \"message\": \"Club name is required.\" }", response.getBody().asString());
    }

    @Test
    public void testUpdateClubWithEmailOnly() {
        String updateJson = "{ \"clubEmail\": \"newemail@club.com\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/clubs/24"); // Use a valid ID

        if (response.getStatusCode() == 200) {
            assertEquals("application/json", response.getContentType());
        } else {
            assertEquals(404, response.getStatusCode());
        }
    }

    @Test
    public void testClubDTOConstructor() {
        ClubDTO clubDTO = new ClubDTO(1, "Chess Club", "A club for chess enthusiasts", "chess@club.com");

        assertEquals(1, clubDTO.getClubId());
        assertEquals("Chess Club", clubDTO.getClubName());
        assertEquals("A club for chess enthusiasts", clubDTO.getDescription());
        assertEquals("chess@club.com", clubDTO.getClubEmail());
    }

    @Test
    public void testSetAndGetClubId() {
        ClubDTO clubDTO = new ClubDTO(0, null, null, null);
        clubDTO.setClubId(10);
        assertEquals(10, clubDTO.getClubId());
    }

    @Test
    public void testSetAndGetClubName() {
        ClubDTO clubDTO = new ClubDTO(0, null, null, null);
        clubDTO.setClubName("Art Club");
        assertEquals("Art Club", clubDTO.getClubName());
    }

    @Test
    public void testSetAndGetDescription() {
        ClubDTO clubDTO = new ClubDTO(0, null, null, null);
        clubDTO.setDescription("A club for artists");
        assertEquals("A club for artists", clubDTO.getDescription());
    }

    @Test
    public void testSetAndGetClubEmail() {
        ClubDTO clubDTO = new ClubDTO(0, null, null, null);
        clubDTO.setClubEmail("art@club.com");
        assertEquals("art@club.com", clubDTO.getClubEmail());
    }

    @Test
    public void testAllFieldsTogether() {
        ClubDTO clubDTO = new ClubDTO(5, "Science Club", "A club for science enthusiasts", "science@club.com");

        // Modify all fields
        clubDTO.setClubId(20);
        clubDTO.setClubName("Updated Club");
        clubDTO.setDescription("An updated description");
        clubDTO.setClubEmail("updated@club.com");

        // Verify all fields
        assertEquals(20, clubDTO.getClubId());
        assertEquals("Updated Club", clubDTO.getClubName());
        assertEquals("An updated description", clubDTO.getDescription());
        assertEquals("updated@club.com", clubDTO.getClubEmail());
    }

    @Test
    public void testEmptyConstructor() {
        ClubDTO clubDTO = new ClubDTO(0, null, null, null);

        assertEquals(0, clubDTO.getClubId());
        assertNull(clubDTO.getClubName());
        assertNull(clubDTO.getDescription());
        assertNull(clubDTO.getClubEmail());
    }

}