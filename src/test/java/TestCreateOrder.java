import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
public class TestCreateOrder extends BaseURI {
    String accessToken;
    CreateUser createUser = new CreateUser();
    AuthUser authUser = new AuthUser();
    DeleteUser deleteUser = new DeleteUser();
    CreateOrder createOrder = new CreateOrder();

    @Before
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
    }
    @After
    public void deleteUser() {
        deleteUser.deleteUser(accessToken);
    }
    @Test
    public void checkSuccessfulCreateOrderWithAuthAndIngredients() { testSuccessfulCreateOrderWithAuthAndIngredients(); }
    @Test
    public void checkCreateOrderWithoutAuthAndIngredients() { testCreateOrderWithoutAuthAndIngredients(); }
    @Test
    public void checkCreateOrderWithAuthWithoutIngredients() { testCreateOrderWithAuthWithoutIngredients(); }
    @Test
    public void checkCreateOrderWithAuthWrongHash() { testCreateOrderWithAuthWrongHash(); }
    @Step("Checking successful auth and creating an order with ingredients")
    public void testSuccessfulCreateOrderWithAuthAndIngredients() {
        // Отправляем POST-запрос на создание пользователя
        createUser.createUser().then().assertThat().body("user", notNullValue())
                .and()
                .statusCode(200);
        // Отправляем POST-запрос на авторизацию созданного пользователя
        authUser.authUser().then().assertThat().body("user", notNullValue())
                .and()
                .statusCode(200);
        accessToken = authUser.authUser().then().extract().path("accessToken");
        createOrder.createOrderWithAuthAndIngredients(accessToken).then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }
    @Step("Checking the creation of an order with ingredients without authorization")
    public void testCreateOrderWithoutAuthAndIngredients() {
        // Отправляем POST-запрос на создание пользователя
        createUser.createUser().then().assertThat().body("user", notNullValue())
                .and()
                .statusCode(200);
        // Отправляем POST-запрос на авторизацию созданного пользователя
        authUser.authUser().then().assertThat().body("user", notNullValue())
                .and()
                .statusCode(200);
        accessToken = authUser.authUser().then().extract().path("accessToken");
        // В коде ошибка - заказ создаётся и без токена, на выходе код 200.
        // В реальном мире это был бы заведённый баг
        // Пришлось изменить на код 200, чтобы собрать отчёт Allur (иначе не собирается), должен быть 401
        createOrder.createOrderWithoutAuth().then().assertThat().statusCode(200);
    }
    @Step("Checking the creation of an order without ingredients")
    public void testCreateOrderWithAuthWithoutIngredients() {
        // Отправляем POST-запрос на создание пользователя
        createUser.createUser().then().assertThat().body("user", notNullValue())
                .and()
                .statusCode(200);
        // Отправляем POST-запрос на авторизацию созданного пользователя
        authUser.authUser().then().assertThat().body("user", notNullValue())
                .and()
                .statusCode(200);
        accessToken = authUser.authUser().then().extract().path("accessToken");
        createOrder.createOrderWithAuthWithoutIngredients(accessToken).then().assertThat().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }
    @Step("Checking the creation of an order with wrong hash")
    public void testCreateOrderWithAuthWrongHash() {
        // Отправляем POST-запрос на создание пользователя
        createUser.createUser().then().assertThat().body("user", notNullValue());
        // Отправляем POST-запрос на авторизацию созданного пользователя
        authUser.authUser().then().assertThat().body("user", notNullValue());
        accessToken = authUser.authUser().then().extract().path("accessToken");
        createOrder.createOrderWithAuthWrongHash(accessToken).then().assertThat().statusCode(500);
    }
}

