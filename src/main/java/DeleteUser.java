import static io.restassured.RestAssured.given;

public class DeleteUser {
    private static final String DELETE_ENDPOINT = "/api/auth/user";
    public void deleteUser(String accessToken) {
        given()
                .header("authorization", accessToken)
                .when()
                .delete(DELETE_ENDPOINT);
   }

    }

