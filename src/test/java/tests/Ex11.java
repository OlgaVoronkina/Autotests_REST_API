package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex11 {
    @Test
    public void getCookie(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();

        Map<String,String> responseCookies = response.getCookies();
        System.out.println(responseCookies);

        assertEquals(response.getCookie("HomeWork"), "hw_value", "Cookie isn't correct");

    }

}
