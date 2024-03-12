import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CreateUser {
    private static final String COURIER_ENDPOINT = "/api/auth/register";
    User createUser = new User("ee@ee.ru", "Qwerty", "aBcDeFgHiGk");
    User withoutEmail = new User("", "Qwerty", "aBcDeFgHiGk");
    User withoutPassword = new User("ee@ee.ru", "", "aBcDeFgHiGk");
    User withoutName = new User("ee@ee.ru", "Qwerty", "");

    public Response createUser() {
        // Отправляем POST-запрос на создание пользователя
        Response response = given()
                .header("Content-type", "application/json")
                .body(createUser)
                .when()
                .post(COURIER_ENDPOINT);
        return response;
    }
    public Response createUserWithoutEmail() {
        // Отправляем POST-запрос на создание курьера без email
        Response response = given()
                .header("Content-type", "application/json")
                .body(withoutEmail)
                .when()
                .post(COURIER_ENDPOINT);
        return response;
    }
    public Response createUserWithoutPassword() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(withoutPassword)
                .when()
                .post(COURIER_ENDPOINT);
        return response;
    }
    public Response createUserWithoutName() {
        Response response = given()
                .header("Content-type", "application/json")
                .body(withoutName)
                .when()
                .post(COURIER_ENDPOINT);
        return response;
    }
}
