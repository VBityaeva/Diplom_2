package model;

import org.apache.commons.lang3.RandomStringUtils;

public class UserGenerator {
    public static User random() {
        String email = "user" + System.currentTimeMillis() + "@example.com";
        String name = RandomStringUtils.randomAlphanumeric(8); // Генерирует случайное имя из 8 символов
        String password = RandomStringUtils.randomAlphanumeric(10); // Генерирует случайный пароль из 10 символов
        return new User(email, name, password);
    }
}