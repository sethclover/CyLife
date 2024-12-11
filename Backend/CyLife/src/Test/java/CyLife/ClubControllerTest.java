package CyLife;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import CyLife.Clubs.Club;
import CyLife.Clubs.ClubDTO;
import CyLife.Clubs.ClubRequest;
import CyLife.Users.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

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
        assertEquals("{\"message\":\"Club created and user associated.\"}", response.getBody().asString());
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

        assertEquals(201, response.getStatusCode()); // Updated to 201
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
    public void testClubConstructor() {
        Club club = new Club("Chess Club", "A club for chess enthusiasts", "chess@club.com");
        assertEquals("Chess Club", club.getClubName());
        assertEquals("A club for chess enthusiasts", club.getDescription());
        assertEquals("chess@club.com", club.getClubEmail());
    }

    @Test
    public void testSetAndGetUsers() {
        Club club = new Club();
        Set<User> users = new HashSet<>();
        User user = new User();
        users.add(user);
        club.setUsers(users);
        assertEquals(users, club.getUsers());
    }

    @Test
    public void testEmptyConstructor() {
        Club club = new Club();
        assertNotNull(club);
        assertNull(club.getClubName());
        assertNull(club.getDescription());
        assertNull(club.getClubEmail());
    }

    @Test
    public void testAddUserToClub() {
        Club club = new Club();
        User user = new User();
        Set<User> users = new HashSet<>();
        users.add(user);
        club.setUsers(users);
        assertTrue(club.getUsers().contains(user));
    }

    @Test
    public void testRemoveUserFromClub() {
        Club club = new Club();
        User user = new User();
        Set<User> users = new HashSet<>();
        users.add(user);
        club.setUsers(users);
        users.remove(user);
        club.setUsers(users);
        assertFalse(club.getUsers().contains(user));
    }

    @Test
    public void testClubEquality() {
        Club club1 = new Club("Chess Club", "For chess players", "chess@club.com");
        Club club2 = new Club("Chess Club", "For chess players", "chess@club.com");
        assertEquals(club1.getClubName(), club2.getClubName());
        assertEquals(club1.getDescription(), club2.getDescription());
        assertEquals(club1.getClubEmail(), club2.getClubEmail());
    }

    @Test
    public void testSetAndGetRequestId() {
        ClubRequest clubRequest = new ClubRequest();
        clubRequest.setRequestId(101);
        assertEquals(101, clubRequest.getRequestId());
    }

    @Test
    public void testSetAndGetStudentId() {
        ClubRequest clubRequest = new ClubRequest();
        clubRequest.setStudentId(2001);
        assertEquals(2001, clubRequest.getStudentId());
    }

    @Test
    public void testSetAndGetStatus() {
        ClubRequest clubRequest = new ClubRequest();
        clubRequest.setStatus("PENDING");
        assertEquals("PENDING", clubRequest.getStatus());
    }

    @Test
    public void testFullClubRequestConstructor() {
        ClubRequest clubRequest = new ClubRequest(1, "Drama Club", "For drama enthusiasts", "drama@club.com", "APPROVED");
        assertEquals(1, clubRequest.getStudentId());
        assertEquals("Drama Club", clubRequest.getClubName());
        assertEquals("For drama enthusiasts", clubRequest.getDescription());
        assertEquals("drama@club.com", clubRequest.getClubEmail());
        assertEquals("APPROVED", clubRequest.getStatus());
    }

    @Test
    public void testEmptyClubRequestConstructor() {
        ClubRequest clubRequest = new ClubRequest();
        assertNotNull(clubRequest);
        assertEquals(0, clubRequest.getStudentId());
        assertNull(clubRequest.getClubName());
        assertNull(clubRequest.getDescription());
        assertNull(clubRequest.getClubEmail());
        assertNull(clubRequest.getStatus());
    }

    @Test
    public void testGetClubByIdNotFound() {
        Response response = RestAssured.given()
                .when()
                .get("/clubs/99999"); // Non-existent ID

        assertEquals(404, response.getStatusCode()); // Expecting 404 Not Found
    }

    @Test
    public void testDeleteClubNotFound() {
        Response response = RestAssured.given()
                .when()
                .delete("/clubs/99999"); // Non-existent ID

        assertEquals(404, response.getStatusCode()); // Expecting 404 Not Found
        assertEquals("{\"message\": \"Club not found.\"}", response.getBody().asString());
    }

    @Test
    public void testUpdateClubNotFound() {
        String updateJson = "{ \"clubName\": \"Non-existent Club\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/clubs/99999"); // Non-existent ID

        assertEquals(404, response.getStatusCode());
        assertEquals("{\"message\": \"Club not found.\"}", response.getBody().asString());
    }

    @Test
    public void testCreateClubWithLongDescription() {
        String longDescription = "A".repeat(5001); // Exceeds the limit of 5000 characters
        String clubJson = "{ \"clubName\": \"Test Club\", \"description\": \"" + longDescription + "\", \"clubEmail\": \"test@club.com\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(clubJson)
                .when()
                .post("/clubs");

        assertEquals(400, response.getStatusCode()); // Expecting 400 Bad Request
        assertEquals("{ \"message\": \"Description is too long.\" }", response.getBody().asString());
    }

    @Test
    public void testUpdateClubRequestInvalidStatus() {
        // Create a valid ClubRequest first
        String clubRequestJson = "{ \"studentId\": 1, \"clubName\": \"Test Club\", \"description\": \"Test\", \"clubEmail\": \"test@club.com\" }";

        Response createResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(clubRequestJson)
                .when()
                .post("/club-requests");

        assertEquals(201, createResponse.getStatusCode()); // Ensure creation succeeds

        int validId = createResponse.jsonPath().getInt("id"); // Extract ID from the response

        // Test invalid status update
        Response response = RestAssured.given()
                .param("status", "INVALID_STATUS")
                .when()
                .put("/club-requests/" + validId + "/status");

        assertEquals(400, response.getStatusCode()); // Expecting 400 Bad Request
        assertEquals("{\"message\": \"Invalid status update.\"}", response.getBody().asString());
    }



}