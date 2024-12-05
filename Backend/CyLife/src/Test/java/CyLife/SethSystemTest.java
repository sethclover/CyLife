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
public class SethSystemTest {
    
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
        }

        @Test
        public void getUpcomingEventsTest() {
            Response response = RestAssured.given()
                    .when()
                    .get("/upcomingEvents");

            assertEquals(200, response.getStatusCode());
            assertEquals("application/json", response.getContentType());
        }

        @Test
        public void updateEventTest() {
            String updateJson = "{ \"eventName\": \"Updated Event\" }";

            Response response = RestAssured.given()
                    .header("Content-Type", "application/json")
                    .body(updateJson)
                    .when()
                    .put("/events/1");

            assertEquals(200, response.getStatusCode());
        }

        @Test
        public void deleteEventTest() {
            Response response = RestAssured.given()
                    .when()
                    .delete("/events/1");

            assertEquals(200, response.getStatusCode());
            assertEquals("{\"message\":\"Success\"}", response.getBody().asString());
        }
}
