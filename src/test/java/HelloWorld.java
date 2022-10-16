import  io.restassured.RestAssured;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import io.restassured.path.json.JsonPath;
import java.util.HashMap;
import java.util.Map;

public class HelloWorld {
    @Test
    public void testHelloWorld(){
        System.out.println("Hello world");
    }

    @Test
    public void RestAssured(){
        Map<String, String> params = new HashMap<>();
        params.put("name", "Olga");
        Response response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();
        response.prettyPrint();
    }

    @Test
    public void jsonPath(){
        Map<String, String> params = new HashMap<>();
        params.put("name", "Olga");
        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String answer = response.get("answer");
        System.out.println(answer);
    }

    @Test
    public void jsonPath2(){
        Map<String, String> params = new HashMap<>();
        params.put("name", "Olga");
        JsonPath response = RestAssured
                .given()
                .queryParams(params)
                .get("https://playground.learnqa.ru/api/hello")
                .jsonPath();
        String name = response.get("answer");
                if(name==null){
            System.out.println("The key 'answer2' is absent ");
        }
                else {
                    System.out.println(name);
                }
    }

//Проверка типа запроса и параметров - GET
    @Test
    public void RestAssured2(){

        Response response = RestAssured
                .given()
                .queryParam("param1", "value1")
                .queryParam("param2", "value2")
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();

    }

    //Проверка типа запроса и параметров - POST
    @Test
    public void RestAssured3(){

        Response response = RestAssured
                .given()
                .body("param1=value1&param2=value2")
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();
    }

    //Проверка типа запроса и параметров - POST - передать параметры json-ом
    @Test
    public void RestAssured4(){

        Response response = RestAssured
                .given()
                .body("{\"param1\":\"value1\",\"param2\":\"value2\"}")
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();
    }

    //Проверка типа запроса и параметров - POST - передать параметры json-ом + Map
    @Test
    public void RestAssured5(){
        Map<String, String> body = new HashMap<>();
        body.put("param1", "value1");
        body.put("param2", "value2");
        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        response.print();
    }

    // Проверка кода ответа
    @Test
    public void RestAssured6(){
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
        response.print();
    }

    @Test
    public void RestAssured7(){
        Response response = RestAssured
                .given()
                .redirects()
                .follow(true)
                .when()
                .get("https://playground.learnqa.ru/api/check_type")
                .andReturn();
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
        response.print();
    }

    //заголовки
    @Test
    public void RestAssured8(){
        Map<String, String> headers = new HashMap<>();
        headers.put("header1", "value1");
        headers.put("header2", "value2");
        Response response = RestAssured
                .given()
                .headers(headers)
                .when()
                .get("https://playground.learnqa.ru/api/show_all_headers")
                .andReturn();

        response.prettyPrint();

        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);
    }

    //заголовки
    @Test
    public void RestAssured9(){
        Map<String, String> headers = new HashMap<>();
        headers.put("header1", "value1");
        headers.put("header2", "value2");
        Response response = RestAssured
                .given()
                .redirects()
                .follow(false)
                .headers(headers)
                .when()
                .get("https://playground.learnqa.ru/api/get_303")
                .andReturn();

        response.prettyPrint();

        String locationHeader = response.getHeader("location");
        System.out.println(locationHeader);
    }

    //Cookie
    //получение cookies

    @Test
    public void RestAssured10(){
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");
        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        System.out.println("\nPretty text:");
        response.prettyPrint();

        System.out.println("\nHeaders:");
        Headers responseHeaders = response.getHeaders();
        System.out.println(responseHeaders);

        System.out.println("\nCookies:");
        Map<String,String> responseCookies = response.getCookies();
        System.out.println(responseCookies);
    }

    @Test
    public void RestAssured11(){
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");
        Response response = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = response.getCookie("auth_cookie");
        System.out.println(responseCookie);
    }

    //передача cookies

    @Test
    public void RestAssured12(){
        Map<String, String> data = new HashMap<>();
        data.put("login", "secret_login");
        data.put("password", "secret_pass");
        Response responseForGet = RestAssured
                .given()
                .body(data)
                .when()
                .post("https://playground.learnqa.ru/api/get_auth_cookie")
                .andReturn();

        String responseCookie = responseForGet.getCookie("auth_cookie");


        Map<String, String> cookies = new HashMap<>();
        if (responseCookie!=null){
            cookies.put("auth_cookie", responseCookie);
        }

        Response responseForCheck = RestAssured
                .given()
                .log().all()
                .body(data)
                .cookies(cookies)
                .when()
                .post("https://playground.learnqa.ru/api/check_auth_cookie")
                .andReturn();

            responseForCheck.print();
    }
}
