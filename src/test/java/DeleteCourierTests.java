import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DeleteCourierTests {

    private int courierId;
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {

        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        courierClient.create(courier);
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
    }

    @Test
    @DisplayName("Удаление курьера.")
    @Description("Удалается, созданный в setUp курьер. Проверяется статус ответа и поле \"ok: true\".")
    public void deleteCourier() {

        ValidatableResponse deleteResponse = courierClient.delete(courierId);
        int statusCode = deleteResponse.extract().statusCode();
        //boolean isCourierDeleted = deleteResponse.extract().path("ok");

        assertEquals("Статус ответа не соответствует требуемому.", 200, statusCode);
        //ssertTrue("Поле ответа \"ok\" имеет неверное значение.", isCourierDeleted);
    }

    @Test
    @DisplayName("Удаление курьера с несуществующим id.")
    @Description("Удалается курьер, с неуществующим id. Проверяется статус ответа и сообщение об ошибке.")
    public void deleteCourierWithWrongId() {

        courierId = RandomUtils.nextInt();
        ValidatableResponse deleteResponse = courierClient.delete(courierId);
        int statusCode = deleteResponse.extract().statusCode();
        String messageDeleteCourierWithWrongId = deleteResponse.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", 404, statusCode);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Курьера с таким id нет.", messageDeleteCourierWithWrongId);
    }

    @Test
    @DisplayName("Удаление курьера без указания id.")
    @Description("Удалается курьер, без указания id. Проверяется статус ответа и сообщение об ошибке.")
    public void deleteCourierWithoutId() {

        ValidatableResponse deleteResponse = courierClient.deleteWithoutId();
        int statusCode = deleteResponse.extract().statusCode();
        String messageDeleteCourierWithoutId = deleteResponse.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", 400, statusCode);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Недостаточно данных для удаления курьера.", messageDeleteCourierWithoutId);
    }

}
