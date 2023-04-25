import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class CreateOrderTests {

    private int orderTrack;
    private OrderClient orderClient;
    private Order order;
    private final List<String> color;

    public CreateOrderTests(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "color = {0}")
    public static Object[][] getColor() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GRAY")},
                {List.of("BLACK, GRAY")},
                {List.of()}
        };
    }

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = OrderGenerator.getConst();
    }

    @After
    public void cleanUp() {
        if (orderTrack != 0) {
            orderClient.cancelOrder(orderTrack);
        }
    }

    @Test
    @DisplayName("Создание заказа с различным выбором цветов.")
    @Description("Создание заказа с различным выбором цветов. Получение заказа в списке по номеру трека. " +
            "Проверка статуса ответа при создании заказа, проверка номера трека и проверка статуса ответа, " +
            "при получении заказа по его номеру")
    public void createOrderWithInDifferentColors() {

        order.setColor(color);
        ValidatableResponse createResponse = orderClient.create(order);

        int statusCode = createResponse.extract().statusCode();
        orderTrack = createResponse.extract().path("track");

        ValidatableResponse orderResponse = orderClient.getOrder(String.valueOf(orderTrack));
        int statusCodeGetOrder = orderResponse.extract().statusCode();

        assertEquals("Статус ответа при создании заказа, не соответствует требуемому.", 201, statusCode);
        assertTrue("Номер трека заказа не должен быть равен 0.", orderTrack != 0);
        assertEquals("Статус ответа получения заказа по номеру, не соответствует требуемому.", 200, statusCodeGetOrder);

    }
}
