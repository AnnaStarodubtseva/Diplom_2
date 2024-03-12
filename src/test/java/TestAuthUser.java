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
        createUser();
        authUser();
    }
    @Test
    public void checkNegativeAuthUserMissingPassword() {
        createUser();
        authUser();
        missingPassword();
    }
    @Test
    public void checkNegativeAuthUserMissingEmail() {
        createUser();
        authUser();
        missingEmail();
    }
    @Test
    public void checkNegativeAuthUserMissingEmailPassword() {
        createUser();
        authUser();
        missingEmailPassword();
    }
    @Test
    public void checkNegativeAuthUserWrongPassword() {
        createUser();
        authUser();
        wrongPassword();
    }
    @Test
    public void checkNegativeAuthUserWrongEmail() {
        createUser();
        authUser();
        wrongEmail();
    }
    @Test
    public void checkNegativeAuthUserWrongEmailPassword() {
        createUser();
        authUser();
        wrongEmailPassword();
    }
    @Step("Checking create user")
    public void createUser() {
        // Отправляем POST-запрос на создание пользователя
        createUser.createUser().then().assertThat().body("user", notNullValue())
                .and()
                .statusCode(200);
    }
    @Step("Checking successful auth")
    public void authUser() {
        // Отправляем POST-запрос на авторизацию созданного пользователя
        authUser.authUser().then().assertThat().body("user", notNullValue())
                .and()
                .statusCode(200);
        accessToken = authUser.authUser().then().extract().path("accessToken");
    }

    @Step("Checking the auth with a missing password")
    public void missingPassword() {
        // Отправляем POST-запрос на авторизацию пользователя без пароля
        authUser.authUserWithoutPassword().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
    @Step("Checking the auth with a missing email")
    public void missingEmail() {
        // Отправляем POST-запрос на авторизацию пользователя без email
        authUser.authUserWithoutEmail().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
    @Step("Checking the auth with a missing email&password")
    public void missingEmailPassword() {
        // Отправляем POST-запрос на авторизацию пользователя без email и пароля
        authUser.authUserWithoutPasswordEmail().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @Step("Checking the auth with a non-existent password")
    public void wrongPassword() {
        // Отправляем POST-запрос на авторизацию пользователя с неверным паролем
        authUser.authUserWrongPassword().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
    @Step("Checking the auth with a non-existent email")
    public void wrongEmail() {
        // Отправляем POST-запрос на авторизацию пользователя с неверным email
        authUser.authUserWrongEmail()
                .then().assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
    @Step("Checking the auth with a non-existent email&password")
    public void wrongEmailPassword() {
        // Отправляем POST-запрос на авторизацию пользователя с неверным паролем и email
        authUser.authUserWrongEmailPassword().then()
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }
}
