import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class PatientIntegrationTest {
    static String _authToken = null;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:4004/";
        String loginPayload = """
            {
                "email" : "testuser@test.com",
                "password" : "password123"
             }
        """;
        _authToken = given()
            .contentType(ContentType.JSON)
            .body(loginPayload)
            .when()
            .post("/api/v1/auth/login")
            .then()
            .statusCode(200)
            .extract().jsonPath().get("data.token");
    }

    @Test
    public void shouldReturnPatientsWithValidToken() {
        given().
                header("Authorization", "Bearer " + _authToken)
                .when().get("/api/v1/patients")
                .then()
                .statusCode(200)
                .body("data", notNullValue());
    }
}
