package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Owner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class Ex6 {
    @Test
    @Owner(value = "Olga Voronkina")
    @Description("Тест, создает GET-запрос, с которого должен происходит редирект на другой адрес. Наша задача — распечатать адрес, на который редиректит указанные URL.")

    public void longRedirect(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .when()
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();

        String locationHeader = response.getHeader("location");
        System.out.println(locationHeader);
    }

}
