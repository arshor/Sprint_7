import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;

public class acceptOrderTests {

    private int orderTrack = 0;
    private int orderId = 0;
    private OrderClient orderClient;
    private Order order;
    private int courierId = 0;
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {

        orderClient = new OrderClient();
        order = OrderGenerator.getConst();
        order.setColor(List.of());
        ValidatableResponse createResponse = orderClient.create(order);
        orderTrack = createResponse.extract().path("track");
        ValidatableResponse createResponse2 = orderClient.getOrder(String.valueOf(orderTrack));
        orderId = createResponse2.extract().path("order.id");

        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
    }

    @After
    public void cleanUp() {

        if (orderTrack != 0) {
            orderClient.cancelOrder(orderTrack);
        }

        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Принять заказ.")
    @Description("Принять заказ по номеру заказа и номеру курьера. Проверка статуса ответа и поля \"ok: true\" .")
    public void acceptOrderByTrackAndCourierId() {

        ValidatableResponse orderResponse = orderClient.acceptOrder(String.valueOf(orderId), String.valueOf(courierId));

        orderResponse.assertThat()
                .statusCode(200)
                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Принять заказ без номера курьера.")
    @Description("Принять заказ по номеру заказа и без номера курьера. Проверка статуса ответа и сообщения об ошибке.")
    public void acceptOrderByTrackAndWithoutCourierId() {

        ValidatableResponse orderResponse = orderClient.acceptOrder(String.valueOf(orderTrack), "");

        orderResponse.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принять заказ без номера заказа.")
    @Description("Принять заказ без номера заказа и с номером курьера. Проверка статуса ответа и сообщения об ошибке.")
    public void acceptOrderByCourierIdAndWithoutTrack() {

        ValidatableResponse orderResponse = orderClient.acceptOrder("", String.valueOf(courierId));

        orderResponse.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принять заказ с несуществующим номером заказа.")
    @Description("Принять заказ с несуществующим номером заказа и номером курьера. Проверка статуса ответа и сообщения об ошибке.")
    public void acceptOrderByWrongTrackAndValidCourierId() {

        ValidatableResponse orderResponse = orderClient.acceptOrder(String.valueOf(RandomUtils.nextInt()), String.valueOf(courierId));

        orderResponse.assertThat()
                .statusCode(404)
                .body("message", equalTo("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Принять заказ с несуществующим номером курьера.")
    @Description("Принять заказ с номером заказа и несуществующим номером курьера. Проверка статуса ответа и сообщения об ошибке.")
    public void acceptOrderByValidTrackAndWrongCourierId() {

        ValidatableResponse orderResponse = orderClient.acceptOrder(String.valueOf(orderTrack), String.valueOf(RandomUtils.nextInt()));

        orderResponse.assertThat()
                .statusCode(404)
                .body("message", equalTo("Курьера с таким id не существует"));
    }
}

