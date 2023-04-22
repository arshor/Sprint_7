import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateCourierTests {

    private int courierId;
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        courier = new Courier("Andrey7286", "asdFGH_86", "Andrey");
    }

    @Test
    @DisplayName("Создание нового курьера")
    public void createNewCourier() {

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

    @After
    public void deleteCreatedCourier() {

        String json = "{\"login\": \"" + courier.getLogin() + "\", \"password\": \"" + courier.getPassword() + "\"}";

        Response response1 = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
        try {
            courierId = response1
                    .then()
                    .assertThat()
                    .statusCode(200)
                    .extract()
                    .path("id");
        } catch (AssertionError assertionError) {
            //System.out.println("Удалять нечего. Такой id отсутствует.");
        }

        if (courierId != 0) {
            given()
                    .when()
                    .delete("/api/v1/courier/" + courierId);
        }

    }
}
