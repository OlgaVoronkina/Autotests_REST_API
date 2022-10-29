package tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Ex17 extends BaseTestCase {
    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

    //1.Попытаемся изменить данные пользователя, будучи неавторизованными
    @Test
    public void editUserWithoutLogin1(){
        //создаем нового пользователя и получаем его id
        Map<String, String> userData = DataGenerator.getRegistrationData();
        String userId = createUserAndGetId(userData);

        //пытаемся изменить его данные
        String newName = "New Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/", editData, userId);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");
    }

    //2.Попытаемся изменить данные пользователя, будучи авторизованными другим пользователем
    @Test
    public void editUserAnotherUser2(){
        //создаем нового пользователя и получаем его id
        Map<String, String> userData = DataGenerator.getRegistrationData();
        String userId = createUserAndGetId(userData);

        //логинимся под другим пользователем
        Response userLogin = login("vinkotov@example.com", "1234");

        //пытаемся изменить данные созданного пользователя
        String newName = "New Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/",
                this.getHeader(userLogin, "x-csrf-token"),
                this.getCookie(userLogin, "auth_sid"),
                editData,
                userId);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Please, do not edit test users with ID 1, 2, 3, 4 or 5.");
    }

    //3.Попытаемся изменить email пользователя, будучи авторизованными тем же пользователем, на новый email без символа @
    @Test
    public void editUserWrongEmail3(){
        //создаем нового пользователя и получаем его id
        Map<String, String> userData = DataGenerator.getRegistrationData();
        String userId = createUserAndGetId(userData);

        //логинимся под созданным пользователем
        Response userLogin = login(userData.get("email"), userData.get("password"));

        //пытаемся изменить email пользователя на некорректный
        String wrongEmail = DataGenerator.getRandomWrongEmail();
        Map<String,String> editData = new HashMap<>();
        editData.put("email", wrongEmail);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/",
                this.getHeader(userLogin, "x-csrf-token"),
                this.getCookie(userLogin, "auth_sid"),
                editData,
                userId);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Invalid email format");
    }

    //Попытаемся изменить firstName пользователя, будучи авторизованными тем же пользователем, на очень короткое значение в один символ
    @Test
    public void editUserShortFirstName4(){
        //создаем нового пользователя и получаем его id
        Map<String, String> userData = DataGenerator.getRegistrationData();
        String userId = createUserAndGetId(userData);

        //логинимся под созданным пользователем
        Response userLogin = login(userData.get("email"), userData.get("password"));

        //пытаемся изменить firstName пользователя
        String shortFirstName = DataGenerator.getRandomString(1);
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", shortFirstName);

        JsonPath responseEditUser = apiCoreRequests.makePutRequestJson(
                "https://playground.learnqa.ru/api/user/",
                this.getHeader(userLogin, "x-csrf-token"),
                this.getCookie(userLogin, "auth_sid"),
                editData,
                userId);

        Assertions.assertResponseValueEquals(responseEditUser, "Too short value for field firstName", "error");
    }

    public  String createUserAndGetId(Map <String, String>  userData){
        JsonPath responseCreateAuth = apiCoreRequests
                .makePOSTRequestJSON("https://playground.learnqa.ru/api/user", userData);

        String id = responseCreateAuth.get("id");
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
