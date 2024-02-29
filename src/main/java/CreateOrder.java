import com.google.gson.Gson;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
public class CreateOrder {
    private static final String ORDER_ENDPOINT = "/api/orders";
    public Response createOrderWithAuthAndIngredients(String accessToken) {
        // Отправляем POST-запрос на создание заказа с авторизацией и ингридиентами
        Order ingredients = new Order(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        Gson gson = new Gson();
        String body = gson.toJson(ingredients);
        Response response = given()
                .header("authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(ORDER_ENDPOINT);
        return response;
    }
    public Response createOrderWithoutAuth() {
        // Отправляем POST-запрос на создание заказа без авторизации
        Order ingredients = new Order(Arrays.asList("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"));
        Gson gson = new Gson();
        String body = gson.toJson(ingredients);
        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(ORDER_ENDPOINT);
        return response;
    }
   public Response createOrderWithAuthWithoutIngredients(String accessToken) {
        // Отправляем POST-запрос на создание заказа с авторизацией и без ингридиентов
       Order withoutIngredients = new Order(Arrays.asList());
       Gson gson = new Gson();
       String body = gson.toJson(withoutIngredients);
       Response response = given()
               .header("authorization", accessToken)
               .contentType(ContentType.JSON)
               .body(body)
               .when()
               .post(ORDER_ENDPOINT);
       return response;
    }
    public Response createOrderWithAuthWrongHash(String accessToken) {
        // Отправляем POST-запрос на создание заказа с авторизацией и с неверным хешем ингридиентов
        Order wrongHashIngredients = new Order(Arrays.asList("", ""));
        Gson gson = new Gson();
        String body = gson.toJson(wrongHashIngredients);
        Response response = given()
                .header("authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post(ORDER_ENDPOINT);
        return response;
    }
}

