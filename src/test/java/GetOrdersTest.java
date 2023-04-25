import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

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

        createResponse.assertThat()
                .statusCode(200)
                .body("orders", notNullValue());
        }

}
