package model;

import io.qameta.allure.Step;
import steps.IngredientsApi;

import java.util.List;

public class IngredientGenerator {

    private final IngredientsApi ingredientsApi = new IngredientsApi();

    @Step("Получение одного валидного ингредиента")
    public String getOneValidIngredient() {
        return ingredientsApi.getIngredients()
                .extract()
                .path("data[0]._id");
    }

    @Step("Получение нескольких валидных ингредиентов")
    public List<String> getSomeValidIngredients(int count) {
        List<String> all = ingredientsApi.getIngredients()
                .extract()
                .path("data._id");

        return all.subList(0, Math.min(count, all.size()));
    }

    @Step("Получение невалидного хеша ингредиента")
    public String getInvalidIngredientHash() {
        return "invalid_hash_" + System.currentTimeMillis();
    }
}