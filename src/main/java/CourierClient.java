import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient extends RestClient {

    private static final String COURIER_PATH = "api/v1/courier/";
    private static final String LOGIN_PATH = "api/v1/courier/login/";

    @Step("Регистрация нового курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }

    @Step("Авторизация курьера")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Удаление курьера с id")
    public ValidatableResponse delete(int id) {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH + id)
                .then();
    }

    @Step("Удаление курьера без id")
    public ValidatableResponse deleteWithoutId() {
        return given()
                .spec(getBaseSpec())
                .when()
                .delete(COURIER_PATH)
                .then();
    }
}
