import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestCreateUser extends BaseURI {

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
    public void checkPositiveUserCreation() {
        createUser();
        authUser();
    }
    @Test
    public void checkDuplicateUserCreation() {
        createUser();
        authUser();
        duplicateUserCreation();
    }
    @Test
    public void checkNegativeUserCreationMissingPassword() {
        createUser();
        authUser();
        missingPassword();
    }
    @Test
    public void checkNegativeUserCreationMissingEmail() {
        createUser();
        authUser();
        missingEmail();
    }
    @Test
    public void checkNegativeUserCreationMissingName() {
        createUser();
        authUser();
        missingName();
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

    @Step("Check that it is impossible to create two identical users")
    public void duplicateUserCreation() {
        // Отправляем POST-запрос на создание такого же пользователя
        createUser.createUser().then().assertThat().body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }
    @Step("Check that in order to create a courier, you need to fill password")
    public void missingPassword() {
        // Отправляем POST-запрос на создание пользователя без пароля
        createUser.createUserWithoutPassword().then()
                .assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
    @Step("Check that in order to create a courier, you need to fill email")
    public void missingEmail() {
        // Отправляем POST-запрос на создание пользователя без email
        createUser.createUserWithoutEmail().then()
                .assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }
    @Step("Check that in order to create a courier, you need to fill name")
    public void missingName() {
        // Отправляем POST-запрос на создание пользователя без имени
        createUser.createUserWithoutName().then()
                .assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

}
