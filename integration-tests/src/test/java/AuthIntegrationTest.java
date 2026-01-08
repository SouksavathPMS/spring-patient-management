import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTest {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnOKWithValidToken() {
        // 1. Arrange
        // 2. Act
        // 3. Assert
        String loginPayload = """
            {
                "email" : "testuser@test.com",
                "password" : "password123"
             }
        """;

        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .when()
                .post("/api/v1/auth/login")
                .then()
                .statusCode(200)
                .body("data.token", notNullValue())
                .extract()
                .response();

        System.out.println("Generated token: " + response.jsonPath().getString("data.token"));
    }
}
