package steps;

import model.UserGenerator;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import model.User;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;

public class UserApi extends BaseApi {

    @Step("Создание пользователя с повторными попытками")
    public ValidatableResponse create(User user) {
        return RetryUtils.withRetry(() ->
                given()
                        .spec(getBaseSpec())
                        .body(user)
                        .when()
                        .post("/api/auth/register")
                        .then()
        );
    }

    @Step("Логин пользователя с повторными попытками")
    public ValidatableResponse login(User user) {
        return RetryUtils.withRetry(() ->
                given()
                        .spec(getBaseSpec())
                        .body(user)
                        .when()
                        .post("/api/auth/login")
                        .then()
        );
    }

    @Step("Создание случайного пользователя с проверкой успешности")
    public User createRandomUser() {
        User user = UserGenerator.random();
        create(user).statusCode(SC_OK);  // Проверка на успешность создания
        return user;
    }

    @Step("Удаление пользователя по accessToken")
    public ValidatableResponse deleteUserByAccessToken(String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .when()
                .delete("/api/auth/user")
                .then();
    }

    @Step("Обновление данных пользователя с авторизацией")
    public ValidatableResponse update(User user, String accessToken) {
        return given()
                .spec(getBaseSpec())
                .header("Authorization", accessToken)
                .body(user)
                .when()
                .patch("/api/auth/user")
                .then();
    }

    @Step("Попытка обновления данных пользователя без авторизации")
    public ValidatableResponse updateWithoutAuth(User user) {
        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .patch("/api/auth/user")
                .then();
    }
}
