package order;

import io.qameta.allure.Description;
import model.IngredientGenerator;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.AuthSteps;
import steps.OrderApi;
import steps.UserApi;

import java.util.List;

import static org.apache.http.HttpStatus.*;

public class GetUserOrdersTest {

    private final UserApi userApi = new UserApi();
    private final OrderApi orderApi = new OrderApi();
    private final IngredientGenerator ingredientGenerator = new IngredientGenerator();
    private final AuthSteps authSteps = new AuthSteps();
    private User user;
    private String accessToken;

    @Before
    public void setUp() {
        user = userApi.createRandomUser();
        accessToken = authSteps.getAccessToken(user);
        List<String> ingredients = ingredientGenerator.getSomeValidIngredients(2);
        orderApi.createOrder(accessToken, ingredients)
                .statusCode(SC_OK);
    }

    @After
    public void tearDown() {
        try {
            userApi.deleteUserByAccessToken(accessToken).statusCode(SC_ACCEPTED);
        } catch (AssertionError ignored) {
        }
    }

    @Test
    @Description("Получение заказов авторизованного пользователя")
    public void getOrdersWithAuth() {
        orderApi.getUserOrders(accessToken)
                .statusCode(SC_OK)
                .body("orders.size()", org.hamcrest.Matchers.greaterThan(0));
    }

    @Test
    @Description("Получение заказов неавторизованного пользователя")
    public void getOrdersWithoutAuth() {
        orderApi.getUserOrders(null)
                .statusCode(SC_UNAUTHORIZED)
                .body("message", org.hamcrest.Matchers.equalTo("You should be authorised"));
    }
}
