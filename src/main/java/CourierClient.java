import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient {

    private static final String COURIER_PATH = "api/v1/courier/";
    private static final String LOGIN_PATH = "api/v1/courier/login/";

    public ValidatableResponse create(Courier courier) {
        return given()
                //.spec(getBaseSpec())
                .header("Content-type", "application/json")
                .and()
                .baseUri("http://qa-scooter.praktikum-services.ru")
                .and()
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                //.spec(getBaseSpec())
                .header("Content-type", "application/json")
                .and()
                .baseUri("http://qa-scooter.praktikum-services.ru")
                .and()
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    public ValidatableResponse delete(int id) {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        return given()
                //.spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH + id)
                .then();
    }
}
