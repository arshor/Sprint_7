import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoginCourierTests {

    private int courierId;
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {

        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
        ValidatableResponse createResponse = courierClient.create(courier);
    }

    @After
    public void cleanUp() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Логин курьера в системе успешен.")
    @Description("Логинимся в системе с действующими данными. Поверяется статус ответа, поле \"ok: true\".")
    public void loginCourierIsOk() {

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int statusCode = loginResponse.extract().statusCode();
        courierId = loginResponse.extract().path("id");

        assertEquals("Статус ответа не соответствует требуемому.", 200, statusCode);
        assertTrue("Id курьера не должен быть равен 0.", courierId != 0);
    }

    @Test
    @DisplayName("Логин курьера в системе без логина.")
    @Description("Логинимся в системе с пустым полем логин. Поверяется статус ответа, поле \"message\".")
    public void loginCourierErrorWithEmptyLogin() {

        courier.setLogin("");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int statusCode = loginResponse.extract().statusCode();
        String messageLoginCourierErrorWithEmptyLogin = loginResponse.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", 400, statusCode);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Недостаточно данных для входа", messageLoginCourierErrorWithEmptyLogin);
    }

    @Test
    @DisplayName("Логин курьера в системе без пароля.")
    @Description("Логинимся в системе с пустым полем пароля. Поверяется статус ответа, поле \"message\".")
    public void loginCourierErrorWithEmptyPassword() {

        courier.setPassword("");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int statusCode = loginResponse.extract().statusCode();
        String messageLoginCourierErrorWithEmptyPassword = loginResponse.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", 400, statusCode);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Недостаточно данных для входа", messageLoginCourierErrorWithEmptyPassword);
    }

    @Test
    @DisplayName("Логин курьера в системе с неверным логином.")
    @Description("Логинимся в системе с неверным логином и действующим паролем. Поверяется статус ответа, поле \"message\".")
    public void loginCourierErrorWithWrongLogin() {

        courier.setLogin(courier.getLogin() + "_");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int statusCode = loginResponse.extract().statusCode();
        String messageLoginCourierErrorWithWrongLogin = loginResponse.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", 404, statusCode);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Учетная запись не найдена", messageLoginCourierErrorWithWrongLogin);
    }

    @Test
    @DisplayName("Логин курьера в системе с неверным паролем.")
    @Description("Логинимся в системе с действующим логином и неверным паролем. Поверяется статус ответа, поле \"message\".")
    public void loginCourierErrorWithWrongPassword() {

        courier.setPassword(courier.getPassword() + "_");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int statusCode = loginResponse.extract().statusCode();
        String messageLoginCourierErrorWithWrongPassword = loginResponse.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", 404, statusCode);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Учетная запись не найдена", messageLoginCourierErrorWithWrongPassword);
    }

}
