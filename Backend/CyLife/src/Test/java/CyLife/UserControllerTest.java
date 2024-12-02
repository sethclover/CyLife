
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

        assertEquals(200, response.getStatusCode());
        assertEquals("application/json", response.getContentType());
    }

    @Test
    public void testSignupUser() {
        String userJson = "{ \"name\": \"John Doe\", \"email\": \"john.doe@example.com\", \"password\": \"password\", \"type\": \"STUDENT\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(userJson)
                .when()
                .post("/signup");

        assertEquals(201, response.getStatusCode());
        assertEquals("{\"message\":\"User registered successfully.\",\"status\":\"201\"}", response.getBody().asString());
    }

    @Test
    public void testLoginUser() {
        String loginJson = "{ \"email\": \"john.doe@example.com\", \"password\": \"password\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(loginJson)
                .when()
                .post("/login");

        assertEquals(200, response.getStatusCode());
        assertEquals("application/json", response.getContentType());
    }

    @Test
    public void testJoinClub() {
        Response response = RestAssured.given()
                .when()
                .put("/joinClub/1/1");

        assertEquals(200, response.getStatusCode());
    }
}
