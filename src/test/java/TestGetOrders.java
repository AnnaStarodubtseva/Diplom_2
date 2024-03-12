import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Test;
import org.junit.Before;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestGetOrders extends BaseURI {
    String accessToken;
    CreateUser createUser = new CreateUser();
    AuthUser authUser = new AuthUser();
    DeleteUser deleteUser = new DeleteUser();
    CreateOrder createOrder = new CreateOrder();
    GetOrders getOrders = new GetOrders();

    @Before
    public void setUp() {
        RestAssured.requestSpecification = requestSpec;
    }
    @After
    public void deleteUser() {
        deleteUser.deleteUser(accessToken);
    }
    @Test
    public void checkGetOrderUserWithAuth() {
        createUser();
        authUser();
        getOrdersUserWithAuth();
    }
    @Test
    public void checkGetOrderUserWithoutAuth() {
        createUser();
        authUser();
        getOrdersUserWithoutAuth();
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
    @Step("Checking the receipt of orders from an authorized user")
    public void getOrdersUserWithAuth() {
        // Отправляем POST-запрос на создание заказа
        createOrder.createOrderWithAuthAndIngredients(accessToken).then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        // Отправляем GET-запрос на получение заказа
        getOrders.getOrdersUserWithAuth(accessToken).then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }
    @Step("Checking the receipt of orders from an unauthorized user")
    public void getOrdersUserWithoutAuth() {
        // Отправляем POST-запрос на создание заказа
        createOrder.createOrderWithAuthAndIngredients(accessToken).then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
        // Отправляем GET-запрос на получение заказа
        getOrders.getOrdersUserWithoutAuth().then().assertThat().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }
}
