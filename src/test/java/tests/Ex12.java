package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex12 {
    @Test
    public void getHeader(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();

        // здесь понимаем что за header и с каким значением
        Headers headers = response.getHeaders();
        System.out.println(headers);

        assertEquals(response.getHeader("x-secret-homework-header"), "Some secret value", "Header isn't correct");
    }
}
