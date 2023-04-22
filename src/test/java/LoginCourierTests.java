public class LoginCourierTests {

    //    @Test
//    @DisplayName("Авторизация, созданного курьера")
//    public void loginNewCourier() {
//
//        String json = "{\"login\": \"Andrey86\", \"password\": \"asdFGH_86\"}";
//
//        Response response = given()
//                .header("Content-type", "application/json")
//                .and()
//                .body(json)
//                .when()
//                .post("/api/v1/courier/login");
//        courierId = response.then().assertThat().statusCode(200).extract().path("id");
//        response.then().assertThat().statusCode(200).and().assertThat().body("id", notNullValue());
//    }
}
