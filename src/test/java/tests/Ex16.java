package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;


public class Ex16 extends BaseTestCase {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    @Link("https://software-testing.ru/lms/mod/assign/view.php?id=294732")
    @Owner(value = "Olga Voronkina")
    @Test
    @Description("В этой задаче нужно написать тест, который авторизовывается одним пользователем, но получает данные другого (т.е. с другим ID).И убедиться, что в этом случае запрос также получает только username, так как мы не должны видеть остальные данные чужого пользователя.")
    @Severity(SeverityLevel.CRITICAL)
    public  void testGetUserDetailsAuthAsSameUser1() {
        Map<String, String> userData = DataGenerator.getRegistrationData();
        int id = createUserAndGetId(userData);
        System.out.println("id созданного пользователя: "+id);

        Response userLogin = login("vinkotov@example.com", "1234");
        System.out.println("id залогиненного пользователя: "+getIntFromJson(userLogin,"user_id"));

        Response responseUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/", id);
        responseUserData.print();

        Assertions.assertJsonHasField(responseUserData, "username");
        Assertions.assertJsonHasNotField(responseUserData, "firstName");
        Assertions.assertJsonHasNotField(responseUserData, "lastName");
        Assertions.assertJsonHasNotField(responseUserData, "email");
    }

}
