import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
public class ChangeUser {
    private static final String CHANGE_ENDPOINT = "/api/auth/user";
    Auth changeData = new Auth("xx@xx.ru", "12345Qwerty");
    public Response changeWithToken(String accessToken) {
        Response response = given()
                .header("authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(changeData)
                .when()
                .patch(CHANGE_ENDPOINT);
        return response;
    }
    public Response changeWithoutToken() {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(changeData)
                .when()
                .patch(CHANGE_ENDPOINT);
        return response;
    }
}
