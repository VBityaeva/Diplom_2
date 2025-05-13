package user;

import io.qameta.allure.Description;
import steps.AuthSteps;
import steps.ResponseChecker;
import model.User;
import model.UserGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserApi;

import static org.apache.http.HttpStatus.*;

public class UserCreateTest {

    private final UserApi userApi = new UserApi();
    private final AuthSteps authSteps = new AuthSteps();
    private final ResponseChecker checker = new ResponseChecker();

    private User user;

    @Before
    public void setUp() {
        user = UserGenerator.random();
    }

    @After
    public void tearDown() {
        if (user != null) {
            try {
                String token = authSteps.getAccessToken(user);
                userApi.deleteUserByAccessToken(token)
                        .statusCode(SC_ACCEPTED);
            } catch (AssertionError ignored) {
            }
        }
    }

    @Test
    @Description("Создание уникального пользователя")
    public void createUniqueUserSuccess() {
        var response = userApi.create(user);
        checker.checkSuccessfulRegisterResponse(response, user.getEmail(), user.getName());
    }

    @Test
    @Description("Создание пользователя, который уже зарегистрирован")
    public void createExistingUserFails() {
        userApi.create(user).statusCode(SC_OK);
        var response = userApi.create(user);
        checker.checkErrorMessage(response, SC_FORBIDDEN, "User already exists");
    }

    @Test
    @Description("Создание пользователя без обязательного поля: email")
    public void createUserWithoutEmailFails() {
        user.setEmail(null);
        var response = userApi.create(user);
        checker.checkErrorMessage(response, SC_FORBIDDEN, "Email, password and name are required fields");
    }

    @Test
    @Description("Создание пользователя без обязательного поля: name")
    public void createUserWithoutNameFails() {
        user.setName(null);
        var response = userApi.create(user);
        checker.checkErrorMessage(response, SC_FORBIDDEN, "Email, password and name are required fields");
    }

    @Test
    @Description("Создание пользователя без обязательного поля: password")
    public void createUserWithoutPasswordFails() {
        user.setPassword(null);
        var response = userApi.create(user);
        checker.checkErrorMessage(response, SC_FORBIDDEN, "Email, password and name are required fields");
    }
}
