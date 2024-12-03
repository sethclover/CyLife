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
        int userId = 95; // Replace with an existing valid user ID from your database
        int clubId = 23; // Replace with a valid club ID, e.g., Computer Science Club

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

}
