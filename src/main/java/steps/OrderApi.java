package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderApi extends BaseApi {

    @Step("Создание заказа")
    public ValidatableResponse createOrder(String token, List<String> ingredients) {
        Map<String, Object> body = Map.of("ingredients", ingredients);

        RequestSpecification spec = getBaseSpec();
        if (token != null) spec.header("Authorization", token);

        return RetryUtils.withRetry(() -> {
            return given()
                    .spec(spec)
                    .body(body)
                    .when()
                    .post("/api/orders")
                    .then();
        });
    }

    @Step("Получение заказов пользователя")
    public ValidatableResponse getUserOrders(String token) {
        RequestSpecification spec = getBaseSpec();

        if (token != null && !token.isEmpty()) {
            spec.header("Authorization", token);
        }

        return given()
                .spec(spec)
                .when()
                .get("/api/orders")
                .then();
    }
}
