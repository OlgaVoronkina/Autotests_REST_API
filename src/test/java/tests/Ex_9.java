package tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;



public class Ex_9 {
    @Test
    public void ex9() throws IOException {

        Set uniquePasswords = this.getPasswords();
        for(Object uniquePassword : uniquePasswords){
        String answer = this.checkCookie((String) uniquePassword);
            if(!answer.equals("You are NOT authorized")){
                System.out.println("Верный пароль - "+uniquePassword);
                break;
            }
        }
    }

    public Set getPasswords() throws IOException {
        Path path = Paths.get("passwords");
        ArrayList<String> passwords = new ArrayList<>(Files.readAllLines(path));
        Set<String> uniquePass = new HashSet<>(passwords);
        return uniquePass;
    }

    public String checkCookie(String uniquePassword) {
        Map<String, String> cookie = getCookie(uniquePassword);
        Response responseForCheck = RestAssured
                .given()
                .cookies(cookie)
                .when()
                .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                .andReturn();

        String answer = responseForCheck.print();
        return answer;
    }

    public Map getCookie(String uniquePassword) {
        Map<String, String> body = new HashMap<>();
        body.put("login", "super_admin");
        body.put("password", uniquePassword);
        Response response = RestAssured
                .given()
                .body(body)
                .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                .andReturn();

        String responseCookies = response.getCookie("auth_cookie");
        Map<String, String> cookies = new HashMap<>();
        if (responseCookies!=null){
            cookies.put("auth_cookie", responseCookies);
        }
        return cookies;
    }
}
