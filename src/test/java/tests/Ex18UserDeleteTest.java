package tests;

import io.qameta.allure.*;
import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Ex18UserDeleteTest extends BaseTestCase {
        private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
        @Link("https://software-testing.ru/lms/mod/assign/view.php?id=294735")
        @Owner(value = "Olga Voronkina")
        @Test
        @Description("Пытается удалить пользователя по ID 2")
        @Severity(SeverityLevel.MINOR)
        public void deleteUserUnsuccessfully() {
            Response userLogin = login("vinkotov@example.com", "1234");
            int userId = getIntFromJson(userLogin, "user_id");

            Response responseDeleteUser = apiCoreRequests.makeDeleteRequest(
                    "https://playground.learnqa.ru/api/user/",
                    this.getHeader(userLogin, "x-csrf-token"),
                    this.getCookie(userLogin, "auth_sid"),
                    userId);

            Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
            Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
        }

        @Test
        @Description("Создать пользователя, авторизоваться из-под него,удалить, затем попробовать получить его данные по ID и убедиться, что пользователь действительно удален")
        @Severity(SeverityLevel.CRITICAL)
        public void deleteUserSuccessfully() {
            Map<String, String> userData = DataGenerator.getRegistrationData();
            int userId = createUserAndGetId(userData);

            Response userLogin = login(userData.get("email"), userData.get("password"));

            Response userDelete = apiCoreRequests.makeDeleteRequest(
                    "https://playground.learnqa.ru/api/user/",
                    this.getHeader(userLogin, "x-csrf-token"),
                    this.getCookie(userLogin, "auth_sid"),
                    userId);

            Response getUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/", userId);

            Assertions.assertResponseCodeEquals(getUserData, 404);
            Assertions.assertResponseTextEquals(getUserData, "User not found");
        }

        @Test
        @Description("Негативный, попробовать удалить пользователя, будучи авторизованными другим пользователем")
        @Severity(SeverityLevel.CRITICAL)

        public void deleteUserAnotherUser() {
            Map<String, String> userData = DataGenerator.getRegistrationData();
            int userId = createUserAndGetId(userData);

            Response userLogin = login("vinkotov@example.com", "1234");

            Response userDelete = apiCoreRequests.makeDeleteRequest(
                    "https://playground.learnqa.ru/api/user/",
                    this.getHeader(userLogin, "x-csrf-token"),
                    this.getCookie(userLogin, "auth_sid"),
                    userId);

            Assertions.assertResponseCodeEquals(userDelete, 400);
            Assertions.assertResponseTextEquals(userDelete, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
        }

    }

