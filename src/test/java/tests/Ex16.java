package tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Ex16 extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    //В этой задаче нужно написать тест, который авторизовывается одним пользователем, но получает данные другого (т.е. с другим ID).
    //И убедиться, что в этом случае запрос также получает только username, так как мы не должны видеть остальные данные чужого пользователя.

    @Test
    public  void testGetUserDetailsAuthAsSameUser1() {
        String id = createUserAndGetId();                       //создаем юзера, получаем его id
        System.out.println("id созданного пользователя: "+id);

        Map<String, String> authData = new HashMap<>();         //логинимся под другим пользователем
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = apiCoreRequests.makePOSTRequest("https://playground.learnqa.ru/api/user/login", authData);

        System.out.println("id залогиненного пользователя = " + responseGetAuth.asString());  //информативно. получаем id пользователя, под которым залогинились

        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/", id);  //получаем данные созданного пользователя по id

        //проверяем что получаем только username
        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");

    }

    public  String createUserAndGetId(){
        Map<String, String> userData = DataGenerator.getRegistrationData();
        JsonPath responseCreateAuth = apiCoreRequests
                .makePOSTRequestJSON("https://playground.learnqa.ru/api/user", userData);

        String id = responseCreateAuth.get("id");
        return id;
    }
}
