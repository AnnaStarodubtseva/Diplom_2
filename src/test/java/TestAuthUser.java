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
    public void checkPositiveAuthUser() {
        testCreateUser();
        testAuthUser();
    }
    @Test
    public void checkNegativeAuthUserMissingFields() {
        testCreateUser();
        testAuthUser();
        testMissingPassword();
        testMissingEmail();
        testMissingEmailPassword();
    }
    @Test
    public void checkNegativeAuthUserWrongCredentials() {
        testCreateUser();
        testAuthUser();
        testWrongPassword();
        testWrongEmail();
        testWrongEmailPassword();
    }
    @Step("Checking create user")
    public void testCreateUser() {
        // Отправляем POST-запрос на создание пользователя
        createUser.createUser().then().assertThat().body("user", notNullValue())
                .and()
                .statusCode(200);
    }
    @Step("Checking successful auth")
    public void testAuthUser() {
        // Отправляем POST-запрос на авторизацию созданного пользователя
        authUser.authUser().then().assertThat().body("user", notNullValue())
                .and()
                .statusCode(200);
        accessToken = authUser.authUser().then().extract().path("accessToken");
    }

    @Step("Checking the auth with a missing password")
    public void testMissingPassword() {
        // Отправляем POST-запрос на авторизацию пользователя без пароля
        authUser.authUserWithoutPassword().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
    @Step("Checking the auth with a missing email")
    public void testMissingEmail() {
        // Отправляем POST-запрос на авторизацию пользователя без email
        authUser.authUserWithoutEmail().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
    @Step("Checking the auth with a missing email&password")
    public void testMissingEmailPassword() {
        // Отправляем POST-запрос на авторизацию пользователя без email и пароля
        authUser.authUserWithoutPasswordEmail().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @Step("Checking the auth with a non-existent password")
    public void testWrongPassword() {
        // Отправляем POST-запрос на авторизацию пользователя с неверным паролем
        authUser.authUserWrongPassword().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
    @Step("Checking the auth with a non-existent email")
    public void testWrongEmail() {
        // Отправляем POST-запрос на авторизацию пользователя с неверным email
        authUser.authUserWrongEmail()
                .then().assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
    @Step("Checking the auth with a non-existent email&password")
    public void testWrongEmailPassword() {
        // Отправляем POST-запрос на авторизацию пользователя с неверным паролем и email
        authUser.authUserWrongEmailPassword().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
}
