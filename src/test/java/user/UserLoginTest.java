package user;

import io.qameta.allure.Description;
import steps.AuthSteps;
import steps.ResponseChecker;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserApi;

import static org.apache.http.HttpStatus.*;

public class UserLoginTest {

    private final UserApi userApi = new UserApi();
    private final AuthSteps authSteps = new AuthSteps();
    private final ResponseChecker checker = new ResponseChecker();

    private User user;

    @Before
    public void setUp() {
        user = userApi.createRandomUser();
    }

    @After
    public void tearDown() {
        if (user != null) {
            try {
                String accessToken = authSteps.getAccessToken(user);
                userApi.deleteUserByAccessToken(accessToken).statusCode(SC_ACCEPTED);
            } catch (AssertionError ignored) {
            }
        }
    }

    @Test
    @Description("Успешный логин с корректными данными")
    public void loginWithValidCredentialsSuccess() {
        var response = userApi.login(user);
        checker.checkSuccessfulLoginResponse(response, user.getEmail(), user.getName());
    }

    @Test
    @Description("Логин с неправильным email")
    public void loginWithWrongEmailFails() {
        user.setEmail("wrong" + user.getEmail());
        var response = userApi.login(user);
        checker.checkErrorMessage(response, SC_UNAUTHORIZED, "email or password are incorrect");
    }

    @Test
    @Description("Логин с неправильным паролем")
    public void loginWithWrongPasswordFails() {
        user.setPassword("wrongPassword123");
        var response = userApi.login(user);
        checker.checkErrorMessage(response, SC_UNAUTHORIZED, "email or password are incorrect");
    }

    @Test
    @Description("Логин без email")
    public void loginWithoutEmailFails() {
        user.setEmail(null);
        var response = userApi.login(user);
        checker.checkErrorMessage(response, SC_UNAUTHORIZED, "email or password are incorrect");
    }

    @Test
    @Description("Логин без пароля")
    public void loginWithoutPasswordFails() {
        user.setPassword(null);
        var response = userApi.login(user);
        checker.checkErrorMessage(response, SC_UNAUTHORIZED, "email or password are incorrect");
    }
}
