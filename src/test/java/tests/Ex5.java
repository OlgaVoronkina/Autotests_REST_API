package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex5 {
    @Epic(value = "Парсинг JSON")
    @Test
    @Owner(value = "Olga Voronkina")
    @Description("Выводит ответ")
    public void getJsonHomework(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .andReturn();
        response.print();
    }

    @Test
    @Owner(value = "Olga Voronkina")
    @Description("Выводит текст второго сообщения")
    public void getMessage(){
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/get_json_homework")
                .jsonPath();
        String message = response.get("messages.message[1]");
        System.out.println(message);
    }
}
