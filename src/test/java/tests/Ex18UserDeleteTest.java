package tests;

import io.restassured.response.Response;
import lib.ApiCoreRequests;
import lib.Assertions;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class Ex18UserDeleteTest extends BaseTestCase {
        private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();

        //Первый - на попытку удалить пользователя по ID 2.
        @Test
        public void deleteUserUnsuccessfully() {
            //логинимся под пользователем и получаем его id
            Response userLogin = login("vinkotov@example.com", "1234");
            int userId = getIntFromJson(userLogin, "user_id");

            //удаляем пользователя
            Response responseDeleteUser = apiCoreRequests.makeDeleteRequest(
                    "https://playground.learnqa.ru/api/user/",
                    this.getHeader(userLogin, "x-csrf-token"),
                    this.getCookie(userLogin, "auth_sid"),
                    userId);

            Assertions.assertResponseCodeEquals(responseDeleteUser, 400);
            Assertions.assertResponseTextEquals(responseDeleteUser, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
        }

        //Второй - позитивный. Создать пользователя, авторизоваться из-под него,
        //удалить, затем попробовать получить его данные по ID и убедиться, что пользователь действительно удален.
        @Test
        public void deleteUserSuccessfully() {
            //создаем нового пользователя и получаем его id
            Map<String, String> userData = DataGenerator.getRegistrationData();
            int userId = createUserAndGetId(userData);

            //логинимся под созданным пользователем
            Response userLogin = login(userData.get("email"), userData.get("password"));

            //удаляем пользователя
            Response userDelete = apiCoreRequests.makeDeleteRequest(
                    "https://playground.learnqa.ru/api/user/",
                    this.getHeader(userLogin, "x-csrf-token"),
                    this.getCookie(userLogin, "auth_sid"),
                    userId);

            //получаем данные пользователя по id
            Response getUserData = apiCoreRequests.makeGetRequest("https://playground.learnqa.ru/api/user/", userId);

            Assertions.assertResponseCodeEquals(getUserData, 404);
            Assertions.assertResponseTextEquals(getUserData, "User not found");
        }

        //Третий - негативный, попробовать удалить пользователя, будучи авторизованными другим пользователем.
        @Test
        public void deleteUserAnotherUser() {
            //создаем нового пользователя и получаем его id
            Map<String, String> userData = DataGenerator.getRegistrationData();
            int userId = createUserAndGetId(userData);

            //логинимся под другим пользователем
            Response userLogin = login("vinkotov@example.com", "1234");

            //удаляем созданного пользователя
            Response userDelete = apiCoreRequests.makeDeleteRequest(
                    "https://playground.learnqa.ru/api/user/",
                    this.getHeader(userLogin, "x-csrf-token"),
                    this.getCookie(userLogin, "auth_sid"),
                    userId);

            Assertions.assertResponseCodeEquals(userDelete, 400);
            Assertions.assertResponseTextEquals(userDelete, "Please, do not delete test users with ID 1, 2, 3, 4 or 5.");
        }

    }

