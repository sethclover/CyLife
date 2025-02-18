package CyLife;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import CyLife.Users.UserDTO;
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

    @Test
    public void testJoinClub() {
        // Use existing user ID and club ID
        int userId = 160;
        int clubId = 24;

        Response response = RestAssured.given()
                .when()
                .put(String.format("/joinClub/%d/%d", userId, clubId));

        assertEquals(200, response.getStatusCode()); // Check for 200 OK
        assertEquals("User successfully joined the club.", response.jsonPath().getString("message"));
    }

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
        String uniqueEmail = "test" + System.currentTimeMillis() + "@example.com";

        // Test with valid input
        String userJson = String.format(
                "{ \"name\": \"Dhvani M\", \"email\": \"%s\", \"password\": \"securePassword123\", \"type\": \"STUDENT\" }",
                uniqueEmail
        );
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(userJson)
                .when()
                .post("/signup");

        assertEquals(201, response.getStatusCode());
        assertEquals("User registered successfully.", response.jsonPath().getString("message"));

        // Test with missing fields
        String invalidJson = "{ \"name\": \"Test User\" }";
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(invalidJson)
                .when()
                .post("/signup");

        assertEquals(409, response.getStatusCode());
        assertTrue(response.jsonPath().getString("message").contains("Invalid input"));
    }

    @Test
    public void testGetUserById() {
        int validUserId = 95;

        // Test valid user
        Response response = RestAssured.given()
                .when()
                .get("/user/" + validUserId);
        assertEquals(200, response.getStatusCode());
        assertNotNull(response.jsonPath().get("user"));

        // Test user not found
        int invalidUserId = -1;
        response = RestAssured.given()
                .when()
                .get("/user/" + invalidUserId);
        assertEquals(404, response.getStatusCode());
        assertEquals("User not found with id: " + invalidUserId, response.jsonPath().getString("message"));

        // Test internal server error (simulate database issue)
        response = RestAssured.given()
                .when()
                .get("/user/errorCase");
        assertEquals(500, response.getStatusCode());
    }

    @Test
    public void testJoinClubConflict() {
        int userId = 130;
        int clubId = 24;

        // Join the club for the first time
        Response response = RestAssured.given()
                .when()
                .put("/joinClub/" + userId + "/" + clubId);
        assertEquals(200, response.getStatusCode());
        assertEquals("User successfully joined the club.", response.jsonPath().getString("message"));

        // Attempt to join the same club again
        response = RestAssured.given()
                .when()
                .put("/joinClub/" + userId + "/" + clubId);
        assertEquals(400, response.getStatusCode());
        assertEquals("User is already a member of this club.", response.jsonPath().getString("message"));
    }

    @Test
    public void testDeleteUserNotFound() {
        // Attempt to delete a non-existent user
        Response response = RestAssured.given()
                .when()
                .delete("/delete/99999");

        assertEquals(404, response.getStatusCode());
        assertEquals("User not found with id: 99999", response.jsonPath().getString("message"));
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
        int userId = 92;
        int clubId = 23;

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
        int userId = 130;

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


    @Test
    public void testCheckMembershipStatus() {
        int userId = 133;
        int clubId = 23;

        // Test 1: User is a member
        Response response = RestAssured.given()
                .when()
                .get(String.format("/checkMembershipStatus/%d/%d", userId, clubId));
        assertEquals(200, response.getStatusCode());
        assertTrue(response.jsonPath().getBoolean("isMember"));
        assertEquals("User is a member of the club.", response.jsonPath().getString("message"));

        // Test 2: User is not a member
        response = RestAssured.given()
                .when()
                .get(String.format("/checkMembershipStatus/%d/%d", userId, 999)); // Invalid club ID
        assertEquals(200, response.getStatusCode());
        assertFalse(response.jsonPath().getBoolean("isMember"));
        assertEquals("User is not a member of the club.", response.jsonPath().getString("message"));

        // Test 3: Non-existent user
        response = RestAssured.given()
                .when()
                .get(String.format("/checkMembershipStatus/%d/%d", 999999, clubId));
        assertEquals(404, response.getStatusCode());
        assertEquals("User not found with id: 999999", response.jsonPath().getString("message"));

        // Test 4: Non-existent club
        response = RestAssured.given()
                .when()
                .get(String.format("/checkMembershipStatus/%d/%d", userId, 999999));
        assertEquals(404, response.getStatusCode());
        assertEquals("Club not found with id: 999999", response.jsonPath().getString("message"));
    }

    @Test
    public void testJoinClubEnhanced() {
        int userId = 100; // Replace with a valid user ID
        int clubId = 200; // Replace with a valid club ID

        // Test 1: Successful join
        Response response = RestAssured.given()
                .when()
                .put(String.format("/joinClub/%d/%d", userId, clubId));
        assertEquals(400, response.getStatusCode());
        assertEquals("User successfully joined the club.", response.jsonPath().getString("message"));

        // Test 2: Already a member
        response = RestAssured.given()
                .when()
                .put(String.format("/joinClub/%d/%d", userId, clubId));
        assertEquals(200, response.getStatusCode());
        assertEquals("User is already a member of this club.", response.jsonPath().getString("message"));

        // Test 3: Invalid user ID
        response = RestAssured.given()
                .when()
                .put(String.format("/joinClub/%d/%d", -1, clubId));
        assertEquals(404, response.getStatusCode());
        assertEquals("User not found with id: -1", response.jsonPath().getString("message"));

        // Test 4: Invalid club ID
        response = RestAssured.given()
                .when()
                .put(String.format("/joinClub/%d/%d", userId, -1));
        assertEquals(404, response.getStatusCode());
        assertEquals("Club not found with id: -1", response.jsonPath().getString("message"));
    }

    @Test
    public void testLeaveClubEnhanced() {
        int userId = 140;
        int clubId = 24;

        // Test 1: Successful leave
        Response response = RestAssured.given()
                .when()
                .put(String.format("/leaveClub/%d/%d", userId, clubId));
        assertEquals(200, response.getStatusCode());
        assertEquals("User successfully left the club.", response.jsonPath().getString("message"));

        // Test 2: Not a member
        response = RestAssured.given()
                .when()
                .put(String.format("/leaveClub/%d/%d", userId, clubId));
        assertEquals(400, response.getStatusCode());
        assertEquals("User is not a member of this club.", response.jsonPath().getString("message"));

        // Test 3: Invalid user ID
        response = RestAssured.given()
                .when()
                .put(String.format("/leaveClub/%d/%d", -1, clubId));
        assertEquals(404, response.getStatusCode());
        assertEquals("User not found with id: -1", response.jsonPath().getString("message"));

        // Test 4: Invalid club ID
        response = RestAssured.given()
                .when()
                .put(String.format("/leaveClub/%d/%d", userId, -1));
        assertEquals(404, response.getStatusCode());
        assertEquals("Club not found with id: -1", response.jsonPath().getString("message"));
    }

    @Test
    public void testUserDTOGettersAndSetters() {
        // Create a UserDTO object
        UserDTO userDTO = new UserDTO(1, "John Doe", "john.doe@example.com", "STUDENT");

        // Verify initial values
        assertEquals(1, userDTO.getUserId());
        assertEquals("John Doe", userDTO.getName());
        assertEquals("john.doe@example.com", userDTO.getEmail());
        assertEquals("STUDENT", userDTO.getType());

        // Update values using setters
        userDTO.setUserId(2);
        userDTO.setName("Jane Doe");
        userDTO.setEmail("jane.doe@example.com");
        userDTO.setType("STAFF");

        // Verify updated values
        assertEquals(2, userDTO.getUserId());
        assertEquals("Jane Doe", userDTO.getName());
        assertEquals("jane.doe@example.com", userDTO.getEmail());
        assertEquals("STAFF", userDTO.getType());
    }

    @Test
    public void testChangePasswordMissingParameters() {
        int userId = 94;

        // Missing newPassword
        Map<String, String> passwords = Map.of("oldPassword", "oldPass123");
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(passwords)
                .when()
                .put(String.format("/user/%d/changePassword", userId));
        assertEquals(400, response.getStatusCode());
        assertEquals("Both old and new passwords are required.", response.jsonPath().getString("message"));
    }

    @Test
    public void testDeleteNonExistentUser() {
        int nonExistentUserId = 999999;

        Response response = RestAssured.given()
                .when()
                .delete("/delete/" + nonExistentUserId);
        assertEquals(404, response.getStatusCode());
        assertEquals("User not found with id: " + nonExistentUserId, response.jsonPath().getString("message"));
    }

    @Test
    public void testJoinClubInvalidInput() {
        int invalidUserId = 1;
        int clubId = 24;

        Response response = RestAssured.given()
                .when()
                .put(String.format("/joinClub/%d/%d", invalidUserId, clubId));
        assertEquals(404, response.getStatusCode());
        assertEquals("User not found with id: " + invalidUserId, response.jsonPath().getString("message"));
    }

    @Test
    public void testSignupExistingEmail() {
        String existingEmail = "john.doe@example.com"; // Use an email that exists in your database

        String userJson = String.format(
                "{ \"name\": \"Existing User\", \"email\": \"%s\", \"password\": \"password123\", \"type\": \"STUDENT\" }",
                existingEmail
        );

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(userJson)
                .when()
                .post("/signup");
        assertEquals(409, response.getStatusCode()); // 409 Conflict
        assertEquals("User already exists.", response.jsonPath().getString("message"));
    }

    @Test
    public void testCheckMembershipStatusInvalidClub() {
        int userId = 133;
        int nonExistentClubId = 999999;

        Response response = RestAssured.given()
                .when()
                .get(String.format("/checkMembershipStatus/%d/%d", userId, nonExistentClubId));
        assertEquals(404, response.getStatusCode());
        assertEquals("Club not found with id: " + nonExistentClubId, response.jsonPath().getString("message"));
    }

    @Test
    public void testUpdateUserPartialFields() {
        int userId = 95; // Replace with a valid user ID
        String updateJson = "{ \"email\": \"partial@example.com\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/update/byId/" + userId);
        assertEquals(200, response.getStatusCode());
        assertEquals("User updated successfully.", response.jsonPath().getString("message"));
    }

    @Test
    public void testSignupInvalidInput() {
        // Test missing email
        String invalidJson = "{ \"name\": \"Test User\", \"password\": \"password123\", \"type\": \"STUDENT\" }";
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(invalidJson)
                .when()
                .post("/signup");
        assertEquals(400, response.getStatusCode()); // Bad Request

        // Test invalid type
        invalidJson = "{ \"name\": \"Test User\", \"email\": \"invalid@example.com\", \"password\": \"password123\", \"type\": \"INVALID\" }";
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(invalidJson)
                .when()
                .post("/signup");
        assertEquals(400, response.getStatusCode()); // Bad Request
    }

    @Test
    public void testUpdateUserWithInvalidKeys() {
        int userId = 95; // Replace with an existing valid user ID
        String invalidJson = "{ \"nonexistentKey\": \"value\", \"email\": \"valid@example.com\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(invalidJson)
                .when()
                .put("/update/byId/" + userId);
        assertEquals(404, response.getStatusCode());
        assertEquals("User updated successfully.", response.jsonPath().getString("message"));

        // Verify that only valid fields were updated
        Response getResponse = RestAssured.given()
                .when()
                .get("/user/" + userId);
        assertEquals(200, getResponse.getStatusCode());
        assertEquals("valid@example.com", getResponse.jsonPath().getMap("user").get("email"));
    }

    @Test
    public void testLoginUserWithMissingCredentials() {
        // Missing password
        String missingPasswordJson = "{ \"email\": \"john.doe@example.com\" }";
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(missingPasswordJson)
                .when()
                .post("/login");
        assertEquals(400, response.getStatusCode());
        assertEquals("Email or password is missing.", response.jsonPath().getString("message"));

        // Missing email
        String missingEmailJson = "{ \"password\": \"password123\" }";
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(missingEmailJson)
                .when()
                .post("/login");
        assertEquals(400, response.getStatusCode());
        assertEquals("Email or password is missing.", response.jsonPath().getString("message"));
    }

    @Test
    public void testGetUserClubsNoClubs() {
        int userId = 88;
        Response response = RestAssured.given()
                .when()
                .get(String.format("/user/%d/clubs", userId));
        assertEquals(200, response.getStatusCode());
        assertTrue(response.jsonPath().getList("clubs").isEmpty());
    }

    @Test
    public void testDeleteUserDatabaseError() {
        int userId = 209;

        // Simulate a database error
        Response response = RestAssured.given()
                .when()
                .delete("/delete/" + userId);
        assertEquals(500, response.getStatusCode());
        assertTrue(response.jsonPath().getString("message").contains("Internal Server Error"));
    }


}
