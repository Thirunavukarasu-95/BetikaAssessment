import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ReqResApiTests {

    private static final String BASE_URL = "https://reqres.in/api/";

    @Test
    public void registerUserTest() {
        String requestBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\"}";

        Response response = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("register");

        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.body().asString().contains("token"));
    }

    @Test
    public void loginUserTest() {
        String requestBody = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";

        Response response = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("login");

        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.body().asString().contains("token"));
    }

    @Test
    public void createUserTest() {
        String requestBody = "{\"name\": \"John\", \"job\": \"developer\"}";

        Response response = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("users");

        assertEquals(response.getStatusCode(), 201);
        assertTrue(response.body().asString().contains("createdAt"));
        assertTrue(response.body().asString().contains("id"));
    }

    @Test
    public void updateUserTest() {
        String requestBody = "{\"name\": \"John Doe\", \"job\": \"senior developer\"}";

        Response response = given()
                .baseUri(BASE_URL)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put("users/2");

        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.body().asString().contains("updatedAt"));
    }

    @Test
    public void deleteUserTest() {
        Response response = given()
                .baseUri(BASE_URL)
                .when()
                .delete("users/2");

        assertEquals(response.getStatusCode(), 204);
    }
}
