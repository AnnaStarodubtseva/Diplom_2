import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
public class GetOrders {
    private static final String GET_ENDPOINT = "/api/orders";
    public Response getOrdersUserWithAuth(String accessToken) {
        Response response = given()
                .header("authorization", accessToken)
                .get(GET_ENDPOINT);
      return response;
    }
    public Response getOrdersUserWithoutAuth() {
        Response response = given()
                .get(GET_ENDPOINT);
        return response;
    }

}
