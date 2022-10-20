package tests;

import io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestJUnitDraft {
//    @Test
//    public void RestAssured13() {
//        Response response = RestAssured
//                .get("https://playground.learnqa.ru/api/map")
//                .andReturn();
////        assertTrue(response.statusCode()==200, "Unexpected status code");
//        assertEquals(200, response.statusCode(),"Unexpected status code");
//
//    }
//
//    @Test
//    public void RestAssured14() {
//        Response response = RestAssured
//                .get("https://playground.learnqa.ru/api/map")
//                .andReturn();
////        assertTrue(response.statusCode()==200, "Unexpected status code");
//        assertEquals(200, response.statusCode(),"Unexpected status code");
//    }

    //покрыть тестами метод helloWorld
    @Test
    public void testHelloMethodWithoutName() {
        JsonPath response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        assertEquals("Hello, someone", answer, "The answer is not expected");
    }
    @Test
    public void testHelloMethodWithoutName2() {
        String name = "UserName";
        JsonPath response = RestAssured
                .given()
                .queryParam("name", name)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        assertEquals("Hello, "+name, answer, "The answer is not expected");
    }

    //Параметризированный тест
    @ParameterizedTest
    @ValueSource(strings = {"", "John", "Pete"}) //сколько параметров, столько раз запуститься тест
    public void testHelloMethodWithoutName3(String name) {    //String name - для хранения параметра с именем
        Map<String, String> queryParams = new HashMap<>(); // пустой HashMap в котором будут храниться параметры
        if(name.length() > 0){      //если длина имени больше 0
            queryParams.put("name", name);  //добавляем имя в качестве параметра
        }
        JsonPath response = RestAssured
                .given()
                .queryParams(queryParams)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.getString("answer");
        String expectedName = (name.length()>0) ? name : "someone"; //если длина больше 0, берется name, если нет - берется someone
        assertEquals("Hello, " + expectedName, answer, "The answer is not expected");
    }

    //Позитивный тест на авторизацию
    @Test //оптимизирован в отдельном классе tests.UserAuthTest
    public void testAuthUser1() {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        Map<String, String> cookies = responseGetAuth.getCookies();
        Headers headers = responseGetAuth.getHeaders();
        int userIdOnAuth = responseGetAuth.jsonPath().getInt("user_id");

        assertEquals(200, responseGetAuth.statusCode(), "Unexpected status Code");
        assertTrue(cookies.containsKey("auth_sid"),"Response doesn't have auth_sid cookie");
        assertTrue(headers.hasHeaderWithName("x-csrf-token"), "Response doesn't have 'x-csrf-token' header");
        assertTrue(responseGetAuth.jsonPath().getInt("user_id")>0, "User id should be greater than 0");

        JsonPath responseCheckAuth = RestAssured
                .given()
                .header("x-csrf-token", responseGetAuth.getHeader("x-csrf-token"))
                .cookie("auth_sid", responseGetAuth.getCookie("auth_sid"))
                .get("https://playground.learnqa.ru/api/user/auth")
                .jsonPath();

        int userIdOnCheck = responseCheckAuth.getInt("user_id");
        assertTrue(userIdOnAuth>0,"Unexpected user id " +userIdOnAuth);

        assertEquals(
                userIdOnAuth,
                userIdOnCheck,
                "user id from auth request is not equal to user_id from check request"
                );
    }

    //Негативные тесты на авторизацию
    @ParameterizedTest
    @ValueSource(strings = {"cookie", "headers"}) // сюда пеердаем строки
    public void testNegativeAuthUser1(String condition) { //в кондишен будут присваиваться значение из параметров
        Map<String, String> authData = new HashMap<>();
        authData.put("email", "vinkotov@example.com");
        authData.put("password", "1234");

        Response responseGetAuth = RestAssured
                .given()
                .body(authData)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();
// раскладываем переменную на куки и хедерс
        Map<String, String> cookies = responseGetAuth.getCookies();
        Headers headers = responseGetAuth.getHeaders();
//специальный класс объектом которого будет переменная спек, с помощью этого механизма мы можем выполнять нужный нам запрос объявляя его по частям
        RequestSpecification spec = RestAssured.given(); //объявили переменную спек
        spec.baseUri("https://playground.learnqa.ru/api/user/auth"); //в спек передали baseUrl - переменная будет использоваться всегда, поэтому она расположена здесь

        if (condition.equals("cookie")){   //здесь будет добавляться параметр переданный в зависимости от вход. параметра
            spec.cookie("auth_sid", cookies.get("auth_sid"));
        } else if (condition.equals("headers")){
            spec.header("x-csrf-token", headers.get("x-csrf-token"));
        }
        else {
            throw new  IllegalArgumentException("Condition value is known" + condition);
        }

        JsonPath responseForCheck = spec.get().jsonPath();
        assertEquals(0, responseForCheck.getInt("user_id"), "user_id should be 0 for unauth request");
    }
}
