package user;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import steps.AuthSteps;
import steps.ResponseChecker;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserApi;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class UserUpdateTest {

    private final UserApi userApi = new UserApi();
    private final AuthSteps authSteps = new AuthSteps();
    private final ResponseChecker checker = new ResponseChecker();

    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        user = userApi.createRandomUser();
        accessToken = authSteps.getAccessToken(user);
    }

    @After
    public void tearDown() {
        try {
            userApi.deleteUserByAccessToken(accessToken)
                    .statusCode(SC_ACCEPTED);
        } catch (AssertionError ignored) {
        }
    }

    @Test
    @Description("Успешное изменение email")
    public void updateEmailSuccess() {
        String newEmail = "updated" + System.currentTimeMillis() + "@example.com";
        user.setEmail(newEmail);

        ValidatableResponse response = userApi.update(user, accessToken);

        checker.checkSuccessfulUpdateResponse(response, newEmail, user.getName());
    }

    @Test
    @Description("Успешное изменение имени")
    public void updateNameSuccess() {
        String newName = "NewName" + System.currentTimeMillis();
        user.setName(newName);

        ValidatableResponse response = userApi.update(user, accessToken);

        checker.checkSuccessfulUpdateResponse(response, user.getEmail(), newName);
    }

    @Test
    @Description("Попытка изменения данных без авторизации")
    public void updateWithoutAuthFails() {
        user.setName("NoAuthNameChange");

        ValidatableResponse response = userApi.updateWithoutAuth(user);

        checker.checkErrorMessage(response, SC_UNAUTHORIZED, "You should be authorised");
    }
}
