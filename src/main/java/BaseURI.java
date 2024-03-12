import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class BaseURI {
    public RequestSpecification requestSpec;

    public BaseURI() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";

        requestSpec = new RequestSpecBuilder()
                .setBaseUri(RestAssured.baseURI)
                .build();
    }
}