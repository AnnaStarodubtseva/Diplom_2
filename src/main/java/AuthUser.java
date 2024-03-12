import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class AuthUser {
    private static final String AUTH_ENDPOINT = "/api/auth/login";
    Auth authUser = new Auth("ee@ee.ru", "Qwerty");
    Auth withoutPassword = new Auth("ee@ee.ru", "");
    Auth withoutEmail = new Auth("", "Qwerty");
    Auth withoutPasswordEmail = new Auth("", "");
    Auth wrongPassword = new Auth("ee@ee.ru", "123");
    Auth wrongEmail = new Auth("ff@ff.ru", "Qwerty");
    Auth wrongPasswordEmail = new Auth("ff@ff.ru", "123");

    public Response authUser() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(authUser)
                .when()
                .post(AUTH_ENDPOINT);
        return response;
    }
    public Response authUserWithoutPassword() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(withoutPassword)
                .when()
                .post(AUTH_ENDPOINT);
        return response;
    }
    public Response authUserWithoutEmail() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(withoutEmail)
                .when()
                .post(AUTH_ENDPOINT);
        return response;
    }
    public Response authUserWithoutPasswordEmail() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(withoutPasswordEmail)
                .when()
                .post(AUTH_ENDPOINT);
        return response;
    }
    public Response authUserWrongPassword() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(wrongPassword)
                .when()
                .post(AUTH_ENDPOINT);
        return response;
    }
    public Response authUserWrongEmail() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(wrongEmail)
                .when()
                .post(AUTH_ENDPOINT);
        return response;
    }
    public Response authUserWrongEmailPassword() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(wrongPasswordEmail)
                .when()
                .post(AUTH_ENDPOINT);
        return response;
    }

}
