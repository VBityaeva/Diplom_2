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

public class CreateOrderTest {

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
    }

    @After
    public void tearDown() {
        try {
            userApi.deleteUserByAccessToken(accessToken).statusCode(SC_ACCEPTED);
        } catch (AssertionError ignored) {
        }
    }

    @Test
    @Description("Создание заказа с авторизацией и валидными ингредиентами")
    public void createOrderWithAuthAndValidIngredients() {
        List<String> ingredients = ingredientGenerator.getSomeValidIngredients(2);
        orderApi.createOrder(accessToken, ingredients)
                .statusCode(SC_OK)
                .body("success", org.hamcrest.Matchers.equalTo(true));
    }

    @Test
    @Description("Создание заказа без авторизации и с валидными ингредиентами")
    public void createOrderWithoutAuthWithValidIngredients() {
        List<String> ingredients = ingredientGenerator.getSomeValidIngredients(2);
        orderApi.createOrder(null, ingredients)
                .statusCode(SC_OK)
                .body("success", org.hamcrest.Matchers.equalTo(true));
    }

    @Test
    @Description("Создание заказа с авторизацией без ингредиентов")
    public void createOrderWithAuthWithoutIngredients() {
        orderApi.createOrder(accessToken, List.of())
                .statusCode(SC_BAD_REQUEST)
                .body("message", org.hamcrest.Matchers.equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("Создание заказа без авторизации и без ингредиентов")
    public void createOrderWithoutAuthWithoutIngredients() {
        orderApi.createOrder(null, List.of())
                .statusCode(SC_BAD_REQUEST)
                .body("message", org.hamcrest.Matchers.equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Description("Создание заказа с невалидным хешем ингредиента")
    public void createOrderWithInvalidIngredientHash() {
        List<String> ingredients = List.of(ingredientGenerator.getInvalidIngredientHash());
        orderApi.createOrder(accessToken, ingredients)
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}