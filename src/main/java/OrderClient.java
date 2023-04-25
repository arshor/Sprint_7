import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestClient {

    private static final String ORDER_PATH = "api/v1/orders/";
    private static final String GET_ORDER_PATH = "api/v1/orders/track";
    private static final String CANCEL_ORDER_PATH = "/api/v1/orders/cancel";

    @Step("Создание нового заказа.")
    public ValidatableResponse create(Order order) {
        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then();
    }

    @Step("Получить заказ по его номеру.")
    public ValidatableResponse getOrder(int orderTrack) {
        return given()
                .spec(getBaseSpec())
                .queryParam("t", orderTrack)
                .when()
                .get(GET_ORDER_PATH)
                .then();
    }

    @Step("Получение списка заказов.")
    public ValidatableResponse getListOrders() {
        return given()
                .spec(getBaseSpec())
                .when()
                .get(ORDER_PATH)
                .then();
    }

    @Step("Отменить заказ.")
    public ValidatableResponse cancelOrder(int orderTrack) {

        String json = "{\"track\": \"" + orderTrack + "\"}";

        return given()
                .spec(getBaseSpec())
                .body(json)
                .when()
                .post(CANCEL_ORDER_PATH)
                .then();
    }

}
