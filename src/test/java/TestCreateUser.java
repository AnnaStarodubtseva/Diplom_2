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
        testUserCreation();
    }
    @Test
    public void checkNegativeUserCreation() {
        testDuplicateUserCreation();
        testMissingFields();
    }
    @Step("Create a user and check the successful creation through the User Auth")
    public void testUserCreation() {
        // Отправляем POST-запрос на создание пользователя
        createUser.createUser().then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        // Отправляем POST-запрос на авторизацию созданного пользователя - исключаем ложно-положительные тесты
        authUser.authUser().then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        accessToken = authUser.authUser().then().extract().path("accessToken");
    }

    @Step("Check that it is impossible to create two identical users")
    public void testDuplicateUserCreation() {
        // Отправляем POST-запрос на создание пользователя
        createUser.createUser().then().assertThat().body("user", notNullValue());
        // Отправляем POST-запрос на авторизацию созданного пользователя - убеждаемся, что пользователь точно создан
        authUser.authUser().then().assertThat().body("user", notNullValue());
        accessToken = authUser.authUser().then().extract().path("accessToken");
        // Отправляем POST-запрос на создание такого же курьера
        createUser.createUser().then().assertThat().body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Step("Check that in order to create a courier, you need to fill in all required fields")
    public void testMissingFields() {
        // Отправляем POST-запрос на создание курьера без пароля
        createUser.createUserWithoutPassword().then()
                .assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
        // Отправляем POST-запрос на создание курьера без email
        createUser.createUserWithoutEmail().then()
                .assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
        // Отправляем POST-запрос на создание курьера без имени
        createUser.createUserWithoutName().then()
                .assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

}
