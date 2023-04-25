import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class getOrderByTrackTests {

    private int orderTrack;
    private OrderClient orderClient;
    private Order order;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = OrderGenerator.getConst();

        ValidatableResponse createResponse = orderClient.create(order);
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

        orderResponse.assertThat()
                .statusCode(200)
                .body("order", notNullValue());
    }

    @Test
    @DisplayName("Получить несуществующий заказ.")
    @Description("Получение заказа по номеру трека. " +
            "Проверка статуса ответа при получении несуществующего заказа по его номеру и проверка сообщения об ошибке.")
    public void getOrderByWrongTrack() {

        orderTrack = RandomUtils.nextInt();

        ValidatableResponse orderResponse = orderClient.getOrder(String.valueOf(orderTrack));

        orderResponse.assertThat()
                .statusCode(404)
                .body("message", equalTo("Заказ не найден"));
    }

    @Test
    @DisplayName("Получить заказ без номера.")
    @Description("Получение заказа без номер трека. " +
            "Проверка статуса ответа при получении заказа без указания его номера и проверка сообщения об ошибке.")
    public void getOrderWithoutTrack() {

        ValidatableResponse orderResponse = orderClient.getOrder(null);

        orderResponse.assertThat()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для поиска"));
    }

}
