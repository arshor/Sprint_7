import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.*;

public class GetOrdersTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов.")
    @Description("Получение списка всех заказов. Проверка статуса ответа и списка заказов на непустоту.")
    public void getOrderList() {

        ValidatableResponse createResponse = orderClient.getListOrders();

        int statusCodeGetOrderByTrack = createResponse.extract().statusCode();

        assertEquals("Статус ответа не соответствует требуемому.", SC_OK, statusCodeGetOrderByTrack);
        assertNotNull("Поле ответа order.track должно совпадать с треком заказа.", createResponse.extract().path("orders"));

//        createResponse.assertThat()
//                .statusCode(SC_OK)
//                .body("orders", notNullValue());
        }

}
