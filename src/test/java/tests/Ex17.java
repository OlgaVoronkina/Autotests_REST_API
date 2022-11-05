package tests;

import io.qameta.allure.*;
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

    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=294733")
    @Owner(value = "Olga Voronkina")
    @Test
    @Description("Попытаемся изменить данные пользователя, будучи неавторизованными")
    @Severity(SeverityLevel.CRITICAL)
    public void editUserWithoutLogin(){
        Map<String, String> userData = DataGenerator.getRegistrationData();
        int userId = createUserAndGetId(userData);

        String newName = "New Name";
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", newName);

        Response responseEditUser = apiCoreRequests.makePutRequest("https://playground.learnqa.ru/api/user/", editData, userId);

        Assertions.assertResponseCodeEquals(responseEditUser, 400);
        Assertions.assertResponseTextEquals(responseEditUser, "Auth token not supplied");
    }

    @Owner(value = "Olga Voronkina")
    @Test
    @Description("Попытаемся изменить данные пользователя, будучи авторизованными другим пользователем")
    @Severity(SeverityLevel.CRITICAL)
    public void editUserAnotherUser(){
        Map<String, String> userData = DataGenerator.getRegistrationData();
        int userId = createUserAndGetId(userData);

        Response userLogin = login("vinkotov@example.com", "1234");

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

    @Owner(value = "Olga Voronkina")
    @Test
    @Description("Попытаемся изменить email пользователя, будучи авторизованными тем же пользователем, на новый email без символа @")
    @Severity(SeverityLevel.CRITICAL)
    public void editUserWrongEmail(){
        Map<String, String> userData = DataGenerator.getRegistrationData();
        int userId = createUserAndGetId(userData);

        Response userLogin = login(userData.get("email"), userData.get("password"));

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

    @Owner(value = "Olga Voronkina")
    @Test
    @Description("Попытаемся изменить firstName пользователя, будучи авторизованными тем же пользователем, на очень короткое значение в один символ")
    @Severity(SeverityLevel.CRITICAL)
    public void editUserShortFirstName(){
        Map<String, String> userData = DataGenerator.getRegistrationData();
        int userId = createUserAndGetId(userData);

        Response userLogin = login(userData.get("email"), userData.get("password"));

        String shortFirstName = DataGenerator.getRandomString(1);
        Map<String,String> editData = new HashMap<>();
        editData.put("firstName", shortFirstName);

        Response responseEditUser = apiCoreRequests.makePutRequest(
                "https://playground.learnqa.ru/api/user/",
                this.getHeader(userLogin, "x-csrf-token"),
                this.getCookie(userLogin, "auth_sid"),
                editData,
                userId);

        String answer = getStringFromJson(responseEditUser,"error");

        Assertions.assertTextEquals(answer, "Too short value for field firstName");
    }

}
