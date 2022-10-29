package lib;

import io.restassured.response.Response;
import io.restassured.http.Headers;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {
    protected String getHeader(Response Response, String name){
        Headers headers = Response.getHeaders();

        assertTrue(headers.hasHeaderWithName(name), "Response doesn't have header with name "+name);
        return headers.getValue(name);
    }

    protected String getCookie(Response Response, String name){
        Map<String, String> cookies = Response.getCookies();

        assertTrue(cookies.containsKey(name), "Response doesn't have cookie with name "+name);
        return cookies.get(name);
    }

    protected int getIntFromJson(Response Response, String name){
        Response.then().assertThat().body("$",hasKey(name)); // $ - означант что ищем ключ в корне json
        return Response.jsonPath().getInt(name);
    }

    protected String getStringFromJson(Response Response, String name){
        Response.then().assertThat().body("$",hasKey(name)); // $ - означант что ищем ключ в корне json
        return Response.jsonPath().getString(name);
    }

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    public int createUserAndGetId(Map<String, String> userData){
        Response responseCreateAuth = apiCoreRequests
                .makePOSTRequest("https://playground.learnqa.ru/api/user", userData);
        int id = getIntFromJson(responseCreateAuth,"id");
        return id;
    }

    public Response login(String email, String password) {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", email);
        authData.put("password", password);
        Response responseGetAuth = apiCoreRequests.makePOSTRequest("https://playground.learnqa.ru/api/user/login", authData);
        return responseGetAuth;
    }
}
