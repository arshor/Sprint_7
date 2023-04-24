import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class CreateCourierTests {

    private int courierId;
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
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
    @DisplayName("Создание нового курьера")
    public void createNewCourier() {


        ValidatableResponse createResponse = courierClient.create(courier);

        int statusCode =createResponse.extract().statusCode();
        boolean isCourierCreated = createResponse.extract().path("ok");

        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

        assertEquals(201, statusCode);
        assertTrue(isCourierCreated);
        assertTrue(courierId != 0);
    }

    @Test
    @DisplayName("Создание нового курьера с уже имеющимся именем")
    public void createNewCourierWithTheSameName() {

        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        response
                .then()
                .assertThat()
                .statusCode(409)
                .and()
                .assertThat()
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Создание нового курьера с пустым полем логина")
    public void createNewCourierWithoutLogin() {

        courier.setLogin(null);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        response
                .then()
                .assertThat()
                .statusCode(400).and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание нового курьера с пустым полем пароля")
    public void createNewCourierWithoutPassword() {

        courier.setPassword(null);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        response
                .then()
                .assertThat()
                .statusCode(400)
                .and()
                .assertThat()
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Создание нового курьера с пустым полем имени")
    public void createNewCourierWithoutName() {

        courier.setFirstname(null);

        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");

        response
                .then()
                .assertThat()
                .statusCode(201)
                .and()
                .assertThat()
                .body("ok", equalTo(true));
    }

}
