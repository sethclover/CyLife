package CyLife;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cglib.core.Local;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class EventControllerTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void getEventsTest() {
        Response response = RestAssured.given()
                .when()
                .get("/events");

        assertEquals(200, response.getStatusCode());
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getBody().asString().contains("eventName")); // Example check for content
    }

    @Test
    public void getUpcomingEventsTest() {
        Response response = RestAssured.given()
                .when()
                .get("/upcomingEvents/1");

        assertEquals(200, response.getStatusCode());
        assertEquals("application/json", response.getContentType());
    }

    @Test
    public void getEventByIdTest() {
        Response response = RestAssured.given()
                .when()
                .get("/events/2");

        if (response.getStatusCode() == 200) {
            assertEquals("application/json", response.getContentType());
        } else {
            assertEquals(404, response.getStatusCode());
        }
    }

    @Test
    public void getEventByIdNotFoundTest() {
        Response response = RestAssured.given()
                .when()
                .get("/events/999"); // Assuming 999 is a non-existent ID

        assertEquals(200, response.getStatusCode());
        assertEquals("", response.getBody().asString()); // Expecting null or empty response
    }

    @Test
    public void createEventTest() {
        String eventJson = "{ \"clubId\": " + 21
                + ", \"eventName\": \"New Event\", \"description\": \"New Event Description\", \"eventLocation\": \"New Event Location\", \"date\": \"2024-12-12\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(eventJson)
                .when()
                .post("/events");

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"message\":\"Success\"}", response.getBody().asString());
    }

    @Test
    public void createEventWithNullBodyTest() {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body("")
                .when()
                .post("/events");

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"message\":\"Failure\"}", response.getBody().asString());
    }

    @Test
    public void updateEventTest() {
        String updateJson = "{ \"eventName\": \"Updated Event\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/events/2"); // existing ID

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void updateEventWithPartialDataTest() {

        String updateJson = "{ \"clubId\": null, \"eventName\": null, \"description\": null, \"eventLocation\": \"New Event Location\", \"date\": \"2024-12-12\" }";
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/events/2"); // existing ID

        assertEquals(200, response.getStatusCode());

        updateJson = "{ \"clubId\": 21, \"eventName\": \"New Event\", \"description\": \"New Event Description\", \"eventLocation\": null, \"date\": null }";
        response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/events/3"); // existing ID

        assertEquals(200, response.getStatusCode());
    }

    @Test
    public void updateEventNoFieldsUpdatedTest() {
        String updateJson = "{}"; // Empty update payload

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/events/2"); // existing ID

        assertEquals(400, response.getStatusCode()); // Expecting Bad Request
        assertTrue(response.getBody().asString().contains("No fields to update"));
    }

    @Test
    public void updateEventAllFieldsUpdatedTest() {
        String updateJson = "{ \"clubId\": 21, \"eventName\": \"Fully Updated Event\", \"description\": \"Full Update\", \"eventLocation\": \"Updated Location\", \"date\": \"2024-12-15\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/events/2"); // existing ID

        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().asString().contains("Fully Updated Event"));
    }

    @Test
    public void updateEventNotFoundTest() {
        String updateJson = "{ \"eventName\": \"Updated Event\" }";

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(updateJson)
                .when()
                .put("/events/1"); // non-existent ID

        assertEquals(200, response.getStatusCode());
        assertEquals("", response.getBody().asString());
    }

    @Test
    public void deleteEventTest() {
        Response response = RestAssured.given()
                .when()
                .delete("/events/2"); // existing ID

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"message\":\"Success\"}", response.getBody().asString());
    }

    @Test
    public void deleteEventNotFoundTest() {
        Response response = RestAssured.given()
                .when()
                .delete("/events/1"); // non-existent ID

        assertEquals(200, response.getStatusCode());
        assertEquals("{\"message\":\"Success\"}", response.getBody().asString());
    }

}
