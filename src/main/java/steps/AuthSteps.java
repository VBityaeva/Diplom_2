package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.LoginUser;
import model.User;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class AuthSteps extends BaseApi {

    @Step("Получение accessToken по логину пользователя")
    public String getAccessToken(User user) {
        LoginUser loginUser = new LoginUser(user.getEmail(), user.getPassword());

        // Используем retry для логина
        ValidatableResponse response = RetryUtils.withRetry(() -> {
            return given()
                    .spec(getBaseSpec())
                    .body(loginUser)
                    .when()
                    .post("/api/auth/login")
                    .then()
                    .statusCode(SC_OK);
        });

        return response.extract().path("accessToken");
    }
}
