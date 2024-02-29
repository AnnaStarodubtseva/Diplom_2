import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
public class GetUser {
    private static final String GET_ENDPOINT = "/api/auth/user";
    public Response getDataUser(String accessToken) {
        Response response = given()
                .header("authorization", accessToken)
                .get(GET_ENDPOINT);
        return response;
    }
}
