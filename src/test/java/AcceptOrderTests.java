import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AcceptOrderTests {

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
        ValidatableResponse createResponse = orderClient.createOrder(order);
        orderTrack = createResponse.extract().path("track");
        ValidatableResponse createResponse2 = orderClient.getOrder(String.valueOf(orderTrack));
        orderId = createResponse2.extract().path("order.id");

        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        courierClient.createCourier(courier);
        ValidatableResponse loginResponse = courierClient.loginCourier(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
    }

    @After
    public void cleanUp() {

        if (orderTrack != 0) {
            orderClient.cancelOrder(orderTrack);
        }

        if (courierId != 0) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Принять заказ.")
    @Description("Принять заказ по номеру заказа и номеру курьера. Проверка статуса ответа и поля \"ok: true\" .")
    public void acceptOrderByTrackAndCourierId() {

        ValidatableResponse orderResponse = orderClient.acceptOrder(String.valueOf(orderId), String.valueOf(courierId));

        int statusCode = orderResponse.extract().statusCode();
        boolean isAcceptOrder = orderResponse.extract().path("ok");

        assertEquals("Статус ответа не соответствует требуемому.", SC_OK, statusCode);
        assertTrue("Поле ответа \"ok\" имеет неверное значение.", isAcceptOrder);

//        orderResponse.assertThat()
//                .statusCode(SC_OK)
//                .body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Принять заказ без номера курьера.")
    @Description("Принять заказ по номеру заказа и без номера курьера. Проверка статуса ответа и сообщения об ошибке.")
    public void acceptOrderByTrackAndWithoutCourierId() {

        ValidatableResponse orderResponse = orderClient.acceptOrder(String.valueOf(orderTrack), "");

        int statusCodeAcceptOrderByTrackAndWithoutCourierId = orderResponse.extract().statusCode();
        String messageAcceptOrderByTrackAndWithoutCourierId = orderResponse.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", SC_BAD_REQUEST, statusCodeAcceptOrderByTrackAndWithoutCourierId);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Недостаточно данных для поиска", messageAcceptOrderByTrackAndWithoutCourierId);

//        orderResponse.assertThat()
//                .statusCode(SC_BAD_REQUEST)
//                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принять заказ без номера заказа.")
    @Description("Принять заказ без номера заказа и с номером курьера. Проверка статуса ответа и сообщения об ошибке.")
    public void acceptOrderByCourierIdAndWithoutTrack() {

        ValidatableResponse orderResponse = orderClient.acceptOrder("", String.valueOf(courierId));

        int statusCodeAcceptOrderByCourierIdAndWithoutTrack = orderResponse.extract().statusCode();
        String messageAcceptOrderByCourierIdAndWithoutTrack = orderResponse.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", SC_BAD_REQUEST, statusCodeAcceptOrderByCourierIdAndWithoutTrack);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Недостаточно данных для поиска", messageAcceptOrderByCourierIdAndWithoutTrack);

//        orderResponse.assertThat()
//                .statusCode(SC_BAD_REQUEST)
//                .body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Принять заказ с несуществующим номером заказа.")
    @Description("Принять заказ с несуществующим номером заказа и номером курьера. Проверка статуса ответа и сообщения об ошибке.")
    public void acceptOrderByWrongTrackAndValidCourierId() {

        ValidatableResponse orderResponse = orderClient.acceptOrder(String.valueOf(RandomUtils.nextInt()), String.valueOf(courierId));

        int statusCodeAcceptByWrongTrackAndValidCourierId = orderResponse.extract().statusCode();
        String messageAcceptByWrongTrackAndValidCourierId = orderResponse.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", SC_NOT_FOUND, statusCodeAcceptByWrongTrackAndValidCourierId);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Заказа с таким id не существует", messageAcceptByWrongTrackAndValidCourierId);

//        orderResponse.assertThat()
//                .statusCode(SC_NOT_FOUND)
//                .body("message", equalTo("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Принять заказ с несуществующим номером курьера.")
    @Description("Принять заказ с номером заказа и несуществующим номером курьера. Проверка статуса ответа и сообщения об ошибке.")
    public void acceptOrderByValidTrackAndWrongCourierId() {

        ValidatableResponse orderResponse = orderClient.acceptOrder(String.valueOf(orderTrack), String.valueOf(RandomUtils.nextInt()));

        int statusCodeAcceptByValidTrackAndWrongCourierId = orderResponse.extract().statusCode();
        String messageAcceptByValidTrackAndWrongCourierId = orderResponse.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", SC_NOT_FOUND, statusCodeAcceptByValidTrackAndWrongCourierId);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Курьера с таким id не существует", messageAcceptByValidTrackAndWrongCourierId);

//        orderResponse.assertThat()
//                .statusCode(SC_NOT_FOUND)
//                .body("message", equalTo("Курьера с таким id не существует"));
    }
}

