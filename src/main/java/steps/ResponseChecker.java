package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.*;

public class ResponseChecker {

    @Step("Проверка успешной регистрации/авторизации")
    public void checkSuccessfulAuthResponse(ValidatableResponse response, String expectedEmail, String expectedName) {
        response
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("user.email", equalTo(expectedEmail))
                .body("user.name", equalTo(expectedName));
    }

    @Step("Проверка сообщения об ошибке")
    public void checkErrorMessage(ValidatableResponse response, int statusCode, String expectedMessage) {
        response
                .statusCode(statusCode)
                .body("success", equalTo(false))
                .body("message", equalTo(expectedMessage));
    }

    @Step("Проверка успешной регистрации")
    public void checkSuccessfulRegisterResponse(ValidatableResponse response, String expectedEmail, String expectedName) {
        response
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user.email", equalTo(expectedEmail))
                .body("user.name", equalTo(expectedName));
    }

    @Step("Проверка успешного ответа при логине")
    public void checkSuccessfulLoginResponse(ValidatableResponse response, String email, String name) {
        response
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Step("Проверка успешного обновления данных пользователя")
    public void checkSuccessfulUpdateResponse(ValidatableResponse response, String expectedEmail, String expectedName) {
        response
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(expectedEmail))
                .body("user.name", equalTo(expectedName));
    }

    @Step("Проверка успешного создания заказа")
    public void checkOrderCreated(ValidatableResponse response) {
        response
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Step("Проверка получения заказов пользователя")
    public void checkUserOrdersList(ValidatableResponse response) {
        response
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("orders", not(empty()));
    }

}
