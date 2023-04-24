import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;


import static org.junit.Assert.*;

public class CreateCourierTests {

    private int courierId;
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {

        courierClient = new CourierClient();
        courier = CourierGenerator.getRandom();
    }

    @After
    public void cleanUp() {
        if (courierId != 0) {
            courierClient.delete(courierId);
        }
    }

    @Test
    @DisplayName("Создание нового курьера.")
    @Description("Создается новый курьер. Производится попытка войти с данными нового курьера. " +
            "Проверяется статус ответа, поле \"ok: true\" и успешность попытки войти, через проверку полученного id.")
    public void createNewCourier() {

        ValidatableResponse createResponse = courierClient.create(courier);

        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

        assertEquals("Статус ответа не соответствует требуемому.", 201, statusCode);
        assertTrue("Поле ответа \"ok\" имеет неверное значение.", isCourierCreated);
        assertTrue("Id курьера не должен быть равен 0.", courierId != 0);
    }

    @Test
    @DisplayName("Создание нового курьера с уже имеющимся именем.")
    @Description("Создается новый курьер с новыми данными. Логинимся с этими данными и получаем id. " +
            "Создается новый курьер с такими же данными. " +
            "Проверяется успешность создания нового курьера и получения id." +
            " Проверяется статус ответа, при создании курьера с такими же данными и поле \"message\".")
    public void createNewCourierWithTheSameName() {

        ValidatableResponse createResponse = courierClient.create(courier);

        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

        ValidatableResponse createResponseCourierWithSameName = courierClient.create(courier);

        int statusCodeCreateNewCourierWithTheSameName = createResponseCourierWithSameName.extract().statusCode();
        String messageCreateNewCourierWithTheSameName = createResponseCourierWithSameName.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", 201, statusCode);
        assertTrue("Поле ответа \"ok\" имеет неверное значение.", isCourierCreated);
        assertTrue("Id курьера не должен быть равен 0.", courierId != 0);

        assertEquals("Статус ответа не соответствует требуемому.", 409, statusCodeCreateNewCourierWithTheSameName);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Этот логин уже используется. Попробуйте другой.", messageCreateNewCourierWithTheSameName);
    }

    @Test
    @DisplayName("Создание нового курьера с пустым полем логина")
    public void createNewCourierWithoutLogin() {

        courier.setLogin(null);

        ValidatableResponse createResponseCreateNewCourierWithoutLogin = courierClient.create(courier);

        int statusCodeCreateNewCourierWithoutLogin = createResponseCreateNewCourierWithoutLogin.extract().statusCode();
        String messageCreateNewCourierWithoutLogin = createResponseCreateNewCourierWithoutLogin.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", 400, statusCodeCreateNewCourierWithoutLogin);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Недостаточно данных для создания учетной записи", messageCreateNewCourierWithoutLogin);
    }

    @Test
    @DisplayName("Создание нового курьера с пустым полем пароля")
    public void createNewCourierWithoutPassword() {

        courier.setPassword(null);

        ValidatableResponse createResponseCreateNewCourierWithoutLogin = courierClient.create(courier);

        int statusCodeCreateNewCourierWithoutLogin = createResponseCreateNewCourierWithoutLogin.extract().statusCode();
        String messageCreateNewCourierWithoutLogin = createResponseCreateNewCourierWithoutLogin.extract().path("message");

        assertEquals("Статус ответа не соответствует требуемому.", 400, statusCodeCreateNewCourierWithoutLogin);
        assertEquals("Поле ответа \"message\" имеет неверное значение.", "Недостаточно данных для создания учетной записи", messageCreateNewCourierWithoutLogin);
    }

    @Test
    @DisplayName("Создание нового курьера с пустым полем имени")
    public void createNewCourierWithoutName() {

        courier.setFirstname(null);

        ValidatableResponse createResponse = courierClient.create(courier);

        int statusCode = createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

        assertEquals("Статус ответа не соответствует требуемому.", 201, statusCode);
        assertTrue("Поле ответа \"ok\" имеет неверное значение.", isCourierCreated);
        assertTrue("Id курьера не должен быть равен 0.", courierId != 0);
    }

}
