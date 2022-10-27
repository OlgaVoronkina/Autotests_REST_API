package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.BaseTestCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import lib.Assertions;

import java.util.HashMap;
import java.util.Map;

import io.qameta.allure.Description;
import  io.qameta.allure.Epic;
import  io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import lib.ApiCoreRequests;

@Epic("Authorisation cases")
@Feature("Authorisation")
public class UserAuthTest extends BaseTestCase {

    String cookie;
    String header;
    int userIdOnAuth;

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    @BeforeEach
    public void loginUser() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");
        Response responseGetAuth = apiCoreRequests
                .makePOSTRequest("https://playground.learnqa.ru/api/user/login", authData);


        this.cookie=this.getCookie(responseGetAuth, "auth_sid");
        this.header=this.getHeader(responseGetAuth, "x-csrf-token");
        this.userIdOnAuth=this.getIntFromJson(responseGetAuth,"user_id");
    }

    @Test
    @Description("This test successfully authorise user by email and password")
    @DisplayName("Test positive auth user")
    public void testAuthUser() {
        Response responseCheckAuth = apiCoreRequests
                .makeGetRequest(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.header,
                    this.cookie
                );
//                .given()
//                .header("x-csrf-token", this.header)
//                .cookie("auth_sid", this.cookie)
//                .get("https://playground.learnqa.ru/api/user/auth")
//                .andReturn();

        Assertions.assertJsonByName(responseCheckAuth, "user_id", this.userIdOnAuth);
    }

    //Негативные тесты на авторизацию
    @Description("This test check authorization status w/o sending auth cookie or token")
    @DisplayName("Test negative auth user")
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"}) // сюда пеердаем строки
    public void testNegativeAuthUser(String condition) { //в кондишен будут присваиваться значение из параметров
//специальный класс объектом которого будет переменная спек, с помощью этого механизма мы можем выполнять нужный нам запрос объявляя его по частям
        RequestSpecification spec = RestAssured.given(); //объявили переменную спек
        spec.baseUri("https://playground.learnqa.ru/api/user/auth"); //в спек передали baseUrl - переменная будет использоваться всегда, поэтому она расположена здесь

        if(condition.equals("cookie")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithCookie(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.cookie);
            Assertions.assertJsonByName(responseForCheck,"user_id", 0);
        } else if (condition.equals("headers")){
            Response responseForCheck = apiCoreRequests.makeGetRequestWithToken(
                    "https://playground.learnqa.ru/api/user/auth",
                    this.header);
            Assertions.assertJsonByName(responseForCheck,"user_id", 0);
        }else {
            throw new  IllegalArgumentException("Condition value is known" + condition);
        }



//        if (condition.equals("cookie")){   //здесь будет добавляться параметр переданный в зависимости от вход. параметра
//            spec.cookie("auth_sid", this.cookie);
//        } else if (condition.equals("headers")){
//            spec.header("x-csrf-token", this.header);
//        }
//        else {
//            throw new  IllegalArgumentException("Condition value is known" + condition);
//        }

//        Response responseForCheck = spec.get().andReturn();
//        Assertions.assertJsonByName(responseForCheck, "user_id", 0);
    }
}
