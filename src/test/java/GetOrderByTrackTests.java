import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class GetOrderByTrackTests {

    private int orderTrack;
    private OrderClient orderClient;
    private Order order;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = OrderGenerator.getConst();

        ValidatableResponse createResponse = orderClient.createOrder(order);
        orderTrack = createResponse.extract().path("track");
    }

    @After
    public void cleanUp() {
        if (orderTrack != 0) {
            orderClient.cancelOrder(orderTrack);
        }
    }

    @Test
    @DisplayName("Получить заказ по его номеру.")
    @Description("Получение заказа по номеру трека. " +
            "Проверка статуса ответа при получении заказа по его номеру и проверка поля order.")
    public void getOrderByTrack() {

        ValidatableResponse orderResponse = orderClient.getOrder(String.valueOf(orderTrack));

        int statusCodeGetOrderByTrack = orderResponse.extract().statusCode();
        int actualOrderTrack = orderResponse.extract().path("order.track");

        assertEquals("Статус ответа не соответствует требуемому.", SC_OK, statusCodeGetOrderByTrack);
        assertEquals("Поле ответа order.track должно совпадать с треком заказа.", actualOrderTrack, orderTrack);

//        orderResponse.assertThat()
//                .statusCode(SC_OK)
//                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Получить несуществующий заказ.")
    @Description("Получение заказа по номеру трека. " +
            "Проверка статуса ответа при получении несуществующего заказа по его номеру и проверка сообщения об ошибке.")
    public void getOrderByWrongTrack() {

        orderTrack = RandomUtils.nextInt();

        ValidatableResponse orderResponse = orderClient.getOrder(String.valueOf(orderTrack));

        int statusCodeGetOrderByWrongTrack = orderResponse.extract().statusCode();
        String messageGetOrderByWrongTrack = orderResponse.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", SC_NOT_FOUND, statusCodeGetOrderByWrongTrack);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Заказ не найден", messageGetOrderByWrongTrack);

//        orderResponse.assertThat()
//                .statusCode(SC_NOT_FOUND)
//                .body("message", equalTo("Заказ не найден"));
    }

    @Test
    @DisplayName("Получить заказ без номера.")
    @Description("Получение заказа без номер трека. " +
            "Проверка статуса ответа при получении заказа без указания его номера и проверка сообщения об ошибке.")
    public void getOrderWithoutTrack() {

        ValidatableResponse orderResponse = orderClient.getOrder(null);

        int statusCodeGetOrderWithoutTrack = orderResponse.extract().statusCode();
        String messageGetOrderWithoutTrack = orderResponse.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", SC_BAD_REQUEST, statusCodeGetOrderWithoutTrack);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Недостаточно данных для поиска", messageGetOrderWithoutTrack);

//        orderResponse.assertThat()
//                .statusCode(	SC_BAD_REQUEST)
//                .body("message", equalTo("Недостаточно данных для поиска"));
    }

}
