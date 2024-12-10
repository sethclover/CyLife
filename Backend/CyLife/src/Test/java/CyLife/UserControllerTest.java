package CyLife;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class UserControllerTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testGetAllUsers() {
        Response response = RestAssured.given()
                .when()
                .get("/users");

        assertEquals(200, response.getStatusCode()); // Check for 200 OK
        assertEquals("application/json", response.getContentType());
    }

//    @Test
//    public void testJoinClub() {
//        // Use existing user ID and club ID
//        int userId = 93; // Replace with an existing valid user ID from your database
//        int clubId = 24; // Replace with a valid club ID, e.g., Computer Science Club
//
//        Response response = RestAssured.given()
//                .when()
//                .put(String.format("/joinClub/%d/%d", userId, clubId));
//
//        assertEquals(200, response.getStatusCode()); // Check for 200 OK
//        assertEquals("User successfully joined the club.", response.jsonPath().getString("message"));
//    }

    @Test
    public void testLoginUser() {
        // Properly formatted JSON string
        String loginJson = String.format(
                "{ \"email\": \"%s\", \"password\": \"%s\" }",
                "john.doe@example.com",
                "password"
        );

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(loginJson)
                .when()
                .post("/login");

        assertEquals(200, response.getStatusCode()); // Check for 200 OK
        assertEquals("application/json", response.getContentType());
    }

    @Test
    public void testSignupUser() {
        // Generate a unique email for each test run
        String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";

        String userJson = String.format(
                "{ \"name\": \"Dhvani M\", \"email\": \"%s\", \"password\": \"securePassword123\", \"type\": \"STUDENT\" }",
                uniqueEmail
        );

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(userJson)
                .when()
                .post("/signup");

        assertEquals(201, response.getStatusCode()); // Check for 201 Created
        assertEquals("application/json", response.getContentType());
        assertEquals("User registered successfully.", response.jsonPath().getString("message"));
    }

    @Test
    public void testGetUserById() {
        // Valid user ID
        int validUserId = 95; // Replace with an actual ID from your database
        Response response = RestAssured.given()
                .when()
                .get("/user/" + validUserId);
        assertEquals(200, response.getStatusCode());
        assertEquals("application/json", response.getContentType());
        assertNotNull(response.jsonPath().getMap("user"));

        // Invalid user ID
        int invalidUserId = -1; // Use an ID you know doesn't exist
        response = RestAssured.given()
                .when()
                .get("/user/" + invalidUserId);
        assertEquals(404, response.getStatusCode());
        assertEquals("User not found with id: " + invalidUserId, response.jsonPath().getString("message"));
    }

    @Test
    public void testUpdateUser() {
        // Setup: Create a user to update
        String uniqueEmail = "update" + System.currentTimeMillis() + "@example.com";
        String userJson = String.format(
                "{ \"name\": \"Update Test\", \"email\": \"%s\", \"password\": \"pass123\", \"type\": \"STUDENT\" }",
                uniqueEmail
        );

        Response createResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(userJson)
                .when()
                .post("/signup");
        assertEquals(201, createResponse.getStatusCode());
        int userId = createResponse.jsonPath().getInt("userId");

        // Test 1: Successful update
        String updateJson = "{ \"name\": \"Updated Name\", \"email\": \"updated@example.com\" }";
        Response updateResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/update/byId/" + userId);
        assertEquals(200, updateResponse.getStatusCode());
        assertEquals("User updated successfully.", updateResponse.jsonPath().getString("message"));

        // Verify: Check the updated details
        Response getResponse = RestAssured.given()
                .when()
                .get("/user/" + userId);
        assertEquals(200, getResponse.getStatusCode());
        assertEquals("Updated Name", getResponse.jsonPath().getMap("user").get("name"));
        assertEquals("updated@example.com", getResponse.jsonPath().getMap("user").get("email"));

        // Test 2: Update non-existent user
        Response nonExistentUpdate = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/update/byId/999999"); // Replace with a clearly non-existent ID
        assertEquals(404, nonExistentUpdate.getStatusCode());
        assertEquals("User not found with id: 999999", nonExistentUpdate.jsonPath().getString("message"));

        // Test 3: Invalid fields
        String invalidFieldJson = "{ \"invalidField\": \"test\", \"email\": \"stillValid@example.com\" }";
        Response invalidUpdateResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(invalidFieldJson)
                .when()
                .put("/update/byId/" + userId);
        assertEquals(200, invalidUpdateResponse.getStatusCode());
        assertEquals("User updated successfully.", invalidUpdateResponse.jsonPath().getString("message"));

        // Verify: Only valid fields should be updated
        Response verifyResponse = RestAssured.given()
                .when()
                .get("/user/" + userId);
        assertEquals(200, verifyResponse.getStatusCode());
        assertEquals("stillValid@example.com", verifyResponse.jsonPath().getMap("user").get("email"));
        assertNull(verifyResponse.jsonPath().getMap("user").get("invalidField")); // This should not exist
    }

    @Test
    public void testDeleteUser() {
        String uniqueEmail = "delete" + System.currentTimeMillis() + "@example.com";
        String userJson = String.format(
                "{ \"name\": \"Delete Test\", \"email\": \"%s\", \"password\": \"deletePass123\", \"type\": \"STUDENT\" }",
                uniqueEmail
        );

        Response createResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(userJson)
                .when()
                .post("/signup");
        assertEquals(201, createResponse.getStatusCode());
        int userId = createResponse.jsonPath().getInt("userId");

        // Test 1: Successful deletion
        Response deleteResponse = RestAssured.given()
                .when()
                .delete("/delete/" + userId);
        assertEquals(200, deleteResponse.getStatusCode());
        assertEquals("User deleted successfully.", deleteResponse.jsonPath().getString("message"));

        // Verify: User no longer exists
        Response getResponse = RestAssured.given()
                .when()
                .get("/user/" + userId);
        assertEquals(404, getResponse.getStatusCode());
        assertEquals("User not found with id: " + userId, getResponse.jsonPath().getString("message"));

        // Test 2: Attempt to delete a non-existent user
        Response deleteNonExistent = RestAssured.given()
                .when()
                .delete("/delete/" + userId); // User already deleted
        assertEquals(404, deleteNonExistent.getStatusCode());
        assertEquals("User not found with id: " + userId, deleteNonExistent.jsonPath().getString("message"));
    }

    @Test
    public void testGetUserClubs() {
        String uniqueEmail = "clubs" + System.currentTimeMillis() + "@example.com";
        String userJson = String.format(
                "{ \"name\": \"Clubs Test\", \"email\": \"%s\", \"password\": \"testPass123\", \"type\": \"STUDENT\" }",
                uniqueEmail
        );

        Response createResponse = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(userJson)
                .when()
                .post("/signup");
        assertEquals(201, createResponse.getStatusCode());
        int userId = createResponse.jsonPath().getInt("userId");

        // Test 1: Valid user with no clubs
        Response getClubsResponse = RestAssured.given()
                .when()
                .get(String.format("/user/%d/clubs", userId));
        assertEquals(200, getClubsResponse.getStatusCode());
        assertNotNull(getClubsResponse.jsonPath().get("clubs"));
        assertTrue(getClubsResponse.jsonPath().getList("clubs").isEmpty());

        // Setup: Add a club and join it
        int clubId = 24; // Replace with a valid club ID
        Response joinClubResponse = RestAssured.given()
                .when()
                .put(String.format("/joinClub/%d/%d", userId, clubId));
        assertEquals(200, joinClubResponse.getStatusCode());
        assertEquals("User successfully joined the club.", joinClubResponse.jsonPath().getString("message"));

        // Test 2: Valid user with clubs
        getClubsResponse = RestAssured.given()
                .when()
                .get(String.format("/user/%d/clubs", userId));
        assertEquals(200, getClubsResponse.getStatusCode());
        assertFalse(getClubsResponse.jsonPath().getList("clubs").isEmpty());

        // Test 3: Invalid user ID
        Response invalidUserResponse = RestAssured.given()
                .when()
                .get("/user/999999/clubs"); // Use an ID that doesn’t exist
        assertEquals(404, invalidUserResponse.getStatusCode());
        assertEquals("User not found with id: 999999", invalidUserResponse.jsonPath().getString("message"));
    }

    @Test
    public void testLeaveClub() {
        int userId = 24;
        int clubId = 92;

        // Test successful leave
        Response response = RestAssured.given()
                .when()
                .put(String.format("/leaveClub/%d/%d", userId, clubId));
        assertEquals(200, response.getStatusCode());
        assertEquals("User successfully left the club.", response.jsonPath().getString("message"));

        // Test leave when not a member
        response = RestAssured.given()
                .when()
                .put(String.format("/leaveClub/%d/%d", userId, clubId));
        assertEquals(400, response.getStatusCode());
        assertEquals("User is not a member of this club.", response.jsonPath().getString("message"));

        // Test invalid user ID
        response = RestAssured.given()
                .when()
                .put(String.format("/leaveClub/%d/%d", -1, clubId));
        assertEquals(404, response.getStatusCode());
        assertEquals("User not found with id: -1", response.jsonPath().getString("message"));

        // Test invalid club ID
        response = RestAssured.given()
                .when()
                .put(String.format("/leaveClub/%d/%d", userId, -1));
        assertEquals(404, response.getStatusCode());
        assertEquals("Club not found with id: -1", response.jsonPath().getString("message"));
    }

    @Test
    public void testChangePassword() {
        int userId = 94;

        // Test successful password change
        Map<String, String> passwords = Map.of("oldPassword", "oldPass123", "newPassword", "newPass123");
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(passwords)
                .when()
                .put(String.format("/user/%d/changePassword", userId));
        assertEquals(200, response.getStatusCode());
        assertEquals("Password changed successfully.", response.jsonPath().getString("message"));

        // Test with incorrect old password
        passwords = Map.of("oldPassword", "wrongOldPass", "newPassword", "newPass123");
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(passwords)
                .when()
                .put(String.format("/user/%d/changePassword", userId));
        assertEquals(401, response.getStatusCode());
        assertEquals("Old password is incorrect.", response.jsonPath().getString("message"));

        // Test missing old or new password
        passwords = Map.of("newPassword", "newPass123");
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(passwords)
                .when()
                .put(String.format("/user/%d/changePassword", userId));
        assertEquals(400, response.getStatusCode());
        assertEquals("Both old and new passwords are required.", response.jsonPath().getString("message"));
    }

}
