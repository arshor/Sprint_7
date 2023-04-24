import io.restassured.builder.RequestSpecBuilder;

public class RestClient {

    public RequestSpecBuilder getBaseSpec() {
        return new RequestSpecBuilder()
                .addParam("baseURI", "http://qa-scooter.praktikum-services.ru")
                .addParam("Content-Type", "application/json");
    }
}
