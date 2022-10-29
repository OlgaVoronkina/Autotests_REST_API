package lib;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ApiCoreRequests {
    @Step("Make a GET - request with token and auth cookie")
    public Response makeGetRequest (String url, String token, String cookie){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET - request with id")
    public Response makeGetRequest (String url, int id){
        return given()
                .filter(new AllureRestAssured())
                .get(url+id)
                .andReturn();
    }

    @Step("Make a GET - request with auth cookie only")
    public Response makeGetRequestWithCookie(String url, String cookie){
        return given()
                .filter(new AllureRestAssured())
                .cookie("auth_sid", cookie)
                .get(url)
                .andReturn();
    }

    @Step("Make a GET - request with auth token only")
    public Response makeGetRequestWithToken (String url, String token){
        return given()
                .filter(new AllureRestAssured())
                .header(new Header("x-csrf-token", token))
                .get(url)
                .andReturn();
    }

    @Step("Make a POST - request")
    public Response makePOSTRequest (String url, Map<String,String> authData){
        return given()
                .filter(new AllureRestAssured())
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Make a PUT - request")
    public Response makePutRequest (String url, Map<String,String> editData, int id){
        return given()
                .body(editData)
                .put(url+id)
                .andReturn();
    }

    @Step("Make a PUT - request with header and cookie")
    public Response makePutRequest (String url, String header, String cookie, Map<String,String> editData, int id){
        return given()
                .header(new Header("x-csrf-token", header))
                .cookie("auth_sid", cookie)
                .body(editData)
                .put(url+id)
                .andReturn();
    }

    @Step("Make a DELETE - request with header and cookie")
    public Response makeDeleteRequest (String url, String header, String cookie,  int id){
        return given()
                .header(new Header("x-csrf-token", header))
                .cookie("auth_sid", cookie)
                .delete(url+id)
                .andReturn();
    }

}
