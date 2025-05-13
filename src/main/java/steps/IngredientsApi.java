package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class IngredientsApi extends BaseApi {

    @Step("Получение списка ингредиентов с повторными попытками")
    public ValidatableResponse getIngredients() {
        return RetryUtils.withRetry(() -> {
            return given()
                    .spec(getBaseSpec())
                    .when()
                    .get("/api/ingredients")
                    .then();
        });
    }
}
