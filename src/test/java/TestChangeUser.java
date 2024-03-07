import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class TestChangeUser extends BaseURI {
    String accessToken;
    String expectResult;
    String actualResult;
    CreateUser createUser = new CreateUser();
    AuthUser authUser = new AuthUser();
    DeleteUser deleteUser = new DeleteUser();
    ChangeUser changeUser = new ChangeUser();
    GetUser getUser = new GetUser();
    @Before
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
    }
    @After
    public void deleteUser() {
        deleteUser.deleteUser(accessToken);
    }
    @Test
    public void checkChangeDataWithToken() {
        testCreateUser();
        testAuthUser();
        testChangeUserWithToken();
    }
    @Test
    public void checkChangeDataWithoutToken() {
        testCreateUser();
        testAuthUser();
        testChangeUserWithoutToken();
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
    @Step("Checking for changes in user data with a token")
    public void testChangeUserWithToken() {
        // Отправляем PATCH-запрос на изменение данных созданного пользователя
        changeUser.changeWithToken(accessToken).then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        // Проверяем что данные изменились
        getUser.getDataUser(accessToken).then().assertThat().body("success", equalTo(true));
        expectResult = changeUser.changeWithToken(accessToken).then().extract().path("email");
        actualResult = getUser.getDataUser(accessToken).then().extract().path("email");
        assertEquals(expectResult, actualResult);
    }
    @Step("Checking for changes in user data without a token")
    public void testChangeUserWithoutToken() {
        //Отправляем PATCH-запрос на изменение данных созданного пользователя
        changeUser.changeWithoutToken().then().assertThat().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }
}
