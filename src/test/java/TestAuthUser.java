import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestAuthUser extends BaseURI {
    String accessToken;
    CreateUser createUser = new CreateUser();
    AuthUser authUser = new AuthUser();
    DeleteUser deleteUser = new DeleteUser();

    @Before
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
    }
    @After
    public void deleteUser() {
        deleteUser.deleteUser(accessToken);
    }
    @Test
    public void checkPositiveAuthUser() { testSuccessfulAuth(); }
    @Test
    public void checkNegativeAuthUserMissingFields() {
        testMissingFields();
    }
    @Test
    public void checkNegativeAuthUserWrongCredentials() {
        testWrongCredentials();
    }

    @Step("Checking successful auth")
    public void testSuccessfulAuth() {
        // Отправляем POST-запрос на создание пользователя
        createUser.createUser().then().assertThat().body("user", notNullValue())
                .and()
                .statusCode(200);
        // Отправляем POST-запрос на авторизацию созданного пользователя
        authUser.authUser().then().assertThat().body("user", notNullValue())
                .and()
                .statusCode(200);
        accessToken = authUser.authUser().then().extract().path("accessToken");
    }

    @Step("Checking the auth with a missing password/email/password&email")
    public void testMissingFields() {
        // Отправляем POST-запрос на создание пользователя
        createUser.createUser().then().assertThat().body("user", notNullValue());
        // Отправляем POST-запрос на авторизацию созданного пользователя - проверяем его валидность
        authUser.authUser().then().assertThat().body("user", notNullValue());
        accessToken = authUser.authUser().then().extract().path("accessToken");
        // Отправляем POST-запрос на авторизацию пользователя без пароля
        authUser.authUserWithoutPassword().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
        // Отправляем POST-запрос на авторизацию пользователя без email
        authUser.authUserWithoutEmail().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
        // Отправляем POST-запрос на авторизацию пользователя без email и пароля
        authUser.authUserWithoutPasswordEmail().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @Step("Checking the auth with a non-existent password/email/password&email")
    public void testWrongCredentials() {
        // Отправляем POST-запрос на создание пользователя
        createUser.createUser().then().assertThat().body("user", notNullValue());
        // Отправляем POST-запрос на авторизацию созданного пользователя - проверяем его валидность
        authUser.authUser().then().assertThat().body("user", notNullValue());
        accessToken = authUser.authUser().then().extract().path("accessToken");
        // Отправляем POST-запрос на авторизацию пользователя с неверным паролем
        authUser.authUserWrongPassword().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
        // Отправляем POST-запрос на авторизацию пользователя с неверным email
        authUser.authUserWrongEmail()
                .then().assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
        // Отправляем POST-запрос на авторизацию пользователя с неверным паролем и email
        authUser.authUserWrongEmailPassword().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
}
